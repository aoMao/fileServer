package client;

import message.Request;

import static client.ClassLoaderTest.DEFAULT_SIZE;

public class ReadFile implements Runnable {

    byte[] msg ;
    int id = 0;
    int fileLength = 0;
    boolean end = false;
    byte[] result;
    int nowIndex = 0;
    volatile boolean dealEnd = false;
    String fileName;

    public ReadFile(String fileName) {
        this.fileName = fileName;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public void run() {
        // 初始化结果数组
        if (result == null) {
            result = new byte[fileLength];
        }
        // 设置result
        for (int i = 0; i < msg.length; ++i) {
            result[nowIndex++] = msg[i];
        }


        if (end) {
            // 删除worker
            Client.getClient().removeCallBack(id);
            // 通知处理结束
            dealEnd = true;
        } else {
            Request request = new Request(id, nowIndex, DEFAULT_SIZE, fileName);
            Client.getClient().getFile(request);
        }
    }

    public boolean isDealEnd() {
        return dealEnd;
    }
}
