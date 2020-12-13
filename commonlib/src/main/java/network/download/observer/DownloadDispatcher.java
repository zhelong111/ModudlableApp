package network.download.observer;

import java.util.List;

import network.download.listener.DownloadListener;

public class DownloadDispatcher {
    private List<DownloadListener> observers;

    private static class Proxy {
        private static DownloadDispatcher dispatcher = new DownloadDispatcher();
    }

    public static DownloadDispatcher getInstance() {
        return Proxy.dispatcher;
    }

    public void addListener(DownloadListener listener) {
        observers.add(listener);
    }

    public void removeListener(DownloadListener listener) {
        observers.remove(listener);
    }

    public void clear() {
        observers.clear();
    }

    public void dispatchFail(String fileUrl, String errMsg) {
        for (DownloadListener listener : observers) {
            listener.onFail(fileUrl, errMsg);
        }
    }

    public void dispatchFinish(String fileUrl, String filePath) {
        for (DownloadListener listener : observers) {
            listener.onFinish(fileUrl, filePath);
        }
    }


//    fun dispatchProgress(fileUrl: String, downloadedSize: Long, totalSize: Long) {
//        observers.forEach { it.onProgress(fileUrl, downloadedSize, totalSize) }
//    }
//
//    fun dispatchStop(fileUrl: String, downloadedSize: Long) {
//        observers.forEach { it.onStop(fileUrl, downloadedSize) }
//    }
//
//    fun dispatchCancel(fileUrl: String) {
//        observers.forEach { it.onCancel(fileUrl) }
//    }
//
//    fun dispatchStart() {
//        observers.forEach { it.onStart() }
//    }
}
