package aixiya.framework.backend.platform.auth.service.impl;

import aixiya.framework.backend.platform.auth.common.util.JwtTokenUtil;
import aixiya.framework.backend.platform.auth.service.SsoService;
import aixiya.framework.backend.platform.auth.util.JWTInfo;
import aixiya.framework.backend.platform.user.api.vo.OrganizationVO;
import aixiya.framework.backend.platform.user.api.vo.UserCheckLoginVO;
import com.aixiya.framework.backend.common.exception.AixiyaFwException;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @Author:wangqun865@163.com
*/
@Service
public class SsoServiceImpl implements SsoService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisService redisService;
    /**
     * 登陆
     * @param jwtUserInfo
     * @return
     * @throws Exception
     */
    @Override
    public String login(UserCheckLoginVO jwtUserInfo) throws Exception {
        List<OrganizationVO> list = jwtUserInfo.getOrganizationForFeigns();
        OrganizationVO org = list.get(0);
    	String uniqueName = jwtUserInfo.getLoginName() + org.getId() ;
    	String jwt = jwtTokenUtil.generateToken(new JWTInfo(uniqueName ,
    			jwtUserInfo.getLoginName() , jwtUserInfo.getUserId() , 
    			jwtUserInfo.getNameCn() , org.getId() , org.getName() ,
    			jwtUserInfo.getIfManager() ));
    		return jwt;
    }


    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
        boolean flag = redisService.hasKey(token);
        if (!flag) {
            throw new AixiyaFwException("validate token fail");
        }
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }


}
