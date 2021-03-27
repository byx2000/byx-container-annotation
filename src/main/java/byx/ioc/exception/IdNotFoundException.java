package byx.ioc.exception;

public class IdNotFoundException extends ByxContainerException {
    public IdNotFoundException(String id) {
        super("There is no object with id \"" + id + "\" in container.");
    }
}
