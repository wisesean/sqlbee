package site.autzone.sqlbee.condition;

public class NotBetween extends AbstractBetween {
	@Override
	protected Operator operate() {
		return Operator.NOT_BETWEEN;
	}
}
