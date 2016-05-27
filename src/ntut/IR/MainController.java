package ntut.IR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {
    private Stage mStage = null;
    private Model mModel = new Model();

    @FXML
    private void ClickCloseMenuItem(){
        mStage.close();
    }

    @FXML
    private void ClickSelectDataSetLocationMenuItem(){
        String DIRECTORY_CHOOSER_TITLE = "Choose Data Set Directory";
        String PWD = ".";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(DIRECTORY_CHOOSER_TITLE);
        directoryChooser.setInitialDirectory(new File(PWD));
        File location = directoryChooser.showDialog(mStage);
        this.mModel.setDataSetLocation(location.getAbsolutePath());
    }

    public void setThisStage(Stage stage){
        mStage = stage;
    }
}
