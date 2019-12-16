package com.graduation.oa.service.impl;

import com.graduation.oa.common.entity.Route;
import com.graduation.oa.common.entity.User;
import com.graduation.oa.common.exception.CredentialsException;
import com.graduation.oa.common.exception.ServiceException;
import com.graduation.oa.common.redis.Cache;
import com.graduation.oa.dao.SysUserDao;
import com.graduation.oa.data.SysUser;
import com.graduation.oa.service.AuthorityService;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.common.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class AuthorityServiceImpl extends BaseService implements AuthorityService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private Cache cache;

    @Value("${app.instance.code}")
    private String appCode;
    @Autowired
    @Qualifier("authorityCache")
    private Cache authorityCache;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${app.authority.login.allow-type:any}")
    private String allowType;
    @Value("${server.servlet.session.timeout}")
    private String sessionTimeout;

    @Override
    public SysUser login(SysUser sysUser, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String userName = sysUser.getId();
        if (StringUtils.isEmpty(userName)) {
            throw new CredentialsException("用户名不能为空");
        }
        String loginType = sysUser.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            loginType = "password";
        }
        if (!allowType.equals("any")) {
            if (!loginType.equals(allowType)) {
                throw new CredentialsException("登录方式异常！");
            }
        }

        String password = sysUser.getPassword();
        Example example = new Example(SysUser.class);
        example.createCriteria().andEqualTo("empId", sysUser.getId()).orEqualTo("name", sysUser.getId()).orEqualTo("id", sysUser.getId()).orEqualTo("mobile", sysUser.getId()).orEqualTo("email", sysUser.getId());
        sysUser = sysUserDao.selectOneByExample(example);
        if (sysUser == null) {
            throw new CredentialsException("用户不存在！");
        }
        // 通过用户名、密码登录
        if (sysUser == null || !new BCryptPasswordEncoder().matches(EncryptUtil.base64Decode(password), sysUser.getPassword())) {
            throw new CredentialsException("用户名或密码错");
        }

        if (sysUser.getGrants() == null || sysUser.getGrants().size() == 0) {
            throw new CredentialsException("用户无权限");
        }
        if(!sysUser.getStatus().equals("0000")){
            throw new CredentialsException("用户已销户");
        }

        /*SysUser sysUser = sysUserDao.selectByPrimaryKey("1");
        sysUser.setRoles("admin");
        sysUser.setToken("admin");*/

        // 生成spring security oauth2 token
        // SimpleGrantedAuthority authority = new SimpleGrantedAuthority("role_client");

        // 用户路由（根据角色权限合成）
        Map<String, Set<String>> userPermissions = new HashMap<>();
        String[] roles = sysUser.getRoles().split(",");
        // 合并多个角色的路由
        for (String roleId : roles) {
            Map<String, List<String>> permissions = authorityCache.getMap("role_permissions:" + roleId);
            if (permissions != null) {
                permissions.forEach((route, operates) -> {
                    Set<String> routeOperates = userPermissions.get(route);
                    if (routeOperates == null) {
                        if (operates != null) {
                            userPermissions.put(route, new HashSet<>(operates));
                        } else {
                            userPermissions.put(route, null);
                        }
                    } else {
                        if (operates != null) {
                            routeOperates.addAll(operates);
                            userPermissions.put(route, routeOperates);
                        }
                    }
                });
            }
        }

        // 用户路由（前端页面展示菜单用）
        List<Route> routes = authorityCache.get("routes:" + appCode + ":async_routes", List.class);
        List<Route> userRoutes = filterRoutes(routes, userPermissions);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getId(), password, AuthorityUtils.commaSeparatedStringToAuthorityList(sysUser.getRoles()).stream().collect(Collectors.toSet()));
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession httpSession = httpServletRequest.getSession(true);
        int timeout = 600;
        if (!StringUtils.isEmpty(sessionTimeout)) {
            if (sessionTimeout.endsWith("s")) {
                timeout = Integer.valueOf(sessionTimeout.substring(0, sessionTimeout.length() - 1));
            } else if (sessionTimeout.endsWith("m")) {
                timeout = 60 * Integer.valueOf(sessionTimeout.substring(0, sessionTimeout.length() - 1));
            } else {
                timeout = Integer.valueOf(sessionTimeout);
            }
        }
        httpSession.setMaxInactiveInterval(timeout);
        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        httpSession.setAttribute("user_id", sysUser.getId());

        sysUser.setToken(httpSession.getId());
        sysUser.setPassword("******");

        User user = new User();
        user.setId(sysUser.getId());
        user.setName(sysUser.getName());
        user.setAvatar(sysUser.getAvatar());
        user.setRoles(sysUser.getRoles());
        user.setCode(sysUser.getEmpId());

        authorityCache.set("token_to_user:" + httpSession.getId(), sysUser.getId());
        /// authorityCache.add("user_permissions:" + sysUser.getId(), urls);
        authorityCache.set("user_routes:" + sysUser.getId(), userRoutes);
        authorityCache.set("user_details:" + sysUser.getId(), user);

        sysUser.setRoutes(userRoutes);

        return sysUser;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String userId = authorityCache.get("token_to_user:" + httpSession.getId(), String.class);
        authorityCache.delete("token_to_user:" + httpSession.getId());
        if (!StringUtils.isEmpty(userId)) {
            authorityCache.delete("user_permissions:" + userId);
            authorityCache.delete("user_routes:" + userId);
            authorityCache.delete("user_details:" + userId);
        }
    }

    @Override
    public void modifyPassword(SysUser sysUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(sysUser.getPassword())) {
            throw new ServiceException("原密码不能为空");
        }
        if (StringUtils.isEmpty(sysUser.getNewPassword())) {
            throw new ServiceException("新密码不能为空");
        }
        SysUser sysUserDb = sysUserDao.selectByPrimaryKey(sysUser.getId());
        if (sysUserDb == null || !new BCryptPasswordEncoder().matches(EncryptUtil.base64Decode(sysUser.getPassword()), sysUserDb.getPassword())) {
            throw new ServiceException("用户名或密码错");
        }
        sysUser.setPassword(new com.graduation.oa.common.crypto.bcrypt.BCryptPasswordEncoder().encode(EncryptUtil.base64Decode(sysUser.getNewPassword())));
        sysUser.setNewPassword(null);
        sysUserDao.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public SysUser getUserInfo(String userId) {
        if (userId != null) {
            SysUser sysUser = sysUserDao.selectByPrimaryKey(userId);
            if (sysUser != null) {
                sysUser.setPassword("******");

                List<Route> routes = authorityCache.get("user_routes:" + userId, List.class);
                if (routes != null) {
                    sysUser.setRoutes(routes);
                }

                return sysUser;
            }
        }
        return null;
    }

    @Value("${app.authority.check-route:true}")
    private Boolean checkRoute;
    private List<Route> filterRoutes(List<Route> routes, Map<String, Set<String>> authorityRoutes) {
        List<Route> results = new ArrayList<>();
        for (Route route : routes) {
            Route temp = new Route();
            BeanUtils.copyProperties(route, temp, "children", "authority");
            if (route.getChildren() != null && route.getChildren().size() > 0) {
                temp.setChildren(filterRoutes(route.getChildren(), authorityRoutes));
            }

            if (checkRoute) {
                if (authorityRoutes.containsKey(temp.getId())) {
                    Set<String> operates = authorityRoutes.get(temp.getId());
                    if (operates != null && !operates.isEmpty() && route.getAuthority() != null && route.getAuthority().getOperates() != null) {
                        Route.Meta meta = temp.getMeta();
                        if (meta == null) {
                            meta = new Route.Meta();
                        }
                        List<Route.Operate> authorityOperates = new ArrayList<>();
                        for (Route.Operate operate : route.getAuthority().getOperates()) {
                            if (operates.contains(operate.getName())) {
                                Route.Operate authorityOperate = new Route.Operate();
                                BeanUtils.copyProperties(operate, authorityOperate, "urls");
                                authorityOperates.add(authorityOperate);
                            }
                        }
                        meta.setOperates(authorityOperates);
                        temp.setMeta(meta);
                    }
                    results.add(temp);
                }
            } else {
                Route.Meta meta = temp.getMeta();
                if (meta == null) {
                    meta = new Route.Meta();
                }
                if (route.getAuthority() != null && route.getAuthority().getOperates() != null) {
                    List<Route.Operate> authorityOperates = new ArrayList<>();
                    for (Route.Operate operate : route.getAuthority().getOperates()) {
                        Route.Operate authorityOperate = new Route.Operate();
                        BeanUtils.copyProperties(operate, authorityOperate, "urls");
                        authorityOperates.add(authorityOperate);
                    }
                    meta.setOperates(authorityOperates);
                }
                temp.setMeta(meta);
                results.add(temp);
            }
        }
        return results;
    }
}
