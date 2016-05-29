package ntut.IR;

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
    private Map<String, SupportClassificationMethodEnumerable> mSupportClassificationMethods = new HashMap<>();
    private SupportClassificationMethodEnumerable mSelectedClassificationMethod = SupportClassificationMethodEnumerable.NOT_SELECT;
    private Integer mSelectDataSetNameIndex = null;

    private enum SupportClassificationMethodEnumerable{
        NOT_SELECT,
        KNN
    }

    private void loadSupportingDataSetList() throws IOException{
        String SUPPORTING_LIST_NAME = "supporting_list";
        char[] readBuff = new char[1024];
        InputStream supportingListStream = this.getClass().getResourceAsStream(SUPPORTING_LIST_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(supportingListStream));
        String aLine;
        while((aLine = reader.readLine())!=null){
            String DELIM = ":";
            StringTokenizer tokenizer = new StringTokenizer(aLine, DELIM);
            mSupportDataSetNameAndFXMLName.put(tokenizer.nextToken(), tokenizer.nextToken());
        }
    }

    private void loadSupportClassificationMethodList(){
        String KNN_NAME = "k-Nearest Neighbor Algorithm";
        this.mSupportClassificationMethods.put(KNN_NAME, SupportClassificationMethodEnumerable.KNN);
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
        SupportClassificationMethodEnumerable method = this.mSupportClassificationMethods.get(methodName);
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
        ret.addAll(this.mSupportClassificationMethods.keySet());
        return ret;
    }

    public boolean isReady(){
        return !this.mDataSetLocation.isEmpty()&
                !this.mReportStoreLocation.isEmpty()&
                (this.mSelectedClassificationMethod != SupportClassificationMethodEnumerable.NOT_SELECT)&
                (this.mSelectDataSetNameIndex != null);
    }

    public final String getFXMLName(String dataSetName){
        return this.mSupportDataSetNameAndFXMLName.get(dataSetName);
    }
}
