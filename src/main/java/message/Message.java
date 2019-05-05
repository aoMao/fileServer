package message;

public class Message {
    private int startIndex;
    private int endIndex;
    private String fileName;

    public Message(int startIndex, int endIndex, String fileName) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fileName = fileName;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
