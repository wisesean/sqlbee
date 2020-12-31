package site.autzone.sqlbee.sql.sql.entity;

import java.util.Date;

public class Employee {
  private int id;
  private String lastname;
  private double salary;
  private Date hireddate;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public Date getHireddate() {
    return hireddate;
  }

  public void setHireddate(Date hireddate) {
    this.hireddate = hireddate;
  }
}
