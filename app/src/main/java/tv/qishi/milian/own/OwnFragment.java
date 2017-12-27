package tv.qishi.milian.own;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.images.GlideCircleTransform;
import com.smart.androidutils.utils.DensityUtils;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;


import butterknife.Bind;
import butterknife.OnClick;
import tv.qishi.milian.MyApplication;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiFragment;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.ReleaseActivity;
import tv.qishi.milian.kuaishou.model.UserInfoModel;
import tv.qishi.milian.login.LoginByPhoneActivity;
import tv.qishi.milian.message.MessageCentreAcitvity;
import tv.qishi.milian.own.authorize.AuthorizeActivity;
import tv.qishi.milian.own.family.FamilyActivity;
import tv.qishi.milian.own.fans.FansActivity;
import tv.qishi.milian.own.follow.FollowActivity;
import tv.qishi.milian.own.goexchange.GoExchangeActivity;
import tv.qishi.milian.own.level.MyLevelActivity;
import tv.qishi.milian.own.money.ChargeMoneyActivity;
import tv.qishi.milian.own.publishrecord.PublishRecordActivity;
import tv.qishi.milian.own.setting.FeedbackActivity;
import tv.qishi.milian.own.setting.SettingActivity;
import tv.qishi.milian.own.userinfo.ContributionActivity;
import tv.qishi.milian.own.userinfo.MyDataActivity;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.utils.MyLogUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by fengjh on 16/7/19.
 */
public class OwnFragment extends BaseSiSiFragment implements View.OnClickListener {

    private static final String TAG = OwnFragment.class.getName();

    @Bind(R.id.frame_own_avatar_container)
    FrameLayout mFrameOwnAvatarContainer;
    @Bind(R.id.text_user_id)
    TextView mTextUserId;
    @Bind(R.id.text_user_nick)
    TextView mTextUserNick;
    @Bind(R.id.text_user_signature)
    TextView mTextUserSignature;
    @Bind(R.id.image_own_user_avatar)
    ImageView mImageOwnUserAvatar;
    @Bind(R.id.text_user_level)
    TextView mTextUserLevel;
//    @Bind(R.id.text_user_balance)
//    TextView mTextUserBalance;
//    @Bind(R.id.text_user_spendcoin)
//    TextView mTextUserSpendcoin;
    @Bind(R.id.image_sex)
    ImageView mImageSex;
//    @Bind(R.id.image_first)
//    ImageView mImageFirst;
//    @Bind(R.id.image_second)
//    ImageView mImageSecond;
//    @Bind(R.id.image_third)
//    ImageView mImageThird;
//    @Bind(R.id.image_fourth)
//    ImageView mImageFourth;
    @Bind(R.id.text_user_fans)
    TextView mTextUserFans;
    @Bind(R.id.text_user_follows)
    TextView mTextUserFollows;
//    @Bind(R.id.text_user_sidou)
//    TextView mTextUserSidou;
//    @Bind(R.id.text_user_level_LV)
//    TextView mTextUserLevelLv;
    @Bind(R.id.image_real)
    ImageView mImageReal;
    @Bind(R.id.image_own_unread)
    ImageView mImageOwnUnread;
    private int mAvatarContainerWidth;
    private int mAvatarContainerHeight;
    private String balance;
    private UserInfoModel userInfoModel;

    private String[] SeatUserIDs = {"0", "0", "0", "0"};

