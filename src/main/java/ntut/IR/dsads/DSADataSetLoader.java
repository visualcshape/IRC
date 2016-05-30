package ntut.IR.dsads;

import ntut.IR.IDataSetLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetLoader implements IDataSetLoader{
    private File dataSetRoot;
    private int ACTION_AMT = 19;
    private int SUBJECT_AMT = 8;
    private int SEGMENT_AMT = 60;
    private int trainSetAmt;
    private int testSetAmt;
    private List<List<List<File>>> actionFileList = new ArrayList<>(ACTION_AMT); //a/p/s

    public void loadFileList(File root) throws FileNotFoundException{
        String TWO_DIGIT_FORMAT = "%02d";
        String ACTION_START_WORD = "a";
        String SUBJECT_START_WORD = "p";
        String SEGMENT_START_WORD = "s";
        String SEGMENT_EXTENSION = ".txt";
        for(int actionNumber = 1; actionNumber <= ACTION_AMT ; actionNumber++) {
            StringBuilder actionPartBuilder = new StringBuilder(root.getAbsolutePath());
            String actionFileDirNumberName = String.format(TWO_DIGIT_FORMAT, actionNumber);
            actionPartBuilder.append(File.separator);
            actionPartBuilder.append(ACTION_START_WORD);
            actionPartBuilder.append(actionFileDirNumberName);
            List<List<File>> subjectList = new ArrayList<>(SEGMENT_AMT);
            for (int subjectNumber = 1; subjectNumber <= SUBJECT_AMT; subjectNumber++) {
                StringBuilder subjectPartBuilder = new StringBuilder(actionPartBuilder);
                subjectPartBuilder.append(File.separator);
                subjectPartBuilder.append(SUBJECT_START_WORD);
                subjectPartBuilder.append(subjectNumber);
                List<File> segmentFileList = new ArrayList<>(SEGMENT_AMT);
                for (int segmentNumber = 1; segmentNumber <= SEGMENT_AMT; segmentNumber++) {
                    String segmentFileNumberName = String.format(TWO_DIGIT_FORMAT, segmentNumber);
                    StringBuilder segmentPartBuilder = new StringBuilder(subjectPartBuilder);
                    segmentPartBuilder.append(File.separator);
                    segmentPartBuilder.append(SEGMENT_START_WORD);
                    segmentPartBuilder.append(segmentFileNumberName);
                    segmentPartBuilder.append(SEGMENT_EXTENSION);
                    String completeFilePath = new String(segmentPartBuilder);
                    File segmentFile = new File(completeFilePath);
                    if(!this.validateFile(segmentFile)){
                        throw new java.io.FileNotFoundException(segmentFile.getAbsolutePath());
                    }
                    segmentFileList.add(segmentFile);
                }
                subjectList.add(segmentFileList);
            }
            actionFileList.add(subjectList);
        }
    }

    private boolean validateFile(File fileToValidate){
        return fileToValidate.canRead();
    }

    public DSADataSetLoader(File dataSetRoot, int trainSetAmt){
        this.dataSetRoot = dataSetRoot;
        this.trainSetAmt = trainSetAmt;
        this.testSetAmt = SUBJECT_AMT - trainSetAmt;
    }

    public void load() throws FileNotFoundException{
        this.loadFileList(this.dataSetRoot);
    }
}
