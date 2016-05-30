package ntut.IR.dsads;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetActionModelerTest {
    @Test
    public void runModelerByFileList() {
        String fileNameTemplate = "/Users/vodalok/Documents/NTUT/IR/data/a01/p1/s%02d.txt";
        List<File> testFiles = new ArrayList<>();
        for(int seg = 1 ; seg <= 60 ; seg++){
            File segmentFile = new File(String.format(fileNameTemplate, seg));
            testFiles.add(segmentFile);
        }

        DSADataSetActionModeler modeler = new DSADataSetActionModeler();

        try {
            modeler.runModelerByFileList(testFiles);
        }catch (IOException exception){
            exception.printStackTrace();
            fail();
        }
        return;
    }

}