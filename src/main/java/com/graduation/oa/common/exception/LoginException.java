package com.graduation.oa.common.exception;

public class LoginException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;
    private String debug;

    public LoginException(String message) {
        super(message);
    }

    public LoginException(Throwable e) {
        super(e);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
