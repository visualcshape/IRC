package ntut.IR;

import ntut.IR.exception.NotSupportedClassifierException;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

/**
 * Created by vodalok on 2016/5/31.
 */
public class ClassifierFactory {
    public static Classifier createKNNClassifier(int k) throws NotSupportedClassifierException{
        return new IBk(k);
    }
}
