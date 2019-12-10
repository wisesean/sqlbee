package site.autzone.sqlbee.executor;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import site.autzone.sqlbee.update.IUpdate;

@Component
public class DefaultUpdateExecute implements UpdateExecute {
  @Autowired DataSource dataSource;

  @Override
  public int update(IUpdate update) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.update(update.toText(), update.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public int update(String updateSql, Object... params) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.update(updateSql, params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
}
