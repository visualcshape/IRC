package ntut.IR;

import ntut.IR.dsads.DSAClassification;
import ntut.IR.dsads.DSADataSetLoader;
import ntut.IR.exception.NoThisDataSetNameException;
import ntut.IR.exception.NoThisMethodException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by vodalok on 2016/6/2.
 */
public class ClassifierRunner extends Thread {
    private ClassifierSettings settings = null;
    private boolean hasException = false;
    private Exception exception = null;

    public ClassifierRunner(ClassifierSettings settings){
        this.settings = settings;
    }

    public boolean isHasException() {
        return hasException;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public void run() {
        try {
            switch (this.settings.selectDS) {
                case DSADS:
                    final String dsClassFileName = "DSADSAction_list";
                    DSADataSetLoader loader = new DSADataSetLoader(new File(this.settings.dsLocation));
                    loader.load();
                    this.settings.classification = new DSAClassification(this.settings.dsSetting, loader, dsClassFileName);
                    break;
                default:
                    throw new NoThisDataSetNameException();
            }
            //Method
            this.settings.classification.prepare();
            //Train
            System.out.printf("Classification Method: %s\n", this.settings.selectClassifier);
            this.settings.classification.train(this.settings.selectClassifier, this.settings.methodSetting);
            //Test
            this.settings.classification.test();
            //Store report
            String reportFileName = "report.txt";
            this.storeReportToFile(this.settings.classification, this.settings.reportStoreLocation, reportFileName);
        }catch (Exception exception){
            this.hasException = true;
            this.exception = exception;
        }
    }

    private void storeReportToFile(IClassification classification, String reportPath, String reportFileName) throws IOException {
        String reportString = classification.getReportString();
        File reportLocation = new File(reportPath + File.separator + reportFileName);
        if(reportLocation.getParentFile().mkdir()){
            System.out.println("Dir already exist");
        }
        if(reportLocation.createNewFile()){
            System.out.println("File already exist, overwritten..");
        }
        FileWriter writer = new FileWriter(reportLocation, false);
        writer.write(reportString);
        writer.flush();
        writer.close();
        System.out.printf("Output Report to : %s\n", reportLocation);
    }
}
