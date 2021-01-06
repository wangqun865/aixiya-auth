package aixiya.framework.backend.platform.auth.controller.form;

import java.util.List;

/**
 * @Author  wangqun865@163.com
 */

public class LoginForm {
	private String bcauthJwt;
	
	private List<LoginOrgForm> orgaList;

	/**
	 * @return the bcauthJwt
	 */
	public String getBcauthJwt() {
		return bcauthJwt;
	}

	/**
	 * @param bcauthJwt the bcauthJwt to set
	 */
	public void setBcauthJwt(String bcauthJwt) {
		this.bcauthJwt = bcauthJwt;
	}

	/**
	 * @return the orgaList
	 */
	public List<LoginOrgForm> getOrgaList() {
		return orgaList;
	}

	/**
	 * @param orgaList the orgaList to set
	 */
	public void setOrgaList(List<LoginOrgForm> orgaList) {
		this.orgaList = orgaList;
	}
	
	
}
