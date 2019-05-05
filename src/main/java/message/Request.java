package message;

public class Request {
    private int id;
    private int startIndex;
    private int size;
    private String fileName;

    public Request(int id, int startIndex, int size, String fileName) {
        this.id = id;
        this.startIndex = startIndex;
        this.size = size;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", startIndex=" + startIndex +
                ", size=" + size +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
