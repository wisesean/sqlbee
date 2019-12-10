package site.autzone.sqlbee.model;

public class DateValue extends Value {
  private String formatter;
  private final static String MYSQL_DATE_FUNC = "DATE_FORMAT";
  private final static String ORTHER_DATE_FUNC = "TO_DATE";
  private Boolean isMysql = false;
  
  /**
   * 默认非mysql数据库
   * @param value
   * @param formatter
   */
  public DateValue(Object value, String formatter) {
    super(value, Value.TYPE.DATE_FORMAT_STR.getName());
    this.formatter = formatter;
  }

  public DateValue(Boolean isMysql, Object value, String formatter) {
    super(value, Value.TYPE.DATE_FORMAT_STR.getName());
    this.formatter = formatter;
    this.isMysql = isMysql;
  }

  @Override
  public String toText() {
    String dateFunc = isMysql?MYSQL_DATE_FUNC:ORTHER_DATE_FUNC;
    return (super.getIdx() == null)
        ? dateFunc+"(?,'" + this.formatter + "')"
        : dateFunc+"(${" + super.getIdx() + "}, '" + this.formatter + "')";
  }

  public String getFormatter() {
    return formatter;
  }

  public void setFormatter(String formatter) {
    this.formatter = formatter;
  }
}
