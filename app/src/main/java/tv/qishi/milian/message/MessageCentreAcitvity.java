package tv.qishi.milian.message;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;

/**
 * Created by 123456 on 2017/12/13.
 */

public class MessageCentreAcitvity  extends BaseSiSiActivity{
    @Bind(R.id.text_top_title)
    TextView mTitle;
    @Bind(R.id.image_top_back)
    ImageView back;
    @Bind(R.id.message_centre_recycler)
    RecyclerView mMessageList;
    @Override
    public int getLayoutResource() {
        return R.layout.activity_message_centre;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle.setText("消息中心");
        initListener();

    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
