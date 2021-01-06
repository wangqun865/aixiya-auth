package aixiya.framework.backend.platform.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author wangqun865@163.com
 */
public class ReflushValidException extends AuthenticationException {
    public ReflushValidException(String msg, Throwable t) {
        super(msg, t);
    }

    public ReflushValidException(String msg) {
        super(msg);
    }
}
