package byx.ioc.exception;

/**
 * 找不到类型转换器
 *
 * @author byx
 */
public class ValueConverterNotFoundException extends ByxContainerException {
    public ValueConverterNotFoundException(Class<?> type) {
        super("Cannot find ValueConverter implement for " + type);
    }
}
