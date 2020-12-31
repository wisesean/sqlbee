package site.autzone.sqlbee;

import java.util.List;

/**
 * 获取字段值
 * @author xiaowj
 *
 */
public interface IHasValues extends ITextAble {
  List<Object> getParameters();
}
