package aixiya.framework.backend.platform.auth.controller.form;

import aixiya.framework.backend.platform.auth.util.IJWTInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author wangqun865@163.com
 */

@Data
public class AuthenticateJson {
    private String jwt;
    private String clientId;
    private String clientName;
    private String uniqueName;
    private String loginName;
    private String userId;
    private String name;
    private String orgId;
    private String orgName;
    private String manager;


    public static AuthenticateJson parseAuthenticateJson (IJWTInfo j) {
        AuthenticateJson authenticateJson = new AuthenticateJson();
        authenticateJson.setUniqueName(j.getUniqueName());
        authenticateJson.setLoginName(j.getLoginName());
        authenticateJson.setUserId(j.getId());
        authenticateJson.setName(j.getName());
        authenticateJson.setOrgId(j.getOrgId());
        authenticateJson.setOrgName(j.getOrgName());
        authenticateJson.setManager(j.getManager());
        return authenticateJson;
    }

    public static void main(String[] args) {
        AuthenticateJson authenticateJson = new AuthenticateJson();
        authenticateJson.setJwt("adsfasdf");
        authenticateJson.setClientId("134132");
        String json = JSONObject.toJSONString(authenticateJson);
        System.out.println(json);
        AuthenticateJson a =  JSON.parseObject(json, AuthenticateJson.class);
        System.out.println(a.getOrgName());
    }
}
