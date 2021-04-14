package byx.ioc.util;

import byx.ioc.exception.PackageScanException;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 包扫描工具类
 *
 * @author byx
 */
public class PackageUtils {
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 获取指定包下的所有类
     */
    public static List<Class<?>> getPackageClasses(String packageName) {
        try {
            String packagePath = packageName.replace(".", File.separator);
            URI uri = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI();
            File classpath = new File(uri);
            File root = new File(classpath, packagePath);
            List<Class<?>> classes = new ArrayList<>();
            traverseAllClassFiles(classpath.getAbsolutePath(), root, classes);
            return classes;
        } catch (Exception e) {
            throw new PackageScanException(e);
        }
    }

    /**
     * 遍历文件夹
     */
    private static void traverseAllClassFiles(String classPath, File file, List<Class<?>> classes) {
        if (!file.isDirectory()) {
            if (file.getName().endsWith(CLASS_FILE_SUFFIX)) {
                String classFilePath = file.getAbsolutePath();
                String fullClassName = classFilePath.substring(classPath.length() + 1, classFilePath.length() - 6).replace(File.separator, ".");
                try {
                    classes.add(Class.forName(fullClassName));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return;
        }

        for (File f : Objects.requireNonNull(file.listFiles())) {
            traverseAllClassFiles(classPath, f, classes);
        }
    }
}