package site.autzone.configurer;

import java.util.Deque;

/**
 * 配置器上下文
 */
public interface ConfigurerContext<P> {
    /**
     * 获取被配置的对象
     * @return
     */
    P parent();

    /**
     * 获取配置器栈
     * @return
     */
    Deque deque();
}
