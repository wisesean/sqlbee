package site.autzone.sqlbee.condition;

public class NotExists extends AbstractExists{
    @Override
    protected Operator existsOp() {
        return Operator.NOT_EXISTS;
    }
}
