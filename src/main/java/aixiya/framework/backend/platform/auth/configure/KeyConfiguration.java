package aixiya.framework.backend.platform.auth.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
* @Author:wangqun865@163.com
*/
@Configuration
public class KeyConfiguration {
    @Value("${jwt.rsa-secret}")
    private String userSecret;
    @Value("${client.rsa-secret}")
    private String serviceSecret;
    private byte[] userPubKey;
    private byte[] userPriKey;
    private byte[] servicePriKey;
    private byte[] servicePubKey;
	/**
	 * @return the userSecret
	 */
	public String getUserSecret() {
		return userSecret;
	}
	/**
	 * @param userSecret the userSecret to set
	 */
	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}
	/**
	 * @return the serviceSecret
	 */
	public String getServiceSecret() {
		return serviceSecret;
	}
	/**
	 * @param serviceSecret the serviceSecret to set
	 */
	public void setServiceSecret(String serviceSecret) {
		this.serviceSecret = serviceSecret;
	}
	/**
	 * @return the userPubKey
	 */
	public byte[] getUserPubKey() {
		return userPubKey;
	}
	/**
	 * @param userPubKey the userPubKey to set
	 */
	public void setUserPubKey(byte[] userPubKey) {
		this.userPubKey = userPubKey;
	}
	/**
	 * @return the userPriKey
	 */
	public byte[] getUserPriKey() {
		return userPriKey;
	}
	/**
	 * @param userPriKey the userPriKey to set
	 */
	public void setUserPriKey(byte[] userPriKey) {
		this.userPriKey = userPriKey;
	}
	/**
	 * @return the servicePriKey
	 */
	public byte[] getServicePriKey() {
		return servicePriKey;
	}
	/**
	 * @param servicePriKey the servicePriKey to set
	 */
	public void setServicePriKey(byte[] servicePriKey) {
		this.servicePriKey = servicePriKey;
	}
	/**
	 * @return the servicePubKey
	 */
	public byte[] getServicePubKey() {
		return servicePubKey;
	}
	/**
	 * @param servicePubKey the servicePubKey to set
	 */
	public void setServicePubKey(byte[] servicePubKey) {
		this.servicePubKey = servicePubKey;
	}
	
    
    
    
    
}
