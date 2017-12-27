package tv.qishi.milian.kuaishou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiFragment;
import tv.qishi.milian.home.adapter.MainViewPagerAdapter;

/**
 * Created by hxj on 2017/11/14.
 */

public class HomeAttentionFgt extends BaseSiSiFragment {
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.img_add_friend)
    ImageView mImgAddFriend;
    private ArrayList<String> mTopTabTitle;
    private ArrayList<Fragment> mFragments;
    private MainViewPagerAdapter mAdapter;
    private AttentionFragment fragment1;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home_attention;
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

        mTopTabTitle.add("我的关注");
//        mTopTabTitle.add("粉丝");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < mTopTabTitle.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        fragment1= new AttentionFragment();
//        FansFragment fragment2 = new FansFragment();
        mFragments.add(fragment1);
//        mFragments.add(fragment2);
        mAdapter = new MainViewPagerAdapter(getChildFragmentManager(), mFragments, mTopTabTitle);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mImgAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("网络连接失败");
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (fragment1!=null){
                fragment1.getData(null);
            }
        }
    }
}
