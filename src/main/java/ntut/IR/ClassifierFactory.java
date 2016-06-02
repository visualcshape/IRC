package ntut.IR;

import ntut.IR.exception.NotSupportedClassifierException;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;

/**
 * Created by vodalok on 2016/5/31.
 */
public class ClassifierFactory {
    public static Classifier createKNNClassifier(int k) throws NotSupportedClassifierException{

        return new IBk(k);
    }

    public static Classifier createNaiveBayes(){
        return new NaiveBayes();
    }

    public static Classifier createJ48() throws Exception{
        J48 ret = new J48();
        String[] options = new String[]{"-U"};
        ret.setOptions(options);
        return new J48();
    }

    public static Classifier createLWL(){
        return new weka.classifiers.lazy.LWL();
    }
}
