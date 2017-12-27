package tv.qishi.milian.home.model;

/**
 * Created by 123456 on 2017/12/15.
 */

public class ShareItemBean {
    private int id;
    private String name;
    public ShareItemBean(int id,String name){
        this.id=id;
        this.name=name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}