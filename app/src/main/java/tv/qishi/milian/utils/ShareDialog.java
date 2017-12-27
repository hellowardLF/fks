package tv.qishi.milian.utils;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.home.adapter.ShareItemAdapter;
import tv.qishi.milian.home.model.ShareItemBean;
import tv.qishi.milian.kuaishou.model.ShareModle;
import tv.qishi.milian.view.MyGridView;

/**
 * @author fengjing:
 * @function 分享dialog
 * @date ：2015年10月10日 下午2:16:54
 * @mail 164303256@qq.com
 */
public class ShareDialog extends Dialog {
    private List<ShareItemBean> list;
    private ShareItemAdapter adapter;
    private MyGridView gv;
    private TextView text_cancel;
    private Context mContext;
    private ShareUtils shareUtils;
    private ShareModle modle;


    public ShareDialog(Context context, ShareModle modle) {
        super(context, R.style.commondialogstyle);
        this.mContext = context;
        this.modle=modle;
        this.setContentView(R.layout.layout_dialog_share);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (context.getResources().getDisplayMetrics().widthPixels); // 设置宽度
        this.getWindow().setAttributes(lp);
        this.getWindow().setGravity(Gravity.BOTTOM);
        gv = (MyGridView) this.findViewById(R.id.dialog_share_gv);
        text_cancel = (TextView) this.findViewById(R.id.dialog_share_cancel);
        list = new ArrayList<ShareItemBean>();
        list.add(new ShareItemBean(R.drawable.icon_share_qq, "QQ好友"));
        list.add(new ShareItemBean(R.drawable.icon_share_zoon, "QQ空间"));
        list.add(new ShareItemBean(R.drawable.icon_share_wx, "微信好友"));
        list.add(new ShareItemBean(R.drawable.icon_share_friend, "微信朋友圈"));
        list.add(new ShareItemBean(R.drawable.icon_share_sina, "新浪微博"));
        list.add(new ShareItemBean(R.drawable.icon_share_copy, "复制链接"));
        adapter = new ShareItemAdapter(context, list);
        gv.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.this.dismiss();
            }
        });
        gv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shareUtils = new ShareUtils(mContext,modle);
                switch (position) {
                    case 0:
                        shareUtils.shareToQQ();
                        break;

                    case 1:
                        shareUtils.shareToQZone();
                        break;
                    case 2:
//                        Toast.makeText(mContext, "暂无账号", Toast.LENGTH_SHORT).show();
                        shareUtils.shareToWechat(0);
                        break;
                    case 3:
//                        Toast.makeText(mContext, "暂无账号", Toast.LENGTH_SHORT).show();
                        shareUtils.shareToWechat(1);
                        break;
                    case 4:
                        shareUtils.shareToSinaWibo();
                        break;
                    case 5:
                        shareUtils.copyLink();
                        break;
                    default:
                        break;
                }
                ShareDialog.this.dismiss();
            }
        });
    }

}
