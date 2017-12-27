package tv.qishi.milian.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.NetworkUtils;
import com.smart.androidutils.utils.SharePrefsUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.MainActivity;
import tv.qishi.milian.R;
import tv.qishi.milian.XieyiActivity;
import tv.qishi.milian.core.BaseSiSiEditActivity;
import tv.qishi.milian.event.BroadCastEvent;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.view.SFProgrssDialog;


public class LoginActivity extends BaseSiSiEditActivity{

    private static String TAG = LoginActivity.class.getName();
    public static String FINISH_EVENT="LoginActivity finish";

    private final int THIRD_LOGIN_SUCCESS = 0;
    private final int THIRD_LOGIN_ERROR = 1;
    private final int THIRD_LOGIN_CANCEL = 2;

//    @Bind(R.id.text_top_title)
//    TextView mTextTopTitle;
    @Bind(R.id.player_surface)
    SurfaceView mPlayerSurface;
    @Bind(R.id.login_bg_image)
    ImageView mLoginBgImage;
//    @Bind(R.id.image_top_back)
//    ImageView mImageTopBack;
//    @OnClick(R.id.image_top_back)
//    public void back(View view) {
//        LoginActivity.this.finish();
//    }

    private SFProgrssDialog mProgressDialog;

    @OnClick(R.id.tv_xieyi)
    public void openXieyi(){
        //TODO 跳转协议页面
        XieyiActivity.newInstance(LoginActivity.this);
    }
    @OnClick(R.id.tv_loginbyphone)
    public void loginByPhone(){
        //TODO 跳转手机注册
        LoginByPhoneActivity.newInstance(this);
    }


    @OnClick(R.id.linear_login_weichat_container)
    public void loginWeChat(View view) {

    }

    @OnClick(R.id.linear_login_qq_container)
    public void loginQQ(View view) {
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {

        } else {
            toast("网络不给力~~");
        }
    }

    @OnClick(R.id.linear_login_sina_container)
    public void loginSina(View view) {
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {

        } else {
            toast("网络不给力~~");
        }
    }



    SurfaceHolder mSurfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTextTopTitle.setText("登录");
//        mImageTopBack.setVisibility(View.GONE);
        mPlayerSurface.setKeepScreenOn(true);


        EventBus.getDefault().register(this);
    }



    @Override
    public int getLayoutResource() {
        return R.layout.activity_login2;
    }




    @Subscribe
    public void onEvent(BroadCastEvent event){
        if(event.getContent().endsWith(LoginActivity.FINISH_EVENT)){
            finish();
        }

    }


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == THIRD_LOGIN_SUCCESS) {
                //成功
                if(null != mProgressDialog)
                mProgressDialog.dismiss();
                toast("登录成功√");
                openActivity(MainActivity.class);
                LoginActivity.this.finish();
            } else if (msg.what == THIRD_LOGIN_ERROR) {
                //失败
                if(null != mProgressDialog)
                    mProgressDialog.dismiss();
                toast("登录失败!");
            } else if (msg.what == THIRD_LOGIN_CANCEL) {
                //取消
                if(null != mProgressDialog)
                    mProgressDialog.dismiss();
                toast("登录取消!");
            }
        }
    };
}
