package com.mnt.tools.dep;


import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


@ControllerAdvice()
public class GlobalExceptionHandler {
    private final static  String XML_HTTP_REQUEST = "XMLHttpRequest";
    private final static  String X_REQUESTED_WITH = "x-requested-with";
    private final static  String ERROR_PATH = "/error";
    private final static  String ERROR_PAGE_403="pages/403";
    private final static  String ERROR_PAGE_403_MESSAGE="UnauthorizedException:您访问的资源，未授权！";
    private final static  String ERROR_PAGE_500="pages/500";
    private final static  String ERROR_PAGE_500_MESSAGE="系统异常";
    private final static  String OUT_JSON_FAILED_MESSAGE  ="outObjectToResponse failed";


    /**
     * ajax异常捕获
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public AjaxResult handleServiceException(MethodArgumentNotValidException e) {
        String errorMassage = "";
        BindingResult errList = e.getBindingResult();
        for (FieldError error : errList.getFieldErrors()) {
            errorMassage += error.getDefaultMessage() + ",";
        }
        return AjaxResult.failed(errorMassage);
    }

}

