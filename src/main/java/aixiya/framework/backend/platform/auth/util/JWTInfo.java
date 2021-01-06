package aixiya.framework.backend.platform.auth.util;


import java.io.Serializable;

/**
* @Description:     可根据实际业务扩展
* @Author:wangqun865@163.com
*/
public class JWTInfo implements Serializable,IJWTInfo {
    private String uniqueName;
    private String loginName;
    private String userId;
    private String name;
    private String orgId;
    private String orgName;
    private String manager;
   /* private String clientId;
    private String clientName;*/

    public JWTInfo(String uniqueName, String loginName , String userId, String name,
    		String orgId ,String orgName, String manager
    		) {
        this.uniqueName = uniqueName;
        this.loginName=loginName;
        this.userId = userId;
        this.name = name;
        this.orgId=orgId;
        this.orgName=orgName;
        this.manager=manager;
        /*this.clientId=clientId;
        this.clientName=clientName;*/
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
  
    @Override
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	
	@Override
	public String getLoginName() {
		return loginName;
	}
	
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (uniqueName != null ? !uniqueName.equals(jwtInfo.uniqueName) : jwtInfo.uniqueName != null) {
            return false;
        }
        return loginName != null ? loginName.equals(jwtInfo.loginName) : jwtInfo.loginName == null;

    }

    @Override
    public int hashCode() {
        int result = uniqueName != null ? uniqueName.hashCode() : 0;
        result = 31 * result + (loginName != null ? loginName.hashCode() : 0);
        return result;
    }
}
