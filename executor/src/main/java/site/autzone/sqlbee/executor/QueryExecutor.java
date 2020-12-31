package site.autzone.sqlbee.executor;

import site.autzone.sqlbee.ISql;

import java.util.List;
import java.util.Map;

/**
 * 查询语句执行器实现
 * @author wisesean
 */

public interface QueryExecutor {
	/**
	 * 执行count
	 * @param sql
	 * @return
	 */
	long count(ISql sql);
	/**
	 * 根据查询构造器查询一条数据
	 * @param sql 查询构造器
	 * @return T数据
	 */
	<T> T queryBean(ISql sql, Class<T> t);
	/**
	 * 根据查询构造器查询数据集
	 * @param sql 查询构造器
	 * @param t 返回的数据集类型
	 * @return List<T>
	 */
	<T> List<T> queryBeans(ISql sql, Class<T> t);
	/**
	 * 根据查询构造器返回数据的映射集
	 * @param sql 查询构造器
	 * @return List<Map<String, Object>>数据集
	 */
	List<Map<String, Object>> queryResults(ISql sql);
	/**
	 * 根据查询构造器返回一个数据映射
	 * @param sql
	 * @return Map<String, Object>
	 */
	Map<String, Object> queryResult(ISql sql);
	/**
	 * 根据查询sql和参数查询一个数据
	 * @param sql 查询语句
	 * @param t 数据类型
	 * @param params 参数
	 * @return T
	 */
	<T> T queryBean(String sql, Class<T> t, Object... params);
	/**
	 * 根据查询sql和参数查询一个数据集
	 * @param sql 查询语句
	 * @param t 数据类型
	 * @param params 参数
	 * @return List<T>
	 */
	<T> List<T> queryBeans(String sql, Class<T> t, Object... params);
	/**
	 * 根据查询语句和参数查询数据映射集
	 * @param sql 查询语句
	 * @param params 参数
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> queryResults(String sql, Object... params);
	/**
	 * 根据查询语句查询一行数据
	 * @param sql 查询语句
	 * @param params 参数
	 * @return Map<String, Object>
	 */
	Map<String, Object> queryResult(String sql, Object... params);
	
	/**
	 * 查询某个值
	 * @param sql
	 * @param params 参数
	 * @return 无结果返回null
	 */
	Object queryObject(String sql, Object... params);

	/**
	 * 查询某个值根据类型转换
	 * @param t
	 * @param sql
	 * @param params
	 * @return
	 */
	Object queryObject(Class t, String sql, Object... params);
}
