package ntut.IR;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by vodalok on 2016/5/30.
 */
public interface IDataSetLoader {
    void loadFileList(File root) throws FileNotFoundException;
    void load() throws FileNotFoundException;
}
