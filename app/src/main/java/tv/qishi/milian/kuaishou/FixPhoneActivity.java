package tv.qishi.milian.kuaishou;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;

/**
 *
 * Created by hxj on 2017/11/14.
 */

public class FixPhoneActivity extends BaseSiSiActivity {
    @Bind(R.id.image_back)
    ImageView mImageBack;
    @Bind(R.id.edit_input_mobile)
    EditText mEditInputMobile;
    @Bind(R.id.edit_input_code)
    EditText mEditInputCode;
    @Bind(R.id.btn_register_code)
    Button mBtnRegisterCode;
    @Bind(R.id.edit_input_password)
    EditText mEditInputPassword;
    @Bind(R.id.iv_showpwd)
    ImageView mIvShowpwd;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_fix_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick({R.id.image_back, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_register:
                toast("验证码不正确");
                break;
        }
    }
}
