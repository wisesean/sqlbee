package site.autzone.sqlbee.model;

public interface IRightJoin extends ITextable {
  void join(IQueryObject rightQueryObj);
  void addCondition(ICondition condition);
  void idx(int idx);
}
