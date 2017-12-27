package tv.qishi.milian.kuaishou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.firebase.appindexing.internal.Thing;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiFragment;
import tv.qishi.milian.home.adapter.TitleRecyclerAdapter;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.adapter.HomeAdapter;
import tv.qishi.milian.kuaishou.ksykit.RecordActivity;
import tv.qishi.milian.kuaishou.ksykit.modle.ClassifyModle;
import tv.qishi.milian.kuaishou.model.HomeInfoModel;
import tv.qishi.milian.kuaishou.model.ShareModle;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.ShareDialog;
import tv.qishi.milian.utils.SharedPreferencesUtils;

/**
 * 主页的列表页
 * Created by hxj on 2017/11/6.
 */

public class HomeLiveFragment extends BaseSiSiFragment implements View.OnClickListener {
    private static final String TAG = "HomeLiveFragment";
    @Bind(R.id.rv_content)
    RecyclerView mTabLayout;
    @Bind(R.id.rv_top)
    RecyclerView mViewPager;
    @Bind(R.id.img_publish_video)
    ImageButton mImageButton;
    @Bind(R.id.rv_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTopTabTitle;
    private List<ClassifyModle> mLibel;
    private List<HomeInfoModel> mData;
    private HomeAdapter mHomeAdapter;
    private String keyId;
    private int mCommentClickPosstion;
    private String libel;
    private LinearLayoutManager manager;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home_fh;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.clear();
        if (mLibel == null) {
            mLibel = new ArrayList<>();
        }
        mLibel.clear();
        mData = new ArrayList<>();
        keyId = getArguments().getString("keyId");
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        mTopTabTitle.clear();
        getLibel();
        mHomeAdapter = new HomeAdapter(getActivity(), mData, 0);
        manager = new LinearLayoutManager(getActivity());
        mTabLayout.setLayoutManager(manager);
        mTabLayout.setAdapter(mHomeAdapter);
        mImageButton.setOnClickListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        getDate(keyId, null);
        initListener();

    }

