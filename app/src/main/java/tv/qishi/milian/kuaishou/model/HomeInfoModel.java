package tv.qishi.milian.kuaishou.model;

/**
 * Created by 123456 on 2017/12/12.
 */

public class HomeInfoModel {


    /**
     * id : 1
     * uid : 156295
     * term_id : 1
     * tag_id : 1,2,3,4,7
     * video_title : 123
     * video_thumb : http://47.96.29.139/data/upload/thumb/data/upload/thumb/5a2f86f364c10.jpg
     * video_url : http://47.96.29.139/data/upload/video/data/upload/video/5a2f86f3651f1.mp4
     * video_time : 5000
     * status : 1
     * create_time : 1513064166
     * ip : 39.106.72.110
     * user_nicename : 猩次元1传媒
     * avatar : http://47.96.29.139/data/upload/avatar/5a2de86183dcb.jpg
     * hits : 0
     */
    private int favorites;
    private String id;
    private String uid;
    private String term_id;
    private String tag_id;
    private String video_title;
    private String video_thumb;
    private String video_url;
    private String video_time;
    private String status;
    private String create_time;
    private String ip;
    private String user_nicename;
    private String avatar;
    private int hits;
    private int comments;
    private String video_id;
    private int is_favorites;


    public int getIs_favorites() {
        return is_favorites;
    }

    public void setIs_favorites(int is_favorites) {
        this.is_favorites = is_favorites;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
