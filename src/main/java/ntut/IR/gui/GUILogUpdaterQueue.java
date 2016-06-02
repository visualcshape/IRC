package ntut.IR.gui;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by vodalok on 2016/6/2.
 */
public class GUILogUpdaterQueue extends Thread{
    private Queue<String> outputQueue = new LinkedList<>();
    private ListView<String> outputListView = null;

    public GUILogUpdaterQueue(ListView<String> outputListView){
        this.outputListView = outputListView;
    }

    public void addStringToQueue(String output){
        this.outputQueue.offer(output);
    }

    @Override
    public void run() {
        //Keep Consume
        while(true){
            if(outputQueue.isEmpty()){
                continue;
            }else{
                Platform.runLater(new ListViewUpdater(this.outputListView, outputQueue.poll()));
            }
        }
    }
}
