package site.autzone.sqlbee.sql.sql.entity;

public class Email {
  private int id;
  private int employeeid;
  private String address;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getEmployeeid() {
    return employeeid;
  }

  public void setEmployeeid(int employeeid) {
    this.employeeid = employeeid;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
