package tv.qishi.milian.kuaishou;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.androidutils.activity.BaseActivity;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.MainActivity;
import tv.qishi.milian.R;

/**
 * Created by hxj on 2017/11/3.
 */

public class PaoPaoActivity extends BaseActivity {

    public static final String TAG = "PaoPaoActivity";
    @Bind(R.id.skip)
    TextView mSkip;
    @Bind(R.id.textView)
    TextView mTextView;
    @Bind(R.id.img_live)
    ImageView mImgLive;
    @Bind(R.id.img_meirong)
    ImageView mImgMeirong;
    @Bind(R.id.img_chuyi)
    ImageView mImgChuyi;
    @Bind(R.id.img_gaoxiao)
    ImageView mImgGaoxiao;
    @Bind(R.id.img_jianshen)
    ImageView mImgJianshen;
    @Bind(R.id.img_jiepai)
    ImageView mImgJiepai;
    boolean[] background;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_wel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        background = new boolean[5];
        for (int i = 0; i < background.length; i++) {
            background[i] = false;
        }
    }

    public void onSkip(View mView) {
        SharePrefsUtils.put(this, "user", TAG, false);
        openActivity(MainActivity.class);
        finish();
    }

    public void onComplete(View mView) {
        boolean canComplete=false;
        for (boolean b :
                background) {
            if (b) canComplete=true;
        }
        if (!canComplete) {toast("请选择您所感兴趣的");return;}
        SharePrefsUtils.put(this, "user", TAG, false);
        openActivity(MainActivity.class);
        finish();
    }

    @OnClick({R.id.img_meirong, R.id.img_chuyi, R.id.img_gaoxiao, R.id.img_jianshen, R.id.img_jiepai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_meirong:
                background[0] = !background[0];
                if (background[0]) {
                    mImgMeirong.setBackgroundResource(R.drawable.paopao_shape);
                } else mImgMeirong.setBackgroundResource(0);
                break;
            case R.id.img_chuyi:
                background[1] = !background[1];
                if (background[1]) {
                    mImgChuyi.setBackgroundResource(R.drawable.paopao_shape);
                } else mImgChuyi.setBackgroundResource(0);
                break;
            case R.id.img_gaoxiao:
                background[2] = !background[2];
                if (background[2]) {
                    mImgGaoxiao.setBackgroundResource(R.drawable.paopao_shape);
                } else mImgGaoxiao.setBackgroundResource(0);
                break;
            case R.id.img_jianshen:
                background[3] = !background[3];
                if (background[3]) {
                    mImgJianshen.setBackgroundResource(R.drawable.paopao_shape);
                } else mImgJianshen.setBackgroundResource(0);
                break;
            case R.id.img_jiepai:
                background[4] = !background[4];
                if (background[4]) {
                    mImgJiepai.setBackgroundResource(R.drawable.paopao_shape);
                } else mImgJiepai.setBackgroundResource(0);
                break;
        }
    }
}
