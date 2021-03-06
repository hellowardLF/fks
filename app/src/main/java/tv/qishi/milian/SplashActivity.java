package tv.qishi.milian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.activity.BaseSplashActivity;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.own.WebviewActivity;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.SharedPreferencesUtils;

public class SplashActivity extends BaseSplashActivity {

    private boolean destroy = false;
    boolean firstOpen;
    @Bind(R.id.lauch_screen)
    ImageView mLauchScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstOpen();
        getStaticData();
        if (!firstOpen) {
//            Api.getLaunchScreen(this, new JSONObject(), new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, final JSONObject data) {
//                    Glide.with(SplashActivity.this).load(data.getString("info")).into(mLauchScreen);
//                    mLauchScreen.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(null != data.getString("url") && null != data.getString("title")){
//                                Bundle temp = new Bundle();
//                                temp.putString("title",data.getString("title"));
//                                temp.putString("jump",data.getString("url"));
//                                openActivity(WebviewActivity.class,temp);
//                                SplashActivity.this.finish();
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//
//                }
//            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!destroy) {
                        openActivity(MainActivity.class);
                        SplashActivity.this.finish();
                    }
                }
            }, 4000);
        }

    }

    private void isFirstOpen() {
        firstOpen = (boolean) SharePrefsUtils.get(SplashActivity.this, "system", "isFirstOpen", true);
        if (firstOpen) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, FirstOpenActivity.class));
                    finish();
                    SharePrefsUtils.put(SplashActivity.this, "system", "isFirstOpen", false);
                }
            }, 3000);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        destroy = true;
        super.onDestroy();
    }

    public void getStaticData() {
        Api.getLibel(this, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                SharedPreferencesUtils.getinstance().setStringValue("libel", data.toString());
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
        Api.getClassify(this, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                SharedPreferencesUtils.getinstance().setStringValue("classifty", data.toString());

            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }
}
