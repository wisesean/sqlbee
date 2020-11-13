package site.autzone.sqlbee.model;

public interface ILeftJoin extends ITextable {
  void join(IQueryObject rightQueryObj);
  void addCondition(ICondition condition);
  void idx(int idx);
}
