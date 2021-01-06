package aixiya.framework.backend.platform.auth.controller.form;

/**
 * @Author  wangqun865@163.com
 */

public class LoginOrgForm {
	private Integer orgaId;
	
	private String orgaName;

	/**
	 * @return the orgaId
	 */
	public Integer getOrgaId() {
		return orgaId;
	}

	/**
	 * @param orgaId the orgaId to set
	 */
	public void setOrgaId(Integer orgaId) {
		this.orgaId = orgaId;
	}

	/**
	 * @return the orgaName
	 */
	public String getOrgaName() {
		return orgaName;
	}

	/**
	 * @param orgaName the orgaName to set
	 */
	public void setOrgaName(String orgaName) {
		this.orgaName = orgaName;
	}
	
	
}
