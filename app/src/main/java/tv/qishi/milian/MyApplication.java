package tv.qishi.milian;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;

import tv.qishi.milian.utils.SharedPreferencesUtils;


/**
 * Created by Administrator on 2016/9/1.
 * Author: XuDeLong
 */
public class MyApplication extends MultiDexApplication {

    public LocationService locationService;
    private final String APP_ID = "eRnl9UaPkhwjmvJPbrW9O0UY-gzGzoHsz";
    private final String APP_KEY = "GbUiKl8aRUTk0TooXR5WDQkG";

    String balance;
    private static Context mContext;

    public static Context getGlobalContext() {
        return mContext;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        locationService = new LocationService(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "111111111", true);

// 初始化fresco库，用来支持动态贴纸功能，若不使用，可以不调用
        Fresco.initialize(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        SharedPreferencesUtils.init(this);

    }

}
