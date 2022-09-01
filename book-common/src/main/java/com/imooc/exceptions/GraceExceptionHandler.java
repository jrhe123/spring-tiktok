package com.imooc.exceptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;

@ControllerAdvice
public class GraceExceptionHandler {
	
	public static final List<String> ANNOTATIONS = Arrays.asList("OneNotNull");

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyCustomException e) {
        e.printStackTrace();
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJSONResult returnMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> map = getErrors(result);
        return GraceJSONResult.errorMap(map);
    }
        
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJSONResult returnMaxUploadSize(MaxUploadSizeExceededException e) {
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_2MB_ERROR);
    }

    
	public Map<String, String> getErrors(BindingResult result) {
		Map<String, String> map = new HashMap<>();
		// field errors
		List<FieldError> errorList = result.getFieldErrors();
		for (FieldError fe : errorList) {
			String field = fe.getField();
			String msg = fe.getDefaultMessage();
			map.put(field, msg);
		}		
		// object errors
		List<ObjectError> list = result.getAllErrors();
		for (ObjectError oe : list) {
			String code = oe.getCode();
			if (ANNOTATIONS.contains(code)) {
				String field = "form";
				String msg = oe.getDefaultMessage();
				map.put(field, msg);
			}
		}
		return map;
	}
}
