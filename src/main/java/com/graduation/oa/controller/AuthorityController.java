package com.graduation.oa.controller;

import com.bestvike.commons.exception.LoginException;
import com.graduation.oa.common.redis.Cache;
import com.graduation.oa.data.SysUser;
import com.graduation.oa.service.AuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

@RestController
public class AuthorityController extends BaseController {
    @Autowired
    private AuthorityService authorityService;

    @Autowired
    @Qualifier("authorityCache")
    private Cache authorityCache;

    @Value("${app.authority.login.allow-retry-times:-1}")
    private Integer allowRetryTimes;

    @GetMapping("/api/authority/routes")
    public List routes() {
        return authorityCache.get("routes:" + appCode + ":async_routes", List.class);
    }

    @PostMapping("/api/authority/login")
    public SysUser login(@RequestBody SysUser sysUser, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String validateCode = (String) httpServletRequest.getSession().getAttribute("validateCode");
        if (!StringUtils.isEmpty(validateCode)) {
            if (StringUtils.isEmpty(sysUser.getValidateCode()) || !validateCode.equalsIgnoreCase(sysUser.getValidateCode())) {
                throw new LoginException("验证码错误");
            }
        }
        try {
            SysUser loginUser = authorityService.login(sysUser, httpServletRequest);
            if (loginUser != null) {
                httpServletRequest.getSession().removeAttribute("retryTimes");
                httpServletRequest.getSession().removeAttribute("validateCode");
                return loginUser;
            }
        } catch (Exception e) {
            checkRetryTimes();
            throw e;
        }

        checkRetryTimes();
        return null;
    }

    @PutMapping("/api/authority/password")
    public void modifyPassword(@RequestBody SysUser sysUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        sysUser.setId(super.getLoginUserId());
        authorityService.modifyPassword(sysUser);
    }

    @GetMapping("/api/authority/code")
    public void generateCode(HttpServletResponse httpServletResponse) throws IOException, InterruptedException {
        httpServletResponse.setContentType("image/jpeg");
        // 验证码图片的宽度。
        int width = 100;
        // 验证码图片的高度。
        int height = 38;
        // 验证码字符个数
        int codeNum = 4;
        int xx = 0;
        // 字体高度
        int fontSize;
        int codeY;
        String codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        // 干扰线
        int lineNum = 0;

        // 初始化
        xx = (width - 2) / codeNum;
        fontSize = height - 2 < xx ? height - 2 : xx;
        codeY = height - 10;

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();

        // 创建一个随机数生成器类
        Random random = new Random();

        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, buffImg.getWidth(), buffImg.getHeight());

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontSize);
        // 设置字体。
        g.setFont(font);

        // 画边框。
//        g.setColor(Color.BLACK);
//        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生n条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.BLACK);
        for (int i = 0; i < lineNum; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeNum; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence.charAt(random.nextInt(codeSequence.length())));
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, 1 + i * xx, codeY);

            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        g.dispose();
        // 将四位数字的验证码保存到Session中。
        httpServletRequest.getSession().setAttribute("validateCode", randomCode.toString());

        // 禁止图像缓存。
///     httpServletResponse.setHeader("Pragma", "no-cache");
///     httpServletResponse.setHeader("Cache-Control", "no-cache");
///     httpServletResponse.setDateHeader("Expires", 0);

//      httpServletResponse.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        OutputStream output = httpServletResponse.getOutputStream();
        ImageIO.write(buffImg, "jpg", output);
        output.flush();
        output.close();

///     out.close();
    }

    @GetMapping("/api/authority/info")
    public SysUser getUserInfo() {
        // System.out.println(httpServletRequest.getSession().getMaxInactiveInterval());
        // return super.getLoginUser();
        return authorityService.getUserInfo(super.getLoginUserId());
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authorityService.getUserInfo(authentication.getName());
        }
        throw new AuthorityException("尚未登录");*/
    }

    @PostMapping("/api/authority/logout")
    public String logout() {
        // 此处调用不到
        authorityService.logout(httpServletRequest);
        return "success";
    }

    private void checkRetryTimes() {
        Integer retryTimes = (Integer) httpServletRequest.getSession().getAttribute("retryTimes");
        if (retryTimes == null) {
            retryTimes = 1;
        } else {
            retryTimes++;
        }
        httpServletRequest.getSession().setAttribute("retryTimes", retryTimes);
        if (retryTimes > allowRetryTimes) {
            throw new LoginException("登录失败");
        }
    }
}
