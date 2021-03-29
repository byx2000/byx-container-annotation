package byx.ioc.exception;

/**
 * 包扫描时发生异常
 *
 * @author byx
 */
public class PackageScanException extends ByxContainerException{
    public PackageScanException(Throwable cause) {
        super("Exception occur when scanning package.", cause);
    }
}
