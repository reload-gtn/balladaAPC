package roomModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiRoom extends Application {

    public static Room room = new Room(5, 4, 3);
    public static SignalSource signalIRS = new SignalSource("ИРС", new Location(0,0,0));
    public static SignalSource signalAS = new SignalSource("АС", new Location(0,0,0));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("room.fxml"));
        primaryStage.setTitle("Параметры помещениЯ");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
