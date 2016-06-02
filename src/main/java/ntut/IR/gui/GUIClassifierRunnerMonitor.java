package ntut.IR.gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import ntut.IR.ClassifierRunner;
import ntut.IR.GUIUtility;

/**
 * Created by vodalok on 2016/6/2.
 */
public class GUIClassifierRunnerMonitor extends Thread {
    ClassifierRunner runner = null;
    Button startButton = null;

    public GUIClassifierRunnerMonitor(ClassifierRunner runner, Button button){
        this.runner = runner;
        this.startButton = button;
    }

    @Override
    public void run() {
        while(runner.isAlive());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(runner.isHasException()){
                    System.out.println("An error has occurred!");
                    GUIUtility.showExceptionAlert(runner.getException());
                }else{
                    System.out.println("All Jobs are done successfully!");
                }
                startButton.setText("開始");
                startButton.setDisable(false);
            }
        });
    }
}
