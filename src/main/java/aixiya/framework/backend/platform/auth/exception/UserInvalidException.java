package aixiya.framework.backend.platform.auth.exception;


import aixiya.framework.backend.platform.auth.common.RestCodeConstants;
import com.aixiya.framework.backend.common.exception.AixiyaFwRuntimeException;

/**
 * @Author wangqun865@163.com
 */
public class UserInvalidException extends AixiyaFwRuntimeException {
    public UserInvalidException(String message) {
        super(message, RestCodeConstants.USER_INVALID_CODE);
    }
}
