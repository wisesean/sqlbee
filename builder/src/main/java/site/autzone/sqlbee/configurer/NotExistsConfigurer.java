package site.autzone.sqlbee.configurer;

import site.autzone.sqlbee.builder.SqlBuilder;
import site.autzone.sqlbee.condition.AbstractCondition;
import site.autzone.sqlbee.condition.NotExists;
import site.autzone.sqlbee.sql.Sql;

public class NotExistsConfigurer extends AbstractConditionConfigurer {
    public NotExistsConfigurer sub(Sql sub) {
        super.add(0, sub);
        return this;
    }
    @Override
    public void doConditionConfigure(SqlBuilder parent) {
        AbstractCondition existsCondition = new NotExists();
        existsCondition.addAll(super.conditions);
        parent.addCondition(existsCondition);
//        Sql sub = (Sql) super.conditions.get(0);
//        sub.valueMap().values().forEach(v -> {
//            IValue value = (IValue) v;
//            parent.getValues().add(value.getValue());
//            parent.manageValue(value);
//        });
    }
}
