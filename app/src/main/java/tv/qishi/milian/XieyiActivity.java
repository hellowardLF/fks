package tv.qishi.milian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import tv.qishi.milian.core.BaseSiSiEditActivity;

import butterknife.OnClick;

public class XieyiActivity extends BaseSiSiEditActivity {

    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    public static void newInstance(Context context) {
        ((Activity) context).startActivity(new Intent(context, XieyiActivity.class));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_xieyi;
    }
}
