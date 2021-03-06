package tv.qishi.milian.kuaishou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiFragment;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.login.LoginByPhoneActivity;
import tv.qishi.milian.own.UserMainActivity;
import tv.qishi.milian.own.fans.FansItem;
import tv.qishi.milian.own.fans.FansListAdapter;
import tv.qishi.milian.utils.Api;

public class FansFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {

    @Bind(R.id.swipeRefreshLayout_fans)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_fans)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_fans)
    RelativeLayout mNodataView;
    private FansListAdapter mFansListAdapter;
    private ArrayList<FansItem> mFansItems;


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);*/
            //mFansItems.clear();
            getData(0, 20, mSwipeRefreshLayout);

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFansItems == null) {
            mFansItems = new ArrayList<>();
        }
        mFansItems.clear();
    }
       /* for (int i = 0; i < 20; i++) {
            FansItem fansItem = new FansItem();
            mFansItems.add(fansItem);
        }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setRefreshing(true);
        getData(0, 20, mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mFansListAdapter = new FansListAdapter(getActivity(), mFansItems);
        mRecyclerView.setAdapter(mFansListAdapter);
        mFansListAdapter.setOnRecyclerViewItemClickListener(this);
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
                        getData(totalItemCount, 20, null);
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

    private void getData(int limit_begin, int limit_num, final SwipeRefreshLayout mSwipeRefreshLayout) {
        String token = (String) SharePrefsUtils.get(getActivity(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(getActivity(), "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            requestParams.put("limit_begin", limit_begin);
            requestParams.put("limit_num", limit_num);
            Api.getUserFansList(getActivity(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mFansItems.clear();
                    }
                    JSONArray list = data.getJSONArray("data");
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        FansItem fansItem = new FansItem();
                        fansItem.setId(item.getString("id"));
                        fansItem.setAttention_status(item.getString("attention_status"));
                        fansItem.setAvatar(item.getString("avatar"));
                        fansItem.setIs_truename(item.getString("is_truename"));
                        fansItem.setSex(item.getString("sex"));
                        fansItem.setChannel_status(item.getString("channel_status"));
                        fansItem.setSignature(item.getString("signature"));
                        fansItem.setUser_level(item.getString("user_level"));
                        fansItem.setUser_nicename(item.getString("user_nicename"));
                        mFansItems.add(fansItem);
                    }
                    mFansListAdapter.notifyDataSetChanged();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    //toast(msg);
                    //加载空数据图
                    if (mFansItems.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            LoginByPhoneActivity.newInstance(getActivity());
            getActivity().finish();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_fans;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }
}
