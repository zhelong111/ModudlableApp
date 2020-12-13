package network.comm.netstatus;

public interface NetworkListener {
    void onDisconnect();

    void onConnected(int netType);

    void onLevel(int level); // 信号强度
}
