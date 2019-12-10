package site.autzone.sqlbee.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.util.Assert;

/**
 * 可配置对象的抽象
 * @author wisesean
 *
 * @param <P>
 */
public class AbstractConfigurerable<P extends AbstractConfigurerable<P>> implements Configurerable<P> {
	/**
	 * 是否允许相同类型的配置存在多个
	 */
	private boolean allowConfigurersOfSameType = true;
	private final LinkedHashMap<Class<Configurer<P>>, List<Configurer<P>>> configurers = new LinkedHashMap<Class<Configurer<P>>, List<Configurer<P>>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void add(Configurer<P> configurer) {
		Assert.notNull(configurer, "configurer cannot be null");
		Class<Configurer<P>> clazz = (Class<Configurer<P>>) configurer.getClass();
		synchronized (configurers) {
			List<Configurer<P>> configs = allowConfigurersOfSameType ? this.configurers
					.get(clazz) : null;
			if (configs == null) {
				configs = new ArrayList<Configurer<P>>(1);
			}
			configs.add(configurer);
			this.configurers.put(clazz, configs);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection<Configurer<P>> getConfigurers() {
		List<Configurer<P>> configs = new ArrayList<>();
		Iterator it = this.configurers.keySet().iterator();
		while(it.hasNext()) {
			Class<Configurer<P>> clazz = (Class<Configurer<P>>) it.next();
			configs.addAll((List<Configurer<P>>) this.configurers.get(clazz));
		}
		return configs;
	}
	
	@Override
	public Collection<Configurer<P>> getConfigurers(Class<Configurer<P>> clazz) {
		List<Configurer<P>> configs = (List<Configurer<P>>) this.configurers.get(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		Collection<Configurer<P>> configurers = getConfigurers();
		for (Configurer<P> configurer : configurers) {
			configurer.init((P) this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configure() {
		Collection<Configurer<P>> configurers = getConfigurers();
		for (Configurer<P> configurer : configurers) {
			configurer.configure((P) this);
		}
	}

	@Override
	public List<Configurer<P>> removeConfigurers(Class<Configurer<P>> clazz) {
		List<Configurer<P>> configs = (List<Configurer<P>>) this.configurers.remove(clazz);
		if (configs == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(configs);
	}

}
