package site.autzone.sqlbee.join;

import site.autzone.sqlbee.ITable;

/**
 * 左连接
 * @author xiaowj
 *
 */
public class Left extends AbstractJoin {

  public Left(ITable mainTable, ITable joinTable) {
    super("LEFT JOIN",mainTable,joinTable);
  }
}
