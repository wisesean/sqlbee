package site.autzone.sqlbee.condition;

public class Exists extends AbstractExists {
    @Override
    protected Operator existsOp() {
        return Operator.EXISTS;
    }
}
