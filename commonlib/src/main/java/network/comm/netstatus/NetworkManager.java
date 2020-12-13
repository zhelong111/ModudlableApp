package network.comm.netstatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理网络状况
 */
public class NetworkManager {

    private List<NetworkListener> netListeners = new ArrayList<>();

    private NetworkManager() {
    }

    private static class Proxy {
        private static NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return Proxy.instance;
    }

    public void addListener(NetworkListener listener) {
        netListeners.add(listener);
    }

    public void removeListener(NetworkListener listener) {
        netListeners.remove(listener);
    }

    public void onDisconnect() {
        for (NetworkListener listener : netListeners) {
            listener.onDisconnect();
        }
    }

    public void onConnect() {
        for (NetworkListener listener : netListeners) {
            listener.onConnected(1);
        }
    }

    public void onLevel(int level) {
        for (NetworkListener listener : netListeners) {
            listener.onLevel(level);
        }
    }

}
