package tv.qishi.milian.kuaishou.ksykit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.shortvideo.kit.KSYRemuxKit;
import com.ksyun.media.shortvideo.kit.KSYMergeKit;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.ksykit.util.FileUtils;

/**
 * 配置窗口示例：
 * 录制参数配置
 * 编辑合成参数配置
 */
public class ConfigActivity extends Activity {

    private final static String TAG = "ConfigActivity";
    private final static int REQUEST_CODE = 6384;
    private final static int PERMISSION_REQUEST_STORAGE = 1;
    private final static int PERMISSION_REQUEST_WRITE = 2;
    private final static String TITLE = "ksy_import_file";
    private final static String EXT_TRANSCODE = "/newTranscode";//转码生成后的文件前缀

    /*******转码参数配置示例******/
    private TextView mOutRes480p;
    private TextView mOutRes540p;
    private TextView mOutEncodeWithH264;
    private TextView mOutEncodeWithH265;
    private TextView mOutEncodeByHW;
    private TextView mOutEncodeBySW;
    private TextView mOutDecodeByHW;
    private TextView mOutDecodeBySW;
    private TextView mOutForMP4;
    private TextView mOutForGIF;
    private TextView[] mOutProfileGroup;
    private EditText mOutFrameRate;
    private EditText mOutVideoBitrate;
    private EditText mOutAudioBitrate;
    private EditText mOutVideoCRF;
    private TextView mLandscape;
    private TextView mPortrait;
    private TextView mOutputConfirm;
    private EditText mTargetWidth;
    private EditText mTargetHeight;
    private List<Uri> mTransCodeUris;
    private ShortVideoConfig mTransConfig; //输出视频参数配置
    private Timer mTimer;

    private static final int[] OUTPUT_PROFILE_ID = {R.id.trans_output_config_low_power,
            R.id.trans_output_config_balance, R.id.trans_output_config_high_performance};

    /*******录制参数配置示例******/
    private TextView mRecRes720p;
    private TextView mRecRes1080p;
    private TextView mRecEncodeWithH264;
    private TextView mRecEncodeWithH265;
    private TextView mRecEncodeByHW;
    private TextView mRecEncodeBySW;
    private TextView[] mRecProfileGroup;
    private EditText mRecFrameRate;
    private EditText mRecVideoBitrate;
    private EditText mRecAudioBitrate;

    private Dialog mTransConfDialog;
    private MergeFilesAlertDialog mTranscodeDialog;

    private LinearLayout mImport;  //从外部导入文件
    private LinearLayout mStartRecord;   //由此进入录制示例窗口

    private KSYMergeKit mKsyMergeKit;

    private ConfigObserver mObserver;
    private static ShortVideoConfig1 mRecordConfig = new ShortVideoConfig1();  //录制参数配置

    private static final int[] RECORD_PROFILE_ID = {R.id.record_config_low_power,
            R.id.record_config_balance, R.id.record_config_high_performance};
    private static final int[] ENCODE_PROFILE_TYPE = {VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER,
            VideoEncodeFormat.ENCODE_PROFILE_BALANCE, VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_config);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mObserver = new ConfigObserver();
        mOutProfileGroup = new TextView[3];

        mRecRes720p = (TextView) findViewById(R.id.record_config_r720p);
        mRecRes720p.setOnClickListener(mObserver);
        mRecRes1080p = (TextView) findViewById(R.id.record_config_r1080p);
        mRecRes1080p.setOnClickListener(mObserver);
        mRecEncodeWithH264 = (TextView) findViewById(R.id.record_config_h264);
        mRecEncodeWithH264.setOnClickListener(mObserver);
        mRecEncodeWithH265 = (TextView) findViewById(R.id.record_config_h265);
        mRecEncodeWithH265.setOnClickListener(mObserver);
        mRecEncodeByHW = (TextView) findViewById(R.id.record_config_hw);
        mRecEncodeByHW.setOnClickListener(mObserver);
        mRecEncodeBySW = (TextView) findViewById(R.id.record_config_sw);
        mRecEncodeBySW.setOnClickListener(mObserver);
        mRecProfileGroup = new TextView[3];
        for (int i = 0; i < mRecProfileGroup.length; i++) {
            mRecProfileGroup[i] = (TextView) findViewById(RECORD_PROFILE_ID[i]);
            mRecProfileGroup[i].setOnClickListener(mObserver);
        }
        mRecFrameRate = (EditText) findViewById(R.id.record_config_frameRate);
        mRecVideoBitrate = (EditText) findViewById(R.id.record_config_video_bitrate);
        mRecAudioBitrate = (EditText) findViewById(R.id.record_config_audio_bitrate);
        mLandscape = (TextView) findViewById(R.id.record_config_landscape);
        mLandscape.setOnClickListener(mObserver);
        mPortrait = (TextView) findViewById(R.id.record_config_portrait);
        mPortrait.setOnClickListener(mObserver);
        mImport = (LinearLayout) findViewById(R.id.config_import);
        mImport.setOnClickListener(mObserver);
        mStartRecord = (LinearLayout) findViewById(R.id.config_record);
        mStartRecord.setOnClickListener(mObserver);
        initView();

