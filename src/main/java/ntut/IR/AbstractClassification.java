package ntut.IR;

/**
 * Created by Vodalok on 2016/5/31.
 */
public abstract class AbstractClassification {
    protected int trainAmt;
    protected int testAmt;
    protected final int TOTAL_SUBJECT_AMT;

    public AbstractClassification(int totalAmt, int trainAmt){
        this.trainAmt = trainAmt;
        this.testAmt = totalAmt - trainAmt;
        this.TOTAL_SUBJECT_AMT = totalAmt;
    }

    abstract public void train(SupportClassifier classifierType, Object[] classifierArgs) throws Exception;

    abstract public void test() throws Exception;

    abstract public void prepare() throws Exception;
}
