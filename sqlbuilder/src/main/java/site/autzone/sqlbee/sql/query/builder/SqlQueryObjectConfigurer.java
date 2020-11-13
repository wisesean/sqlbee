package site.autzone.sqlbee.sql.query.builder;

import java.util.ArrayList;
import java.util.List;
import site.autzone.sqlbee.builder.AbstractConfigurer;
import site.autzone.sqlbee.model.IQueryObject;
import site.autzone.sqlbee.sql.query.SqlQueryObject;

public class SqlQueryObjectConfigurer extends AbstractConfigurer<SqlQueryBuilder> {
  private List<IQueryObject> queryObjects = new ArrayList<>();

  public SqlQueryObjectConfigurer() {}

  public SqlQueryObjectConfigurer(SqlQueryBuilder parent) {
    init(parent);
  }

  public SqlQueryObjectConfigurer queryObject(String name) {
    queryObject(name, null);
    return this;
  }

  public SqlQueryObjectConfigurer queryObject(String name, String alias) {
    queryObjects.add(new SqlQueryObject(name, alias));
    return this;
  }
  
  @Override
  public void configure(SqlQueryBuilder parent) {
    parent.addAllQueryObject(this.queryObjects);
  }
}
