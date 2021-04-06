package byx.ioc.core;

/**
 * 依赖项
 *
 * @author byx
 */
public class Dependency {
    private final String id;
    private final Class<?> type;

    private Dependency(String id, Class<?> type) {
        this.id = id;
        this.type = type;
    }

    public static Dependency id(String id) {
        return new Dependency(id, null);
    }

    public static Dependency type(Class<?> type) {
        return new Dependency(null, type);
    }

    public String getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Dependency{" + "id='" + id + '\'' + ", type=" + type + '}';
    }
}
