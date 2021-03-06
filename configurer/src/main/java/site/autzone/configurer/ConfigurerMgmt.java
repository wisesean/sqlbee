package site.autzone.configurer;

import java.util.Collection;
import java.util.List;

/**
 * 可配置对象
 * @author wisesean
 *
 * @param <P>
 */
public interface ConfigurerMgmt<P> {
	/**
	 * 添加一个配置
	 * @param configurer
	 * @throws Exception
	 */
	 void add(Configurer<P> configurer);

	/**
	 * 获取所有的配置
	 * @return
	 */
	Collection<Configurer<P>> getConfigurers();
	
	/**
	 * 根据配置器类型,获取配置
	 * @return
	 */
	Collection<Configurer<P>> getConfigurers(Class<Configurer<P>> clazz);
	
	/**
	 * 初始化所有配置
	 * @throws Exception
	 */
	void init();
	
	/**
	 * 应用所有的配置
	 * @throws Exception
	 */
	void configure() throws Exception;
	
	/**
	 * 移除某一类配置
	 * @param clazz
	 * @return
	 */
	List<Configurer<P>> removeConfigurers(Class<Configurer<P>> clazz);
}
