package tv.qishi.milian.kuaishou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.PlayActivity;
import tv.qishi.milian.kuaishou.ReleaseActivity;
import tv.qishi.milian.kuaishou.model.HomeInfoModel;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.ShareDialog;


/**
 * Created by hxj on 2017/11/8.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context mContext;
    List<HomeInfoModel> data;
    int start;

    /**
     *
     * @param start  0位首页   1关注列表
     */
    public HomeAdapter(Activity mContext, List<HomeInfoModel> mData,int start) {
        this.mContext = mContext;
        data = mData;
        this.start=start;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_video, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder holder, final int position) {
        final HomeInfoModel homeInfoModel = data.get(position);
        holder.commentNumber.setText(homeInfoModel.getComments() + "");
        holder.like.setText(homeInfoModel.getHits() + "");
        holder.title.setText(homeInfoModel.getVideo_title());
        holder.name.setText(homeInfoModel.getUser_nicename());
        Glide.with(mContext).load(homeInfoModel.getVideo_thumb()).asBitmap().centerCrop().into(holder.img);
        Glide.with(mContext).load(homeInfoModel.getAvatar()).asBitmap().into(new BitmapImageViewTarget(holder.avatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.avatar.setImageDrawable(circularBitmapDrawable);
            }
        });
//        Glide.with(mContext).load(homeInfoModel.getAvatar()).asBitmap().into(holder.avatar);
        if (data.get(position).getIs_favorites()==0){
            holder.foucs.setBackgroundResource(R.drawable.zhibo_guanzhu);
        }else {
            holder.foucs.setBackgroundResource(R.drawable.zhibo_guanzhu_s);
        }
        holder.foucsNumber.setText(homeInfoModel.getFavorites()+"");
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayActivity.class);
                if (start==0){
                    intent .putExtra("video_id", homeInfoModel.getId());
                }else {
                    intent .putExtra("video_id", homeInfoModel.getVideo_id());
                }
                mContext.startActivity(intent);
            }
        });
    /*    holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });*/
        holder.dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.likeclick(position);
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.mCommentClick(position);
                }
            }
        });
        holder.mFoucs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.mFoucsClick(position, holder.foucs);
                }
            }
        });
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mOnItemClick!=null){
                  mOnItemClick.mShareClick(position);
              }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClick {
        void likeclick(int postion);

        void mCommentClick(int postion);

        void mFoucsClick(int postion, ImageView imageView);
        void mShareClick(int posttion);
    }

    private OnItemClick mOnItemClick;

    public void setmOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, avatar, foucs, likeImage;
        LinearLayout  dianzan, comment, mFoucs,mShare;
        TextView name, title, commentNumber, like, foucsNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            mShare= (LinearLayout) itemView.findViewById(R.id.ll_share);
            dianzan = (LinearLayout) itemView.findViewById(R.id.ll_msg);
            img = (ImageView) itemView.findViewById(R.id.img);
            avatar = (ImageView) itemView.findViewById(R.id.homeinfo_avatar);
            title = (TextView) itemView.findViewById(R.id.homeinfo_title);
            name = (TextView) itemView.findViewById(R.id.homeinfo_name);
            like = (TextView) itemView.findViewById(R.id.homeinfo_number);
            commentNumber = (TextView) itemView.findViewById(R.id.homeinfo_commont);
            comment = (LinearLayout) itemView.findViewById(R.id.ll_attention);
            mFoucs = (LinearLayout) itemView.findViewById(R.id.ll_focus);
            foucs = (ImageView) itemView.findViewById(R.id.ll_focus_islive);
            likeImage = (ImageView) itemView.findViewById(R.id.homeinfo_likeimage);
            foucsNumber = (TextView) itemView.findViewById(R.id.ll_focus_islive_number);

        }
    }
}
