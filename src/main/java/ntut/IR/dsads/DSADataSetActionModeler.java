package ntut.IR.dsads;

import java.io.*;
import java.util.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetActionModeler {
    private int LINE_LIMIT = 125;
    private int SAMPLING_TIMES = 125;
    private int UNIT_AMT = 5;
    private int METER_AMT = 3;
    private int SEG_AMT = 60;
    private List<Units> unitsList = new ArrayList<>(UNIT_AMT);
    private List<Meters> metersList = new ArrayList<>(METER_AMT);
    private Map<Meters, Map<Units, List<Vector3<Float>>>> activityData = new HashMap<>(METER_AMT);

    private void switchState(Units state){

    }

    private void addUnits(){
        unitsList.add(Units.Torso);
        unitsList.add(Units.RightArm);
        unitsList.add(Units.LeftArm);
        unitsList.add(Units.RightLeg);
        unitsList.add(Units.LeftLeg);
    }

    private void addMeters(){
        metersList.add(Meters.Accelerator);
        metersList.add(Meters.Gyro);
        metersList.add(Meters.Magneto);
    }

    private void initData(){
        for(Meters meter:metersList) {
            Map<Units, List<Vector3<Float>>> unitsListMap = new HashMap<>(UNIT_AMT);
            for(Units unit:unitsList){
                unitsListMap.put(unit, new ArrayList<>(SAMPLING_TIMES * SEG_AMT));
            }
            this.activityData.put(meter, unitsListMap);
        }
    }

    public DSADataSetActionModeler(){
        addUnits();
        addMeters();
        initData();
    }

    public void runModelerByFileList(List<File> segmentFileList) throws IOException{
        String DELIM = ",";
        for(File aFile : segmentFileList){
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(aFile)));
            for(int line = 0 ; line < LINE_LIMIT ; line++){
                StringTokenizer values = new StringTokenizer(reader.readLine(), DELIM);
                for(Units unit: unitsList){
                    for(Meters meter:metersList){
                        Float x = Float.valueOf(values.nextToken());
                        Float y = Float.valueOf(values.nextToken());
                        Float z = Float.valueOf(values.nextToken());
                        Vector3<Float> aTimeInformation = new Vector3<>(x, y, z);
                        this.activityData.get(meter).get(unit).add(aTimeInformation);
                    }
                }
            }
        }
    }
}
