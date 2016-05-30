package ntut.IR.dsads;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by vodalok on 2016/5/30.
 */
public class DSADataSetLoaderTest {
    @Test
    public void loadFileList() {
        DSADataSetLoader loader = new DSADataSetLoader(new File("/Users/vodalok/Documents/NTUT/IR/data"), 8);
        try {
            loader.loadFileList(new File("/Users/vodalok/Documents/NTUT/IR/data"));
        }catch (IOException exception){
            exception.printStackTrace();
            fail();
        }
        return;
    }

    @Test
    public void load() {

    }

}