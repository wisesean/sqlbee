package site.autzone.sqlbee.join;

import site.autzone.sqlbee.ITable;

public class Right extends AbstractJoin {

  public Right(ITable mainTable, ITable joinTable) {
    super("RIGHT JOIN",mainTable,joinTable);
  }}
