package aixiya.framework.backend.platform.auth.common.exception;


import aixiya.framework.backend.platform.auth.common.RestCodeConstants;

public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, RestCodeConstants.EX_USER_INVALID_CODE);
    }
}
