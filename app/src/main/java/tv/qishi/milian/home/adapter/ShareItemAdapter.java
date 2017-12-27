package tv.qishi.milian.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.home.model.ShareItemBean;

/**
 * Created by 123456 on 2017/12/15.
 */

public class ShareItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShareItemBean> mList;

    public ShareItemAdapter(Context context,List<ShareItemBean> list){
        this.mContext=context;
        this.mList=list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.layout_item_share, null);
        ImageView image=(ImageView) convertView.findViewById(R.id.share_item_img);
        image.setImageResource(mList.get(position).getId());
        TextView text=(TextView) convertView.findViewById(R.id.share_item_text);
        text.setText(mList.get(position).getName());
        return convertView;
    }

}