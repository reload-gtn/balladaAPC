package roomModel;

public class Location {
    private double x,y,z;

    Location (double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double l){
        this.x = l;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
