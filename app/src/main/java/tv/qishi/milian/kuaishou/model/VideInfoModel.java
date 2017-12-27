package tv.qishi.milian.kuaishou.model;

import java.util.List;

/**
 * Created by 123456 on 2017/12/13.
 */

public class VideInfoModel {

        /**
         * term_id : 5
         * avatar : http://47.96.29.139/data/upload/avatar/5a2f92a9bfb1b.jpg
         * user_nicename : 李杰锋
         * video_time : 5000
         * video_title : 测试111111
         * comments : [{"status":"1","id":"1","parentid":"0","uid":"156295","content":"视频内容很不错，赞一个","to_uid":"156297","type":"1","create_time":"1513069999","video_id":"5"}]
         * status : 1
         * id : 5
         * tag_id : 4,5
         * ip : 117.184.168.62
         * uid : 156297
         * video_url : http://47.96.29.139/data/upload/video/5a2f8967b58da.mp4
         * video_thumb : http://47.96.29.139/data/upload/thumb/5a2f8967b52e7.jpg
         * create_time : 1513064807
         */

        private String term_id;
        private String avatar;
        private String user_nicename;
        private String video_time;
        private String video_title;
        private String status;
        private String id;
        private String tag_id;
        private String ip;
        private String uid;
        private String video_url;
        private String video_thumb;
        private String create_time;
        private List<CommentModel> comments;

        public String getTerm_id() {
            return term_id;
        }

        public void setTerm_id(String term_id) {
            this.term_id = term_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getVideo_time() {
            return video_time;
        }

        public void setVideo_time(String video_time) {
            this.video_time = video_time;
        }

        public String getVideo_title() {
            return video_title;
        }

        public void setVideo_title(String video_title) {
            this.video_title = video_title;
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

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_thumb() {
            return video_thumb;
        }

        public void setVideo_thumb(String video_thumb) {
            this.video_thumb = video_thumb;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public List<CommentModel> getComments() {
            return comments;
        }

        public void setComments(List<CommentModel> comments) {
            this.comments = comments;
        }
}