        mKsyMergeKit = new KSYMergeKit(ConfigActivity.this);
        mKsyMergeKit.setOnErrorListener(mOnErrorListener);
        mKsyMergeKit.setOnInfoListener(mOnInfoListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermisson();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTranscodeDialog != null) {
            mTranscodeDialog.dismiss();
            mTranscodeDialog = null;
        }

        if (mKsyMergeKit != null) {
            mKsyMergeKit.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                break;
            }
        }
    }

    private void initView() {
        mRecRes720p.setActivated(true);
        mRecEncodeWithH264.setActivated(true);
        mRecEncodeByHW.setActivated(true);
        mRecProfileGroup[2].setActivated(true);
        mPortrait.setActivated(true);
    }

    private void onRecordEncodeProfileClick(int index) {
        mRecProfileGroup[index].setActivated(true);
        for (int i = 0; i < mRecProfileGroup.length; i++) {
            if (i != index) {
                mRecProfileGroup[i].setActivated(false);
            }
        }
    }

    private void confirmConfig() {
//        if (mRecRes720p.isActivated()) {
            mRecordConfig.resolution = StreamerConstants.VIDEO_RESOLUTION_720P;
//        } else if (mRecRes1080p.isActivated()) {
//            mRecordConfig.resolution = StreamerConstants.VIDEO_RESOLUTION_1080P;
//        }
//        if (mRecEncodeWithH264.isActivated()) {
            mRecordConfig.encodeType = AVConst.CODEC_ID_AVC;
//        } else if (mRecEncodeWithH265.isActivated()) {
            mRecordConfig.encodeType = AVConst.CODEC_ID_HEVC;
//        }
//        if (mRecEncodeByHW.isActivated()) {
//            mRecordConfig.encodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;
//        } else if (mRecEncodeBySW.isActivated()) {
            mRecordConfig.encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
//        }
        mRecordConfig.encodeProfile=2;
//        for (int i = 0; i < mRecProfileGroup.length; i++) {
//            if (mRecProfileGroup[i].isActivated()) {
//                mRecordConfig.encodeProfile = ENCODE_PROFILE_TYPE[i];
//                break;
//            }
//        }
//        if (mLandscape.isActivated()) {
//            mRecordConfig.isLandscape = true;
//        } else {
            mRecordConfig.isLandscape = false;
//        }
        mRecordConfig.fps = Integer.parseInt(mRecFrameRate.getText().toString());
        mRecordConfig.videoBitrate = Integer.parseInt(mRecVideoBitrate.getText().toString());
        mRecordConfig.audioBitrate = Integer.parseInt(mRecAudioBitrate.getText().toString());
    }

    /**
     * 读取磁盘权限检查
     */
    private void checkPermisson() {
        int storagePer = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePer = ActivityCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);

        if (storagePer != PackageManager.PERMISSION_GRANTED || writePer != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "hasPermission: API version < M");

            } else {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_STORAGE);
            }
        } else {

        }
    }

    public class ConfigObserver implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.record_config_r720p:
                    mRecRes720p.setActivated(true);
                    mRecRes1080p.setActivated(false);
                    break;
                case R.id.record_config_r1080p:
                    mRecRes720p.setActivated(false);
                    mRecRes1080p.setActivated(true);
                    break;
                case R.id.record_config_h264:
                    mRecEncodeWithH264.setActivated(true);
                    mRecEncodeWithH265.setActivated(false);
                    break;
                case R.id.record_config_h265:
                    mRecEncodeWithH264.setActivated(false);
                    mRecEncodeWithH265.setActivated(true);
                    break;
                case R.id.record_config_hw:
                    mRecEncodeByHW.setActivated(true);
                    mRecEncodeBySW.setActivated(false);
                    break;
                case R.id.record_config_sw:
                    mRecEncodeByHW.setActivated(false);
                    mRecEncodeBySW.setActivated(true);
                    break;
                case R.id.record_config_low_power:
                    onRecordEncodeProfileClick(0);
                    break;
                case R.id.record_config_balance:
                    onRecordEncodeProfileClick(1);
                    break;
                case R.id.record_config_high_performance:
                    onRecordEncodeProfileClick(2);
                    break;
                case R.id.record_config_landscape:
                    mLandscape.setActivated(true);
                    mPortrait.setActivated(false);
                    break;
                case R.id.record_config_portrait:
                    mLandscape.setActivated(false);
                    mPortrait.setActivated(true);
                    break;
                case R.id.config_import:
                    confirmConfig();
                    //启动本地导入页面
                    onImportClick();
                    break;
                case R.id.config_record:
                    confirmConfig();
                    //启动短视频录制
                    RecordActivity.startActivity(getApplicationContext());
                    break;
                case R.id.trans_output_config_audio_bitrate:
                    mOutRes480p.setActivated(true);
                    mOutRes540p.setActivated(false);
                    break;
                case R.id.trans_output_config_r540p:
                    mOutRes480p.setActivated(false);
                    mOutRes540p.setActivated(true);
                    break;
                case R.id.trans_output_config_h264:
                    mOutEncodeWithH264.setActivated(true);
                    mOutEncodeWithH265.setActivated(false);
                    break;
                case R.id.trans_output_config_h265:
                    mOutEncodeWithH264.setActivated(false);
                    mOutEncodeWithH265.setActivated(true);
                    break;
                case R.id.trans_output_config_hw:
                    mOutEncodeByHW.setActivated(true);
                    mOutEncodeBySW.setActivated(false);
                    mOutVideoCRF.setEnabled(false);
                    break;
                case R.id.trans_output_config_sw:
                    mOutEncodeByHW.setActivated(false);
                    mOutEncodeBySW.setActivated(true);
                    mOutVideoCRF.setEnabled(true);
                    break;
                case R.id.trans_output_config_decode_hw:
                    mOutDecodeByHW.setActivated(true);
                    mOutDecodeBySW.setActivated(false);
                    break;
                case R.id.trans_output_config_decode_sw:
                    mOutDecodeBySW.setActivated(true);
                    mOutDecodeByHW.setActivated(false);
                    break;
                case R.id.trans_output_config_mp4:
                    mOutForMP4.setActivated(true);
                    mOutForGIF.setActivated(false);
                    mOutEncodeWithH264.setEnabled(true);
                    mOutEncodeWithH265.setEnabled(true);
                    mOutEncodeByHW.setEnabled(true);
                    break;
                case R.id.trans_output_config_gif:
                    mOutForMP4.setActivated(false);
                    mOutForGIF.setActivated(true);
                    mOutEncodeWithH264.setActivated(false);
                    mOutEncodeWithH265.setActivated(false);
                    mOutEncodeWithH264.setEnabled(false);
                    mOutEncodeWithH265.setEnabled(false);
                    //gif 不支持硬编
                    mOutEncodeByHW.setEnabled(false);
                    mOutEncodeByHW.setActivated(false);
                    mOutEncodeBySW.setActivated(true);
                    break;
                case R.id.output_config_low_power:
                    onOutputEncodeProfileClick(0);
                    break;
                case R.id.output_config_balance:
                    onOutputEncodeProfileClick(1);
                    break;
                case R.id.output_config_high_performance:
                    onOutputEncodeProfileClick(2);
                    break;
                default:
                    break;
            }

        }
    }

    private void onImportClick() {
        Intent intent = new Intent(this, MediaImportActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 从本地导入视频文件结果处理
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        List<Uri> uris = new LinkedList<>();
                        ArrayList<String> pathList = data.getStringArrayListExtra("filePath");
                        for (int i = 0; i < pathList.size(); i++) {
                            Uri uri = Uri.parse("file://" + pathList.get(i));
                            uris.add(uri);
                        }
                        if (uris.size() > 1) {
                            //多选后转码和拼接处理
                            showTransCodeDialog(uris);
                        } else {
                            Uri uri = uris.get(0);
                            Log.i(TAG, "Uri = " + uri.toString());
                            try {
                                // Get the file path from the URI
                                final String path = FileUtils.getPath(this, uri);
                                String mimeType = FileUtils.getMimeType(this, uri);

                                if (!TextUtils.isEmpty(mimeType) && isSupportedMimeType(mimeType)) {

                                    Toast.makeText(ConfigActivity.this,
                                            "File Selected: " + path, Toast.LENGTH_LONG).show();
                                    EditActivity.startActivity(getApplicationContext(), path);
                                } else {
                                    if (path.endsWith("m3u8")) {

                                        final MergeFilesAlertDialog dialog = new MergeFilesAlertDialog
                                                (ConfigActivity.this, R.style.dialog);
                                        dialog.setCancelable(false);
                                        dialog.show();

                                        KSYRemuxKit ksyRemuxKit = new KSYRemuxKit();
                                        ksyRemuxKit.setOnInfoListener(new KSYRemuxKit.OnInfoListener() {
                                            @Override
                                            public void onInfo(KSYRemuxKit ksyRemuxKit, int type, String msg) {
                                                if (type == KSYRemuxKit.INFO_PUBLISHER_STOPPED) {
                                                    ksyRemuxKit.release();
                                                    dialog.dismiss();
                                                    EditActivity.startActivity(ConfigActivity.this, Environment
                                                            .getExternalStorageDirectory() + "/newRemux" +
                                                            ".mp4");
                                                }
                                            }
                                        });
                                        ksyRemuxKit.setOnErrorListener(new KSYRemuxKit.OnErrorListener() {
                                            @Override
                                            public void onError(KSYRemuxKit ksyRemuxKit, int type, long msg) {
                                                ksyRemuxKit.release();
                                                dialog.dismiss();
                                                Toast.makeText(ConfigActivity.this, "Remux m3u8 " +
                                                        "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        ksyRemuxKit.start(path, Environment
                                                .getExternalStorageDirectory() + "/newRemux" +
                                                ".mp4");
                                    } else {
                                        Toast.makeText(ConfigActivity.this,
                                                "Do not support this file, please select other File ", Toast
                                                        .LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "File select error:" + e);
                            }
                        }

                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startTranscode(final List<Uri> srcFiles) {
        mTranscodeDialog = new MergeFilesAlertDialog
                (ConfigActivity.this, R.style.dialog);
        mTranscodeDialog.setCancelable(false);
        mTranscodeDialog.show();

        mKsyMergeKit.setEncodeMethod(mTransConfig.encodeMethod);
        mKsyMergeKit.setTargetSize(mTransConfig.width, mTransConfig.height);
        mKsyMergeKit.setVideoKBitrate(mTransConfig.videoBitrate);
        mKsyMergeKit.setAudioKBitrate(mTransConfig.audioBitrate);
        mKsyMergeKit.setAudioChannels(mTransConfig.audioChannel);
        mKsyMergeKit.setAudioSampleRate(mTransConfig.audioSampleRate);
        mKsyMergeKit.setVideoFps(mTransConfig.fps);
        mKsyMergeKit.setVideoDecodeMethod(mTransConfig.decodeMethod);
        String outputFile = getTranscodeFileFolder() + "/mergedFile" +
                System.currentTimeMillis() + ".mp4";
        mKsyMergeKit.start(mTransCodeUris, outputFile, null, true);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mKsyMergeKit != null && mTranscodeDialog != null) {
                    mTranscodeDialog.updateProgress((int) mKsyMergeKit.getTranscodeProgress(),
                            mKsyMergeKit.getCurrentTransFileId());
                }
            }
        }, 500, 500);
    }

    private KSYMergeKit.OnInfoListener mOnInfoListener = new KSYMergeKit.OnInfoListener() {
        @Override
        public void onInfo(int type, String msg) {
            switch (type) {
                case KSYMergeKit.INFO_MERGE_FINISH:
                    if (mTranscodeDialog != null) {
                        mTranscodeDialog.dismiss();
                        mTranscodeDialog = null;
                    }
                    EditActivity.startActivity(ConfigActivity.this, msg);
                    break;
                case KSYMergeKit.INFO_TRANSCODE_UNSUPPORT:
                    Log.d(TAG, "onInfo: " + msg);
                    break;
                case KSYMergeKit.INFO_TRANSCODE_STOPBYUSERS:
                    if (mTranscodeDialog != null) {
                        mTranscodeDialog.dismiss();
                        mTranscodeDialog = null;
                    }
                    Log.d(TAG, "onInfo: stopped by user");
                default:
                    break;
            }
        }
    };

    private KSYMergeKit.OnErrorListener mOnErrorListener = new KSYMergeKit.OnErrorListener() {
        @Override
        public void onError(int type, int reason, long msg) {
            Toast.makeText(ConfigActivity.this, "Transcode " +
                    "failed: " + type + "， reason:" + reason, Toast.LENGTH_SHORT).show();

            switch (type) {
                case KSYMergeKit.ERROR_MERGE_EMPTY:
                case KSYMergeKit.ERROR_MERGE_FAILED:
                    if (mTranscodeDialog != null) {
                        mTranscodeDialog.dismiss();
                        mTranscodeDialog = null;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private String getTranscodeFileFolder() {
        String fileFolder = "/sdcard/ksy_sv_transcode_test";
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        return fileFolder;
    }

    /**
     * 判断是否是所支持的MIME类型
     */
    private boolean isSupportedMimeType(String mimeType) {
        for (int i = 0; i < SUPPORT_FILE_MIME_TYPE.length; i++) {
            if (mimeType.equals(SUPPORT_FILE_MIME_TYPE[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 支持的MIME类型数组
     */
    private String[] SUPPORT_FILE_MIME_TYPE = new String[]{
            "video/mp4",  //.mp4
            "video/ext-mp4",  //.mp4
            "video/3gpp",   //.3gp
            "video/quicktime" //.mov
    };

    public static ShortVideoConfig1 getRecordConfig() {
        return mRecordConfig;
    }

    private class MergeFilesAlertDialog extends AlertDialog {
        private TextView mProgress;
        private AlertDialog mConfimDialog;

        protected MergeFilesAlertDialog(Context context, int themID) {
            super(context, themID);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.merge_record_files_layout);
            mProgress = (TextView) findViewById(R.id.progress_text);
        }

        public void updateProgress(final int progress, final int index) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgress.getVisibility() != View.VISIBLE) {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.valueOf(index));
                    builder.append(":");
                    builder.append(String.valueOf(progress));
                    builder.append("%");
                    mProgress.setText(String.valueOf(builder.toString()));
                }
            });
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    mConfimDialog = new Builder(ConfigActivity.this).setCancelable
                            (true)
                            .setTitle("中止导入?")
                            .setNegativeButton("取消", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    mConfimDialog = null;
                                }
                            })
                            .setPositiveButton("确定", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (mTranscodeDialog != null) {
                                        mTranscodeDialog.dismiss();
                                        mTranscodeDialog = null;
                                    }
                                    mConfimDialog = null;
                                    mKsyMergeKit.stop();
                                }
                            }).show();

                    break;
                default:
                    break;
            }
            return false;
        }
    }

    private void showTransCodeDialog(List<Uri> uris) {
        if (mTransConfDialog != null) {
            mTransCodeUris = uris;
            mTransConfDialog.show();
            return;
        }
        mTransConfDialog = new Dialog(this, R.style.TransCodeDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.transcode_popup_layout, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTransConfDialog.setContentView(contentView, params);
        mOutRes480p = (TextView) contentView.findViewById(R.id.trans_output_config_r480p);
        mOutRes480p.setOnClickListener(mObserver);
        mOutRes540p = (TextView) contentView.findViewById(R.id.trans_output_config_r540p);
        mOutRes540p.setOnClickListener(mObserver);
        mOutEncodeWithH264 = (TextView) contentView.findViewById(R.id.trans_output_config_h264);
        mOutEncodeWithH264.setOnClickListener(mObserver);
        mOutEncodeWithH265 = (TextView) contentView.findViewById(R.id.trans_output_config_h265);
        mOutEncodeWithH265.setOnClickListener(mObserver);
        mOutEncodeByHW = (TextView) contentView.findViewById(R.id.trans_output_config_hw);
        mOutEncodeByHW.setOnClickListener(mObserver);
        mOutEncodeBySW = (TextView) contentView.findViewById(R.id.trans_output_config_sw);
        mOutEncodeBySW.setOnClickListener(mObserver);
        mOutDecodeByHW = (TextView) contentView.findViewById(R.id.trans_output_config_decode_hw);
        mOutDecodeByHW.setOnClickListener(mObserver);
        mOutDecodeBySW = (TextView) contentView.findViewById(R.id.trans_output_config_decode_sw);
        mOutDecodeBySW.setOnClickListener(mObserver);
        mOutForMP4 = (TextView) contentView.findViewById(R.id.trans_output_config_mp4);
        mOutForMP4.setOnClickListener(mObserver);
        mOutForGIF = (TextView) contentView.findViewById(R.id.trans_output_config_gif);
        mOutForGIF.setOnClickListener(mObserver);
        mOutProfileGroup = new TextView[3];
        for (int i = 0; i < mOutProfileGroup.length; i++) {
            mOutProfileGroup[i] = (TextView) contentView.findViewById(OUTPUT_PROFILE_ID[i]);
            mOutProfileGroup[i].setOnClickListener(mObserver);
        }
        mOutFrameRate = (EditText) contentView.findViewById(R.id.trans_output_config_frameRate);
        mOutVideoBitrate = (EditText) contentView.findViewById(R.id.trans_output_config_video_bitrate);
        mOutAudioBitrate = (EditText) contentView.findViewById(R.id.trans_output_config_audio_bitrate);
        mTargetWidth = (EditText) contentView.findViewById(R.id.trans_output_config_video_width);
        mTargetHeight = (EditText) contentView.findViewById(R.id.trans_output_config_video_height);
        mOutVideoCRF = (EditText) contentView.findViewById(R.id.trans_output_config_video_crf);
        mTransConfig = new ShortVideoConfig();
        mOutputConfirm = (TextView) contentView.findViewById(R.id.trans_output_confirm);
        mOutputConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOutputConfirmClick();
            }
        });
        mTransCodeUris = uris;
        mOutRes480p.setActivated(true);
        mOutEncodeWithH264.setActivated(true);
        mOutEncodeBySW.setActivated(true);
        mOutDecodeByHW.setActivated(true);
        mOutForMP4.setActivated(true);
        mOutProfileGroup[1].setActivated(true);
        mTransConfDialog.show();

    }

    private void onOutputConfirmClick() {
        confirmTransConfig();
        if (mTransConfDialog.isShowing()) {
            mTransConfDialog.dismiss();
        }
        startTranscode(mTransCodeUris);
    }

    private void confirmTransConfig() {
        if (mOutRes480p.isActivated()) {
            mTransConfig.resolution = StreamerConstants.VIDEO_RESOLUTION_480P;
        } else if (mOutRes540p.isActivated()) {
            mTransConfig.resolution = StreamerConstants.VIDEO_RESOLUTION_540P;
        }
        if (mOutEncodeWithH264.isActivated()) {
            mTransConfig.encodeType = AVConst.CODEC_ID_AVC;
        } else if (mOutEncodeWithH265.isActivated()) {
            mTransConfig.encodeType = AVConst.CODEC_ID_HEVC;
        }

        if (mOutEncodeByHW.isActivated()) {
            mTransConfig.encodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;
        } else if (mOutEncodeBySW.isActivated()) {
            mTransConfig.encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
        }

        if (mOutDecodeByHW.isActivated()) {
            mTransConfig.decodeMethod = StreamerConstants.DECODE_METHOD_HARDWARE;
        } else if (mOutDecodeBySW.isActivated()) {
            mTransConfig.decodeMethod = StreamerConstants.DECODE_METHOD_SOFTWARE;
        }

        if (mOutForGIF.isActivated()) {
            mTransConfig.encodeType = AVConst.CODEC_ID_GIF;
        }
        for (int i = 0; i < mOutProfileGroup.length; i++) {
            if (mOutProfileGroup[i].isActivated()) {
                mTransConfig.encodeProfile = ENCODE_PROFILE_TYPE[i];
                break;
            }
        }
        mTransConfig.fps = Integer.parseInt(mOutFrameRate.getText().toString());
        mTransConfig.videoBitrate = Integer.parseInt(mOutVideoBitrate.getText().toString());
        mTransConfig.audioBitrate = Integer.parseInt(mOutAudioBitrate.getText().toString());
        mTransConfig.videoCRF = Integer.parseInt(mOutVideoCRF.getText().toString());
        mTransConfig.width = Integer.parseInt(mTargetWidth.getText().toString());
        mTransConfig.height = Integer.parseInt(mTargetHeight.getText().toString());
    }

    private void onOutputEncodeProfileClick(int index) {
        mOutProfileGroup[index].setActivated(true);
        for (int i = 0; i < mOutProfileGroup.length; i++) {
            if (i != index) {
                mOutProfileGroup[i].setActivated(false);
            }
        }
    }
}
