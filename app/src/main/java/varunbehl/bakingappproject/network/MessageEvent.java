package varunbehl.bakingappproject.network;


public class MessageEvent {
    private int request;

    public MessageEvent(int request) {
        this.request = request;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }
}
