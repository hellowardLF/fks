package tv.qishi.milian.own.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.FixPhoneActivity;
import tv.qishi.milian.login.LoginActivity;
import tv.qishi.milian.login.LoginByPhoneActivity;
import tv.qishi.milian.login.ModifyPasswordActivity;
import tv.qishi.milian.own.userinfo.MobileOutActivity;
import tv.qishi.milian.utils.Api;

public class SettingActivity extends BaseSiSiActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        SettingActivity.this.finish();
    }

    @OnClick(R.id.linear_about_container)
    public void about(View view) {
        openActivity(AboutActivity.class);
    }

    @OnClick(R.id.linear_feedback_container)
    public void feedback(View view) {//修改手机号
//        openActivity(FeedbackActivity.class);
        openActivity(MobileOutActivity.class);
    }

    @OnClick(R.id.linear_modify_password_container)
    public void modifyPassword(View view) {
        openActivity(ModifyPasswordActivity.class);
    }

    @OnClick(R.id.linear_version_container)
    public void checkUpdate(){
        toast("目前很干净");
    }

    @OnClick(R.id.btn_sign_out)
    public void signOut(View view) {
        SharePrefsUtils.clear(this, "user");
//        LoginByPhoneActivity.newInstance(this);
//        SettingActivity.this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefsUtils.get(this, "user", "token", "").equals("")) {
            LoginByPhoneActivity.newInstance(this);
            SettingActivity.this.finish();
        }
        mTextTopTitle.setText("系统设置");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_setting;
    }
}
