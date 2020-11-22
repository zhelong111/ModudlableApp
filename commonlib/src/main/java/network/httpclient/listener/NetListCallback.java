package network.httpclient.listener;

import java.util.List;

public interface NetListCallback<T> {
    void onStart();
    void onSuccess(List<T> result);
    void onFail(int errorCode, String errorMsg);
} 
