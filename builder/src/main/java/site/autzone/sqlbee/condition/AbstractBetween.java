package site.autzone.sqlbee.condition;

import org.apache.commons.lang3.Validate;
import site.autzone.sqlbee.column.Column;
import site.autzone.sqlbee.IColumn;
import site.autzone.sqlbee.IValue;

public abstract class AbstractBetween extends AbstractCondition {
    public void addField(IColumn column) {
        super.conditions.set(0, column);
    }
    public void setLeftValue(IValue leftValue) {
        super.conditions.set(1, leftValue);
    }

    public void setRightValue(Column rightValue) {
        super.conditions.set(2, rightValue);
    }

    @Override
    public String output() {
        Validate.isTrue(super.conditions.size() == 3);
        Validate.notNull(super.conditions.get(0));
        Validate.notNull(super.conditions.get(1));
        Validate.notNull(super.conditions.get(2));
        StringBuffer sb = new StringBuffer("(");
        sb.append(super.conditions.get(0).output());
        sb.append(" ").append(this.operate()).append(" ");
        sb.append(super.conditions.get(1).output());
        sb.append(" AND ");
        sb.append(super.conditions.get(2).output()).append(")");
        return sb.toString();
    }

    protected abstract Operator operate();
}