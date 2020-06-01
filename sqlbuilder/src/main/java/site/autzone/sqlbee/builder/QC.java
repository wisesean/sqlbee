package site.autzone.sqlbee.builder;

/**
 * 查询条件类型
 * 
 * @author xiaowj
 *
 */
public enum QC {
  NOT_EQUALS("<>"), EQUALS("="), LIKE("LIKE"), LT("<"), LTE("<="), GT(">"), GTE(">="), BETWEEN(
      "BETWEEN"), IN("IN"), IS_NULL("IS NULL"), IS_NOT_NULL("IS NOT NULL");

  private String connector;

  QC(String connector) {
    this.connector = connector;
  }

  public String connector() {
    return connector;
  }
}
