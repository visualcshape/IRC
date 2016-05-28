package ntut.IR;

import javafx.fxml.FXMLLoader;

/**
 * Created by Vodalok on 2016/5/29.
 */
public class DSADSState implements DataSetGUIState {
    private String DSADSFXML_NAME = "DSADSGUI.fxml";

    @Override
    public FXMLLoader loadGUI() {
        return new FXMLLoader(this.getClass().getResource(DSADSFXML_NAME));
    }
}