    @OnClick(R.id.image_own_user_avatar)
    public void clickAvatarName(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.image_own_edit)
    public void clickAvatarName1(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }
    @OnClick(R.id.text_contribution)
    public void contribution(View view) {
        openActivity(ContributionActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case 1:
                initData();
            }
        }

    }
    @OnClick(R.id.linear_own_setting_family)
    public void linearOwnSetingFamily(){
        openActivity(FamilyActivity.class);
    }

    @OnClick(R.id.ll_my_video)
    public void llMyVideo(){
        if (userInfoModel!=null){
            Bundle bundle=new Bundle();
            bundle.putSerializable("userBean",userInfoModel);
            openActivity(ReleaseActivity.class,bundle);
        }else {
            toast("正在获取用户信息请稍后");
        }
    }

    @OnClick(R.id.linear_own_setting_publish)
    public void publishRecord(View v){
        openActivity(PublishRecordActivity.class);
    }

    @OnClick(R.id.image_own_message)
    public void clickMessage(View view) {
        openActivity(MessageCentreAcitvity.class);
    }

    private PopupWindow mPopupShareWindow;


    @Override
    public void onClick(final View v) {
    }

    @OnClick(R.id.linear_own_money_container)
    public void chargeMoney(View view) {
        Bundle data = new Bundle();
        data.putString("balance", balance);
        openActivity(ChargeMoneyActivity.class, data);
    }

    @OnClick(R.id.linear_own_info_container)
    public void message(View view) {
        Intent i = new Intent(this.getContext(), MyDataActivity.class);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.linear_own_setting_container)
    public void setting(View view) {
        openActivity(SettingActivity.class);
    }


    @OnClick(R.id.linear_version_container)
    public void checkUpdate(){
        JSONObject params = new JSONObject();
        try {
            String versionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            params.put("ver_num",versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Api.checkUpdate(getActivity(),params , new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if(StringUtils.isNotEmpty(info.getString("package"))){
                    checkUpgrade(info.getString("package"),info.getString("description"));
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }
    AlertDialog tipsAlertDialog;
    private void checkUpgrade(final String downloadUrl,String mes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        tipsAlertDialog = builder.setTitle("提示")
                .setMessage(mes)
                .setNegativeButton("再等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipsAlertDialog.isShowing()) {
                            tipsAlertDialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("更新下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(downloadUrl);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create();
        tipsAlertDialog.show();
        tipsAlertDialog.setCancelable(false);
        tipsAlertDialog.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.linear_feedback_container)
    public void feedback(View view) {
        openActivity(FeedbackActivity.class);
    }

    @OnClick(R.id.linear_own_follow_container)
    public void follow(View view) {
        openActivity(FollowActivity.class);
    }

    @OnClick(R.id.linear_own_fans_container)
    public void fans(View view) {
        openActivity(FansActivity.class);
    }

    @OnClick(R.id.text_own_goExchange)
    public void GoExchange(View view) {
        openActivity(GoExchangeActivity.class);
    }

    @OnClick(R.id.linear_own_level_container)
    public void myLevel(View view) {
        openActivity(MyLevelActivity.class);
    }

    @OnClick(R.id.linear_own_authorize_container)
    public void authorize(View view) {
        openActivity(AuthorizeActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mThirdShare = new ThirdShare(getContext());
//        mThirdShare.setOnShareStatusListener(this);
        mAvatarContainerWidth = DensityUtils.screenWidth(getActivity());
        mAvatarContainerHeight = (mAvatarContainerWidth * 440) / 750;
        PackageManager packageManager = this.getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mAvatarContainerWidth, mAvatarContainerHeight);
//        mFrameOwnAvatarContainer.setLayoutParams(params);
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
//        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        String token = (String) SharePrefsUtils.get(this.getContext(), "user", "token", "");
        final String userId = (String) SharePrefsUtils.get(this.getContext(), "user", "userId", "");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(userId)) {
            JSONObject requestParams = new JSONObject();
            requestParams.put("token", token);
            requestParams.put("id", userId);
            Api.getUserInfo(this.getContext(), requestParams, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject data) {
                    if(isActive){
                        JSONObject userInfo = data.getJSONObject("data");
                        userInfoModel= JSON.parseObject(userInfo.toString(),UserInfoModel.class);
                        MyLogUtils.d(userInfo.toString());
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "user_nicename", userInfo.getString("user_nicename"));
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "user_level", userInfo.getString("user_level"));
                        SharePrefsUtils.put(OwnFragment.this.getContext(), "user", "avatar", userInfo.getString("avatar"));
                        MyApplication app = (MyApplication) getActivity().getApplication();
                        app.setBalance(userInfo.getString("balance"));
                        mTextUserNick.setText(userInfo.getString("user_nicename"));
                        mTextUserId.setText("ID:" + userInfo.getString("id"));
                        if (StringUtils.isNotEmpty(userInfo.getString("signature"))) {
                            mTextUserSignature.setText(userInfo.getString("signature"));
                        }
                        if ("1".equals(userInfo.getString("sex"))) {
                            mImageSex.setImageResource(R.drawable.userinfo_male);
                        }else {
                            mImageSex.setImageResource(R.drawable.userinfo_female);
                        }
                        if("1".equals(userInfo.getString("is_truename"))){
                            mImageReal.setVisibility(View.VISIBLE);
                        }
                        mTextUserLevel.setText(userInfo.getString("user_level"));
//                    mTextUserLevelLv.setText(userInfo.getString("user_level"));
                        int level = Integer.parseInt(userInfo.getString("user_level"));
                        if(level<5){
                            mTextUserLevel.setBackgroundResource(R.drawable.level1);
                        }
                        if(level>4 && level<9 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level2);
                        }
                        if(level>8 && level<13 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level3);
                        }
                        if(level>12 ){
                            mTextUserLevel.setBackgroundResource(R.drawable.level3);
                        }

//                    mTextUserSpendcoin.setText(userInfo.getString("total_spend"));
//                    mTextUserSidou.setText(userInfo.getString("sidou"));
                        mTextUserFans.setText(userInfo.getString("fans_num"));
                        mTextUserFollows.setText(userInfo.getString("attention_num"));
                        balance = userInfo.getString("balance");
//                    mTextUserBalance.setText(balance);
                        Glide.with(getActivity()).load(userInfo.getString("avatar"))
                                .error(R.drawable.icon_avatar_default)
                                .transform(new GlideCircleTransform(getActivity()))
                                .into(mImageOwnUserAvatar);
                        //CustomUserProvider provider = (CustomUserProvider) LCChatKit.getInstance().getProfileProvider();
                        //provider.getAllUsers().add(new LCChatKitUser(userInfo.getString("id"), userInfo.getString("user_nicename"), userInfo.getString("avatar")));
                    }

                }

                @Override
                public void requestFailure(int code, String msg) {

                    if (msg!=null){
                        toast(msg);
                    }
                }
            });
            requestParams.put("limit_num", 4);
            for (int i = 0; i < 4; i++) { //初始ID为0
                SeatUserIDs[i] = "0";
            }

        } else {
            LoginByPhoneActivity.newInstance(getActivity());
            getActivity().finish();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public int getLayoutResource() {
        return R.layout.fragment_own;
    }

}
