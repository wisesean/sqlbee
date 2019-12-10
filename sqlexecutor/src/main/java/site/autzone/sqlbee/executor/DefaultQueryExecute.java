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
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import site.autzone.sqlbee.query.IQuery;

@Component
public class DefaultQueryExecute implements QueryExecute {
  @Autowired DataSource dataSource;

  @Override
  public <T> List<T> queryBeans(IQuery query, Class<T> t) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(
          query.toText(), new BeanListHandler<T>(t, processor), query.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> queryResults(IQuery query) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(query.toText(), new MapListHandler(), query.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T queryBean(IQuery query, Class<T> t) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(
          query.toText(), new BeanHandler<T>(t, processor), query.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Map<String, Object> queryResult(IQuery query) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(query.toText(), new MapHandler(), query.getValues().toArray());
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T queryBean(String sql, Class<T> t, Object... params) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(sql, new BeanHandler<T>(t, processor), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> List<T> queryBeans(String sql, Class<T> t, Object... params) {
    BeanProcessor bean = new GenerousBeanProcessor();
    RowProcessor processor = new BasicRowProcessor(bean);
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(sql, new BeanListHandler<T>(t, processor), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Map<String, Object>> queryResults(String sql, Object... params) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(sql, new MapListHandler(), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Map<String, Object> queryResult(String sql, Object... params) {
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return run.query(sql, new MapHandler(), params);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object queryOneOrNull(String sql, Object... params) {
    Map<String, Object> result = queryResult(sql, params);
    if (result == null || result.isEmpty()) {
      return null;
    }
    return result.get(result.keySet().iterator().next());
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
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
  public long count(IQuery query) {
    if (!query.isCount()) {
      throw new RuntimeException("非Count的Query不能支持!");
    }
    QueryRunner run = new QueryRunner(dataSource);
    try {
      return Long.parseLong(
          String.valueOf(
              run.query(query.toText(), new ScalarHandler<Object>(), query.getValues().toArray())));
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
