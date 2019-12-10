package site.autzone.sqlbee.executor.proc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AbstractQueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 基于DbUtils的调用带数据返回的存过,线程安全的类
 * @author xiaowj
 * @see ResultSetHandler
 */
public class ProcRunner extends AbstractQueryRunner {

  public ProcRunner() {
    super();
  }

  public ProcRunner(DataSource ds) {
    super(ds);
  }

  public ProcRunner(boolean pmdKnownBroken) {
    super(pmdKnownBroken);
  }

  public ProcRunner(DataSource ds, boolean pmdKnownBroken) {
    super(ds, pmdKnownBroken);
  }

  public <T> T queryProc(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params)
      throws SQLException {
    return this.queryProc(conn, false, sql, rsh, params);
  }

  public <T> T queryProc(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
    return this.queryProc(conn, false, sql, rsh, (Object[]) null);
  }

  public <T> T queryProc(String sql, ResultSetHandler<T> rsh, Object... params)
      throws SQLException {
    Connection conn = this.prepareConnection();
    return this.queryProc(conn, true, sql, rsh, params);
  }

  public <T> T queryProc(String sql, ResultSetHandler<T> rsh) throws SQLException {
    Connection conn = this.prepareConnection();
    return this.queryProc(conn, true, sql, rsh, (Object[]) null);
  }

  /**
   * 调用存过
   *
   * @param conn 连接字符串
   * @param closeConn 是否关闭连接, true 关闭, false 不关闭
   * @param sql 存过执行语句
   * @param params 存过参数
   * @return 返回查询结果
   * @throws SQLException 参数错误抛出异常.
   */
  private <T> T queryProc(
      Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
      throws SQLException {
    if (conn == null) {
      throw new SQLException("Null connection");
    }
    if (sql == null) {
      if (closeConn) {
        close(conn);
      }
      throw new SQLException("Null SQL statement");
    }
    if (rsh == null) {
      if (closeConn) {
        close(conn);
      }
      throw new SQLException("Null ResultSetHandler");
    }
    if (sql.toUpperCase().indexOf("CALL") == -1) {
      if (closeConn) {
        close(conn);
      }
      throw new SQLException("Not a callable statement");
    }
    CallableStatement stmt = null;
    ResultSet rs = null;
    T result = null;

    try {
      stmt = this.prepareCall(conn, sql);
      this.fillStatement(stmt, params);
      rs = this.wrap(stmt.executeQuery());
      result = rsh.handle(rs);
    } catch (SQLException e) {
      this.rethrow(e, sql, params);
    } finally {
      try {
        close(rs);
      } finally {
        close(stmt);
        if (closeConn) {
          close(conn);
        }
      }
    }
    return result;
  }

  protected CallableStatement prepareCall(Connection conn, String sql) throws SQLException {
    return conn.prepareCall(sql);
  }
}