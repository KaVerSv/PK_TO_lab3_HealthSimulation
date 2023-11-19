package vectors_custom;

public class Vector2D implements IVector{
    private double x;
    private double y;


    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D source) {
        this.x = source.x;
        this.y = source.y;
    }
    
    public double[] getComponents() {
        double[] tmp = {x,y};
        return tmp;
    }

    public double abs() {
        double abs = Math.sqrt(this.x*this.x + this.y*this.y);
        return abs;
    }

    public double cdot(IVector param) {
        double[] var = param.getComponents();
        return this.x * var[0] + this.y * var[1];
    }

    public void add(IVector param) {
        double[] tmp = param.getComponents();

        this.x = this.x + tmp[0];
        this.y = this.y + tmp[1];
    }

    // methods for sick tracking
    public boolean spreadPossible(Vector2D vector) {
        // Obliczamy odległość między dwoma wektorami
        double distance = Math.sqrt(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2));
        // Sprawdzamy, czy odległość jest mniejsza lub równa ustalonej wartości progowej
        return distance <= 3.0;
    }

}