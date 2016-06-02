package ntut.IR.dsads;

import ntut.IR.AbstractClassification;
import ntut.IR.ClassifierFactory;
import ntut.IR.SupportClassifier;
import ntut.IR.exception.NotPreparedException;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
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
    private String reportString = null;

    private List<String> generateClassListFromPattern(String formatPattern){
        List<String> returnClassNames = new ArrayList<>(ACTION_AMT);
        for(int actionClass = 1 ; actionClass <= ACTION_AMT ; actionClass++){
            String aClassName = String.format(formatPattern, actionClass);
            returnClassNames.add(aClassName);
        }
        return returnClassNames;
    }

    private List<String> generateClassListFromFile(String classListFileName) throws NullPointerException, IOException{
        List<String> returnClassNames = new ArrayList<>(ACTION_AMT);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(classListFileName)));
        }catch(IOException exception){
            //try load from resource
            reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(classListFileName)));
        }
        for(int actionClass = 1 ; actionClass <= ACTION_AMT ; actionClass++){
            String aClassName = reader.readLine();
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
        return this.trainAmt*ACTION_AMT;
    }

    private int testInstancesCapacity(){
        return this.testAmt*ACTION_AMT;
    }

    private Classifier createClassifierByType(SupportClassifier classifierType, Object[] classifierArgs) throws Exception{
        Classifier classifier = null;
        switch (classifierType){
            case KNN:
                classifier = ClassifierFactory.createKNNClassifier((int)classifierArgs[0]);
                break;
            case NaiveBayes:
                classifier = ClassifierFactory.createNaiveBayes();
                break;
            case J48:
                classifier = ClassifierFactory.createJ48();
                break;
            case LWL:
                classifier = ClassifierFactory.createLWL();
        }
        return classifier;
    }

    private Instances generateTestSet(int testAmt) throws IOException, NotPreparedException{
        Instances retTestSet = this.createInstances(this.testInstancesCapacity());
        for(int testClass = 0 ; testClass < ACTION_AMT ; testClass++){
            for(int subject = TOTAL_SUBJECT_AMT - testAmt ; subject < TOTAL_SUBJECT_AMT; subject++){
                retTestSet.add(generateAnInstanceBySubject(testClass, subject));
            }
        }

        return retTestSet;
    }

    private Instance generateAnInstanceBySubject(int testClassIndex, int subjectIndex) throws NotPreparedException, IOException {
        DSADataSetSubjectActionModeler aSubjectModeler = new DSADataSetSubjectActionModeler();
        List<File> segFileList = this.loader.getDataSetList().get(testClassIndex).get(subjectIndex);
        aSubjectModeler.runModelerByFileList(segFileList);
        //Create Instance
        Instance anInstance = new DenseInstance(COL_AMT + 1); //Plus one class attr
        for (Units unit : UNITS_LIST) {
            for (Meters meter : METERS_LIST) {
                List<Vector3<Float>> vectorList = aSubjectModeler.getSubjectData().get(meter).get(unit);
                int listSize = vectorList.size();
                Float xSum = 0.f;
                Float ySum = 0.f;
                Float zSum = 0.f;
                System.out.printf("Generating Instance : Class: %d, Subject:%s Unit : %s, Meter: %s\n",testClassIndex, subjectIndex, unit.toString(), meter.toString());
                for(Vector3<Float> aVector:vectorList) {
                    //Calculate Average
                    xSum += aVector.x;
                    ySum += aVector.y;
                    zSum += aVector.z;
                }
                final String xAttrName = this.getAttributeName(unit, meter, "x");
                final String yAttrName = this.getAttributeName(unit, meter, "y");
                final String zAttrName = this.getAttributeName(unit, meter, "z");
                anInstance.setValue(this.allAttrMap.get(xAttrName), xSum/listSize);
                anInstance.setValue(this.allAttrMap.get(yAttrName), ySum/listSize);
                anInstance.setValue(this.allAttrMap.get(zAttrName), zSum/listSize);
            }
        }
        String curClassName = this.classNames.get(testClassIndex);
        anInstance.setValue(this.allAttrMap.get(CLASS_NAME), curClassName);
        return anInstance;
    }

    private Instances generateTrainSet(int trainAmt) throws NotPreparedException, IOException {
        Instances retTrainSet = this.createInstances(this.trainInstanceCapacity());
        for(int trainingClass = 0 ; trainingClass < ACTION_AMT ; trainingClass++){
            for(int subject = 0 ; subject < trainAmt; subject++){
                retTrainSet.add(generateAnInstanceBySubject(trainingClass, subject));
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

    @Deprecated
    private DSAClassification(Object[] dsSetting,DSADataSetLoader loader) throws IllegalArgumentException, IOException{
        this(dsSetting, loader, "");
    }

    public DSAClassification(Object[] dsSetting, DSADataSetLoader loader, String fileName) throws IOException, NullPointerException{
        super(SUBJECT_AMT, (int)dsSetting[0]);
        if(SUBJECT_AMT <= this.trainAmt)
            throw new IllegalArgumentException("Amount of train set should not larger than amount of set");
        this.loader = loader;
        this.classNames = this.generateClassListFromFile(fileName);

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

    //21小時
    public void test() throws Exception{
        Evaluation evaluation = new Evaluation(isTrainSet);
        System.out.println("Evaluating Classifier");

        evaluation.evaluateModel(classifierModel, this.isTestSet);
        //store to report string
        StringBuilder reportStringBuilder = new StringBuilder();
        reportStringBuilder.append(evaluation.toSummaryString());
        reportStringBuilder.append("\n");
        reportStringBuilder.append(evaluation.toMatrixString());
        reportStringBuilder.append("\n");
        reportStringBuilder.append(evaluation.toClassDetailsString());
        reportStringBuilder.append("\n");
        this.reportString = new String(reportStringBuilder);
    }

    @Override
    public String getReportString() {
        return this.reportString;
    }
}
