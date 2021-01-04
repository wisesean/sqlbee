package site.autzone.configurer;

/**
 * 抽象配置类
 * @author wisesean
 *
 * @param <P>
 */
public abstract class AbstractConfigurer<P> implements Configurer<P> {
	private P parent;
	
	@Override
	public Configurer<P> init(P parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * 获取被配置的对象
	 * @return
	 */
	public P getParent() {
		if(parent == null) {
			throw new RuntimeException("must init configurer!");
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
