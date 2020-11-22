package network.httpclient.listener;

public interface NetCallback<T> {

    void onStart();

    void onSuccess(T result);

    void onFail(int errorCode, String errorMsg);
} 
