package site.autzone.configurer;

/**
 * 可配置的构造器
 * @author wisesean
 *
 * @param <O>
 * @param <P>
 */
public interface ConfigMgmtBuilder<O, P> extends ConfigurerMgmt<P> {
	/**
	 * 构建
	 * @return
	 */
	O build();
}
