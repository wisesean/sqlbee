package site.autzone.sqlbee.model;

public interface IField extends ITextable {
	String getPrefix();
	void setPrefix(String prefix);
	String getName();
	void setName(String name);
	String getAlias();
	void setAlias(String alias);
}
