package tv.qishi.milian.kuaishou.model;

/**
 * Created by 123456 on 2017/12/14.
 */

public class ReplyBean {
    /**
     * status : 1
     * id : 9
     * parentid : 0
     * to_user_nicename : 李杰锋
     * uid : 156295
     * user_nicename : 12-1417:40
     * content : 哈哈
     * to_uid : 156297
     * type : 2
     * create_time : 1513244423
     * video_id : 8
     */

    private String status;
    private String avatar;
    private String id;
    private String parentid;
    private String to_user_nicename;
    private String uid;
    private String user_nicename;
    private String content;
    private String to_uid;
    private String type;
    private String create_time;
    private String video_id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getTo_user_nicename() {
        return to_user_nicename;
    }

    public void setTo_user_nicename(String to_user_nicename) {
        this.to_user_nicename = to_user_nicename;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
