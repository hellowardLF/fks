package tv.qishi.milian.kuaishou;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.utils.Api;

/**
 * Created by 123456 on 2017/12/13.
 */

public class CommentActivity extends BaseSiSiActivity {
    private static final String TAG = "CommentActivity";
    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.image_top_back)
    ImageView mBack;
    @Bind(R.id.comment_edit)
    EditText mEditText;
    @Bind(R.id.comment_send)
    TextView mSend;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_comment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("评论");
        initListener();
    }

    private void initListener() {
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEditText.getText().toString().trim())){
                    toast("评论内容不能为空");
                    return;
                }
                sendComment(mEditText.getText());
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendComment(Editable text) {
        String vedio_id = getIntent().getExtras().getString("vedio_id");
        String user_id = getIntent().getExtras().getString("user_id");
        String s = (String) SharePrefsUtils.get(this, "user", "userId", "");
        if (user_id.equalsIgnoreCase(s)){
            toast("自己不能评论自己");
            return;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("token",(String) SharePrefsUtils.get(this, "user", "token", ""));
        jsonObject.put("id",vedio_id);
        jsonObject.put("content",text);
        jsonObject.put("to_uid",user_id);
       Api.addComment(this, jsonObject, new OnRequestDataListener() {
           @Override
           public void requestSuccess(int code, JSONObject data) {
               Log.e(TAG, "requestSuccess: "+data.toString());
               toast(data.getString("descrp"));
               setResult(RESULT_OK);
               finish();
           }

           @Override
           public void requestFailure(int code, String msg) {
               toast(msg);

           }
       });
    }
}
