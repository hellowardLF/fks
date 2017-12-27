package tv.qishi.milian.kuaishou.model;

import java.io.Serializable;

/**
 * Created by 123456 on 2017/12/19.
 */

public class ShareModle implements Serializable{

    /**
     * shareUrl : http://47.96.29.139/data/upload/video/5a37794d19221.mp4
     * title : 如何在短时间内唱名曲
     * video_thumb : http://47.96.29.139/data/upload/thumb/5a37794d17ad0.jpg
     */

    private String shareUrl;
    private String title;
    private String video_thumb;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }
}
