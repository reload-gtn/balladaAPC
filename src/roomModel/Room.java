package roomModel;

public class Room {
    private double length, width, height;

    public Room(double length, double width, double height){
        this.length = length;
        this.width = width;
        this.height = height;

    }

    double getLength(){
        return this.length;
    }
    double getWidth(){
        return this.width;
    }
    double getHeight(){
        return this.height;
    }

    void setLength(double length){
        this.length = length;
    }
    void setWidth(double width){
        this.width = width;
    }
    void setHeight(double height){
        this.height = height;
    }

}

