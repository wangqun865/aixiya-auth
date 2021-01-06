package aixiya.framework.backend.platform.auth.common;

/**
 * @Author  wangqun865@163.com
 */

public class RestObjectResponse<T> extends BaseResponse {
	T data;
	
	@SuppressWarnings("rawtypes")
	public RestObjectResponse mess(String mess){
        this.setMessage(mess);
        return this;
    }


    @SuppressWarnings("rawtypes")
	public RestObjectResponse data(T data) {
        this.setData(data);
        return this;
    }
    
    
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
	
}