    private void initListener() {
        mHomeAdapter.setmOnItemClick(new HomeAdapter.OnItemClick() {
            @Override
            public void likeclick(final int postion) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", SharePrefsUtils.get(HomeLiveFragment.this.getContext(), "user", "token", ""));
                jsonObject.put("video_id", mData.get(postion).getId());
                Api.addLikeVideo(HomeLiveFragment.this.getContext(), jsonObject, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        Log.e(TAG, "requestSuccess: " + data.toString());
                        mData.get(postion).setHits(mData.get(postion).getHits() + 1);
                        mHomeAdapter.notifyDataSetChanged();
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
                if (mData.get(postion).getUid().equalsIgnoreCase(s)) {
                    toast("自己不能评论自己");
                    return;
                }
                mCommentClickPosstion = postion;
                Bundle bundle = new Bundle();
                bundle.putString("vedio_id", mData.get(postion).getId());
                bundle.putString("user_id", mData.get(postion).getUid());
                openActivityForResult(CommentActivity.class, bundle, 100);
            }

            @Override
            public void mFoucsClick(final int postion, final ImageView imageView) {
                String s = (String) SharePrefsUtils.get(getContext(), "user", "userId", "");
                if (s.equalsIgnoreCase(mData.get(postion).getUid())) {
                    Toast.makeText(getContext(), "自己无法关注自己", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject params = new JSONObject();
                params.put("token", (String) SharePrefsUtils.get(getContext(), "user", "token", ""));
                params.put("video_id", mData.get(postion).getId());
                if (mData.get(postion).getIs_favorites() == 0) {
                    Api.addAttention(getContext(), params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                            mData.get(postion).setFavorites(mData.get(postion).getFavorites() + 1);
                            mData.get(postion).setIs_favorites(1);
                            mHomeAdapter.notifyDataSetChanged();
//                            if (mData.get(postion).getFavorites()==0){
//                                mData.get(postion).setFavorites(1);
//                            }else {
//                                mData.get(postion).setFavorites(0);
//                            }
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                } else {
                    Api.cancelAttention(getContext(), params, new OnRequestDataListener() {
                        @Override
                        public void requestSuccess(int code, JSONObject data) {
                            toast(data.getString("descrp"));
                            mData.get(postion).setFavorites(mData.get(postion).getFavorites() - 1);
//                            if (mData.get(postion).getFavorites()==0){
//                                mData.get(postion).setFavorites(1);
//                            }else {
                                mData.get(postion).setIs_favorites(0);
                            mHomeAdapter.notifyDataSetChanged();
//                            }
                        }

                        @Override
                        public void requestFailure(int code, String msg) {
                            toast(msg);
                        }
                    });
                }


            }

            @Override
            public void mShareClick(int posttion) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("token", SharePrefsUtils.get(getContext(), "user", "token", ""));
                jsonObject.put("video_id", mData.get(posttion).getId());
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

        mTabLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        if (!TextUtils.isEmpty(libel)) {
                            getDate(keyId, libel);

                        } else {
                            getDate(keyId, null);

                        }
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

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            if (mFollowItems != null) {
//                mFollowItems.clear();
//            }
            if (!TextUtils.isEmpty(libel)) {
                getDate(keyId, libel);

            } else {
                getDate(keyId, null);

            }
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);*/

        }
    };

    @Override
    public void onClick(View v) {
        openActivity(RecordActivity.class);
    }


    private void getDate(String keyId, @Nullable String label) {
        if (getActivity()==null){
            return;
        }
        JSONObject params = new JSONObject();
        params.put("term_id", keyId);
        if (!TextUtils.isEmpty(label)) {
            params.put("tag_id", label);
        }
        params.put("token", SharePrefsUtils.get(getContext(), "user", "token", ""));
        Api.getHomeInfo(this.getContext(), params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                JSONArray data1 = data.getJSONArray("data");
                mData.clear();
                if (data1 != null) {
                    for (int i = 0; i < data1.size(); i++) {
                        mData.add(JSON.parseObject(data1.getJSONObject(i).toString(), HomeInfoModel.class));
                    }
//                mData = JSON.parseArray(jsonObject, HomeInfoModel.class);

                } else {
                    toast("该类无数据");
                }
                mHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                toast(msg);
            }
        });
    }

    public void getLibel() {
        String libel = SharedPreferencesUtils.getinstance().getStringValue("libel");
        if (!TextUtils.isEmpty(libel)) {
            JSONObject object = JSON.parseObject(libel);
            JSONArray data1 = object.getJSONArray("data");
            for (int i = 0; i < data1.size(); i++) {
                JSONObject jsonObject = data1.getJSONObject(i);
                ClassifyModle modle = new ClassifyModle();
                modle.setName(jsonObject.getString("name"));
                modle.setTerm_id(jsonObject.getString("term_id"));
                modle.setIsPitch(0);
                mLibel.add(modle);
            }
            showView();
        } else {
            Api.getLibel(this.getContext(), new JSONObject(), new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    SharedPreferencesUtils.getinstance().setStringValue("libel", data.toString());
                    JSONArray data1 = data.getJSONArray("data");
                    for (int i = 0; i < data1.size(); i++) {
                        JSONObject jsonObject = data1.getJSONObject(i);
                        ClassifyModle modle = new ClassifyModle();
                        modle.setName(jsonObject.getString("name"));
                        modle.setTerm_id(jsonObject.getString("term_id"));
                        modle.setIsPitch(0);
                        mLibel.add(modle);
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
            showView();
        }
    }

    private void showView() {
        mTopTabTitle.add("全部");
        for (int i = 0; i < mLibel.size(); i++) {
            mTopTabTitle.add(mLibel.get(i).getName());
        }
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        TitleRecyclerAdapter mAdapter = new TitleRecyclerAdapter(mTopTabTitle, getActivity());
        mViewPager.setLayoutManager(mLayoutManager1);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, int position) {
                String libelId = getLibelId(mTopTabTitle.get(position));
                libel = libelId;
                getDate(keyId, libelId);
            }
        });
    }

    private String getLibelId(String s) {
        for (int i = 0; i < mLibel.size(); i++) {
            if (s.equals(mLibel.get(i).getName())) {
                return mLibel.get(i).getTerm_id();
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 100:
                    mData.get(mCommentClickPosstion).setComments(mData.get(mCommentClickPosstion).getComments() + 1);
                    mHomeAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    public void getResumeData(){
        if (!TextUtils.isEmpty(libel)) {
            getDate(keyId, libel);
        } else {
            getDate(keyId, null);
        }
    }

}