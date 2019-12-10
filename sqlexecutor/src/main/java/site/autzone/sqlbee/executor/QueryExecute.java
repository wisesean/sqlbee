package site.autzone.sqlbee.executor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import site.autzone.sqlbee.query.IQuery;

/**
 * 查询语句执行器实现
 * @author wisesean
 */

public interface QueryExecute {
	/**
	 * 设置数据源
	 * @param dataSource
	 */
	void setDataSource(DataSource dataSource);
	/**
	 * 执行count
	 * @param query
	 * @return
	 */
	long count(IQuery query);
	/**
	 * 根据查询构造器查询一条数据
	 * @param 查询构造器
	 * @return T数据
	 * @throws SQLException
	 */
	<T> T queryBean(IQuery query, Class<T> t);
	/**
	 * 根据查询构造器查询数据集
	 * @param query查询构造器
	 * @param t返回的数据集类型
	 * @return List<T>
	 * @throws SQLException
	 */
	<T> List<T> queryBeans(IQuery query, Class<T> t);
	/**
	 * 根据查询构造器返回数据的映射集
	 * @param query 查询构造器
	 * @return List<Map<String, Object>>数据集
	 * @throws SQLException
	 */
	List<Map<String, Object>> queryResults(IQuery query);
	/**
	 * 根据查询构造器返回一个数据映射
	 * @param query
	 * @return Map<String, Object>
	 * @throws SQLException
	 */
	Map<String, Object> queryResult(IQuery query);
	/**
	 * 根据查询sql和参数查询一个数据
	 * @param sql查询语句
	 * @param t数据类型
	 * @param params参数
	 * @return T
	 * @throws SQLException
	 */
	<T> T queryBean(String sql, Class<T> t, Object... params);
	/**
	 * 根据查询sql和参数查询一个数据集
	 * @param sql查询语句
	 * @param t数据类型
	 * @param params参数
	 * @return List<T>
	 * @throws SQLException
	 */
	<T> List<T> queryBeans(String sql, Class<T> t, Object... params);
	/**
	 * 根据查询语句和参数查询数据映射集
	 * @param sql查询语句
	 * @param 参数
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	List<Map<String, Object>> queryResults(String sql, Object... params);
	/**
	 * 根据查询语句和参数查询数据映射
	 * @param sql查询语句
	 * @param 参数
	 * @return Map<String, Object>
	 * @throws SQLException
	 */
	Map<String, Object> queryResult(String sql, Object... params);
	
	/**
	 * 查询某个值
	 * @param sql
	 * @param args
	 * @return 无结果返回null
	 */
	Object queryOneOrNull(String sql, Object... args);
	
	/**
	 * 调用无返回的存过
	 * @see com.ccssoft.smart.execute.BatchExecute.callProcedure(String, Object...)
	 * @param sql
	 * @param args
	 * @return
	 */
	@Deprecated
	int callProcedure(String sql, Object... args);
}
