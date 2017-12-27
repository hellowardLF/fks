package tv.qishi.milian.kuaishou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.model.CommentModel;
import tv.qishi.milian.kuaishou.model.ReplyBean;
import tv.qishi.milian.kuaishou.model.VideInfoModel;
import tv.qishi.milian.kuaishou.view.MyListView;

/**
 * Created by hxj on 2017/11/14.
 */

public class MsgAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<CommentModel> data;
    private List<VideInfoModel> modle;
    private List<ListViewAdapter> list;

    public MsgAdapter(Context mContext, List<CommentModel> data, List<VideInfoModel> modle) {
        this.mContext = mContext;
        this.data = data;
        this.modle = modle;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1) {
            viewHolder = new TopViewHolde(LayoutInflater.from(mContext).inflate(R.layout.item_msg_top, parent, false));
        } else {
            viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false));
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolder) {
            if (data.size() > 0) {
                ViewHolder holder = (ViewHolder) viewHolder;
                CommentModel commentModel = data.get(position - 1);
                final ViewHolder mViewHodle = holder;
                Glide.with(mContext).load(commentModel.getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mViewHodle.mAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mViewHodle.mAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
                holder.mContent.setText(commentModel.getContent());
                holder.mName.setText(commentModel.getUser_nicename());
                holder.mTime.setText(commentModel.getCreate_time());
                holder.mSendMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClick != null) {
                            itemClick.mItemClick(position - 1);
                        }
                    }
                });
                List<ReplyBean> reply = commentModel.getReply();
                if (reply == null) {
                    reply = new ArrayList<>();
                }
                ListViewAdapter adapter = new ListViewAdapter(mContext, reply);
                list.add(adapter);
                holder.mListView.setAdapter(adapter);
            }
        } else {
            if (modle.size() != 0) {
                final TopViewHolde holde = (TopViewHolde) viewHolder;
                Glide.with(mContext).load(modle.get(0).getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holde.mAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holde.mAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
//        Glide.with(this).load(videInfoModel.getAvatar()).into(mAvatar);
                holde.mVideoTitle.setText(modle.get(0).getVideo_title());
                holde.mName.setText(modle.get(0).getUser_nicename());
                holde.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClick!=null){
                            itemClick.mComment();
                        }
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        LinearLayout mLayout;
        TextView mName, mContent, mTime, mSendMsg;
        ImageView mAvatar;
        MyListView mListView;

        public ViewHolder(View itemView) {
            super(itemView);
//            mLayout = (LinearLayout) itemView.findViewById(R.id.ll_zan);
            mAvatar = (ImageView) itemView.findViewById(R.id.comment_item_avatar);
            mContent = (TextView) itemView.findViewById(R.id.comment_item_content);
            mTime = (TextView) itemView.findViewById(R.id.comment_item_time);
            mName = (TextView) itemView.findViewById(R.id.comment_item_title);
            mSendMsg = (TextView) itemView.findViewById(R.id.comment_item_sendmsg);
            mListView = (MyListView) itemView.findViewById(R.id.comment_list);
        }
    }

    class TopViewHolde extends RecyclerView.ViewHolder {
        ImageView mAvatar;
        TextView mName,comments;
        TextView mVideoTitle;
        TextView mContent;

        public TopViewHolde(View itemView) {
            super(itemView);
            comments= (TextView) itemView.findViewById(R.id.play_comments);
            mAvatar = (ImageView) itemView.findViewById(R.id.play_avatar);
            mName = (TextView) itemView.findViewById(R.id.play_name);
            mVideoTitle = (TextView) itemView.findViewById(R.id.play_title);
            mContent = (TextView) itemView.findViewById(R.id.play_content);
        }
    }

    public interface ItemClick {
        void mItemClick(int postion);
        void mComment();
    }

    public ListViewAdapter getAdapter(int postion) {
        return list.get(postion);
    }
    public List<CommentModel> getListView(){
        return data;
    }

    private ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }
}
