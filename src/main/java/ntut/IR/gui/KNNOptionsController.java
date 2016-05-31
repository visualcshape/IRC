package ntut.IR.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Created by vodalok on 2016/5/31.
 */
public class KNNOptionsController {
    private Integer k = 1;

    private ChangeListener<Integer> onSpinnerValueChange = new ChangeListener<Integer>() {
        @Override
        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
            k = newValue;
        }
    };

    @FXML
    Spinner<Integer> kSpinner;

    public int getK(){
        return this.k;
    }

    public void initialize(){
        final int MAX = 100;
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        this.kSpinner.setValueFactory(factory);
        this.kSpinner.valueProperty().addListener(this.onSpinnerValueChange);
    }
}
