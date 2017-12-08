package tv.dfyc.yckt.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2017/11/23.
 */

public class ThemeList {
    private String type;
    private List<ThemeListItem> sublist = new ArrayList<>();

    @Override
    public String toString() {
        return "ThemeList{" +
                "type='" + type + '\'' +
                ", result=" + sublist +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ThemeListItem> getSublist() {
        return sublist;
    }

    public void setSublist(List<ThemeListItem> sublist) {
        this.sublist = sublist;
    }
}
