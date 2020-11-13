package site.autzone.sqlbee.model;

public interface IQueryObject extends ITextable {
	String getName();
	void setName(String name);
	String getAlias();
	void setAlias(String alias);
	void leftJoin(ILeftJoin leftJoin);
	void rightJoin(IRightJoin rightJoin);
}