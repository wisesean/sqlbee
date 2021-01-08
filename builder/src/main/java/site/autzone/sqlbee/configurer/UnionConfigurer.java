package site.autzone.sqlbee.configurer;

import site.autzone.configurer.AbstractConfigurerAbleConfigurer;
import site.autzone.sqlbee.ISql;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.sql.Union;

public class UnionConfigurer extends AbstractConfigurerAbleConfigurer<SqlBuilder> {
    private Union union = new Union();
    public UnionConfigurer all() {
        union.all();
        return this;
    }
    public UnionConfigurer sql(ISql subSql) {
        union.setUnionSql(subSql);
        return this;
    }
    @Override
    public void doConfigure(SqlBuilder parent) {
        //若条件是值的需要纳入管理
        parent.manageValue(this.union);
        parent.addUnion(this.union);
    }
}
