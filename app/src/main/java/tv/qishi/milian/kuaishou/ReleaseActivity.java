package tv.qishi.milian.kuaishou;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.smart.androidutils.activity.BaseActivity;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.R;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.adapter.ZuoPinAdapter;
import tv.qishi.milian.kuaishou.ksykit.RecordActivity;
import tv.qishi.milian.kuaishou.model.HomeInfoModel;
import tv.qishi.milian.kuaishou.model.MyVideoItemModle;
import tv.qishi.milian.kuaishou.model.MyVideoModle;
import tv.qishi.milian.kuaishou.model.UserInfoModel;
import tv.qishi.milian.utils.Api;

/**
 * Created by hxj on 2017/11/10.
 */

public class ReleaseActivity extends BaseActivity {
    private static final String TAG = "ReleaseActivity";
    @Bind(R.id.img_error)
    ImageView mImgError;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.img_top)
    ImageView mImgTop;
    @Bind(R.id.rv)
    RecyclerView mRv;
    @Bind(R.id.btn_push)
    Button mBtnPush;
    @Bind(R.id.relase_avatar)
    ImageView mAvatar;
    @Bind(R.id.relase_name)
    TextView mName;
    @Bind(R.id.relase_fans)
    TextView mFans;
    @Bind(R.id.relase_age)
    ImageView mGender;
    @Bind(R.id.relase_guanzhu)
    TextView mGuanzhu;
    private List<MyVideoItemModle> list;
    private ZuoPinAdapter adapter;

    //
    private ArrayList<String> mTopTabTitle;
    private UserInfoModel mUserInfoModel;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_release_video;
    }

    @OnClick({R.id.img_top, R.id.btn_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top:
                finish();
                break;
            case R.id.btn_push:
                openActivity(RecordActivity.class);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mTopTabTitle == null) {
            mTopTabTitle = new ArrayList<>();
        }
        list = new ArrayList<>();
        mTopTabTitle.clear();
        mUserInfoModel = (UserInfoModel) getIntent().getExtras().getSerializable("userBean");
        initView();
        mTopTabTitle.add("作品");
        mTopTabTitle.add("收藏");
        for (String ignored :
                mTopTabTitle) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        for (int i = 0; i < mTopTabTitle.size(); i++) {
            mTabLayout.getTabAt(i).setText(mTopTabTitle.get(i));
        }
        adapter = new ZuoPinAdapter(this, list);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);
        mRv.setVisibility(View.GONE);
        mRv.setFocusable(false);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.clear();
                adapter.notifyDataSetChanged();
                switch (tab.getPosition()) {
                    case 0:
                        getVideoData();
                        break;
                    case 1:
                        getFansData();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getVideoData();
    }

    private void initView() {
        JSONObject json = new JSONObject();
        json.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        Api.getMyVideoNumber(this, json, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                Log.e(TAG, "requestSuccess: " + data.toString());
                MyVideoModle data1 = JSON.parseObject(data.getString("data"), MyVideoModle.class);
                Glide.with(ReleaseActivity.this).load(data1.getAvatar()).asBitmap().into(new BitmapImageViewTarget(mAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
                mName.setText(data1.getUser_nicename());
                mGuanzhu.setText("作品(" + data1.getVideo_num() + ")");
                mFans.setText("关注(" + data1.getVideo_favorites() + ")");
                if ("1".equals(data1.getSex())) {
                    mGender.setImageResource(R.drawable.userinfo_male);
                }else {
                    mGender.setImageResource(R.drawable.userinfo_female);
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    public void getVideoData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        Api.getGetMyvideoList(this, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray data1 = data.getJSONArray("data");
                if (data1!=null){
                    for (int i = 0; i <data1.size() ; i++) {
                        list.add(JSON.parseObject(data1.getJSONObject(i).toString(), MyVideoItemModle.class));
                    }
                }
                isShowErrorImage();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                isShowErrorImage();
                toast(msg);
            }
        });
    }

    public void getFansData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        Api.getFoucsList(this, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray data1 = data.getJSONArray("data");
                if (data1!=null){
                    for (int i = 0; i <data1.size() ; i++) {
                        list.add(JSON.parseObject(data1.getJSONObject(i).toString(), MyVideoItemModle.class));
                    }
                }
                isShowErrorImage();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                isShowErrorImage();
                toast(msg);
            }
        });
    }

    private void isShowErrorImage(){
        if (list.size()==0){
            mImgError.setVisibility(View.VISIBLE);
            mBtnPush.setVisibility(View.VISIBLE);
            mRv.setVisibility(View.GONE);
        }else {
            mRv.setVisibility(View.VISIBLE);
            mImgError.setVisibility(View.GONE);
            mBtnPush.setVisibility(View.GONE);
        }
    }
}
