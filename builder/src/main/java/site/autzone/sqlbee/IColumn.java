package site.autzone.sqlbee;

/**
 * 字段
 */
public interface IColumn extends ITextAble {
	String getPrefix();
	void setPrefix(String prefix);
	String getName();
	void setName(String name);
	String getAlias();
	void setAlias(String alias);
}
