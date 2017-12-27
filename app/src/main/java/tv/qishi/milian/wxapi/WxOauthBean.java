package tv.qishi.milian.wxapi;
/** 
* @function 微信授权登陆bean
* @author  fengjing: 
* @date ：2016年4月15日 上午11:12:51 
* @mail 164303256@qq.com
*/
public class WxOauthBean {
private String access_token;
private String openid;
private String nickname;
private String headimgurl;
private String unionid;
private int sex;
public String getAccess_token() {
	return access_token;
}
public void setAccess_token(String access_token) {
	this.access_token = access_token;
}
public String getOpenid() {
	return openid;
}
public void setOpenid(String openid) {
	this.openid = openid;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getHeadimgurl() {
	return headimgurl;
}
public void setHeadimgurl(String headimgurl) {
	this.headimgurl = headimgurl;
}
public int getSex() {
	return sex;
}
public void setSex(int sex) {
	this.sex = sex;
}
public String getUnionid() {
	return unionid;
}
public void setUnionid(String unionid) {
	this.unionid = unionid;
}



}
