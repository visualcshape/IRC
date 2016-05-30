package ntut.IR.dsads;

import ntut.IR.exception.NotPreparedException;

import java.io.*;
import java.util.*;
import static ntut.IR.dsads.DSADSConstants.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetSubjectActionModeler {
    private boolean isDataPrepared = false;
    private Map<Meters, Map<Units, List<Vector3<Float>>>> activityData = new HashMap<>(METER_AMT);

    private void initData(){
        for(Meters meter:METERS_LIST) {
            Map<Units, List<Vector3<Float>>> unitsListMap = new HashMap<>(UNIT_AMT);
            for(Units unit:UNITS_LIST){
                unitsListMap.put(unit, new ArrayList<>(SAMPLING_TIMES * SEGMENT_AMT));
            }
            this.activityData.put(meter, unitsListMap);
        }
    }

    public DSADataSetSubjectActionModeler(){
        initData();
    }

    public void runModelerByFileList(List<File> segmentFileList) throws IOException{
        String DELIM = ",";
        for(File aFile : segmentFileList){
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(aFile)));
            for(int line = 0; line < LINE_AMT; line++){
                StringTokenizer values = new StringTokenizer(reader.readLine(), DELIM);
                for(Units unit: UNITS_LIST){
                    for(Meters meter:METERS_LIST){
                        Float x = Float.valueOf(values.nextToken());
                        Float y = Float.valueOf(values.nextToken());
                        Float z = Float.valueOf(values.nextToken());
                        Vector3<Float> aTimeInformation = new Vector3<>(x, y, z);
                        this.activityData.get(meter).get(unit).add(aTimeInformation);
                    }
                }
            }
        }
        this.isDataPrepared = true;
    }

    public final Map<Meters, Map<Units, List<Vector3<Float>>>> getSubjectData() throws NotPreparedException{
        if(!this.isDataPrepared)
            throw new NotPreparedException();
        return this.activityData;
    }
}
