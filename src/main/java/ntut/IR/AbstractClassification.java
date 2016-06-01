package ntut.IR;

/**
 * Created by Vodalok on 2016/5/31.
 */
public abstract class AbstractClassification implements IClassification {
    protected int trainAmt;
    protected int testAmt;
    protected final int TOTAL_SUBJECT_AMT;

    public AbstractClassification(int totalAmt, int trainAmt){
        this.trainAmt = trainAmt;
        this.testAmt = totalAmt - trainAmt;
        this.TOTAL_SUBJECT_AMT = totalAmt;
    }
}
