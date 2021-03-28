package byx.ioc.core;

/**
 * 容器工厂
 *
 * @author byx
 */
public interface ContainerFactory {
    /**
     * 创建容器
     * @return 容器
     */
    Container create();
}
