package vectors_custom;

public interface IVector {
    public double abs();
    public double cdot(IVector p);
    public double[] getComponents();
}
