package site.autzone.sqlbee.condition;

public class Between extends AbstractBetween {
	@Override
	protected Operator operate() {
		return Operator.BETWEEN;
	}
}
