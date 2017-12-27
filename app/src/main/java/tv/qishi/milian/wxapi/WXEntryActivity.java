package tv.qishi.milian.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.google.gson.Gson;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;

import org.json.JSONException;
import org.json.JSONObject;

import tv.qishi.milian.R;
import tv.qishi.milian.pay.Constants;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.wxapi.WxOauthBean;

public class WXEntryActivity extends Activity {


    private WxOauthBean wxOauthBean;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wxentry);

        wxOauthBean = new WxOauthBean();

        gson = new Gson();
        handleIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new Resp(intent.getExtras());
        // 判断是否为分享回调 是则return
        if (TextUtils.isEmpty(resp.code) || resp.errCode != BaseResp.ErrCode.ERR_OK) {
            finish();
            return;
        }
    }


}
