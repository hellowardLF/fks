package tv.qishi.milian.kuaishou.ksykit;

import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;

/**
 * 录制和输出参数类
 */

public class ShortVideoConfig1 {
    public boolean isLandscape = false;
    public float fps = 30.0f;
    public int resolution = StreamerConstants.VIDEO_RESOLUTION_540P;
    public int videoBitrate = 4000;
    public int audioBitrate = 48;
    public int encodeType = AVConst.CODEC_ID_AVC;
    public int encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
    public int encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;
    public int decodeMethod = StreamerConstants.DECODE_METHOD_HARDWARE;
    public int videoCRF = 24;
    public int audioChannel = 1;
    public int audioSampleRate = 44100;
    public int width = 480;
    public int height = 480;
}
