package com.fpt.fokhttp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    private NetworkStatusEvent event = new NetworkStatusEvent();

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateReturnView(NetworkStatusEvent event) {
        this.event.fill(event);

        updateView(event);
    }

    private void updateView(NetworkStatusEvent event) {
        if (event.isConnect()){
            tv_title.setText(String.format("已未连接网络，网络类型为%s",event.getNetworkType().name()));
        }else {
            tv_title.setText("未连接网络");
        }
    }

    @OnClick({R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4, R.id.bt_5, R.id.bt_6, R.id.bt_7})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.bt_1:

                break;
            case R.id.bt_2:

                break;
            case R.id.bt_3:

                break;
            case R.id.bt_4:

                break;
            case R.id.bt_5:

                break;
            case R.id.bt_6:

                break;
            case R.id.bt_7:

                break;
            default:
                break;
        }
    }

}
