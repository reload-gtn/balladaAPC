package roomModel;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Test extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create a Box
        Box box = new Box(100, 100, 100);
        box.setTranslateX(100);
        box.setTranslateY(100);
        box.setTranslateZ(100);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGREEN);
        box.setMaterial(material);

        // Create a Camera to view the 3D Shapes
        ParallelCamera camera = new ParallelCamera();
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        // Add the Shapes and the Light to the Group
        Group root = new Group(box);
        root.setRotationAxis(Rotate.Z_AXIS);
        root.setRotate(20);
        // Create a Scene with depth buffer enabled
        Scene scene = new Scene(root, 200, 200, true);
        // Add the Camera to the Scene
        scene.setCamera(camera);

        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("An Example with Predefined 3D Shapes");
        // Display the Stage
        stage.show();
    }
}