package ntut.IR.exception;

/**
 * Created by Vodalok on 2016/5/29.
 */
public class NoThisMethodException extends Exception {
    public NoThisMethodException(){
        super("The Method is not Supported.");
    }
}
