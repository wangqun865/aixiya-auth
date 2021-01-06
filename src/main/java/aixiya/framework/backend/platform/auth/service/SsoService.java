package aixiya.framework.backend.platform.auth.service;


import aixiya.framework.backend.platform.user.api.vo.UserCheckLoginVO;

/**
* @Author:wangqun865@163.com
*/
public interface SsoService {
    String login(UserCheckLoginVO jwtUserInfo) throws Exception;
    String refresh(String oldToken) throws Exception;
    void validate(String token) throws Exception;




}
