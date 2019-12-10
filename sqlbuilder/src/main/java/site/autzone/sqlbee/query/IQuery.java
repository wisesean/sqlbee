package site.autzone.sqlbee.query;

import java.util.List;

import site.autzone.sqlbee.model.ICondition;
import site.autzone.sqlbee.model.IField;
import site.autzone.sqlbee.model.IHasValues;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.model.ITextable;

/**
 * 查询构造器接口
 * @author wisesean
 *
 */
public interface IQuery extends IHasValues {
	/**
	 * 添加查询对象
	 * @param queryObject
	 */
	void addQueryObject(IQueryObject queryObject);
	/**
	 * 添加查询列
	 * @param field
	 */
	void addCloumn(IField field);
	/**
	 * 添加查询条件
	 * @param condition
	 */
	void addCondition(ICondition condition);
	/**
	 * 设置排序
	 * @param orderBy
	 */
	void setOrderBy(ITextable orderBy);
	/**
	 * 设置是否count
	 * @param isCount
	 */
	void isCount(boolean isCount);
	/**
	 * 获取是否count
	 * @return
	 */
	boolean isCount();
	/**
	 * 设置起始行
	 * @param firstResult
	 */
	void setFirstResultKey(String firstResultKey);
	/**
	 * 设置最大行
	 * @param maxResult
	 */
	void setMaxResultsKey(String maxResultsKey);
	/**
	 * 获取构造器管理的所有Value
	 * @return
	 */
	List<Object> getValues();
	/**
	 * 构造查询文本
	 * @return
	 */
	String buildQueryText();
	/**
	 * 构造查询
	 */
	void build();
}
