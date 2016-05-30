package ntut.IR.dsads;

import ntut.IR.AbstractClassification;
import weka.core.Attribute;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

import static ntut.IR.dsads.DSADSConstants.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSAClassification extends AbstractClassification{
    DSADataSetLoader loader = null;
    List<String> classNames = new ArrayList<>(ACTION_AMT);
    Instances trainInstances = null;

    private void generateClassList(String formatPattern){
        for(int actionClass = 1 ; actionClass <= ACTION_AMT ; actionClass++){
            String aClassName = String.format(formatPattern, actionClass);
            classNames.add(aClassName);
        }
    }

    private List<Attribute> generateAttributes(){
        List<Attribute> ret = new ArrayList<>(COL_AMT);
        for(Units unit:UNITS_LIST){
            for(Meters meter:METERS_LIST){
                final String X = "x";
                final String Y = "y";
                final String Z = "z";
                ret.add(this.buildNumericAttribute(unit, meter, X));
                ret.add(this.buildNumericAttribute(unit, meter, Y));
                ret.add(this.buildNumericAttribute(unit, meter, Z));
            }
        }
        return ret;
    }

    private Attribute buildNumericAttribute(Units unit, Meters meter, String axis){
        StringBuilder attrName = new StringBuilder();
        final String UNDER_SCORE = "_";
        attrName.append(unit.toString());
        attrName.append(UNDER_SCORE);
        attrName.append(axis);
        attrName.append(meter.toString());
        return new Attribute(new String(attrName));
    }

    private int overallCapacity(){
        return COL_AMT*LINE_AMT*SEGMENT_AMT*this.trainAmt;
    }

    public DSAClassification(int trainAmt,DSADataSetLoader loader) throws IllegalArgumentException{
        this(trainAmt, loader, "Action %02d");
    }

    public DSAClassification(int trainAmt, DSADataSetLoader loader, String classPattern){
        super(SUBJECT_AMT, trainAmt);
        if(SUBJECT_AMT <= trainAmt)
            throw new IllegalArgumentException("Amount of train set should not larger than amount of set");
        this.loader = loader;
        this.generateClassList(classPattern);
    }

    public void prepare(){
        final String RELATION_NAME = "Actions";
        //Sensor attributes
        List<Attribute> attributes = this.generateAttributes();
        //Class Attribute
        Attribute classAttr = new Attribute("Action Class", classNames);

        ArrayList<Attribute> allAttr = new ArrayList<>(COL_AMT + 1);//plus 1 class attr
        allAttr.addAll(attributes);
        allAttr.add(classAttr);

        this.trainInstances = new Instances(RELATION_NAME, allAttr, this.overallCapacity());
        this.trainInstances.setClass(classAttr);
    }

    public void train(){
        for(int subject = 0 ; subject < trainAmt ; subject++){
            //TODO: train classifirer by input subject...
        }
    }
}
