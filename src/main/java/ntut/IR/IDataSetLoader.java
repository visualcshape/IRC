package ntut.IR;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by vodalok on 2016/5/30.
 */
public interface IDataSetLoader {
    void load() throws FileNotFoundException;
    List<?> getDataSetList() throws Exception;
}
