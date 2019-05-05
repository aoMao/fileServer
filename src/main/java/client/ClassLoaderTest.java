package client;

public class ClassLoaderTest extends ClassLoader {
    public static final int DEFAULT_SIZE = 1024;
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        // 走到这里证明本地找不到这个类，直接查询远程
        ReadFile readFile = new ReadFile(name);
        Client.getClient().getFile(name, readFile, 0, DEFAULT_SIZE);
        while (!readFile.dealEnd) {
            // 空循环，需要等待处理结束
        }
        System.out.println("file read end");
        return defineClass(name, readFile.result, 0, readFile.result.length);
    }
}
