package ntut.IR.gui;

import ntut.IR.Model;

/**
 * Created by vodalok on 2016/6/2.
 */
public class NoneController implements IController {
    Model model = null;

    @Override
    public void setModel(Model model) {
        this.model = model;
        this.syncModel();
    }

    @Override
    public void syncModel() {
        Object[] methodSetting = null;
        this.model.setMethodSetting(methodSetting);
    }
}
