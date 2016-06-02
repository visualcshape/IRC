package ntut.IR.gui;

import javafx.scene.control.ListView;

/**
 * Created by vodalok on 2016/6/2.
 */
public class ListViewUpdater implements Runnable {
    ListView<String> outputListView = null;
    String updateString = "";

    public ListViewUpdater(ListView<String> updateListView, String updateString){
        this.outputListView = updateListView;
        this.updateString = updateString;
    }

    @Override
    public void run() {
        outputListView.getItems().add(updateString);
        outputListView.scrollTo(outputListView.getItems().size() - 1);
    }
}
