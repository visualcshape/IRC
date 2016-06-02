package ntut.IR;

import ntut.IR.exception.NoThisDataSetNameException;
import ntut.IR.exception.NoThisMethodException;

import java.io.*;
import java.util.*;

/**
 * Created by VodalokLab on 2016/5/27.
 */
public class Model {
    private String mDataSetLocation = "";
    private String mReportStoreLocation = "";
    private Map<String, Pair<String, SupportDS>> mSupportDataSetNameAndFXMLAndType = new HashMap<>();
    private Map<String, Pair<String, SupportClassifier>> mSupportMethodGUIMap = new HashMap<>();
    private SupportClassifier mSelectedClassificationMethod = SupportClassifier.UNKNOWN;
    private SupportDS mSelectDS = SupportDS.UNKNOWN;
    //-----Data Set Setting----
    private Object[] dsSetting = null;
    //-----Method Setting----
    private Object[] methodSetting = null;

    private void loadSupportingDataSetList() throws IOException{
        String SUPPORTING_LIST_NAME = "supporting_list";
        InputStream supportingListStream = this.getClass().getClassLoader().getResourceAsStream(SUPPORTING_LIST_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(supportingListStream));
        String aLine;
        while((aLine = reader.readLine())!=null){
            String DELIM = ":";
            StringTokenizer tokenizer = new StringTokenizer(aLine, DELIM);
            String dsName = tokenizer.nextToken();
            String fxmlName = tokenizer.nextToken();
            String dsType = tokenizer.nextToken();
            Pair<String, SupportDS> dsPair = null;
            for(SupportDS ds:SupportDS.values()){
                if(ds.toString().equals(dsType)){
                    dsPair = new Pair<>(fxmlName, ds);
                }
            }
            mSupportDataSetNameAndFXMLAndType.put(dsName, dsPair);
        }
    }

    private void loadSupportClassificationMethodList() throws IOException{
        String SUPPORT_METHOD_LIST_NAME = "support_methods";
        InputStream supportingListStream = this.getClass().getClassLoader().getResourceAsStream(SUPPORT_METHOD_LIST_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(supportingListStream));
        String aLine;
        while((aLine = reader.readLine())!=null){
            String DELIM = ":";
            StringTokenizer tokenizer = new StringTokenizer(aLine, DELIM);
            String methodName = tokenizer.nextToken();
            String guiName = tokenizer.nextToken();
            String enumName = tokenizer.nextToken();
            Pair<String, SupportClassifier> classifierMethodPair = null;
            for(SupportClassifier type:SupportClassifier.values()){
                if(type.toString().equals(enumName)){
                    classifierMethodPair = new Pair<>(guiName, type);
                }
            }
            mSupportMethodGUIMap.put(methodName, classifierMethodPair);
        }
    }

    public Model(){

    }

    public void init() throws IOException{
        this.loadSupportingDataSetList();
        this.loadSupportClassificationMethodList();
    }

    //DS Setting...
    public void setDSSetting(Object[] setting){
        this.dsSetting = setting;
    }

    public Object[] getDSSetting(){
        return this.dsSetting;
    }
    //--------------//
    //Method Setting
    public void setMethodSetting(Object[] setting){
        this.methodSetting = setting;
    }

    public Object[] getMethodSetting(){
        return this.methodSetting;
    }

    public void setDataSetLocation(String location){
        this.mDataSetLocation = location;
    }

    public String getDataSetLocation(){
        return this.mDataSetLocation;
    }

    public String getReportStoreLocation() {
        return mReportStoreLocation;
    }

    public void setReportStoreLocation(String reportStoreLocation) {
        this.mReportStoreLocation = reportStoreLocation;
    }

    public void setSelectedClassificationMethod(String methodName) throws NoThisMethodException{
        SupportClassifier method = this.mSupportMethodGUIMap.get(methodName).second;
        if(method!=null){
            this.mSelectedClassificationMethod = method;
        }else{
            throw new NoThisMethodException();
        }
    }

    public final List<String> getSupportingDataSetList() {
        List<String> dataSetNames = new ArrayList<>();
        dataSetNames.addAll(mSupportDataSetNameAndFXMLAndType.keySet());
        return dataSetNames;
    }

    public final List<String> getSupportClassificationMethodList(){
        List<String> ret = new ArrayList<>();
        ret.addAll(this.mSupportMethodGUIMap.keySet());
        return ret;
    }

    public void setSelectDataSetByName(String dataSetName) throws NoThisDataSetNameException{
        if(this.mSupportDataSetNameAndFXMLAndType.containsKey(dataSetName)){
            this.mSelectDS = this.mSupportDataSetNameAndFXMLAndType.get(dataSetName).second;
        }else{
            throw new NoThisDataSetNameException();
        }
    }

    public boolean isReady(){
        return !this.mDataSetLocation.isEmpty()&
                !this.mReportStoreLocation.isEmpty()&
                (this.mSelectedClassificationMethod != SupportClassifier.UNKNOWN)&
                (this.mSelectDS != SupportDS.UNKNOWN);
    }

    public final String getDataSetFXMLName(String dataSetName) throws NoThisDataSetNameException{
        Pair<String, SupportDS> fxmlName = this.mSupportDataSetNameAndFXMLAndType.get(dataSetName);
        if(fxmlName == null){
            throw new NoThisDataSetNameException();
        }
        return this.mSupportDataSetNameAndFXMLAndType.get(dataSetName).first;
    }

    public final String getMethodFXMLName(String name) throws NoThisMethodException{
        String fxmlName = this.mSupportMethodGUIMap.get(name).first;
        if(fxmlName == null){
            throw new NoThisMethodException();
        }
        return this.mSupportMethodGUIMap.get(name).first;
    }

    public ClassifierRunner startClassifying() throws Exception{
        //Prepare Settings
        ClassifierSettings settings = new ClassifierSettings();
        settings.selectClassifier = this.mSelectedClassificationMethod;
        settings.selectDS = this.mSelectDS;
        settings.dsLocation = this.mDataSetLocation;
        settings.dsSetting = this.dsSetting;
        settings.methodSetting = this.methodSetting;
        settings.reportStoreLocation = this.mReportStoreLocation;

        ClassifierRunner classifierThread = new ClassifierRunner(settings);
        classifierThread.start();
        return classifierThread;
    }
}
