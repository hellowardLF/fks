package tv.qishi.milian.kuaishou;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.adapter.MsgAdapter;
import tv.qishi.milian.kuaishou.listener.OnTransitionListener;
import tv.qishi.milian.kuaishou.model.CommentModel;
import tv.qishi.milian.kuaishou.model.ReplyBean;
import tv.qishi.milian.kuaishou.model.VideInfoModel;
import tv.qishi.milian.kuaishou.view.SampleVideo;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.PixelUtil;


/**
 * player
 * Created by hxj on 2017/11/8.
 */

public class PlayActivity extends BaseSiSiActivity {
    private static final String TAG = "PlayActivity";

    @Bind(R.id.rv)
    RecyclerView mRv;
    @Bind(R.id.player_view)
    SampleVideo videoPlayer;
    @Bind(R.id.play_comment)
    LinearLayout mLin;
    @Bind(R.id.play_send)
    TextView mSend;
    @Bind(R.id.play_edit)
    EditText mEdit;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    OrientationUtils orientationUtils;
    private List<VideInfoModel> listModle;

    private boolean isTransition;

    private Transition transition;
    private String mVideoId;
    private MsgAdapter mAdapter;
    private List<CommentModel> mList;
    private int mPostion = -1;

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_play;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PixelUtil.dp2px(this, 200));
        videoPlayer.setLayoutParams(mParams);
        mVideoId = getIntent().getStringExtra("video_id");
        mList = new ArrayList<>();
        listModle = new ArrayList<>();
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVideoData();
            }
        });
        mAdapter = new MsgAdapter(this, mList, listModle);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter);
        mRv.setFocusable(false);
//        mRv.setNestedScrollingEnabled(false);
        getVideoData();
    }

    boolean first = true;

    private void init(VideInfoModel videInfoModel) {
        if (videInfoModel == null) {
            return;
        }
        listModle.add(videInfoModel);
        if(videInfoModel.getComments()!=null){
            mList.addAll(videInfoModel.getComments());
        }
        mAdapter.notifyDataSetChanged();
        String mVideoUrl = videInfoModel.getVideo_url();
//        String mVideoUrl = "http://211.138.121.202/sohu/v1/TmXiTmPiTUq3kfjS4HlOkLFy9lhajkvBjCIRN8PHqM14wmfcymcAr.mp4?k=0CfIhf&p=XZxIWhoBoJ2svm1BqVPcNmsdytP&r=TmI20LscWOo70Sc2ZD6Sqt8IS3eslDbJkDbtZ5Nak7gaUDQEwah0whesaOGECBfjvJAt6ODOfoIWDvXvmXAyBj&q=OpCChW7IRYodRDW4vmfCyY2sWhyHfhd45G6tZYXtfJo2ZDv4WhyXZY6Owm4cWG1SqF2sY&cip=117.184.168.62";
        if (!videoPlayer.setUp(mVideoUrl, false, videInfoModel.getVideo_title())) {
            videoPlayer.setUp(mVideoUrl, false, videInfoModel.getVideo_title());
        }
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(this).load(videInfoModel.getVideo_thumb()).into(imageView);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams mParams;
                if (first) {
                    mParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    first = !first;
                } else {
                    mParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            PixelUtil.dp2px(PlayActivity.this, 200));
                    first = !first;
                }
                videoPlayer.setLayoutParams(mParams);
                orientationUtils.resolveByClick();
            }
        });
        mAdapter.setItemClick(new MsgAdapter.ItemClick() {
            @Override
            public void mItemClick(int postion) {
                String userId = (String) SharePrefsUtils.get(PlayActivity.this, "user", "userId", "");
                if (userId.equalsIgnoreCase(listModle.get(0).getComments().get(postion).getUid())){
                    toast("自己无法回复自己");
                    return;
                }
                if (mLin.getVisibility() == View.GONE) {
                    mLin.setVisibility(View.VISIBLE);
                    mEdit.requestFocus();
                }
                mPostion = postion;
            }

            @Override
            public void mComment() {
                String userId = (String) SharePrefsUtils.get(PlayActivity.this, "user", "userId", "");
                if (userId.equalsIgnoreCase(listModle.get(0).getUid())){
                    toast("自己无法评论自己");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("vedio_id", mVideoId);
                bundle.putString("user_id", listModle.get(0).getUid());
                openActivityForResult(CommentActivity.class, bundle, 1000);
            }
        });
        mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && mLin.getVisibility() == View.VISIBLE) {
                    mEdit.clearFocus();
                    mLin.setVisibility(View.GONE);
                }
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostion == -1) {
                    toast("未获取想回复的信息,请重新点击回复");
                    return;
                }
                if (TextUtils.isEmpty(mEdit.getText().toString().trim())) {
                    toast("回复内容不能为空");
                    return;
                }

                sendCommetn();
            }
        });
       /* mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mLin.getVisibility()==View.VISIBLE){
                    mLin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLin.getVisibility()==View.VISIBLE){
                    mLin.setVisibility(View.GONE);
                }
            }
        });*/
        mRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mLin.getVisibility() == View.VISIBLE) {
                    mLin.setVisibility(View.GONE);
                }
                return false;
            }
        });


        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //过渡动画
        initTransition();
    }

    private void sendCommetn() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        jsonObject.put("id", mVideoId);
        jsonObject.put("content", mEdit.getText());
        jsonObject.put("to_uid", mList.get(mPostion).getUid());
        Api.report_comment(this, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                Log.e(TAG, "requestSuccess: " + data);
                JSONObject data1 = data.getJSONObject("data");
                if (!TextUtils.isEmpty(data1.toString())) {
                    mLin.setVisibility(View.GONE);
                    mEdit.setText("");
                    ReplyBean replyBean = JSON.parseObject(data1.toString(), ReplyBean.class);
                    List<ReplyBean> reply = mList.get(mPostion).getReply();
                    if (reply != null) {
                        mList.get(mPostion).getReply().add(replyBean);
                        if (listModle!=null&&listModle.get(0).getComments()!=null&&listModle.get(0).getComments().get(mPostion).getReply()!=null){
                            listModle.get(0).getComments().get(mPostion).getReply().add(replyBean);
                        }
                    } else {
                        reply = new ArrayList<>();
                        reply.add(replyBean);
                        mList.get(mPostion).setReply(reply);
                    }
                    mAdapter.getAdapter(mPostion).notifyDataSetChanged();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (1000 == requestCode) {
                getVideoData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils != null && orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }


    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    videoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    public void getVideoData() {
        listModle.clear();
        mList.clear();
        mAdapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(mVideoId)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mVideoId);
            jsonObject.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
            Api.getVideoInfo(this, jsonObject, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    Log.e(TAG, "requestSuccess: " + data.toString());
                    JSONObject json = data.getJSONObject("data");
                    if (!TextUtils.isEmpty(json.toString())) {
                        init(JSON.parseObject(json.toString(), VideInfoModel.class));
                    } else {
                        toast("获取数据失败");
                    }
                }

                @Override
                public void requestFailure(int code, String msg) {
                    if (mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    toast(msg);
                }
            });
        } else {
            toast("获取视频信息失败");
        }

    }
}

