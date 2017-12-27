package tv.qishi.milian.kuaishou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import tv.qishi.milian.core.BaseSiSiFragment;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.adapter.HomeAdapter;
import tv.qishi.milian.kuaishou.model.HomeInfoModel;
import tv.qishi.milian.kuaishou.model.ShareModle;
import tv.qishi.milian.login.LoginByPhoneActivity;
import tv.qishi.milian.own.UserMainActivity;
import tv.qishi.milian.own.follow.FollowActivity;
import tv.qishi.milian.own.follow.FollowItem;
import tv.qishi.milian.own.follow.FollowListAdapter;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.ShareDialog;

/**
 * Created by hxj on 2017/11/6.
 */

public class AttentionFragment extends BaseSiSiFragment implements OnRecyclerViewItemClickListener {
    private static final String TAG = "AttentionFragment";
    @Bind(R.id.swipeRefreshLayout_follow)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView_follow)
    RecyclerView mRecyclerView;
    @Bind(R.id.noDataLayout_follow)
    RelativeLayout mNodataView;
    //    private FollowListAdapter mFollowListAdapter;
    private HomeAdapter mFollowListAdapter;
    //    private ArrayList<FollowItem> mFollowItems;
    private List<HomeInfoModel> mFollowItems;
    private LinearLayoutManager manager;
    private int mCommentClickPosstion;


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            if (mFollowItems != null) {
//                mFollowItems.clear();
//            }
            getData(mSwipeRefreshLayout);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);*/

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        fl_top.setVisibility(View.GONE);
        if (mFollowItems == null) {
            mFollowItems = new ArrayList<>();
        }
        mFollowItems.clear();
       /* for (int i = 0; i < 20; i++) {
            FollowItem followItem = new FollowItem();
            mFollowItems.add(followItem);
        }*/
        mSwipeRefreshLayout.setRefreshing(true);
        getData(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mFollowListAdapter = new HomeAdapter(getActivity(), mFollowItems,1);
//        mFollowListAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mFollowListAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        initListener();
    }

    private void initListener() {
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
                        //toast("没有更多数据了~~");
                        getData(null);
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
        mFollowListAdapter.setmOnItemClick(new HomeAdapter.OnItemClick() {
            @Override
            public void likeclick(final int postion) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", SharePrefsUtils.get(AttentionFragment.this.getContext(), "user", "token", ""));
                jsonObject.put("video_id", mFollowItems.get(postion).getVideo_id());
                Api.addLikeVideo(AttentionFragment.this.getContext(), jsonObject, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        Log.e(TAG, "requestSuccess: " + data.toString());
                        mFollowItems.get(postion).setHits(mFollowItems.get(postion).getHits() + 1);
                        mFollowListAdapter.notifyDataSetChanged();
                        toast(data.getString("descrp"));
                    }

                    @Override
                    public void requestFailure(int code, String msg) {
                        toast(msg);
                    }
                });
            }

            @Override
            public void mCommentClick(int postion) {
                String s = (String) SharePrefsUtils.get(getContext(), "user", "userId", "");
                if (mFollowItems.get(postion).getUid().equalsIgnoreCase(s)) {
                    toast("自己不能评论自己");
                    return;
                }
                mCommentClickPosstion = postion;
                Bundle bundle = new Bundle();
                bundle.putString("vedio_id", mFollowItems.get(postion).getVideo_id());
                bundle.putString("user_id", mFollowItems.get(postion).getUid());
                openActivityForResult(CommentActivity.class, bundle, 100);
            }

            @Override
            public void mFoucsClick(final int postion, ImageView imageView) {
                JSONObject params = new JSONObject();
                params.put("token", (String) SharePrefsUtils.get(getContext(), "user", "token", ""));
                params.put("video_id", mFollowItems.get(postion).getVideo_id());
                Api.cancelAttention(getContext(), params, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        mFollowItems.remove(postion);
                        mFollowListAdapter.notifyDataSetChanged();
                        toast(data.getString("descrp"));
                    }
                    @Override
                    public void requestFailure(int code, String msg) {
                        toast(msg);
                    }
                });
                mFollowListAdapter.notifyDataSetChanged();
            }

            @Override
            public void mShareClick(int posttion) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("token", SharePrefsUtils.get(getContext(), "user", "token", ""));
                jsonObject.put("video_id", mFollowItems.get(posttion).getVideo_id());
                Api.getShareInfo(getActivity(), jsonObject, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        String data1 = data.getString("data");
                        if (data1!=null){
                            ShareModle shareModle = JSON.parseObject(data1, ShareModle.class);
                            if (shareModle!=null){
                                ShareDialog dialog=new ShareDialog(getActivity(),shareModle);
                                dialog.show();
                            }else {
                                toast("获取分享信息失败");
                            }

                        }
                    }

                    @Override
                    public void requestFailure(int code, String msg) {
                        toast(msg);
                    }
                });
            }
        });
    }

    public void getData(final SwipeRefreshLayout mSwipeRefreshLayout) {
        if (getActivity()==null){
            return;
        }
        String token = (String) SharePrefsUtils.get(getActivity(), "user", "token", "");
        String userId = (String) SharePrefsUtils.get(getActivity(), "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            Api.getFoucsList(getActivity(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    if (mFollowItems != null) {
                        mFollowItems.clear();
                    }
                    JSONArray list = data.getJSONArray("data");
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            HomeInfoModel homeInfoModel = JSON.parseObject(list.getJSONObject(i).toString(), HomeInfoModel.class);
                            homeInfoModel.setIs_favorites(1);
                            mFollowItems.add(homeInfoModel);
                        }
//                mData = JSON.parseArray(jsonObject, HomeInfoModel.class);

                    } else {
                        toast("该类无数据");
                    }
                    mFollowListAdapter.notifyDataSetChanged();

                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    //toast(msg);
                    //加载空数据图
                    if (mFollowItems.size() == 0) {
                        mNodataView.setVisibility(View.VISIBLE);
                    } else {
                        mNodataView.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            LoginByPhoneActivity.newInstance(getActivity());
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_follow;
    }

    @Override
    public void onRecyclerViewItemClick(View view, int position) {
        Bundle data = new Bundle();
        String id = (String) view.getTag();
        data.putString("id", id);
        openActivity(UserMainActivity.class, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    mFollowItems.get(mCommentClickPosstion).setComments(mFollowItems.get(mCommentClickPosstion).getComments() + 1);
                    mFollowListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
