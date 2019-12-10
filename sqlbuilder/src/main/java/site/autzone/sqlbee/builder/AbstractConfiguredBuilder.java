package site.autzone.sqlbee.builder;

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
public abstract class AbstractConfiguredBuilder<O> extends AbstractConfigableBuilder<O> {
	private final Log logger = LogFactory.getLog(getClass());
	private BuildState buildState = BuildState.UNBUILT;
	
	/**
	 * 是否允许相同类型的配置存在多个
	 */
	private boolean allowConfigurersOfSameType = true;
	protected final LinkedHashMap<Class<Configurer<Configurerable>>, List<Configurer<Configurerable>>> configurers = 
				new LinkedHashMap<Class<Configurer<Configurerable>>, List<Configurer<Configurerable>>>();
	
	@SuppressWarnings("unchecked")
	public void add(Configurer<Configurerable> configurer) {
		Assert.notNull(configurer, "configurer cannot be null");

		Class<Configurer<Configurerable>> clazz = (Class<Configurer<Configurerable>>) configurer.getClass();
		synchronized (configurers) {
			List<Configurer<Configurerable>> configs = allowConfigurersOfSameType ? this.configurers
					.get(clazz) : null;
			if (configs == null) {
				configs = new ArrayList<Configurer<Configurerable>>(1);
			}
			configs.add(configurer);
			this.configurers.put(clazz, configs);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Configurer<Configurerable>> getConfigurers() {
		List<Configurer<Configurerable>> configs = new ArrayList<>();
		Iterator it = this.configurers.keySet().iterator();
		while(it.hasNext()) {
			Class<Configurer<Configurerable>> clazz = (Class<Configurer<Configurerable>>) it.next();
			configs.addAll((List<Configurer<Configurerable>>) this.configurers.get(clazz));
		}
		return configs;
	}
	
	@Override
	public Collection<Configurer<Configurerable>> getConfigurers(Class<Configurer<Configurerable>> clazz) {
		List<Configurer<Configurerable>> configs = 
				(List<Configurer<Configurerable>>) this.configurers.get(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}

	@Override
	public void init() {
		Collection<Configurer<Configurerable>> configurers = getConfigurers();
		for (Configurer<Configurerable> configurer : configurers) {
			configurer.init(this);
		}
	}

	@Override
	public void configure() {
		Collection<Configurer<Configurerable>> configurers = getConfigurers();
		for (Configurer<Configurerable> configurer : configurers) {
			configurer.configure(this);
		}
	}

	@Override
	public List<Configurer<Configurerable>> removeConfigurers(Class<Configurer<Configurerable>> clazz) {
		List<Configurer<Configurerable>> configs = (List<Configurer<Configurerable>>) this.configurers.remove(clazz);
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
			logger.debug("init builder...");
			beforeInit();
			init();
			
			buildState = BuildState.CONFIGURING;
			logger.debug("configuring builder...");
			beforeConfigure();
			configure();

			buildState = BuildState.BUILDING;
			logger.debug("building...");
			O result = performBuild();
			
			buildState = BuildState.CLEANING;
			logger.debug("cleaning...");
			afterBuild();
			
			buildState = BuildState.BUILT;
			logger.debug("built.");
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
