package ntut.IR.dsads;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ntut.IR.dsads.DSADSConstants.SEGMENT_AMT;
import static org.junit.Assert.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetSubjectActionModelerTest {
    @Test
    public void runModelerByFileList() {
        String fileNameTemplate = "D:\\My Documents\\NTUT_Graduate\\IR\\DataSets\\Daily and Sports Activities Data Set\\a01\\p1\\s%02d.txt";
        List<File> testFiles = new ArrayList<>();
        for(int seg = 1 ; seg <= SEGMENT_AMT ; seg++){
            File segmentFile = new File(String.format(fileNameTemplate, seg));
            testFiles.add(segmentFile);
        }

        DSADataSetSubjectActionModeler modeler = new DSADataSetSubjectActionModeler();

        try {
            modeler.runModelerByFileList(testFiles);
        }catch (IOException exception){
            exception.printStackTrace();
            fail();
        }
        return;
    }

}