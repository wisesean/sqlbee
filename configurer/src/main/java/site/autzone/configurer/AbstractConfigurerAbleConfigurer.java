package site.autzone.configurer;

/**
 * 一个存在可配置构建器的配置器
 * @author wisesean
 *
 * @param <P>
 */
public abstract class AbstractConfigurerAbleConfigurer<P> extends AbstractConfigurerMgmt<AbstractConfigurerAbleConfigurer<P>> implements Configurer<P> {
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
	
	@Override
	public void configure(P parent) {
		synchronized (parent) {
			init();
			configure();
			doConfigure(parent);
		}
	}

	public abstract void doConfigure(P parent);
}
