package ntut.IR.exception;

/**
 * Created by vodalok on 2016/5/31.
 */
public class NotSupportedClassifierException extends Exception {
    public NotSupportedClassifierException(){
        super("Does not support this classifier");
    }
}
