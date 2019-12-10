package site.autzone.sqlbee.model;

import java.util.List;

/**
 * 获取字段值
 * @author xiaowj
 *
 */
public interface IHasValues extends ITextable {
  List<Object> getValues();
}
