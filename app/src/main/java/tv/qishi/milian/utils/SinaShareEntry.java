package tv.qishi.milian.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;


import butterknife.Bind;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.pay.Constants;

/**
 * @function 新浪分享回掉
 * @author fengjing:
 * @date ：2016年4月16日 下午12:56:04
 * @mail 164303256@qq.com
 */
public class SinaShareEntry extends BaseSiSiActivity {
	IWeiboShareAPI mWeiboShareAPI;

	@Bind(R.id.sinashareentry_text)
	 TextView text;
	// com.sjing.huakr.common.SinaShareEntry

	protected void initValues() {
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APPID_SINAWEIBO);
		mWeiboShareAPI.registerApp();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, new Response() {
			@Override
			public void onResponse(BaseResponse baseResp) {
				switch (baseResp.errCode) {
				case WBConstants.ErrorCode.ERR_OK:
					text.setText(R.string.share_success);
					break;
				case WBConstants.ErrorCode.ERR_CANCEL:
					text.setText(R.string.cancel_share);
					break;
				case WBConstants.ErrorCode.ERR_FAIL:
					text.setText(R.string.share_fail);
					break;
				}

			}
		}); //
	}
	@Override
	public int getLayoutResource() {
		return R.layout.activity_sinashareentry;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initValues();
	}
}
