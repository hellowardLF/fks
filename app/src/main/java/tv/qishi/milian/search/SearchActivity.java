package tv.qishi.milian.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiEditActivity;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.model.MyVideoItemModle;
import tv.qishi.milian.own.UserMainActivity;
import tv.qishi.milian.own.fans.FansItem;
import tv.qishi.milian.utils.Api;

public class SearchActivity extends BaseSiSiEditActivity implements OnRecyclerViewItemClickListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.edit_input_keyword)
    EditText mEditInputKeyword;
    @Bind(R.id.noDataLayout_search)
    RelativeLayout mNodataView;
    @Bind(R.id.search_key)
    TextView mSearchKey;
    private String keyWords;
    private List<MyVideoItemModle> result;
    private SearchAdapter mSearchAdapter;

    @OnClick(R.id.image_back)

    public void back(View view) {
        SearchActivity.this.finish();
    }

    @OnClick(R.id.btn_search)
    public void search(View view) {
        keyWords = mEditInputKeyword.getText().toString();
        if (StringUtils.isEmpty(keyWords)) {
            toast("请输入要搜索的用户名");
            return;
        }
        if (result != null) {
            result.clear();
        }
        mNodataView.setVisibility(View.GONE);
        getData(keyWords);
    }

    private void getData(final String keyWords) {
        result.clear();
        mSearchAdapter.notifyDataSetChanged();
        String token = (String) SharePrefsUtils.get(this, "user", "token", "");
        JSONObject params = new JSONObject();
        params.put("token", token);
        params.put("keyword", keyWords);
//        params.put("limit_begin", limit_begin);
//        params.put("limit_num", limit_num);
        Api.getSearchWords(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray data1 = data.getJSONArray("data");
                if (data1!=null){
                    for (int i = 0; i <data1.size() ; i++) {
                        result.add(JSON.parseObject(data1.getJSONObject(i).toString(),MyVideoItemModle.class));
                    }
                }
                if(result.size()==0){
                    mSearchAdapter.notifyDataSetChanged();
                    //toast(msg);
                    //加载空数据图
                    if (result.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                        mSearchKey.setText("\""+keyWords+"\"");
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }
                mSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                if(result.size()==0){
                    mSearchAdapter.notifyDataSetChanged();
                    //toast(msg);
                    //加载空数据图
                    if (result.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                        mSearchKey.setText("\""+keyWords+"\"");
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }else{
                    toast("没有更多数据了");
                }

            }
        });

    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isActive)
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result = new ArrayList<>();
        if (mSearchAdapter == null) {
            final LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mSearchAdapter = new SearchAdapter(this, result);
            mRecyclerView.setAdapter(mSearchAdapter);
//            mSearchAdapter.setOnRecyclerViewItemClickListener(this);
            mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                boolean isSlidingToLast = false;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("MainViewPagerFragment", "--------onScrollStateChanged");
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();
                        if (lastVisibleItemPosition == (totalItemCount - 1) && isSlidingToLast) {
                            // toast("没有更多数据了~~");
                            getData(keyWords);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.e("MainViewPagerFragment", "--------onScrolled=dx=" + dx + "---dy=" + dy);
                    if (dy > 0) {
                        isSlidingToLast = true;
                    } else {
                        isSlidingToLast = false;
                    }
                }
            });
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }
}
