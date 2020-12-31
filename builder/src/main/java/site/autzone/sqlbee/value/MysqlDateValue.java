package site.autzone.sqlbee.value;

public class MysqlDateValue extends Value {
  private String formatter;
  
  /**
   * 默认非mysql数据库
   * @param value
   * @param formatter
   */
  public MysqlDateValue(Object value, String formatter) {
    super(value, String.class);
    this.formatter = formatter;
  }

  @Override
  public String output() {
    return (super.getIdx() == null)
        ? "DATE_FORMAT(?,'" + this.formatter + "')"
        : "DATE_FORMAT(${" + super.getIdx() + "}, '" + this.formatter + "')";
  }
}
