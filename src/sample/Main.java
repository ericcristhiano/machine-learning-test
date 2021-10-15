package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        float[] hsb = java.awt.Color.RGBtoHSB((int) 207, (int) 124, (int) 56, null);
        
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Machine Learning app");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
