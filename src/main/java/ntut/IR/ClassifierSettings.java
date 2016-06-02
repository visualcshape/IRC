package ntut.IR;

/**
 * Created by vodalok on 2016/6/2.
 */

public class ClassifierSettings {
    public SupportDS selectDS = SupportDS.UNKNOWN;
    public SupportClassifier selectClassifier = SupportClassifier.UNKNOWN;
    public IClassification classification = null;
    public String dsLocation = "";
    public String reportStoreLocation = "";
    public Object[] dsSetting = null;
    public Object[] methodSetting = null;
}
