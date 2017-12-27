package tv.qishi.milian.kuaishou.ksykit.modle;

import java.util.List;

/**
 * Created by 123456 on 2017/12/12.
 */

public class ClassifyModle {

    /**
     * term_id : 1
     * name : 生活
     */

    private String term_id;
    private String name;
    private int isPitch;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsPitch() {
        return isPitch;
    }

    public void setIsPitch(int isPitch) {
        this.isPitch = isPitch;
    }
}
