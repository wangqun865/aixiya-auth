package aixiya.framework.backend.platform.auth.controller.form;

import java.util.List;

/**
 * @Author  wangqun865@163.com
 */

public class JwtUserInfo {
	/**
	 * jwt 相关
	 */
    private String loginName;
    private String userId;
    private String name;
    private String orgId;
    private String orgName;
    //1-组织管理员  0-普通用户
    private String manager;
    //todo 未来JWT里 应该不包含client
    //private String clientId;
    //private String clientName;
    /**
     * 异常相关
     */
    private String errorInfo;
    private boolean relFlag ;
    
    /**
     * 其他业务数据
     */
    List<LoginOrgForm> loginOrgFormList;
    
    
    
	/**
	 * @param errorInfo
	 * @param relFlag
	 */
	public JwtUserInfo(String errorInfo, boolean relFlag) {
		super();
		this.errorInfo = errorInfo;
		this.relFlag = relFlag;
	}
	
	/**
	 * 
	 */
	public JwtUserInfo() {
		super();
	}

	

	/**
	 * @return the relFlag
	 */
	public boolean isRelFlag() {
		return relFlag;
	}

	/**
	 * @param relFlag the relFlag to set
	 */
	public void setRelFlag(boolean relFlag) {
		this.relFlag = relFlag;
	}


	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}
	/**
	 * @param manager the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * @return the errorInfo
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
	/**
	 * @param errorInfo the errorInfo to set
	 */
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	/**
	 * @return the loginOrgFormList
	 */
	public List<LoginOrgForm> getLoginOrgFormList() {
		return loginOrgFormList;
	}

	/**
	 * @param loginOrgFormList the loginOrgFormList to set
	 */
	public void setLoginOrgFormList(List<LoginOrgForm> loginOrgFormList) {
		this.loginOrgFormList = loginOrgFormList;
	}
    
}
