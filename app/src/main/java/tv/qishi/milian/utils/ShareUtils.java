package tv.qishi.milian.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.model.ShareModle;
import tv.qishi.milian.pay.Constants;

public class ShareUtils {
    private Tencent mTencent;
        private IWXAPI iwxapi;
    private IWeiboShareAPI mWeiboShareAPI;
    private Context mContext;

    // 标题
    private String share_title = "猩次元传媒";
    // 内容
//    private String share_summary = "发送的内容";
    // 链接地址
    private String share_target_url = "https://www.baidu.com/";
    // logo地址
    private String share_image_url = "http://as7.oss-cn-shanghai.aliyuncs.com/8ea36abb3e0a37aac80e78229f6cc823.png";
    private String share_appname = "猩次元传媒";

    public ShareUtils(Context context, ShareModle shareModle) {
        this.mContext = context;
        share_title=shareModle.getTitle();
        share_target_url=shareModle.getShareUrl();
        share_image_url=shareModle.getVideo_thumb();
    }

    /*   *
        * @param flag 0 微信好友 1 微信朋友圈
        */
    public void shareToWechat(int flag) {
        if (flag != 0 && flag != 1) {
            return;
        }
        // IContants.APPID_WECHAT 微信开放平台对应的appid
        iwxapi = WXAPIFactory.createWXAPI(mContext, Constants.WECHAT_APPID, true);
        iwxapi.registerApp(Constants.WECHAT_APPID);
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(mContext, "未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share_target_url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share_title;
//        msg.description = share_summary;
        // 这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);


    }

    public void shareToQQ() {
        // IContants.APPID_QQ 腾讯开放平台对应的appid
        mTencent = Tencent.createInstance(Constants.APPID_QQ, mContext);

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share_title);
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share_summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share_target_url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share_image_url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share_appname);

        mTencent.shareToQQ((Activity) mContext, params, new IUiListener() {
            @Override
            public void onError(UiError arg0) {
            }

            @Override
            public void onComplete(Object arg0) {
            }

            @Override
            public void onCancel() {
            }
        });

    }

    public void shareToQZone() {
        mTencent = Tencent.createInstance(Constants.APPID_QQ, mContext);
        ArrayList<String> list = new ArrayList<String>();
        list.add(share_image_url);
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, share_title);// 必填
//        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share_summary);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, share_target_url);// 必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        mTencent.shareToQzone((Activity) mContext, params, new IUiListener() {

            @Override
            public void onError(UiError arg0) {
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onCancel() {

            }
        });

    }

    public void shareToSinaWibo() {
        //IContants.APPID_SINAWEIBO  新浪开放对应的appid
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Constants.APPID_SINAWEIBO);
        mWeiboShareAPI.registerApp();
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            Toast.makeText(mContext, "未安装微博客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mWeiboShareAPI.isWeiboAppSupportAPI()) {
            Toast.makeText(mContext, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。", Toast.LENGTH_SHORT).show();
            return;
        }
        WeiboMessage message = new WeiboMessage();
        WebpageObject webObject = new WebpageObject();
        webObject.identify = Utility.generateGUID();
//        webObject.description = share_summary;
        webObject.title = share_title;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        webObject.setThumbImage(bitmap);
        webObject.actionUrl = share_target_url;
        webObject.defaultText = "选择心仪的好友，即刻开始沟通";
        message.mediaObject = webObject;
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = message;
        mWeiboShareAPI.sendRequest((Activity) mContext, request);

    }

   /* *
     * 复制链接*/

    public void copyLink() {
        String text = "http://www.huakr.com/";
        ClipData myClip = ClipData.newPlainText("text", text);
        ClipboardManager myClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        myClipboard.setPrimaryClip(myClip);

    }


}