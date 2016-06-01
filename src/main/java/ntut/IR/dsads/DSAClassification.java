package ntut.IR.dsads;

import ntut.IR.AbstractClassification;
import ntut.IR.ClassifierFactory;
import ntut.IR.SupportClassifier;
import ntut.IR.exception.NotPreparedException;
import ntut.IR.exception.NotSupportedClassifierException;
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
    private final String RELATION_NAME = "Actions";
    private DSADataSetLoader loader = null;
    private List<String> classNames = null;
    private Map<String, Attribute> allAttrMap = new HashMap<>(COL_AMT +1);//Plus one class attribute
    private Instances isTrainSet = null;
    private Instances isTestSet = null;
    private Classifier classifierModel = null;

    private List<String> generateClassList(String formatPattern){
        List<String> returnClassNames = new ArrayList<>(ACTION_AMT);
        for(int actionClass = 1 ; actionClass <= ACTION_AMT ; actionClass++){
            String aClassName = String.format(formatPattern, actionClass);
            returnClassNames.add(aClassName);
        }
        return returnClassNames;
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

    private int trainInstanceCapacity(){
        return COL_AMT*LINE_AMT*SEGMENT_AMT*this.trainAmt;
    }

    private int testInstancesCapacity(){
        return COL_AMT*LINE_AMT*SEGMENT_AMT*this.testAmt;
    }

    private Classifier createClassifierByType(SupportClassifier classifierType, Object[] classifierArgs) throws NotSupportedClassifierException{
        Classifier classifier = null;
        switch (classifierType){
            case KNN:
                classifier = ClassifierFactory.createKNNClassifier((int)classifierArgs[0]);
        }
        return classifier;
    }

    private Instances generateTestSet(int testAmt) throws IOException, NotPreparedException{
        Instances retTestSet = this.createInstances(this.testInstancesCapacity());
        for(int testClass = 0 ; testClass < ACTION_AMT ; testClass++){
            for(int subject = TOTAL_SUBJECT_AMT - testAmt ; subject < TOTAL_SUBJECT_AMT; subject++){
                retTestSet.addAll(generateInstanceBySubject(testClass, subject));
            }
        }

        return retTestSet;
    }

    private List<Instance> generateInstanceBySubject(int testClassIndex, int subjectIndex) throws NotPreparedException, IOException {
        DSADataSetSubjectActionModeler aSubjectModeler = new DSADataSetSubjectActionModeler();
        List<File> segFileList = this.loader.getDataSetList().get(testClassIndex).get(subjectIndex);
        aSubjectModeler.runModelerByFileList(segFileList);
        //Create Instance
        List<Instance> subjectInstances = new ArrayList<>(LINE_AMT*SEGMENT_AMT);
        for(int index = 0 ; index < LINE_AMT*SEGMENT_AMT ; index++) {
            Instance anInstance = new DenseInstance(COL_AMT + 1); //Plus one class attr
            for (Units unit : UNITS_LIST) {
                for (Meters meter : METERS_LIST) {
                    Vector3<Float> aVector = aSubjectModeler.getSubjectData().get(meter).get(unit).get(index);
                    final String xAttrName = this.getAttributeName(unit, meter, "x");
                    final String yAttrName = this.getAttributeName(unit, meter, "y");
                    final String zAttrName = this.getAttributeName(unit, meter, "z");
                    anInstance.setValue(this.allAttrMap.get(xAttrName), aVector.x);
                    anInstance.setValue(this.allAttrMap.get(yAttrName), aVector.y);
                    anInstance.setValue(this.allAttrMap.get(zAttrName), aVector.z);
                    //log
                    System.out.println("Generating: (Class:" + testClassIndex + ", Subject:"+ subjectIndex+ ", Index:"+ index + ", Unit:" + unit.toString() + ", Meter:" + meter.toString() + ")");
                }
            }
            subjectInstances.add(anInstance);
            String curClassName = this.classNames.get(testClassIndex);
            anInstance.setValue(this.allAttrMap.get(CLASS_NAME), curClassName);
        }
        return subjectInstances;
    }

    private Instances generateTrainSet(int trainAmt) throws NotPreparedException, IOException {
        Instances retTrainSet = this.createInstances(this.trainInstanceCapacity());
        for(int trainingClass = 0 ; trainingClass < ACTION_AMT ; trainingClass++){
            for(int subject = 0 ; subject < trainAmt; subject++){
                retTrainSet.addAll(generateInstanceBySubject(trainingClass, subject));
            }
        }

        return retTrainSet;
    }

    private Instances createInstances(int capacity) {
        Instances ret = new Instances(RELATION_NAME, (ArrayList<Attribute>) this.getAttributeList(this.allAttrMap), capacity);
        ret.setClass(this.allAttrMap.get(CLASS_NAME));
        return ret;
    }

    private List<Attribute> getAttributeList(Map<String, Attribute> attributeMap){
        return new ArrayList<>(attributeMap.values());
    }

    public DSAClassification(int trainAmt,DSADataSetLoader loader) throws IllegalArgumentException{
        this(trainAmt, loader, "Action %02d");
    }

    public DSAClassification(int trainAmt, DSADataSetLoader loader, String classPattern){
        super(SUBJECT_AMT, trainAmt);
        if(SUBJECT_AMT <= trainAmt)
            throw new IllegalArgumentException("Amount of train set should not larger than amount of set");
        this.loader = loader;
        this.classNames = this.generateClassList(classPattern);

        //Sensor attributes
        List<Attribute> attributes = this.generateAttributes();
        //Class Attribute
        Attribute classAttr = new Attribute(CLASS_NAME, classNames);

        ArrayList<Attribute> allAttr = new ArrayList<>(COL_AMT + 1);//plus 1 class attr
        allAttr.addAll(attributes);
        allAttr.add(classAttr);
        this.allAttrMap = this.getDataAttributesMap(allAttr);
    }

    public void prepare() throws NotPreparedException, IOException{
        System.out.println("Generating Training Sets...");
        this.isTrainSet = generateTrainSet(this.trainAmt);
        System.out.println("Generating Test Sets...");
        this.isTestSet = generateTestSet(this.testAmt);
    }

    public void train(SupportClassifier classifierType, Object[] classifierArgs) throws Exception{
        this.classifierModel = this.createClassifierByType(classifierType, classifierArgs);
        System.out.println("Building Classifier");
        this.classifierModel.buildClassifier(this.isTrainSet);
    }


    public void test() throws Exception{
        Evaluation evaluation = new Evaluation(isTrainSet);
        System.out.println("Evaluating Classifier");

        evaluation.evaluateModel(classifierModel, this.isTestSet);
        //evaluation.evaluateModel(classifierModel, testInstances);
        System.out.println(evaluation.toSummaryString(true));
        System.out.println(evaluation.toMatrixString());
        System.out.println(evaluation.toClassDetailsString());
    }
}
