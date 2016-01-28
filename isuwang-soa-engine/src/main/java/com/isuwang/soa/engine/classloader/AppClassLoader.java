package com.isuwang.soa.engine.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * App Class Loader
 *
 * @author craneding
 * @date 16/1/28
 */
public class AppClassLoader extends URLClassLoader {

    public AppClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if(name.startsWith("com.isuwang.soa.core") || name.startsWith("org.apache.thrift"))
            return ClassLoaderManager.shareClassLoader.loadClass(name);

        return super.loadClass(name, resolve);
    }
}
