package site.autzone.configurer;

/**
 * 配置信息
 * @author wisesean
 *
 * @param <P>
 */
public interface Configurer<P> {
	/**
	 * 根据被配置的对象初始化配置
	 * @param parent
	 * @throws Exception
	 */
	void init(P parent);

	/**
	 * 将配置配置到被配置对象
	 * @param parent
	 * @throws Exception
	 */
	void configure(P parent);
}
