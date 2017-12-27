package tv.qishi.milian.kuaishou.model;

import java.io.Serializable;

/**
 * Created by 123456 on 2017/12/13.
 */

public class UserInfoModel implements Serializable{

    /**
     * fans_num : 0
     * signature : 骑士直
     * mobile_status : 1
     * mobile : 13701779267
     * avatar : http://47.96.29.139/data/upload/avatar/5a2f92a9bfb1b.jpg
     * user_nicename : 李杰锋
     * channel_status : 1
     * user_level : 1
     * attention_status : 1
     * live_times : 0
     * balance : 0
     * attention_num : 0
     * is_truename : 0
     * sidou : 0
     * total_spend : 0
     * id : 156297
     * room_id : 35
     * sex : 1
     */

    private String fans_num;
    private String signature;
    private String mobile_status;
    private String mobile;
    private String avatar;
    private String user_nicename;
    private String channel_status;
    private int user_level;
    private int attention_status;
    private String live_times;
    private String balance;
    private String attention_num;
    private String is_truename;
    private String sidou;
    private String total_spend;
    private String id;
    private String room_id;
    private String sex;

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMobile_status() {
        return mobile_status;
    }

    public void setMobile_status(String mobile_status) {
        this.mobile_status = mobile_status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getChannel_status() {
        return channel_status;
    }

    public void setChannel_status(String channel_status) {
        this.channel_status = channel_status;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getAttention_status() {
        return attention_status;
    }

    public void setAttention_status(int attention_status) {
        this.attention_status = attention_status;
    }

    public String getLive_times() {
        return live_times;
    }

    public void setLive_times(String live_times) {
        this.live_times = live_times;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAttention_num() {
        return attention_num;
    }

    public void setAttention_num(String attention_num) {
        this.attention_num = attention_num;
    }

    public String getIs_truename() {
        return is_truename;
    }

    public void setIs_truename(String is_truename) {
        this.is_truename = is_truename;
    }

    public String getSidou() {
        return sidou;
    }

    public void setSidou(String sidou) {
        this.sidou = sidou;
    }

    public String getTotal_spend() {
        return total_spend;
    }

    public void setTotal_spend(String total_spend) {
        this.total_spend = total_spend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
