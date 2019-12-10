package site.autzone.sqlbee.builder;

/**
 * 可配置的构造器
 * @author wisesean
 *
 * @param <O>
 * @param <P>
 */
public interface ConfigableBuilder<O, P> extends Configurerable<P> {
	O build() throws Exception;
}
