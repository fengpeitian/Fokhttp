package com.fpt.okhttp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * <pre>
 *   @author  : fpt
 *   e-mail  : fengfei0205@sina.com
 *   time    : 2018/07/26 18:55
 *   desc    :使用前一定要初始化工具类Utils.init(this);
 *   version : 1.0
 * </pre>
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
    private Context mContext;
    private OnNetworkStatusListener listener;
    private static String mHost = null;

    public NetworkChangedReceiver(Context context) {
        register(context);
        setHost(null);
    }

    public NetworkChangedReceiver(Context context, String host) {
        register(context);
        setHost(host);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
            boolean isConnected = NetworkUtils.isConnected();
            if (isConnected){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean ping = NetworkUtils.isAvailableByPing(mHost);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null){
                                    listener.onNetworkStatus(ping);
                                }
                            }
                        });
                    }
                }).start();
            }else {
                if (listener != null){
                    listener.onNetworkStatus(false);
                }
            }
        }
    }

    private void register(Context context){
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(this, filter);
    }

    public void setHost(String mHost) {
        this.mHost = mHost;
    }

    public void unregister(){
        mContext.unregisterReceiver(this);
    }

    public void setOnNetworkStatusListener(OnNetworkStatusListener listener) {
        this.listener = listener;
    }

    public interface OnNetworkStatusListener{
        void onNetworkStatus(boolean isConnect);
    }

}
