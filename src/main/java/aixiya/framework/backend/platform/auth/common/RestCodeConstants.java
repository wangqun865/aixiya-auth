package aixiya.framework.backend.platform.auth.common;

/*
* @Author:wangqun865@163.com
*/
public class RestCodeConstants {

	/**
     * 6位异常码  x'xx'xxx
     *  第一位：
     *  成功：2
     *  警告类型 3
     *  业务异常 4
     * 	系统异常5
     *
     * 	2-3位：
     * 01 : 鉴权中心  02 ：用户中心 03 XX中心
     *
     * 后3位
     * 各业务系统自定义异常码
     *
	 */
	
	
    public static final int TOKEN_ERROR_CODE = 40101;
    public static final int TOKEN_FORBIDDEN_CODE = 40301;

    //用户token异常  jwt 换取code
    public static final Integer EX_USER_INVALID_CODE = 401001;
    //登陆验证异常  code换取token  code异常
    public static final Integer Client_INVALID_CODE = 401002;
    //用户登陆，调用API，校验失败UserInvalidException
    public static final Integer USER_INVALID_CODE = 401003;


    
    //用户登陆，组织多个异常
    public static final String LOGIN_ORG_INVALID_CODE = "301001";
    
    
}
