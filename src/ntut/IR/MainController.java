package ntut.IR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MainController extends Observable implements Observer{
    //Data
    private Stage mStage = null;
    private Model mModel = new Model();

    //GUI Component
    @FXML
    private Label mDataSetDirectoryLabel;

    //GUI Data
    private String mDataSetDirectoryLabelText = "---";

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
        if(location != null) {
            this.mModel.setDataSetLocation(location.getAbsolutePath());
            this.setDataSetDirectoryLabelText(this.mModel.getDataSetLocation());
        }
    }

    @FXML
    private void ClickAboutMenuItem(){
        String HEADER_TEXT = "Information Retrieval : Classification";
        String TITLE = "About";
        String CONTENT = "Authors:\n 104598013 孫季加\n 104598050 黃仲毅\n";
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setHeaderText(HEADER_TEXT);
        aboutAlert.setTitle(TITLE);
        aboutAlert.setContentText(CONTENT);
        aboutAlert.show();
    }

    public void syncValues(){
        this.addObserver(this);
        this.setChanged();
        this.notifyObservers();
    }

    public void setThisStage(Stage stage){
        mStage = stage;
    }

    public String getDataSetDirectoryLabelText() {
        return mDataSetDirectoryLabelText;
    }

    public void setDataSetDirectoryLabelText(String mDataSetDirectoryLabelText) {
        this.mDataSetDirectoryLabelText = mDataSetDirectoryLabelText;
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.mDataSetDirectoryLabel.setText(this.getDataSetDirectoryLabelText());
    }
}
