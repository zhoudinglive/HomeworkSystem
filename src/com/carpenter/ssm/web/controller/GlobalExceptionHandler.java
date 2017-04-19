package com.carpenter.ssm.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**   
* @Title: GlobalExceptionHandler.java 
* @Package com.carpenter.ssm.web.controller 
* @Description: TODO 
* @author carpenter   
* @date 2016年8月27日 下午12:13:10 
* @version V1.0   
*/

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public @ResponseBody String handleError404(HttpServletRequest request, Exception e){
        return "{state:0, note:404}";
    }
    
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public @ResponseBody String handleError500(HttpServletRequest request, Exception e){
    	return "{state:0, note:500}";
    }
    
    @ExceptionHandler(ConversionNotSupportedException.class)
    public @ResponseBody String handleError500_2(HttpServletRequest request, Exception e){
    	return "{state:0, note:500}";
    }
}
