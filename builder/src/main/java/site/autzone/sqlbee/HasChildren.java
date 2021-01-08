package site.autzone.sqlbee;

import java.util.List;

public interface HasChildren {
    List<ITextAble> getChildren();
    ITextAble getChild(int index);
}
