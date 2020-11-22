package network.httpclient;
/**
 * 网络请求回调接口
 * @author Administrator
 *
 */
public interface AsyncHttpCallback {

	/**
	 * 请求start回调
	 */
    void onStart();
	
	/**
	 * 请求结束回调
	 * @param result
	 */
    void onEnd(String result);
}
