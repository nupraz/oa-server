package com.graduation.oa.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private Object[] params;
    private String debug;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String id, Object... params) {
        this.code = this.code;
        this.params = params;
    }

    public ServiceException(Throwable cause, String code, String message) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, String id, Object... params) {
        super(cause);
        this.code = this.code;
        this.params = params;
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getDebug() {
        return this.debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
