package message;

import java.util.Arrays;

public class Result {
    int id;
    byte[] data;
    int fileLength;
    boolean end;

    public Result(int id, byte[] data, int fileLength, boolean end) {
        this.id = id;
        this.data = data;
        this.fileLength = fileLength;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public int getFileLength() {
        return fileLength;
    }

    public boolean isEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", data=" + Arrays.toString(data) +
                ", fileLength=" + fileLength +
                ", end=" + end +
                '}';
    }
}
