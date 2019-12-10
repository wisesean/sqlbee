package site.autzone.sqlbee.builder;

/**
 * 抽象配置类
 * @author wisesean
 *
 * @param <P>
 */
public abstract class AbstractConfigurer<P> implements Configurer<P> {
	private P parent;
	
	@Override
	public void init(P parent) {
		this.parent = parent;
	}

	/**
	 * 获取被配置的对象
	 * @return
	 */
	public P getParent() {
		if(parent == null) {
			throw new RuntimeException("请先初始化配置!");
		}
		return this.parent;
	}
	
	/**
	 * 结束配置
	 * @return
	 */
	public P end() {
		return getParent();
	}
}
