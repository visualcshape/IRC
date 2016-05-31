package ntut.IR.dsads;

import ntut.IR.AbstractClassification;
import ntut.IR.ClassifierFactory;
import ntut.IR.SupportClassifier;
import ntut.IR.exception.NotPreparedException;
import ntut.IR.exception.NotSupportedClassifierException;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ntut.IR.dsads.DSADSConstants.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSAClassification extends AbstractClassification{
    private final String CLASS_NAME = "Action Class";
    private DSADataSetLoader loader = null;
    private List<String> classNames = new ArrayList<>(ACTION_AMT);
    private Map<String, Attribute> allAttrMap = new HashMap<>(COL_AMT +1);//Plus one class attribute
    private Instances trainInstances = null;
    private Classifier model = null;

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

    private Map<String, Attribute> getDataAttributesMap(List<Attribute> attributes){
        Map<String, Attribute> ret = new HashMap<>(attributes.size());
        for(Attribute attribute:attributes){
            ret.put(attribute.name(), attribute);
        }
        return ret;
    }

    private Attribute buildNumericAttribute(Units unit, Meters meter, String axis){
        String attrName = getAttributeName(unit, meter, axis);
        return new Attribute(attrName);
    }

    private String getAttributeName(Units unit, Meters meter, String axis) {
        StringBuilder attrName = new StringBuilder();
        //Final result UNIT_(x,y,z)_METER
        attrName.append(unit.toString());
        attrName.append(axis);
        attrName.append(meter.toString());
        return new String(attrName);
    }

    private int overallCapacity(){
        return COL_AMT*LINE_AMT*SEGMENT_AMT*this.trainAmt;
    }

    private Classifier createClassifierByType(SupportClassifier classifierType, Object[] classifierArgs) throws NotSupportedClassifierException{
        Classifier classifier = null;
        switch (classifierType){
            case KNN:
                classifier = ClassifierFactory.createKNNClassifier((int)classifierArgs[0]);
        }
        return classifier;
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
        Attribute classAttr = new Attribute(CLASS_NAME, classNames);

        ArrayList<Attribute> allAttr = new ArrayList<>(COL_AMT + 1);//plus 1 class attr
        allAttr.addAll(attributes);
        allAttr.add(classAttr);
        this.allAttrMap = this.getDataAttributesMap(allAttr);

        this.trainInstances = new Instances(RELATION_NAME, allAttr, this.overallCapacity());
        this.trainInstances.setClass(classAttr);
    }

    public void train(SupportClassifier classifierType,Object[] classifierArgs) throws Exception{
        for(int trainingClass = 0 ; trainingClass < ACTION_AMT ; trainingClass++){
            for(int subject = 0 ; subject < this.trainAmt; subject++){
                DSADataSetSubjectActionModeler aSubjectModeler = new DSADataSetSubjectActionModeler();
                List<File> segFileList = this.loader.getDataSetList().get(trainingClass).get(subject);
                aSubjectModeler.runModelerByFileList(segFileList);
                //Create Instance
                Instance aInstance = new DenseInstance(COL_AMT);
                for(Units unit:UNITS_LIST){
                    for(Meters meter:METERS_LIST){
                        for(Vector3<Float> aVector:aSubjectModeler.getSubjectData().get(meter).get(unit)){
                            final String xAttrName = this.getAttributeName(unit, meter, "x");
                            final String yAttrName = this.getAttributeName(unit, meter, "y");
                            final String zAttrName = this.getAttributeName(unit, meter, "z");
                            aInstance.setValue(this.allAttrMap.get(xAttrName), aVector.x);
                            aInstance.setValue(this.allAttrMap.get(yAttrName), aVector.y);
                            aInstance.setValue(this.allAttrMap.get(zAttrName), aVector.z);
                        }
                    }
                }
                String curClassName = this.classNames.get(trainingClass);
                aInstance.setValue(this.allAttrMap.get(CLASS_NAME), curClassName);
                this.trainInstances.add(aInstance);
            }
        }

        this.model = this.createClassifierByType(classifierType, classifierArgs);
        this.model.buildClassifier(this.trainInstances);
    }

    public void test() throws Exception{
        Evaluation evaluation = new Evaluation(trainInstances);
        //TODO: test the classifier.
    }
}
