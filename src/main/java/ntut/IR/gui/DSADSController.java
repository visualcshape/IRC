package ntut.IR.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import ntut.IR.Model;
import ntut.IR.dsads.DSADSSetting;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Vodalok on 2016/5/29.
 */
public class DSADSController extends Observable implements Observer, IController{
    //DSSetting [0] trainAmt

    //Constant
    private int TOTAL_SET = 8;
    private int DEFAULT_TRAINING_SET = 6;
    private String UCIURI = "https://archive.ics.uci.edu/ml/datasets/Daily+and+Sports+Activities";
    private Model model;

    //GUI
    @FXML
    private Spinner<Integer> trainingSetSpinner;
    @FXML
    private Label testSetLabel;

    //GUI Data
    private String testSetLabelText = Integer.toString(TOTAL_SET - DEFAULT_TRAINING_SET);

    //Data
    private Integer trainingSetSpinnerValue = DEFAULT_TRAINING_SET;

    @FXML
    private void ClickUCIHyperLink() throws URISyntaxException,IOException{
        if(Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI(UCIURI));
    }

    private ChangeListener<Integer> onSpinnerValueChanged = new ChangeListener<Integer>() {
        @Override
        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
            Integer testingSetCount = TOTAL_SET - newValue;
            trainingSetSpinnerValue = newValue;
            model.getDSSetting()[0] = trainingSetSpinnerValue;
            setTestSetLabelText(testingSetCount.toString());
        }
    };

    @Override
    public void setModel(Model model) {
        this.model = model;
        this.syncModel();
    }

    @Override
    public void syncModel() {
        Object[] defaultSetting = new Object[]{trainingSetSpinnerValue};
        this.model.setDSSetting(defaultSetting);
    }

    public void initialize(){
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, TOTAL_SET - 1, DEFAULT_TRAINING_SET);
        this.trainingSetSpinner.setValueFactory(valueFactory);
        this.trainingSetSpinner.getValueFactory().valueProperty().addListener(this.onSpinnerValueChanged);
        this.addObserver(this);
        this.update(this, null);
    }

    public Integer getTrainingSetSpinnerValue() {
        return trainingSetSpinnerValue;
    }

    public void setTrainingSetSpinnerValue(Integer trainingSetSpinnerValue) {
        this.trainingSetSpinnerValue = trainingSetSpinnerValue;
    }

    public String getTestSetLabelText() {
        return testSetLabelText;
    }

    public void setTestSetLabelText(String testSetLabelText) {
        this.testSetLabelText = testSetLabelText;
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.testSetLabel.setText(this.getTestSetLabelText());
    }
}
