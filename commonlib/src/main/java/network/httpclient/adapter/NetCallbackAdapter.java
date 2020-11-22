package network.httpclient.adapter;

import network.httpclient.listener.NetCallback;

public class NetCallbackAdapter<T> implements NetCallback<T> {

	@Override
	public void onStart() {
	}

	@Override
	public void onSuccess(T result) {
	}

	@Override
	public void onFail(int errorCode, String errorMsg) {
	}

}
