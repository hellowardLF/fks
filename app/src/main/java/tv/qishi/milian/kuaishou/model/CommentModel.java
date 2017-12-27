package tv.qishi.milian.kuaishou.model;

import java.util.List;

/**
 * Created by 123456 on 2017/12/13.
 */

public class CommentModel {


    /**
     * id : 1
     * avatar : http://47.96.29.139/data/upload/avatar/5a2de86183dcb.jpg
     * uid : 156295
     * user_nicename : 猩次元1传媒
     * to_uid : 156297
     * content : 视频内容很不错，赞一个
     * create_time : 12-13 11:57
     */
    private String status;
    private String parentid;
    private List<ReplyBean> reply;
    private String type;
    private String video_id;
    private String id;
    private String avatar;
    private String uid;
    private String user_nicename;
    private String to_uid;
    private String content;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
