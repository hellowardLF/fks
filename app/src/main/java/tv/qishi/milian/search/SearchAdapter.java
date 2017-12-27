package tv.qishi.milian.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.qishi.milian.R;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.PlayActivity;
import tv.qishi.milian.kuaishou.adapter.ZuoPinAdapter;
import tv.qishi.milian.kuaishou.model.MyVideoItemModle;
import tv.qishi.milian.own.fans.FansItem;
import tv.qishi.milian.own.follow.FollowItem;
import tv.qishi.milian.utils.Api;

/**
 * Created by fengjh on 16/7/31.
 */
public class SearchAdapter extends RecyclerView.Adapter {

    /* private Context mContext;
     private ArrayList<MyVideoItemModle> mFansItems;
     private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
     public SearchAdapter(Context mContext, ArrayList<MyVideoItemModle> mFansItems) {
         this.mContext = mContext;
         this.mFansItems = mFansItems;
     }

     public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
         mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
     }

     @Override
     public FansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyFouseViewHolder(LayoutInflater.from(mContext).
                 inflate(R.layout.item_zuopin, parent, false))
     }

     @Override
     public void onBindViewHolder(final FansViewHolder holder, int position) {
        final FansItem item = mFansItems.get(position);
         Glide.with(this.mContext).load(item.getAvatar())
                 .error(R.drawable.icon_avatar_default)
                 .transform(new GlideCircleTransform(this.mContext))
                 .into(holder.mFansAvatar);
         holder.mFansNicename.setText(item.getUser_nicename());
         if(StringUtils.isNotEmpty(item.getSignature()))
         holder.mFansSignature.setText(item.getSignature());
         holder.mFansLevel.setText(item.getUser_level());
         int level = Integer.parseInt(item.getUser_level());
         Drawable levelMoon1 = this.mContext.getResources().getDrawable(R.drawable.icon_small_star);
         levelMoon1.setBounds(0, 0, levelMoon1.getMinimumWidth(), levelMoon1.getMinimumHeight());
         if(level<4){
             holder.mFansLevel.setBackgroundResource(R.drawable.level1);
         }else if(level<16){
             holder.mFansLevel.setBackgroundResource(R.drawable.level2);
         }else{
             holder.mFansLevel.setBackgroundResource(R.drawable.level3);
         }
         holder.mItemContainer.setTag(item.getId());
         if("1".equals(item.getSex())){
             holder.mFansSex.setImageResource(R.drawable.userinfo_male);
         }
         if( !"0".equals(item.getIs_truename())){
             holder.mFansReal.setVisibility(View.VISIBLE);
         }
         holder.mIsLiving.setVisibility(View.GONE);
         if("2".equals(item.getChannel_status())){
             holder.mIsLiving.setVisibility(View.VISIBLE);
         }
         holder.mImageAddAttention.setVisibility(View.VISIBLE);
         holder.mFansBtnAttention.setText("关注");
         if("1".equals(item.getAttention_status())){
             //holder.mFansBtnAttention.setBackground(mContext.getResources().getDrawable(R.drawable.checkbox_follow_shape_blue));
             holder.mFansBtnAttention.setText("取消关注");
             holder.mFansBtnAttention.setTextColor(mContext.getResources().getColor(R.color.colorGrayFont));
             holder.mImageAddAttention.setVisibility(View.GONE);
         }
         holder.mFansBtnAttention.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final TextView v = (TextView)view;
                 if("取消关注".equals(v.getText())){
                     JSONObject params = new JSONObject();
                     params.put("token", SharePrefsUtils.get(mContext,"user","token",""));
                     params.put("userid",item.getId());
                     Api.cancelAttention(mContext, params, new OnRequestDataListener() {
                         @Override
                         public void requestSuccess(int code, JSONObject data) {
                             FollowItem tem = new FollowItem();
                             tem.setId(item.getId());
                             v.setText("关注");
                             v.setTextColor(mContext.getResources().getColor(R.color.home_tab1));
                             holder.mImageAddAttention.setVisibility(View.VISIBLE);
                             // mFollowItems.remove(tem);
                             // FollowListAdapter.this.notifyDataSetChanged();
                         }

                         @Override
                         public void requestFailure(int code, String msg) {
                             Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                         }
                     });
                 }else{
                     JSONObject params = new JSONObject();
                     params.put("token", SharePrefsUtils.get(mContext,"user","token",""));
                     params.put("userid",item.getId());
                     Api.addAttention(mContext, params, new OnRequestDataListener() {
                         @Override
                         public void requestSuccess(int code, JSONObject data) {
                             FollowItem tem = new FollowItem();
                             tem.setId(item.getId());
                             v.setText("取消关注");
                             v.setTextColor(mContext.getResources().getColor(R.color.colorGrayFont));
                             holder.mImageAddAttention.setVisibility(View.GONE);
                             // mFollowItems.remove(tem);
                             // FollowListAdapter.this.notifyDataSetChanged();
                         }

                         @Override
                         public void requestFailure(int code, String msg) {
                             Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                         }
                     });

                 }

             }
         });

         holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mOnRecyclerViewItemClickListener != null) {
                     mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, holder.getLayoutPosition());
                 }
             }
         });


     }

     @Override
     public int getItemCount() {
         return mFansItems.size();
     }

     class FansViewHolder extends RecyclerView.ViewHolder {
         @Bind(R.id.fans_avatar)
         ImageView mFansAvatar;
         @Bind(R.id.fans_level)
         TextView mFansLevel;
         @Bind(R.id.fans_nicename)
         TextView mFansNicename;
         @Bind(R.id.fans_sex)
         ImageView mFansSex;
         @Bind(R.id.fans_signature)
         TextView mFansSignature;
         @Bind(R.id.fans_real)
         ImageView mFansReal;
         @Bind(R.id.fans_btn_attention)
         TextView mFansBtnAttention;
         @Bind(R.id.item_container)
         LinearLayout mItemContainer;
         @Bind(R.id.is_living)
         TextView mIsLiving;
         @Bind(R.id.image_add_attention)
         ImageView mImageAddAttention;

         public FansViewHolder(View itemView) {
             super(itemView);
             ButterKnife.bind(this, itemView);
         }
     }*/
    private static final String TAG = "SearchAdapter";
    private Context mContext;
    private List<MyVideoItemModle> list;

