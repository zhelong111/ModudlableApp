package network.comm.netstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接受网络变化广播
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkManager.getInstance().onConnect();
    }
}
