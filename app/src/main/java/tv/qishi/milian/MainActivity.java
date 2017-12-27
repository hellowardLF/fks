package tv.qishi.milian;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.media.shortvideo.utils.AuthInfoManager;
import com.smart.androidui.widget.menu.BottomTabMenu;
import com.smart.androidui.widget.menu.OnTabClickListener;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.ksykit.modle.ClassifyModle;
import tv.qishi.milian.kuaishou.ksykit.util.HttpRequestTask;
import tv.qishi.milian.login.LoginByPhoneActivity;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.MyLogUtils;
import tv.qishi.milian.utils.SharedPreferencesUtils;

import static tv.qishi.milian.kuaishou.Contants.AUTH_SERVER_URI;

public class MainActivity extends BaseSiSiActivity {
    private static final String TAG = "MainActivity";

    @Bind(R.id.bottomTabMenu)
    BottomTabMenu mBottomTabMenu;
    private Fragment[] mTabFragment = new Fragment[3];
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private Handler mMainHandler;
    //config params
    //auth
    private HttpRequestTask mAuthTask;  //SDK鉴权异步任务
    private HttpRequestTask.HttpResponseListener mAuthResponse;
    private static final int MAX_RETRY_COUNT = 3;  //若AKSK请求失败尝试3次
    private int mRetryCount = 0;
    private List<ClassifyModle> mClassifyList;
    private List<ClassifyModle> mLibel;
//    @OnClick(R.id.image_start_living)
//    public void startLiving(View view) {
//        openActivity(PublishActivity.class);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words", "Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    MyLogUtils.d("saved", "success!");
//                }
//            }
//        });
        if ("".equals(SharePrefsUtils.get(this, "user", "token", "")) || "".equals(SharePrefsUtils.get(this, "user", "userId", ""))) {
            // LoginByPhoneActivity.newInstance(this);
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            LoginByPhoneActivity.newInstance(this);
            finish();
            return;
        }
        mClassifyList = new ArrayList<>();
        mLibel = new ArrayList<>();
        getClassify();
        getLibel();
        mMainHandler = new Handler();
        setDoubleBack(true);
        mFragmentManager = getSupportFragmentManager();
        mTabFragment[0] = mFragmentManager.findFragmentById(R.id.fragment_home);
        mTabFragment[1] = mFragmentManager.findFragmentById(R.id.fragment_attention);
        mTabFragment[2] = mFragmentManager.findFragmentById(R.id.fragment_own);
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]).hide(mTabFragment[2]);
        mTransaction.show(mTabFragment[0]).commit();
        mBottomTabMenu.addTextImageTab(R.drawable.zhibo_shou, R.drawable.zhibo_shou_s, "首页", true);
        mBottomTabMenu.addTextImageTab(R.drawable.zhibo_guanzhu, R.drawable.zhibo_guanzhu_s, "关注", true);
        mBottomTabMenu.addTextImageTab(R.drawable.zhibo_me, R.drawable.zhibo_me_s, "我的", false);
        mBottomTabMenu.changeImageTab(0);
        mBottomTabMenu.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(View view, int position) {
                mTransaction = mFragmentManager.beginTransaction();
                mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]).hide(mTabFragment[2]);
                mTransaction.show(mTabFragment[position]).commit();
            }
        });

        JSONObject params = new JSONObject();
        try {
            String versionCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            params.put("ver_num", versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Api.checkUpdate(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if (StringUtils.isNotEmpty(info.getString("package"))) {
                    checkUpgrade(info.getString("package"), info.getString("description"));
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
            }
        });
        checkAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLibel.size()==0){
            getLibel();
        }
        if (mClassifyList.size()==0){
            getClassify();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    AlertDialog tipsAlertDialog;

    private void checkUpgrade(final String downloadUrl, String mes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        tipsAlertDialog = builder.setTitle("提示")
                .setMessage(mes)
                .setNegativeButton("再等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipsAlertDialog.isShowing()) {
                            tipsAlertDialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("更新下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(downloadUrl);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create();
        tipsAlertDialog.show();
        tipsAlertDialog.setCancelable(false);
        tipsAlertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }


    private AuthInfoManager.CheckAuthResultListener mCheckAuthResultListener =
            new AuthInfoManager.CheckAuthResultListener() {
                @Override
                public void onAuthResult(int mI) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AuthInfoManager.getInstance().removeAuthResultListener(mCheckAuthResultListener);
                            if (AuthInfoManager.getInstance().getAuthState()) {
//                                Toast.makeText(MainActivity.this, "Auth Success", Toast.LENGTH_SHORT)
//                                        .show();
                            } else {
//                                Toast.makeText(MainActivity.this, "Auth Failed", Toast.LENGTH_SHORT)
//                                        .show();
                            }
                        }
                    });
                }
            };

    /**
     * SDK鉴权
     */
    private void checkAuth() {
        String token = null;
        try {
            InputStream in = getResources().getAssets().open("AuthForTest.pkg");

            int length = in.available();

            byte[] buffer = new byte[length];

            in.read(buffer);

            token = EncodingUtils.getString(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(token)) {
            AuthInfoManager.getInstance().setAuthInfo(token);
            AuthInfoManager.getInstance().checkAuth();
            if (AuthInfoManager.getInstance().getAuthState()) {
//                Toast.makeText(MainActivity.this, "Auth Success", Toast.LENGTH_SHORT)
//                        .show();
            } else {
//                Toast.makeText(MainActivity.this, "Auth Failed", Toast.LENGTH_SHORT)
//                        .show();
            }
        } else {
            AuthInfoManager.getInstance().addAuthResultListener(new AuthInfoManager.CheckAuthResultListener() {
                @Override
                public void onAuthResult(int i) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AuthInfoManager.getInstance().removeAuthResultListener(mCheckAuthResultListener);
                            if (AuthInfoManager.getInstance().getAuthState()) {
                                Log.e(TAG, "run:  Auth Success " );
//                                Toast.makeText(MainActivity.this, "Auth Success", Toast.LENGTH_SHORT)
//                                        .show();
                            } else {
//                                Toast.makeText(MainActivity.this, "Auth Failed", Toast.LENGTH_SHORT)
//                                        .show();
                                Log.e(TAG, "run: Auth Failed" );
                            }
                        }
                    });
                }
            });
            if (mAuthResponse == null) {
                mAuthResponse = new HttpRequestTask.HttpResponseListener() {
                    @Override
                    public void onHttpResponse(int responseCode, String response) {
                        //params response
                        boolean authResult = false;
                        if (responseCode == 200) {
                            try {
                                org.json.JSONObject temp = new org.json.JSONObject(response);
                                org.json.JSONObject data = temp.getJSONObject("Data");
                                int result = data.getInt("RetCode");
                                if (result == 0) {
                                    String authInfo = data.getString("Authorization");
                                    String date = data.getString("x-amz-date");
                                    //初始化鉴权信息
                                    AuthInfoManager.getInstance().setAuthInfo(authInfo, date);
                                    //添加鉴权结果回调接口(不是必须)
                                    AuthInfoManager.getInstance().addAuthResultListener(mCheckAuthResultListener);
                                    //开始向KSServer申请鉴权
                                    AuthInfoManager.getInstance().checkAuth();
                                    authResult = true;
                                } else {
                                    MyLogUtils.e("get auth failed from app server RetCode:" + result);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                MyLogUtils.e("get auth failed from app server json parse failed");
                            }
                        } else {
                            MyLogUtils.e("get auth failed from app server responseCode:" + responseCode);
                        }

                        final boolean finalAuthResult = authResult;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!finalAuthResult) {
                                    Toast.makeText(MainActivity.this, "get auth failed from app " +
                                            "server", Toast.LENGTH_SHORT)
                                            .show();
                                    //鉴权失败，尝试3次
                                    if (mRetryCount < MAX_RETRY_COUNT) {
                                        mRetryCount++;
                                        checkAuth();
                                    }
                                }
                            }
                        });
                    }
                };
            }

            if (mAuthTask != null && mAuthTask.getStatus() != AsyncTask.Status.FINISHED) {
                mAuthTask.cancel(true);
                mAuthTask = null;
            }
            //开启异步任务，向AppServer请求鉴权信息
            mAuthTask = new HttpRequestTask(mAuthResponse);
            String url = AUTH_SERVER_URI + "?Pkg=" +
                    getApplicationContext().getPackageName();
            MyLogUtils.d("request auth:" + url);
            mAuthTask.execute(url);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask.release();
            mAuthTask = null;
        }
        AuthInfoManager.getInstance().removeAuthResultListener(mCheckAuthResultListener);
    }

    public void getClassify() {
        String classifty = SharedPreferencesUtils.getinstance().getStringValue("classifty");
        if (!TextUtils.isEmpty(classifty)) {
            JSONObject object = JSON.parseObject(classifty);
            JSONArray data1 = object.getJSONArray("data");
            for (int i = 0; i < data1.size(); i++) {
                JSONObject jsonObject = data1.getJSONObject(i);
                ClassifyModle modle = new ClassifyModle();
                modle.setName(jsonObject.getString("name"));
                modle.setTerm_id(jsonObject.getString("term_id"));
                modle.setIsPitch(0);
                mClassifyList.add(modle);
            }
        } else {
            Api.getLibel(this, new JSONObject(), new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    SharedPreferencesUtils.getinstance().setStringValue("classifty",data.toString());
                    JSONArray data1 = data.getJSONArray("data");
                    for (int i = 0; i < data1.size(); i++) {
                        JSONObject jsonObject = data1.getJSONObject(i);
                        ClassifyModle modle = new ClassifyModle();
                        modle.setName(jsonObject.getString("name"));
                        modle.setTerm_id(jsonObject.getString("term_id"));
                        modle.setIsPitch(0);
                        mClassifyList.add(modle);
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }
    }
    public void getLibel() {
        String libel = SharedPreferencesUtils.getinstance().getStringValue("libel");
        if (!TextUtils.isEmpty(libel)) {
            JSONObject object = JSON.parseObject(libel);
            JSONArray data1 = object.getJSONArray("data");
            for (int i = 0; i < data1.size(); i++) {
                JSONObject jsonObject = data1.getJSONObject(i);
                ClassifyModle modle = new ClassifyModle();
                modle.setName(jsonObject.getString("name"));
                modle.setTerm_id(jsonObject.getString("term_id"));
                modle.setIsPitch(0);
                mLibel.add(modle);
            }
        } else {
            Api.getLibel(this, new JSONObject(), new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    SharedPreferencesUtils.getinstance().setStringValue("libel",data.toString());
                    JSONArray data1 = data.getJSONArray("data");
                    for (int i = 0; i < data1.size(); i++) {
                        JSONObject jsonObject = data1.getJSONObject(i);
                        ClassifyModle modle = new ClassifyModle();
                        modle.setName(jsonObject.getString("name"));
                        modle.setTerm_id(jsonObject.getString("term_id"));
                        modle.setIsPitch(0);
                        mLibel.add(modle);
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }
    }
}
