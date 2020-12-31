package site.autzone.sqlbee.configurer;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.condition.Exists;
import site.autzone.sqlbee.sql.Sql;

public class ExistsConfigurer extends AbstractConditionConfigurer {
    public ExistsConfigurer sub(Sql sub) {
        Validate.isTrue(sub.isSubSql());
        super.add(0, sub);
        return this;
    }
    @Override
    public void doConditionConfigure(SqlBuilder parent) {
        AbstractCondition existsCondition = new Exists();
        existsCondition.addAll(super.conditions);
        parent.addCondition(existsCondition);
    }
}
