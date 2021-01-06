package aixiya.framework.backend.platform.auth.controller.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


/**
 * @Author  wangqun865@163.com
 */

@Data
public class LoginRequest {
	@Length(max = 30  , message = "客户端id超长")
	//@NotBlank(message = "客户端id不能为空")
	private String client_id;
	
	@Length(max = 30)
	private String orga_id;
	
	@Length(max = 30 , message = "账户名超长")
	@NotBlank(message = "账户名不能为空")
	private String user_login_name;
	
	@Length(max = 30 , message = "密码超长")
	@NotBlank(message = "密码不能为空")
	private String user_login_pwd;

	@Length(max = 30 , message = "验证码不正确")
	@NotBlank(message = "验证码不正确")
	private String captcha_key;

	@Length(max = 30 , message = "验证码不正确")
	@NotBlank(message = "验证码不正确")
	private String captcha_value;


	/**
	 * @return the client_id
	 *//*
	public String getClient_id() {
		return client_id;
	}

	*//**
	 * @param client_id the client_id to set
	 *//*
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	*//**
	 * @return the orga_id
	 *//*
	public String getOrga_id() {
		return orga_id;
	}

	*//**
	 * @param orga_id the orga_id to set
	 *//*
	public void setOrga_id(String orga_id) {
		this.orga_id = orga_id;
	}

	*//**
	 * @return the user_login_name
	 *//*
	public String getUser_login_name() {
		return user_login_name;
	}

	*//**
	 * @param user_login_name the user_login_name to set
	 *//*
	public void setUser_login_name(String user_login_name) {
		this.user_login_name = user_login_name;
	}

	*//**
	 * @return the user_login_pwd
	 *//*
	public String getUser_login_pwd() {
		return user_login_pwd;
	}

	*//**
	 * @param user_login_pwd the user_login_pwd to set
	 *//*
	public void setUser_login_pwd(String user_login_pwd) {
		this.user_login_pwd = user_login_pwd;
	}
	*/
	
	
}
