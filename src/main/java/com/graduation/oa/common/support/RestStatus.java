package com.graduation.oa.common.support;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public enum RestStatus implements Serializable {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "90", "请求异常"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "92", "未授权"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "93", "禁止访问"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "95", "服务器内部错误"),
    SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "99", "服务器内部错误");

    private HttpStatus status;
    private String code;
    private String message;

    private RestStatus(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
