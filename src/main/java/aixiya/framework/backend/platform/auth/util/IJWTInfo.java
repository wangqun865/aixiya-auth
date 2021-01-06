package aixiya.framework.backend.platform.auth.util;

/**
* @Description:     可根据业务扩展
* @Author:wangqun865@163.com
*/
public interface IJWTInfo {
    /**
     * 获取用户名(用户loginname+组织id+产品clientId)
     * @return
     */
    String getUniqueName();
    
    /**
     * 获取登录名
     * @return
     */
    String getLoginName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取组织
     * @return
     */
    String getOrgId();
    
    /**
     * 获取组织名称
     * @return
     */
    String getOrgName();
    
    /**
     * 获取是否为组织管理员
     * 1-是  0-否
     * @return
     */
    String getManager();
    
  /*  *//**
     * 产品id
     * @return
     *//*
    String getClientId();
    
    *//**
     * 产品名称
     * @return
     *//*
    String getClientName();*/

    
}
