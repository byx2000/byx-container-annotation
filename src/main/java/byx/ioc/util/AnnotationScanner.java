package byx.ioc.util;

import byx.ioc.exception.PackageScanException;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 注解扫描器，支持复合注解
 *
 * @author byx
 */
public class AnnotationScanner {
    private static final String SUFFIX = ".class";

    private final Map<Class<?>, Map<Class<? extends Annotation>, Annotation>> classes = new HashMap<>();

    /**
     * 创建注解扫描器
     *
     * @param type 包所在的类
     */
    public AnnotationScanner(Class<?> type) {
        this(type.getPackageName());
    }

    /**
     * 创建注解扫描器
     *
     * @param packageName 包名
     */
    public AnnotationScanner(String packageName) {
        try {
            String packagePath = packageName.replace(".", File.separator);
            URI uri = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI();
            File classpath = new File(uri);
            File basePath = new File(classpath, packagePath);
            traverseAllClassFiles(classpath.getAbsolutePath(), basePath);
        } catch (Exception e) {
            throw new PackageScanException(e);
        }
    }

    /**
     * 获取包下所有类
     */
    public Set<Class<?>> getClasses() {
        return classes.keySet();
    }

    /**
     * 获取被指定注解标注的所有类
     */
    public Set<Class<?>> getClassesAnnotatedBy(Class<? extends Annotation> annotationClass) {
        return classes.keySet().stream()
                .filter(c -> classes.get(c).containsKey(annotationClass))
                .collect(Collectors.toSet());
    }

    /**
     * 获取某类型上的指定注解
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<?> type, Class<T> annotationType) {
        return (T) classes.get(type).get(annotationType);
    }

    /**
     * 遍历目录下所有class文件
     */
    private void traverseAllClassFiles(String classPath, File file) {
        if (!file.isDirectory()) {
            if (file.getName().endsWith(SUFFIX)) {
                String classFilePath = file.getAbsolutePath();
                String fullClassName = classFilePath.substring(classPath.length() + 1, classFilePath.length() - 6).replace(File.separator, ".");
                try {
                    Class<?> type = Class.forName(fullClassName);
                    classes.put(type, getTypeAnnotations(type));
                } catch (ClassNotFoundException e) {
                    throw new PackageScanException(e);
                }
            }
            return;
        }

        for (File f : Objects.requireNonNull(file.listFiles())) {
            traverseAllClassFiles(classPath, f);
        }
    }

    /**
     * 获取某类型被标注的所有注解
     */
    private Map<Class<? extends Annotation>, Annotation> getTypeAnnotations(Class<?> type) {
        Annotation[] annotations = type.getAnnotations();
        Queue<Annotation> queue = new LinkedList<>(Arrays.asList(annotations));
        Set<Annotation> book = new HashSet<>(Set.of(annotations));
        Set<Annotation> result = new HashSet<>();
        while (!queue.isEmpty()) {
            Annotation a = queue.remove();
            result.add(a);
            for (Annotation aa : a.annotationType().getAnnotations()) {
                if (!book.contains(aa)) {
                    queue.add(aa);
                    book.add(aa);
                }
            }
        }
        return result.stream()
                .collect(Collectors.toMap(Annotation::annotationType, a -> a, (e, r) -> e));
    }
}
