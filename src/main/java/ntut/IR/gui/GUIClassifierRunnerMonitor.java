package ntut.IR.gui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import ntut.IR.ClassifierRunner;

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
        System.out.println("Finished.");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                startButton.setText("開始");
                startButton.setDisable(false);
            }
        });
    }
}