    public SearchAdapter(Context mContext, List<MyVideoItemModle> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyFouseViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_zuopin, parent, false));

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyVideoItemModle myVideoItemModle = list.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        MyFouseViewHolder viewHolder = (MyFouseViewHolder) holder;
        viewHolder.title.setText(myVideoItemModle.getVideo_title());
        viewHolder.creatTime.setText(myVideoItemModle.getCreate_time());
        viewHolder.name.setText(myVideoItemModle.getUser_nicename());
        if (!TextUtils.isEmpty(myVideoItemModle.getVideo_time())) {
            viewHolder.videoTime.setText(getTiem(Integer.parseInt(myVideoItemModle.getVideo_time()) / 1000));
        }
        viewHolder.commentNumber.setText(myVideoItemModle.getComments() + "评论");
        Glide.with(mContext).load(myVideoItemModle.getVideo_thumb()).centerCrop().into(viewHolder.videoImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, PlayActivity.class);
                mIntent.putExtra("video_id", myVideoItemModle.getId());
                mContext.startActivity(mIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + list.size());
        return list.size();
    }

    public String getTiem(int video_time) {
        String time = "";
        String ss = String.valueOf(video_time % 60);
        String mm = String.valueOf(video_time / 60 % 60);
        time = (mm.length() == 1 ? "0" + mm : mm) + ":" + (ss.length() == 1 ? "0" + ss : ss);
        return time;
    }


    class MyFouseViewHolder extends RecyclerView.ViewHolder {
        TextView title, name, creatTime, videoTime, commentNumber;
        ImageView videoImg;

        public MyFouseViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.zuoping_title);
            name = (TextView) itemView.findViewById(R.id.zuoping_name);
            creatTime = (TextView) itemView.findViewById(R.id.zuoping_creattime);
            videoTime = (TextView) itemView.findViewById(R.id.zuoping_videotime);
            commentNumber = (TextView) itemView.findViewById(R.id.zuoping_comment);
            videoImg = (ImageView) itemView.findViewById(R.id.zuoping_img);
        }
    }
}
