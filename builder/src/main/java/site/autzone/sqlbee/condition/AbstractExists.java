package site.autzone.sqlbee.condition;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.sql.Sql;

public abstract class AbstractExists extends AbstractCondition {
    public void sub(Sql sql) {
        super.conditions.set(0, sql);
    }

    @Override
    public String output() {
        Validate.isTrue(super.conditions.size() == 1);
        StringBuffer sb = new StringBuffer(this.existsOp().operator()).append(" (")
                .append(super.conditions.get(0).output()).append(")");
        return sb.toString();
    }

    protected abstract Operator existsOp();
}