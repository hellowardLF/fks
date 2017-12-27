package tv.qishi.milian.kuaishou.ksykit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.ksykit.modle.ClassifyModle;


public class LibelAdapter extends RecyclerView.Adapter<LibelAdapter.ViewHolder>{
    private Context mContext;
    private List<ClassifyModle> mList;
    public LibelAdapter(Context context,List<ClassifyModle> list){
        mContext=context;
        mList=list;
    }
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mList==null?0:mList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ClassifyModle modle = mList.get(position);
        String path=modle.getName();
        viewHolder.text.setText(path);
        if (modle.getIsPitch()==0){
            viewHolder.text.setBackgroundResource(R.drawable.bg_n);
        }else {
            viewHolder.text.setBackgroundResource(R.drawable.bg_s);
        }
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modle.getIsPitch()==0){
                    modle.setIsPitch(1);
                    viewHolder.text.setBackgroundResource(R.drawable.bg_s);
                }else {
                    modle.setIsPitch(0);
                    viewHolder.text.setBackgroundResource(R.drawable.bg_n);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int pposition) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_libeladapter,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.text=(TextView) view.findViewById(R.id.libel_text);
        return viewHolder;
    }


    public List<ClassifyModle>  getmList(){
        return mList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }



}
