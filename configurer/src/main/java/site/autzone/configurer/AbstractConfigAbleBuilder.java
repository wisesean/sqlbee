package site.autzone.configurer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可配置的构建抽象类
 * @author wisesean
 *
 * @param <O>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractConfigAbleBuilder<O> implements ConfigAbleBuilder<O, ConfigurerAble> {
	//builder
	private AtomicBoolean building = new AtomicBoolean();

	private O object;
	
	/**
	 * 不可复写的build
	 */
	@Override
	public final O build() {
		if (this.building.compareAndSet(false, true)) {
			this.object = doBuild();
			return this.object;
		}
		this.object = doBuild();
		return this.object;
		//throw new AlreadyBuiltException("This object has already been built");
	}

	/**
	 * 获取构建好的对象
	 * @return
	 */
	public final O getObject() {
		if (!this.building.get()) {
			throw new IllegalStateException("This object has not been built");
		}
		return this.object;
	}

	/**
	 * 子类build
	 * @return
	 * @throws Exception
	 */
	protected abstract O doBuild();
}
