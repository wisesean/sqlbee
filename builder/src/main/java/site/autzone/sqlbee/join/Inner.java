package site.autzone.sqlbee.join;

import site.autzone.sqlbee.ITable;

public class Inner extends AbstractJoin {

  public Inner(ITable mainTable, ITable joinTable) {
    super("INNER JOIN",mainTable,joinTable);
  }}
