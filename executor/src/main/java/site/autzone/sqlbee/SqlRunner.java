package site.autzone.sqlbee;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import site.autzone.sqlbee.executor.SqlExecutor;
import site.autzone.sqlbee.runner.ProcRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Sql执行器
 */
public class SqlRunner implements SqlExecutor {
    @Autowired private DataSource dataSource;
    @Value("${site.autzone.sqlbee.execute.async.nThreads: 100}") private Integer nThreads;

    @Override
    public QueryRunner getQueryRunner() {
        return new QueryRunner(this.dataSource);
    }

    @Override
    public AsyncQueryRunner getAsyncQueryRunner() {
        return new AsyncQueryRunner(Executors.newFixedThreadPool(nThreads), this.getQueryRunner());
    }

    @Override
    public ProcRunner getProcRunner() {
        return new ProcRunner(this.dataSource);
    }

    @Override
    public int execute(String sql, Object... params) {
        try {
            return this.getQueryRunner().execute(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int[] batch(ISql sql, Object[][] data) {
        return this.batch(sql.output(), data);
    }

    @Override
    public int update(ISql sql) {
        return this.update(sql.output(), sql.getParameters().toArray());
    }

    @Override
    public int update(String updateSql, Object... params) {
        try {
            return this.getQueryRunner().update(updateSql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int[] batch(String sql, Object[][] data) {
        try {
            return this.getQueryRunner().batch(sql, data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<Integer> updateAsync(String updateSql, Object... params) {
        try {
            return this.getAsyncQueryRunner().update(updateSql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<int[]> batchAsync(String updateSql, Object[][] params) {
        try {
            return this.getAsyncQueryRunner().batch(updateSql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int callProcedure(String sql, Object... args) {
        return this.execute(sql, args);
    }

    @Override
    public <T> T queryProcBean(String sql, Class<T> t, Object... params) {
        try {
            return this.getProcRunner().queryProc(sql, new BeanHandler<T>(t,
                    new BasicRowProcessor(new GenerousBeanProcessor())), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> queryProcBeans(String sql, Class<T> t, Object... params) {
        try {
            return this.getProcRunner().queryProc(sql, new BeanListHandler<T>(t,
                    new BasicRowProcessor(new GenerousBeanProcessor())), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long count(ISql sql) {
        Validate.isTrue(sql.isCount(), "sql must be count.");
        try {
            return (Long) this.getQueryRunner().query(sql.output(), new ScalarHandler<Object>(), sql.getParameters().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T queryBean(ISql sql, Class<T> t) {
        return this.queryBean(sql.output(), t, sql.getParameters().toArray());
    }

    @Override
    public <T> List<T> queryBeans(ISql sql, Class<T> t) {
        return this.queryBeans(sql.output(), t, sql.getParameters().toArray());
    }

    @Override
    public List<Map<String, Object>> queryResults(ISql sql) {
        return this.queryResults(sql.output(), sql.getParameters().toArray());
    }

    @Override
    public Map<String, Object> queryResult(ISql sql) {
        return this.queryResult(sql.output(), sql.getParameters().toArray());
    }

    @Override
    public <T> T queryBean(String sql, Class<T> t, Object... params) {
        try {
            return this.getQueryRunner().query(sql, new BeanHandler<T>(t, new BasicRowProcessor(new GenerousBeanProcessor())), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> queryBeans(String sql, Class<T> t, Object... params) {
        try {
            return this.getQueryRunner().query(sql, new BeanListHandler<T>(t, new BasicRowProcessor(new GenerousBeanProcessor())), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> queryResults(String sql, Object... params) {
        try {
            return this.getQueryRunner().query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> queryResult(String sql, Object... params) {
        try {
            return this.getQueryRunner().query(sql, new MapHandler(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object queryObject(String sql, Object... params) {
        try {
            return this.getQueryRunner().query(sql, new ScalarHandler<Object>(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object queryObject(Class t, String sql, Object... params) {
        Object result = this.queryObject(sql, params);
        if(result == null) {
            return null;
        }
        return ConvertUtils.convert(result, t);
    }

    @Override
    public int delete(String sql, Object... params) {
        return this.execute(sql, params);
    }

    @Override
    public int delete(ISql sql) {
        return this.execute(sql.output(), sql.getParameters().toArray());
    }

    @Override
    public int insert(ISql sql) {
        return this.execute(sql.output(), sql.getParameters().toArray());
    }

    @Override
    public <T> T insert(ISql sql, Class<T> t) {
        return this.insert(sql.output(), t, sql.getParameters().toArray());
    }

    @Override
    public <T> T insert(String sql, Class<T> t, Object... params) {
        try {
            return this.getQueryRunner().insert(sql,
                    new BeanHandler<T>(t, new BasicRowProcessor(new GenerousBeanProcessor())),
                    params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insert(String sql, Object... params) {
        return this.execute(sql, params);
    }
}
