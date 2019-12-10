package site.autzone.sqlbee.executor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.beans.factory.annotation.Autowired;

import site.autzone.sqlbee.delete.IDelete;
import site.autzone.sqlbee.executor.proc.ProcRunner;
import site.autzone.sqlbee.insert.IInsert;
import site.autzone.sqlbee.model.ITextable;
import site.autzone.sqlbee.update.IUpdate;

public class DefaultBatchExecute implements BatchExecute {
  @Autowired DataSource dataSource;

  @Override
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public int[] batch(ITextable batch, Object[][] data) {
    if (!(batch instanceof IUpdate || batch instanceof IInsert || batch instanceof IDelete)) {
      throw new RuntimeException("只能支持更新,插入,删除构建器,传入的构建器不支持该操作!");
    }
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.batch(batch.toText(), data);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public int[] batch(String sql, Object[][] data) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.batch(sql, data);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public int callProcedure(String sql, Object... args) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.execute(sql, args);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T queryProcBean(String sql, Class<T> t, Object... params) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    ProcRunner procRunner = new ProcRunner(dataSource);
    try {
      return procRunner.queryProc(sql, new BeanHandler<T>(t, processor), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> List<T> queryProcBeans(String sql, Class<T> t, Object... params) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    ProcRunner procRunner = new ProcRunner(dataSource);
    try {
      return procRunner.queryProc(sql, new BeanListHandler<T>(t, processor), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> queryResults(String sql, Object... params) {
    ProcRunner procRunner = new ProcRunner(dataSource);
    try {
      return procRunner.queryProc(sql, new MapListHandler(), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Map<String, Object> queryResult(String sql, Object... params) {
    ProcRunner procRunner = new ProcRunner(dataSource);
    try {
      return procRunner.queryProc(sql, new MapHandler(), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
