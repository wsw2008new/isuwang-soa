package com.isuwang.soa.engine.classloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Loader Manager
 *
 * @author craneding
 * @date 16/1/28
 */
public class ClassLoaderManager {

    public static ShareClassLoader shareClassLoader;

    public static PlatformClassLoader platformClassLoader;

    public static List<AppClassLoader> appClassLoaders = new ArrayList<>();

}
