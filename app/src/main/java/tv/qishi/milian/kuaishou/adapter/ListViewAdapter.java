package tv.qishi.milian.kuaishou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.Collections;
import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.model.ReplyBean;

/**
 * Created by 123456 on 2017/12/14.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<ReplyBean> list;
    private Context mContext;


    public ListViewAdapter(Context mContext, List<ReplyBean> reply) {
        this.mContext = mContext;
        list=reply;
        Collections.reverse(list);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LisrViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_listview,parent,false);
            viewHolder=new LisrViewHolder();
            viewHolder.textView= (TextView) convertView.findViewById(R.id.listview_name);
            viewHolder.content= (TextView) convertView.findViewById(R.id.listview_content);
            viewHolder.creatTime= (TextView) convertView.findViewById(R.id.listview_creat);
            viewHolder.mAvatar= (ImageView) convertView.findViewById(R.id.listview_avatar);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (LisrViewHolder) convertView.getTag();
        }
        if (list.get(position).getTo_uid().equalsIgnoreCase(list.get(position).getUid())){
            viewHolder.textView.setText(list.get(position).getTo_user_nicename()+" 说: ");
        }else {
            viewHolder.textView.setText(list.get(position).getUser_nicename()+" 回复  "+list.get(position).getTo_user_nicename()+" : ");

        }
        viewHolder.content.setText(list.get(position).getContent());
        viewHolder.creatTime.setText(list.get(position).getCreate_time());
        final LisrViewHolder finalViewHolder = viewHolder;
        Glide.with(mContext).load(list.get(position).getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(finalViewHolder.mAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                finalViewHolder.mAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });
        return convertView;
    }

    class LisrViewHolder{
        TextView textView,content,creatTime;
        ImageView mAvatar;
    }
}
