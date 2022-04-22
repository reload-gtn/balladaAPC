package roomModel;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Test2 extends Application {

    private static final float WIDTH=1400;
    private  static  final  float HEIGHT=800;

    private double anchorX, anchorY;
    private  double anchorAngleX=0;
    private  double anchorAngleY=0;
    private  final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private  final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Box box = prepareBox();

        SmartGroup group = new SmartGroup();
        group.getChildren().add(box);
        //group.getChildren().add(new PointLight());
        group.getChildren().add(new AmbientLight());
        group.getChildren().addAll(prepareLightSource());

        //1. Ambient Light
        //2. Point Light

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-200);

        Scene scene =new Scene(group,  WIDTH,  HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(0);
        group.translateYProperty().set(0);
        group.translateZProperty().set(0);

        initMouseControl(group, scene, primaryStage);


        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event->{
            switch (event.getCode()){
                case W:
                    group.translateZProperty().set(group.getTranslateZ()+50);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ()-50);
                    break;
                case Q:
                    group.rotateByX(10);
                    break;
                case E:
                    group.rotateByX(-10);
                    break;
                case A:
                    group.rotateByY(10);
                    break;
                case D:
                    group.rotateByY(-10);
                    break;
            }
        });


        primaryStage.setTitle("Camera3D");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                pointLight.setRotate(pointLight.getRotate()+1);
            }
        };
        timer.start();
    }

    private final PointLight pointLight = new PointLight();
    private Node[] prepareLightSource() {
        //AmbientLight ambientLight = new AmbientLight();
        //ambientLight.setColor(Color.AQUA);
        //return  ambientLight;
        //pointLight = new PointLight();
        pointLight.setColor(Color.RED);
        pointLight.getTransforms().add(new Translate(0,-100, 20));
        pointLight.setRotationAxis(Rotate.X_AXIS);

        Sphere sphere = new Sphere(2);
        sphere.getTransforms().setAll(pointLight.getTransforms());
        sphere.rotateProperty().bind(pointLight.rotateProperty());
        sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());

        return  new Node[]{pointLight, sphere};
    }

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial();
        //material.setDiffuseColor(Color.BLUE);
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/wood.jpg")));
        material.setSpecularColor(Color.WHITE);
        //material.setSpecularColor(Color.BLACK); //no reflection
        Box box = new Box( 100, 20, 50);
        box.setMaterial(material);
        return box;
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(mouseEvent -> {
            anchorX = mouseEvent.getSceneX();
            anchorY = mouseEvent.getScreenY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            angleX.set(anchorAngleX - (anchorY - mouseEvent.getSceneY()));
            angleY.set(anchorAngleY - (anchorX- mouseEvent.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            double delta = scrollEvent.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ()+delta);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group{
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang){
            r=new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
        void rotateByY(int ang){
            r=new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

    }
}
