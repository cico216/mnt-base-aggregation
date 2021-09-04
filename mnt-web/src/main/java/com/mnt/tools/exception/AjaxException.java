package com.mnt.tools.exception;

/**
 * 基础业务异常
 *
 * @author jiangbiao
 * @Date 2017年4月18日上午11:32:28
 */
public class AjaxException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int errorcode;
    private String errorMessage;

    public AjaxException() {
        super();
    }

    public AjaxException(int code, String message) {
        super(code + "：" + message);
        this.errorcode = code;
        this.errorMessage = message;
    }
    
   

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String toString() {
    	return errorcode + "：" + errorMessage;
    }
}
