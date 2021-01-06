package aixiya.framework.backend.platform.auth.common.util;

import aixiya.framework.backend.platform.auth.common.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * @Author  wangqun865@163.com
 */


@RestControllerAdvice
public class CommonExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(BindException.class)
	public BaseResponse handlerNotValidException(BindException exception) {
		logger.info("begin resolve argument exception");
	    BindingResult result = exception.getBindingResult();
	    String message = "";
	    if (result.hasErrors()) {
	        List<FieldError> fieldErrors = result.getFieldErrors();
	        if (fieldErrors != null && fieldErrors.size() > 0) {
	        	for (FieldError fieldError: fieldErrors) {
					message = message + /* fieldError.getField() + ":" + */fieldError.getDefaultMessage() + ";";
	        	}
	        }
	    } 
	    return new BaseResponse(400 , message);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handlerArgumentNotValidException(MethodArgumentNotValidException exception) {
		logger.info("begin resolve argument exception");
	    BindingResult result = exception.getBindingResult();
	    String message = "";
	    if (result.hasErrors()) {
	        List<FieldError> fieldErrors = result.getFieldErrors();
	        if (fieldErrors != null && fieldErrors.size() > 0) {
	        	for (FieldError fieldError: fieldErrors) {
					message = message + /* fieldError.getField() + ":" + */fieldError.getDefaultMessage() + ";";
	        	}
	        }
	    } 
	    return new BaseResponse(400 , message);
    }
	
	
	
}
