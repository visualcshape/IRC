package ntut.IR;

import javafx.scene.control.Alert;

/**
 * Created by Vodalok on 2016/6/2.
 */
public final class GUIUtility {
    public static void showExceptionAlert(Exception exception) {
        String ALERT_TITLE = "Error";
        String ALERT_HEADER = exception.getClass().getName();
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(ALERT_TITLE);
        errorAlert.setHeaderText(ALERT_HEADER);
        errorAlert.setContentText(exception.getLocalizedMessage());
        errorAlert.show();
    }
}
