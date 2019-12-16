package com.graduation.oa.common.exception;

public class CredentialsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;
    private String debug;

    public CredentialsException(String message) {
        super(message);
    }

    public CredentialsException(Throwable e) {
        super(e);
    }

    public CredentialsException(String code, String message) {
        super(message);
        this.code = code;
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
