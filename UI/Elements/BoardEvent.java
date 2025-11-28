package UI.Elements;

public class BoardEvent {
    private int x;
    private int y;

    public BoardEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
