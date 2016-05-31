package ntut.IR;

import ntut.IR.exception.NoThisDataSetNameException;
import ntut.IR.exception.NoThisMethodException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by VodalokLab on 2016/5/27.
 */
public class Model {
    private String mDataSetLocation = "";
    private String mReportStoreLocation = "";
    private Map<String, String> mSupportDataSetNameAndFXMLName = new HashMap<>();
    private Map<String, Pair<String, SupportClassifier>> mSupportMethodGUIMap = new HashMap<>();
    private SupportClassifier mSelectedClassificationMethod = SupportClassifier.UNKNOWN;
    private String mSelectDataSetName = "";

    private void loadSupportingDataSetList() throws IOException{
        String SUPPORTING_LIST_NAME = "supporting_list";
        InputStream supportingListStream = this.getClass().getClassLoader().getResourceAsStream(SUPPORTING_LIST_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(supportingListStream));
        String aLine;
        while((aLine = reader.readLine())!=null){
            String DELIM = ":";
            StringTokenizer tokenizer = new StringTokenizer(aLine, DELIM);
            mSupportDataSetNameAndFXMLName.put(tokenizer.nextToken(), tokenizer.nextToken());
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
        dataSetNames.addAll(mSupportDataSetNameAndFXMLName.keySet());
        return dataSetNames;
    }

    public final List<String> getSupportClassificationMethodList(){
        List<String> ret = new ArrayList<>();
        ret.addAll(this.mSupportMethodGUIMap.keySet());
        return ret;
    }

    public void setSelectDataSetName(String dataSetName) throws NoThisDataSetNameException{
        if(this.mSupportDataSetNameAndFXMLName.containsKey(dataSetName)){
            this.mSelectDataSetName = dataSetName;
        }else{
            throw new NoThisDataSetNameException();
        }
    }

    public boolean isReady(){
        return !this.mDataSetLocation.isEmpty()&
                !this.mReportStoreLocation.isEmpty()&
                (this.mSelectedClassificationMethod != SupportClassifier.UNKNOWN)&
                !this.mSelectDataSetName.isEmpty();
    }

    public final String getDataSetFXMLName(String dataSetName) throws NoThisDataSetNameException{
        String fxmlName = this.mSupportDataSetNameAndFXMLName.get(dataSetName);
        if(fxmlName == null){
            throw new NoThisDataSetNameException();
        }
        return this.mSupportDataSetNameAndFXMLName.get(dataSetName);
    }

    public final String getMethodFXMLName(String name) throws NoThisMethodException{
        String fxmlName = this.mSupportMethodGUIMap.get(name).first;
        if(fxmlName == null){
            throw new NoThisMethodException();
        }
        return this.mSupportMethodGUIMap.get(name).first;
    }
}
