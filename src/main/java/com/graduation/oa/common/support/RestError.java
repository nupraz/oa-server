package com.graduation.oa.common.support;

import com.graduation.oa.common.support.RestStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class RestError implements Serializable {
    private static final long serialVersionUID = 1L;
    private int status;
    private String code;
    private String message;
    private String debug;
    private String more;
    private String appCode;

    public RestError() {
    }

    public static RestError build(String appCode, String prefix, RestStatus restStatus, String debug) {
        RestError restError = new RestError();
        restError.setStatus(restStatus.getStatus().value());
        restError.setCode(prefix + restStatus.getCode());
        restError.setDebug(debug);
        restError.setAppCode(appCode);
        restError.setMessage(restStatus.getMessage());
        return restError;
    }

    public static RestError build(String appCode, String prefix, RestStatus restStatus, String code, String message, String debug) {
        RestError restError = new RestError();
        restError.setStatus(restStatus.getStatus().value());
        restError.setCode(prefix + code);
        restError.setDebug(debug);
        restError.setAppCode(appCode);
        if (!StringUtils.isEmpty(message)) {
            restError.setMessage(message);
        } else {
            restError.setMessage(restStatus.getMessage());
        }

        return restError;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getMore() {
        return this.more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
