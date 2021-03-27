package byx.ioc.exception;

public class IdDuplicatedException extends ByxContainerException {
    public IdDuplicatedException(String id) {
        super("Id has exist: " + id);
    }
}
