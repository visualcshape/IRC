package ntut.IR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VodalokLab on 2016/5/27.
 */
public class Model {
    private String mDataSetLocation = "";
    private String mReportStoreLocation = "";
    private List<String> mSupportingDataSetList = new ArrayList<>();
    private Map<String, SupportClassificationMethodEnumerable> mSupportClassificationMethods = new HashMap<>();

    private enum SupportClassificationMethodEnumerable{
        KNN
    }

    private void loadSupportingDataSetList() throws IOException{
        String SUPPORTING_LIST_NAME = "supporting_list";
        char[] readBuff = new char[1024];
        InputStream supportingListStream = this.getClass().getResourceAsStream(SUPPORTING_LIST_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(supportingListStream));
        String aLine;
        while((aLine = reader.readLine())!=null){
            mSupportingDataSetList.add(aLine);
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

    public void setReportStoreLocation(String mReportStoreLocation) {
        this.mReportStoreLocation = mReportStoreLocation;
    }

    public final List<String> getSupportingDataSetList() {
        return mSupportingDataSetList;
    }

    public final List<String> getSupportClassificationMethodList(){
        List<String> ret = new ArrayList<>();
        ret.addAll(this.mSupportClassificationMethods.keySet());
        return ret;
    }
}
