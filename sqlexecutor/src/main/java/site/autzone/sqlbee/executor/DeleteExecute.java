package site.autzone.sqlbee.executor;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 数据删除执行器
 * @author wisesean
 *
 */
public interface DeleteExecute {
	/**
	 * 设置数据源
	 * @param dataSource
	 */
	void setDataSource(DataSource dataSource);
	/**
	 * 根据语句删除
	 * @param sql sql语句
	 * @param params 参数
	 * @return The number of rows updated.
	 * @throws SQLException
	 */
	int delete(String sql , Object... params);
}
