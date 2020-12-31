package site.autzone.sqlbee.value;

public class DateValue extends Value {
  private String formatter;
  private final static String MYSQL_DATE_FUNC = "DATE_FORMAT";
  private final static String OTHER_DATE_FUNC = "TO_DATE";

  /**
   * 默认非mysql数据库
   * @param value
   * @param formatter
   */
  public DateValue(Object value, String formatter) {
    super(value, String.class);
    this.formatter = formatter;
  }

  @Override
  public String output() {
    return (super.getIdx() == null)
        ? "TO_DATE(?,'" + this.formatter + "')"
        : "TO_DATE(${" + super.getIdx() + "}, '" + this.formatter + "')";
  }
}
