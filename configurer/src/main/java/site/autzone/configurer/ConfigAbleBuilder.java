package site.autzone.configurer;

/**
 * 可配置的构造器
 * @author wisesean
 *
 * @param <O>
 * @param <P>
 */
public interface ConfigAbleBuilder<O, P> extends ConfigurerAble<P> {
	O build() throws Exception;
}
