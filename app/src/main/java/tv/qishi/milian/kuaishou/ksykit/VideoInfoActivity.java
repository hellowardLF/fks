package tv.qishi.milian.kuaishou.ksykit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.smart.androidutils.utils.SharePrefsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import tv.qishi.milian.MainActivity;
import tv.qishi.milian.R;
import tv.qishi.milian.core.BaseSiSiActivity;
import tv.qishi.milian.intf.OnRequestDataListener;
import tv.qishi.milian.kuaishou.ksykit.adapter.LibelAdapter;
import tv.qishi.milian.kuaishou.ksykit.modle.ClassifyModle;
import tv.qishi.milian.kuaishou.ksykit.util.LoadLocalImage;
import tv.qishi.milian.utils.Api;
import tv.qishi.milian.view.SFProgrssDialog;

/**
 * Created by 123456 on 2017/12/12.
 */

public class VideoInfoActivity extends BaseSiSiActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "VideoInfoActivity";
    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.videoinfo_pic)
    ImageView mPic;
    @Bind(R.id.videoinfo_recycle)
    RecyclerView mLable;
    @Bind(R.id.videoinfo_title)
    EditText mTitle;
    @Bind(R.id.videoinfo_sure)
    TextView mSure;
    @Bind(R.id.videoinfo_classify)
    Spinner mSpinner;
    private ArrayAdapter mAdapter;
    private List<ClassifyModle> mList;
    private List<String> mClassifyName;
    private List<ClassifyModle> mLableList;
    private static final int RC_CAMERA_AND_WRITE = 1;
    private StaggeredGridLayoutManager mManager;
    private LibelAdapter mLibelAdapter;
    private String mClassName;
    private String mPicURL;
    private String mVideoTime;
    private String mVideoUrl;
    public final static String COMPOSE_PATH = "compose_path";
    public final static String MIME_TYPE = "mime_type";
    public final static String PREVIEW_LEN = "preview_length";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_videoinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("视频信息");
        mList = new ArrayList<>();
        mLableList = new ArrayList<>();
        mVideoUrl=getIntent().getStringExtra(COMPOSE_PATH);
        mVideoTime=getIntent().getLongExtra(PREVIEW_LEN,0)+"";
        mClassifyName = new ArrayList<>();
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mClassifyName);
        mSpinner.setAdapter(mAdapter);
        mManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mLibelAdapter = new LibelAdapter(this, mLableList);
        mLable.setLayoutManager(mManager);
        mLable.setAdapter(mLibelAdapter);
        getClassify();
        getLable();
        initListener();

    }

    private void initListener() {
        //下拉设置
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: ");
                mClassName = mClassifyName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: ");
            }
        });
        //图片选择
        mPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPermissions();
            }
        });
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerId = getSpinnerName();
                if (TextUtils.isEmpty(spinnerId)) {
                    toast("获取分类信息失败");
                    return;
                }
                if (TextUtils.isEmpty(mTitle.getText())) {
                    toast("请输入视频标题");
                    return;
                }
                if (TextUtils.isEmpty(mPicURL)) {
                    toast("请选择视频封面");
                    return;
                }
                if (TextUtils.isEmpty(mVideoTime) || TextUtils.isEmpty(mVideoUrl)) {
                    toast("获取视频信息失败");
                    return;
                }
                String libelId="";
                List<ClassifyModle> classifyModles = mLibelAdapter.getmList();
                for (int i = 0; i < classifyModles.size(); i++) {
                    if (classifyModles.get(i).getIsPitch()==1){
                        libelId=classifyModles.get(i).getTerm_id()+",";
                    }
                }
                if (TextUtils.isEmpty(libelId)){
                    toast("请选择标签信息");
                    return;
                }
                update("如何"+mTitle.getText().toString(),new File(mPicURL),new File(mVideoUrl),mVideoTime,spinnerId,libelId);
            }
        });
    }

    private void update(final String text, File mPicURL, File mVideoUrl, String mVideoTime, String spinnerId, String libelId) {
        SFProgrssDialog dialog = SFProgrssDialog.show(this, "请稍后...");
        RequestParams params = new RequestParams();
        params.put("token",(String) SharePrefsUtils.get(this, "user", "token", ""));
        params.put("video_title",text);
        try {
            params.put("video_thumb",mPicURL);
            params.put("video_url",mVideoUrl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("video_time",mVideoTime);
        params.put("term_id",spinnerId);
        params.put("tag_id",libelId);
        Api.excuteUpload(Api.POST_UPVIDEO, this, params, dialog, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                Log.e(TAG, "requestSuccess: "+data );
                toast(data.getString("descrp"));
                openActivity(MainActivity.class);
                VideoInfoActivity.this.finish();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }


    public void getClassify() {
        Api.getClassify(VideoInfoActivity.this, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray data1 = data.getJSONArray("data");
                for (int i = 0; i < data1.size(); i++) {
                    JSONObject jsonObject = data1.getJSONObject(i);
                    ClassifyModle modle = new ClassifyModle();
                    mClassifyName.add(jsonObject.getString("name"));
                    modle.setName(jsonObject.getString("name"));
                    modle.setTerm_id(jsonObject.getString("term_id"));
                    mList.add(modle);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }


    /**
     * 权限申请
     */
    @AfterPermissionGranted(RC_CAMERA_AND_WRITE)
    private void applyPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType("image/*");
            startActivityForResult(getAlbum, 11);
        } else {
            EasyPermissions.requestPermissions(this, "请求文件读写权限",
                    RC_CAMERA_AND_WRITE, perms);
        }

    }

    /**
     * 申请 权限 回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // 权限允许的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 权限拒绝的回调
        new AppSettingsDialog.Builder(this).setTitle("权限设置").setRationale("无法进去文件操作，因为您拒绝了读写存储必要的权限请求。请点击\\\"确定\\\"跳转到设置界面，找到\\\"权限管理\\\"，然后请允许\\\"读写手机存储\\\"的权限").setRequestCode(
                RC_CAMERA_AND_WRITE).setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("您没有授予文件读写权限");
                    }
                }).build().show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    if (data == null) {
                        return;
                    }
                    Uri originalUri = data.getData(); //
                    String imagepath = LoadLocalImage.getPath(this, originalUri);
//				doTailor(Uri.fromFile(new File(imagepath)));// 身份证上传 取消裁剪框
                    File fileImage = new File(imagepath);
                    mPicURL=imagepath;
                    if (fileImage.exists()) {
                        Glide.with(this).load(fileImage).asBitmap().into(mPic);
                    }
                    break;
            }
        }
    }

    public void getLable() {
        Api.getLibel(this, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONArray data1 = data.getJSONArray("data");
                for (int i = 0; i < data1.size(); i++) {
                    JSONObject jsonObject = data1.getJSONObject(i);
                    ClassifyModle modle = new ClassifyModle();
                    modle.setName(jsonObject.getString("name"));
                    modle.setTerm_id(jsonObject.getString("term_id"));
                    modle.setIsPitch(0);
                    mLableList.add(modle);
                }
                mLibelAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    public String getSpinnerName() {
        if (TextUtils.isEmpty(mClassName)) {
            mClassName = mClassifyName.get(0);
        }
        for (int i = 0; i < mList.size(); i++) {
            String name = mList.get(i).getName();
            if (name.equals(mClassName)) {
                return mList.get(i).getTerm_id();
            }
        }
        return mList.get(0).getTerm_id();
    }
}
