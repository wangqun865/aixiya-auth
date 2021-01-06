package aixiya.framework.backend.platform.auth.handler;

import aixiya.framework.backend.platform.auth.common.RestCodeConstants;
import aixiya.framework.backend.platform.auth.common.exception.UserTokenException;
import aixiya.framework.backend.platform.auth.exception.UserInvalidException;
import com.aixiya.framework.backend.common.api.AixiyaFwResponse;
import com.aixiya.framework.backend.common.handler.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wangqun865@163.com
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(UserInvalidException.class)
    @ResponseStatus(HttpStatus.OK)
    public AixiyaFwResponse userInvalidExceptionHandler(UserInvalidException ex) {
        log.error(ex.getMessage(), ex);
        return new AixiyaFwResponse().data(ex.getStatus()+"","", ex.getMessage());
    }

    @ExceptionHandler(UserTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AixiyaFwResponse userTokenExceptionHandler(UserTokenException ex) {
        log.error(ex.getMessage(), ex);
        return new AixiyaFwResponse().data(RestCodeConstants.EX_USER_INVALID_CODE+"","", ex.getMessage());
    }

    /*@ExceptionHandler(ClientValidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AixiyaFwResponse userTokenExceptionHandler(ClientValidException ex) {
        log.error(ex.getMessage(), ex);
        return new AixiyaFwResponse().data(RestCodeConstants.Client_INVALID_CODE+"","", ex.getMessage());
    }*/


}
