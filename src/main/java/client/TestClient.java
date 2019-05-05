package client;

import config.Config;

import java.lang.reflect.InvocationTargetException;

public class TestClient {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Thread.currentThread().setContextClassLoader(new ClassLoaderTest());
        ClassLoaderTest classLoaderTest = new ClassLoaderTest();
        Class clz = classLoaderTest.loadClass(Config.getProp("filename"));
        java.lang.reflect.Method method = clz.getMethod("testFinally", null);
        if (method != null)
            method.invoke(null);
    }
}
