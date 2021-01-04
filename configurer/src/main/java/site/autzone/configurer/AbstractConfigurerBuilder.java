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
public abstract class AbstractConfigurerBuilder<O> extends AbstractConfigurerMgmtBuilder<O> {
	private final Log logger = LogFactory.getLog(getClass());
	private BuildState buildState = BuildState.UNBUILT;
	
	/**
	 * 是否允许相同类型的配置存在多个
	 */
	private boolean allowConfigurersOfSameType = true;
	protected final LinkedHashMap<Class<Configurer<ConfigurerMgmt>>, List<Configurer<ConfigurerMgmt>>> configurers =
				new LinkedHashMap<Class<Configurer<ConfigurerMgmt>>, List<Configurer<ConfigurerMgmt>>>();
	
	@SuppressWarnings("unchecked")
	public void add(Configurer<ConfigurerMgmt> configurer) {
		Assert.notNull(configurer, "configurer cannot be null");

		Class<Configurer<ConfigurerMgmt>> clazz = (Class<Configurer<ConfigurerMgmt>>) configurer.getClass();
		synchronized (configurers) {
			List<Configurer<ConfigurerMgmt>> cfgs = allowConfigurersOfSameType ? this.configurers
					.get(clazz) : null;
			if (cfgs == null) {
				cfgs = new ArrayList<Configurer<ConfigurerMgmt>>(1);
			}
			cfgs.add(configurer);
			this.configurers.put(clazz, cfgs);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Configurer<ConfigurerMgmt>> getConfigurers() {
		List<Configurer<ConfigurerMgmt>> configs = new ArrayList<>();
		Iterator it = this.configurers.keySet().iterator();
		while(it.hasNext()) {
			Class<Configurer<ConfigurerMgmt>> clazz = (Class<Configurer<ConfigurerMgmt>>) it.next();
			configs.addAll((List<Configurer<ConfigurerMgmt>>) this.configurers.get(clazz));
		}
		return configs;
	}
	
	@Override
	public Collection<Configurer<ConfigurerMgmt>> getConfigurers(Class<Configurer<ConfigurerMgmt>> clazz) {
		List<Configurer<ConfigurerMgmt>> configs =
				(List<Configurer<ConfigurerMgmt>>) this.configurers.get(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}

	@Override
	public void init() {
		Collection<Configurer<ConfigurerMgmt>> configurers = getConfigurers();
		for (Configurer<ConfigurerMgmt> configurer : configurers) {
			configurer.init(this);
		}
	}

	@Override
	public void configure() {
		Collection<Configurer<ConfigurerMgmt>> configurers = getConfigurers();
		for (Configurer<ConfigurerMgmt> configurer : configurers) {
			configurer.configure(configurer.getParent());
		}
	}

	@Override
	public List<Configurer<ConfigurerMgmt>> removeConfigurers(Class<Configurer<ConfigurerMgmt>> clazz) {
		List<Configurer<ConfigurerMgmt>> configs = (List<Configurer<ConfigurerMgmt>>) this.configurers.remove(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}
	
	/**
	 * 是否还未构建
	 * @return
	 */
	private boolean isUnBuilt() {
		synchronized (this.configurers) {
			return buildState == BuildState.UNBUILT;
		}
	}
	
	/**
	 * 未构建则构建对象，已经构建直接返回
	 * @return
	 */
	public O getOrBuild() {
		if (isUnBuilt()) {
			return build();
		}else {
			return getTarget();
		}
	}

	/**
	 * 初始化前
	 */
	protected void beforeInit() {};

	/**
	 * 构建前
	 */
	protected void beforeConfigure() {};

	/**
	 * 执行构建
	 * @return
	 */
	protected abstract O performBuild();
	
	/**
	 * 构建完成
	 */
	protected void afterBuild() {};

	/**
	 * 构建生命周期
	 * @return
	 */
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
