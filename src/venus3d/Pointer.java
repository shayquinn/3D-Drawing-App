
package venus3d;

public class Pointer {
    private double pointer;
    private int ArrayPointer;

    public Pointer(int ArrayPointer, double pointer) {
        this.pointer = pointer;
        this.ArrayPointer = ArrayPointer;
    }

    public double getPointer() {
        return pointer;
    }

    public int getArrayPointer() {
        return ArrayPointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public void setArrayPointer(int ArrayPointer) {
        this.ArrayPointer = ArrayPointer;
    }

    @Override
    public String toString() {
        return "Pointer{" + "pointer=" + pointer + ", ArrayPointer=" + ArrayPointer + '}';
    }
    
    
}
