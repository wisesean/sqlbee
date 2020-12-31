package site.autzone.sqlbee.condition;

/**
 * 查询条件类型
 *
 * @author xiaowj
 */
public enum Operator {
    NOT_EQUALS("<>"), EQUALS("="), LIKE("LIKE"), LT("<"), LTE("<="), GT(">"), GTE(">="), BETWEEN(
            "BETWEEN"), NOT_BETWEEN(
            "NOT BETWEEN"), IN("IN"), NOT_IN("NOT IN"), IS_NULL("IS NULL"), IS_NOT_NULL("IS NOT NULL"),
    EXISTS("EXISTS"),
    NOT_EXISTS("NOT EXISTS");

    private String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    public String operator() {
        return operator;
    }
}
