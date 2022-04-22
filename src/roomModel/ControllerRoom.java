package roomModel;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ControllerRoom implements Initializable {

    public Button buttonMaterial;
    public TextField lengthRoom, widthRoom, heightRoom;
    public TextField xIRS,yIRS,zIRS;
    public TextField xAS,yAS,zAS;
    public Rectangle floorPlane;
    public LinkedList<SignalSource> sources;
    public Line lineXIRS, lineYIRS, lineXAS, lineYAS;
    public Text textBDataSignatures, textLDataSignatures;
    public Text textIRS, textAS;
    public Text textAxisX,textAxisY;
    public Pane paneRoomMaket;


    public static double sizeFloorPlane = 230;
    public static double sizeDataSignatures = 10;
    @FXML
    private Circle circleIRS;
    @FXML
    private Circle circleAS;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sources = new LinkedList<>();
        sources.add(GuiRoom.signalIRS);
        sources.get(0).getCircle().setFill(Color.BLUE);
        sources.add(GuiRoom.signalAS);
        sources.get(1).getCircle().setFill(Color.RED);
    }


    @FXML
    public void buttonClicked() {
        buttonMaterial.setText("Кнопка нажата!");
    }

    public void setEnableTextAreaIRSAS(){
        xIRS.setDisable(false);
        yIRS.setDisable(false);
        zIRS.setDisable(false);
        xAS.setDisable(false);
        yAS.setDisable(false);
        zAS.setDisable(false);
    }

    public void setRoomMaket(){
        double kRatio = GuiRoom.room.getLength()/GuiRoom.room.getWidth();
        if(kRatio >= 1){
            floorPlane.setWidth(sizeFloorPlane);
            floorPlane.setHeight(sizeFloorPlane/kRatio);
            floorPlane.setX(sizeDataSignatures);
            floorPlane.setY(sizeFloorPlane+sizeDataSignatures - floorPlane.getHeight());

            textBDataSignatures.setY(sizeFloorPlane+sizeDataSignatures - floorPlane.getHeight());
            textLDataSignatures.setX(sizeFloorPlane+sizeDataSignatures);
            textLDataSignatures.setY(sizeFloorPlane+2*sizeDataSignatures);

        } else {
            floorPlane.setHeight(sizeFloorPlane);
            floorPlane.setWidth(sizeFloorPlane*kRatio);
            floorPlane.setY(sizeDataSignatures);
            floorPlane.setX(sizeDataSignatures);

            textBDataSignatures.setY(sizeDataSignatures);
            textLDataSignatures.setY(sizeFloorPlane+2*sizeDataSignatures);
            textLDataSignatures.setX(floorPlane.getWidth()+sizeDataSignatures);
        }
    }


    @FXML
    public void buttonEnableSizeRoom() {
        GuiRoom.room.setHeight(Double.valueOf(heightRoom.getText()));
        GuiRoom.room.setLength(Double.valueOf(lengthRoom.getText()));
        GuiRoom.room.setWidth(Double.valueOf(widthRoom.getText()));
        setRoomMaket();
        setPosTextAxis();
        setEnableTextAreaIRSAS();
        setUnVisibleAllForSignal();
    }

    @FXML
    public void buttonEstablishSignals(){

        //заполнение позиции для источника речевого сигнала
        sources.get(0).getLocation().setX(Double.valueOf(xIRS.getText()));
        sources.get(0).getLocation().setY(Double.valueOf(yIRS.getText()));
        sources.get(0).getLocation().setZ(Double.valueOf(zIRS.getText()));
        //заполнение позиции для акустической
        sources.get(1).getLocation().setX(Double.valueOf(xAS.getText()));
        sources.get(1).getLocation().setY(Double.valueOf(yAS.getText()));
        sources.get(1).getLocation().setZ(Double.valueOf(zAS.getText()));

        setPosCircleSignal(sources.get(0));
        setPosCircleSignal(sources.get(1));
        //setPosCircleSignals();

        setPosLineSignals();
        setPosTextSignals();
        setVisibleAllForSignal();
        paneRoomMaket.getChildren().remove(sources.get(0).getCircle());
        paneRoomMaket.getChildren().remove(sources.get(1).getCircle());
        paneRoomMaket.getChildren().add(sources.get(0).getCircle());
        paneRoomMaket.getChildren().add(sources.get(1).getCircle());
    }

    public void setPosCircleSignal(SignalSource source){
        source.getCircle().setLayoutX(sizeDataSignatures+sizeFloorPlane*(source.getLocation().getX()/GuiRoom.room.getLength()));
        source.getCircle().setLayoutY(sizeDataSignatures+sizeFloorPlane-floorPlane.getHeight()*(source.getLocation().getY()/GuiRoom.room.getWidth()));
    }


    public void setPosLineSignals(){
        lineXIRS.setStartX(sizeDataSignatures);
        lineXIRS.setEndX(sources.get(0).getCircle().getLayoutX());
        lineXIRS.setStartY(sources.get(0).getCircle().getLayoutY());
        lineXIRS.setEndY(sources.get(0).getCircle().getLayoutY());

        lineYIRS.setStartX(sources.get(0).getCircle().getLayoutX());
        lineYIRS.setEndX(sources.get(0).getCircle().getLayoutX());
        lineYIRS.setStartY(sources.get(0).getCircle().getLayoutY());
        lineYIRS.setEndY(sizeDataSignatures+sizeFloorPlane);

        lineXAS.setStartX(sizeDataSignatures);
        lineXAS.setEndX(sources.get(1).getCircle().getLayoutX());
        lineXAS.setStartY(sources.get(1).getCircle().getLayoutY());
        lineXAS.setEndY(sources.get(1).getCircle().getLayoutY());

        lineYAS.setStartX(sources.get(1).getCircle().getLayoutX());
        lineYAS.setEndX(sources.get(1).getCircle().getLayoutX());
        lineYAS.setStartY(sources.get(1).getCircle().getLayoutY());
        lineYAS.setEndY(sizeDataSignatures+sizeFloorPlane);

    }
    public void setPosTextSignals(){
        textIRS.setX(sources.get(0).getCircle().getLayoutX());
        textIRS.setY(sources.get(0).getCircle().getLayoutY()-6);

        textAS.setX(sources.get(1).getCircle().getLayoutX());
        textAS.setY(sources.get(1).getCircle().getLayoutY()-6);

    }

    public void setPosTextAxis(){
        textAxisX.setX(sizeDataSignatures+floorPlane.getWidth()/2);
        textAxisY.setY(sizeDataSignatures+sizeFloorPlane-floorPlane.getHeight()/2);
    }

    public void setVisibleAllForSignal(){
        sources.get(0).getCircle().setVisible(true);
        sources.get(1).getCircle().setVisible(true);
        lineXIRS.setVisible(true);
        lineYIRS.setVisible(true);
        lineXAS.setVisible(true);
        lineYAS.setVisible(true);
        textIRS.setVisible(true);
        textAS.setVisible(true);
    }

    public void setUnVisibleAllForSignal(){
        sources.get(0).getCircle().setVisible(false);
        sources.get(1).getCircle().setVisible(false);
        lineXIRS.setVisible(false);
        lineYIRS.setVisible(false);
        lineXAS.setVisible(false);
        lineYAS.setVisible(false);
        textIRS.setVisible(false);
        textAS.setVisible(false);
    }

}
