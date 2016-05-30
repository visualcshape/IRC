package ntut.IR;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ntut.IR.gui.MainController;

public class Main extends Application {
    private String ICON_PNG_NAME = "icon.png";
    private String FXML_FILE_NAME = "GUI.fxml";
    private String TITLE = "Classification";
    private int WIN_WIDTH = 800;
    private int WIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(FXML_FILE_NAME));
        Parent root = loader.load();
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WIN_WIDTH, WIN_HEIGHT));
        primaryStage.show();
        primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResourceAsStream(ICON_PNG_NAME)));
        MainController mainController = loader.getController();
        mainController.setThisStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
