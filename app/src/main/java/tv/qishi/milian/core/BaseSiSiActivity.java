package tv.qishi.milian.core;

import com.smart.androidutils.activity.BaseActivity;

import tv.qishi.milian.R;
import tv.qishi.milian.utils.ToastHelper;

/**
 * Created by fengjh on 16/9/12.
 */
public abstract class BaseSiSiActivity extends BaseActivity {


    @Override
    public void toast(String s) {
//        super.toast(s);
//        ToastUtils.makeText(this, s).show();
        ToastHelper.makeText(this, s, ToastHelper.LENGTH_SHORT).setAnimation(R.style.Animation_Toast).show();
    }

}
