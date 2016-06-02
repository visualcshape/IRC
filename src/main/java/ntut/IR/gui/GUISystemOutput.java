package ntut.IR.gui;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by vodalok on 2016/6/2.
 */
public class GUISystemOutput extends OutputStream {
    private StringBuilder outputBuilder = new StringBuilder();
    private ListView<String> listView = null;

    public GUISystemOutput(ListView<String> listView){
        this.listView = listView;
    }

    @Override
    public void write(int b) throws IOException {
        String aChar = "";
        if(!(aChar = String.valueOf((char) b)).equals("\n")){
            this.outputBuilder.append(aChar);
        }else{
            Platform.runLater(new ListViewUpdater(this.listView, new String(outputBuilder)));
            outputBuilder.delete(0, outputBuilder.length());
        }
    }
}
