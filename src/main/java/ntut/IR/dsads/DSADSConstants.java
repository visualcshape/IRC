package ntut.IR.dsads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vodalok on 2016/5/31.
 */
public class DSADSConstants {
    public static final int ACTION_AMT = 19;
    public static final int SUBJECT_AMT = 8;
    public static final int SEGMENT_AMT = 60;
    public static final int LINE_AMT = 125;
    public static final int SAMPLING_TIMES = 125;
    public static final int UNIT_AMT = 5;
    public static final int METER_AMT = 3;
    public static final int COL_AMT = 45;
    public static final List<Units> UNITS_LIST = new ArrayList<>(Arrays.asList(new Units[]{Units.Torso, Units.RightArm, Units.LeftArm, Units.RightLeg, Units.LeftLeg}));
    public static final List<Meters> METERS_LIST = new ArrayList<>(Arrays.asList(new Meters[]{Meters.Accelerator, Meters.Gyro, Meters.Magneto}));
}
