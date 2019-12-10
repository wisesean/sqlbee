package site.autzone.sqlbee.executor;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import site.autzone.sqlbee.insert.IInsert;

@Component
public class DefaultInsertExecute implements InsertExecute {
  @Autowired DataSource dataSource;

  public <T> T insert(String sql, Class<T> t, Object... params) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.insert(sql, new BeanHandler<T>(t, processor), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public int insert(String sql, Object... params) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.execute(sql, params);
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

  @Override
  public int insert(IInsert insert) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.update(insert.toText(), insert.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
