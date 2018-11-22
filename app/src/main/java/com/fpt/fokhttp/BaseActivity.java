package com.fpt.fokhttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.fpt.okhttp.util.NetworkChangedReceiver;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;

    /**
     * 网络变化监听
     */
    private NetworkChangedReceiver networkChangedReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                unbinder = ButterKnife.bind(this);
                //如果要使用 Eventbus 请将此方法返回 true
                //初始化工具框架
                Utils.init(this);
                //初始化eventbus
                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
                networkChangedReceiver = new NetworkChangedReceiver(getApplicationContext(), "223.5.5.5");
                networkChangedReceiver.setOnNetworkStatusListener(new NetworkChangedReceiver.OnNetworkStatusListener() {
                    @Override
                    public void onNetworkStatus(boolean isConnect) {
                        NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
                        EventBus.getDefault().post(new NetworkStatusEvent(isConnect, networkType));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    protected abstract int initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            this.unbinder = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (networkChangedReceiver != null) {
            networkChangedReceiver.unregister();
        }
    }

}
