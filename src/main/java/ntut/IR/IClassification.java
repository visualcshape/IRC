package ntut.IR;

import ntut.IR.SupportClassifier;

/**
 * Created by vodalok on 2016/6/2.
 */
public interface IClassification {
    void train(SupportClassifier classifierType, Object[] classifierArgs) throws Exception;

    void test() throws Exception;

    void prepare() throws Exception;

    String getReportString();
}
