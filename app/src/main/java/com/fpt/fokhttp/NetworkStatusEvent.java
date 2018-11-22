package com.fpt.fokhttp;

import com.blankj.utilcode.util.NetworkUtils;

public class NetworkStatusEvent {
    private boolean isConnect;
    private NetworkUtils.NetworkType networkType;

    public NetworkStatusEvent() {
    }

    public NetworkStatusEvent(boolean isConnect, NetworkUtils.NetworkType networkType) {
        this.isConnect = isConnect;
        this.networkType = networkType;
    }

    private void setNetworkStatusEvent(boolean connect,NetworkUtils.NetworkType networkType) {
        this.isConnect = connect;
        this.networkType = networkType;
    }

    public void fill(NetworkStatusEvent event){
        setNetworkStatusEvent(event.isConnect(),event.getNetworkType());
    }

    public boolean isConnect() {
        return isConnect;
    }

    public NetworkUtils.NetworkType getNetworkType() {
        return networkType;
    }
}
