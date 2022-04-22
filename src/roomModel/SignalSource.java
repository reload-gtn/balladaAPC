package roomModel;

import javafx.scene.shape.Circle;

import static roomModel.ControllerRoom.sizeDataSignatures;
import static roomModel.ControllerRoom.sizeFloorPlane;

public class SignalSource {
    private String name;
    private Location location;
    private Circle circle;

    SignalSource(String name, Location location) {
        this.name = name;
        this.location = location;
        this.circle = new Circle(4);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Circle getCircle() {
        return circle;
    }
}