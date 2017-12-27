package tv.qishi.milian.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.R;
import tv.qishi.milian.home.adapter.MainViewPagerAdapter;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.HomeLiveFragment;
import tv.qishi.milian.kuaishou.ksykit.modle.ClassifyModle;
import tv.qishi.milian.message.MessageCentreAcitvity;
import tv.qishi.milian.own.userinfo.ContributionAllActivity;
import tv.qishi.milian.search.SearchActivity;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.SharedPreferencesUtils;

/**
 * Created by fengjh on 16/7/19.
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.conversation_item_layout_avatar)
    RelativeLayout mMessageCentre;
    private List<ClassifyModle> mClassifyList;

    private ArrayList<String> mTopTabTitle;
    private ArrayList<Fragment> mFragments;
    private MainViewPagerAdapter mAdapter;
    private int mTab;

    @OnClick(R.id.image_home_search)
    public void homeSearch(View view) {
        openActivity(SearchActivity.class);
    }

    @OnClick(R.id.conversation_item_layout_avatar)
    public void messageCentre(View view) {
        openActivity(MessageCentreAcitvity.class);
    }
    @OnClick(R.id.image_home_message)
    public void homeMessage(View view) {
//        openActivity(MessageActivity.class);
//        openActivity(ConversationListActivity.class);
    }

    @OnClick(R.id.image_home_rank)
    public void imageHomeRank(View v) {
        openActivity(ContributionAllActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.clear();
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        mTopTabTitle.clear();
    }
    private void initListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected:               \n " );
                mTab=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabUnselected: " );
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected() returned: ");

            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClassifyList = new ArrayList<>();
        getClassify();
        initListener();
    }

    private void showView() {
        if (mClassifyList.size() != 0) {
            for (int i = 0; i < mClassifyList.size(); i++) {
                mTopTabTitle.add(mClassifyList.get(i).getName());
                mTabLayout.addTab(mTabLayout.newTab());
                HomeLiveFragment fragment1 = new HomeLiveFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString("keyId", mClassifyList.get(i).getTerm_id());
                fragment1.setArguments(mBundle);
                mFragments.add(fragment1);
            }

            mAdapter = new MainViewPagerAdapter(getChildFragmentManager(), mFragments, mTopTabTitle);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(0);
            mViewPager.setOffscreenPageLimit(0);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    public void getClassify() {
        String classifty = SharedPreferencesUtils.getinstance().getStringValue("classifty");
        if (!TextUtils.isEmpty(classifty)) {
            JSONObject object = JSON.parseObject(classifty);
            JSONArray data1 = object.getJSONArray("data");
            for (int i = 0; i < data1.size(); i++) {
                JSONObject jsonObject = data1.getJSONObject(i);
                ClassifyModle modle = new ClassifyModle();
                modle.setName(jsonObject.getString("name"));
                modle.setTerm_id(jsonObject.getString("term_id"));
                modle.setIsPitch(0);
                mClassifyList.add(modle);
            }
            showView();
        } else {
            Api.getLibel(this.getContext(), new JSONObject(), new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    SharedPreferencesUtils.getinstance().setStringValue("classifty", data.toString());
                    JSONArray data1 = data.getJSONArray("data");
                    for (int i = 0; i < data1.size(); i++) {
                        JSONObject jsonObject = data1.getJSONObject(i);
                        ClassifyModle modle = new ClassifyModle();
                        modle.setName(jsonObject.getString("name"));
                        modle.setTerm_id(jsonObject.getString("term_id"));
                        modle.setIsPitch(0);
                        mClassifyList.add(modle);
                    }
                    showView();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    toast(msg);
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            HomeLiveFragment fragment = (HomeLiveFragment) mFragments.get(mTab);
            fragment.getResumeData();
        }
    }
}
