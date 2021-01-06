package aixiya.framework.backend.platform.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author wangqun865@163.com
 */
public class ClientValidException extends AuthenticationException {
    public ClientValidException(String msg, Throwable t) {
        super(msg, t);
    }

    public ClientValidException(String msg) {
        super(msg);
    }
}
