package tv.qishi.milian.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import tv.qishi.milian.intf.OnDownLoadListener;

/**
 * Created by Administrator on 2016/11/28.
 * Author: XuDeLong
 */
public class DownLoadFile {

    public static void downloadFile(final Context context, String url, String[] allowedContentTypes, final OnDownLoadListener downLoadListener){

        AsyncHttpClient client = new AsyncHttpClient();
        // 指定文件类型
       // String[] allowedContentTypes = new String[]{"audio/mpeg"};
        // 获取二进制数据如图片和其他文件

        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            int curProgress = 0;
            @Override
            public void onSuccess(int mI, org.apache.http.Header[] mHeaders, byte[] binaryData) {
                downLoadListener.OnSuccess(binaryData);

            }

            @Override
            public void onFailure(int mI, org.apache.http.Header[] mHeaders, byte[] mBytes, Throwable mThrowable) {
                downLoadListener.onFailure();
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);

                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                if(count != curProgress){
                    curProgress = count;
                    downLoadListener.onProgress(count);
                }
            }


            @Override
            public void onRetry(int retryNo) {
                // TODO Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }

        });

    }

}
