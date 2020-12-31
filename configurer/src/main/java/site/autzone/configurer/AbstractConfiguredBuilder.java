package site.autzone.configurer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * 携带多个配置信息的抽象构建器
 * @author wisesean
 *
 * @param <O>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractConfiguredBuilder<O> extends AbstractConfigAbleBuilder<O> {
	private final Log logger = LogFactory.getLog(getClass());
	private BuildState buildState = BuildState.UNBUILT;
	
	/**
	 * 是否允许相同类型的配置存在多个
	 */
	private boolean allowConfigurersOfSameType = true;
	protected final LinkedHashMap<Class<Configurer<ConfigurerAble>>, List<Configurer<ConfigurerAble>>> configurers =
				new LinkedHashMap<Class<Configurer<ConfigurerAble>>, List<Configurer<ConfigurerAble>>>();
	
	@SuppressWarnings("unchecked")
	public void add(Configurer<ConfigurerAble> configurer) {
		Assert.notNull(configurer, "configurer cannot be null");

		Class<Configurer<ConfigurerAble>> clazz = (Class<Configurer<ConfigurerAble>>) configurer.getClass();
		synchronized (configurers) {
			List<Configurer<ConfigurerAble>> configs = allowConfigurersOfSameType ? this.configurers
					.get(clazz) : null;
			if (configs == null) {
				configs = new ArrayList<Configurer<ConfigurerAble>>(1);
			}
			configs.add(configurer);
			this.configurers.put(clazz, configs);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Configurer<ConfigurerAble>> getConfigurers() {
		List<Configurer<ConfigurerAble>> configs = new ArrayList<>();
		Iterator it = this.configurers.keySet().iterator();
		while(it.hasNext()) {
			Class<Configurer<ConfigurerAble>> clazz = (Class<Configurer<ConfigurerAble>>) it.next();
			configs.addAll((List<Configurer<ConfigurerAble>>) this.configurers.get(clazz));
		}
		return configs;
	}
	
	@Override
	public Collection<Configurer<ConfigurerAble>> getConfigurers(Class<Configurer<ConfigurerAble>> clazz) {
		List<Configurer<ConfigurerAble>> configs =
				(List<Configurer<ConfigurerAble>>) this.configurers.get(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}

	@Override
	public void init() {
		Collection<Configurer<ConfigurerAble>> configurers = getConfigurers();
		for (Configurer<ConfigurerAble> configurer : configurers) {
			configurer.init(this);
		}
	}

	@Override
	public void configure() {
		Collection<Configurer<ConfigurerAble>> configurers = getConfigurers();
		for (Configurer<ConfigurerAble> configurer : configurers) {
			configurer.configure(this);
		}
	}

	@Override
	public List<Configurer<ConfigurerAble>> removeConfigurers(Class<Configurer<ConfigurerAble>> clazz) {
		List<Configurer<ConfigurerAble>> configs = (List<Configurer<ConfigurerAble>>) this.configurers.remove(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}
	
	/**
	 * 是否还未构建
	 * @return
	 */
	private boolean isUnbuilt() {
		synchronized (this.configurers) {
			return buildState == BuildState.UNBUILT;
		}
	}
	
	/**
	 * 未构建则构建对象，已经构建直接返回
	 * @return
	 */
	public O getOrBuild() {
		if (isUnbuilt()) {
			try {
				return build();
			}catch (Exception e) {
				logger.info("Failed to perform build. Returning null", e);
				return null;
			}
		}else {
			return getObject();
		}
	}
	
	protected void beforeInit() {};

	protected void beforeConfigure() {};

	protected abstract O performBuild();
	
	/**
	 * 结束构建之后清理构建器的配置器配置过程变量
	 * @throws Exception
	 */
	protected void afterBuild() {};
	
	@Override
	protected final O doBuild() {
		synchronized (this.configurers) {
			buildState = BuildState.INITIALIZING;
			logger.trace("init builder...");
			beforeInit();
			init();
			
			buildState = BuildState.CONFIGURING;
			logger.trace("configuring builder...");
			beforeConfigure();
			configure();

			buildState = BuildState.BUILDING;
			logger.trace("building...");
			O result = performBuild();
			
			buildState = BuildState.CLEANING;
			logger.trace("cleaning...");
			afterBuild();
			
			buildState = BuildState.BUILT;
			logger.trace("built.");
			return result;
		}
	}
	
	private static enum BuildState {
		UNBUILT,

		INITIALIZING,

		CONFIGURING,

		BUILDING,

		CLEANING,
		
		BUILT;
	}
}
