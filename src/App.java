import SST.StringSearcherTable;
import SST.WordSearcher;

import java.io.File;

import static java.lang.Thread.sleep;

/**
 * Created by Gvozd on 03.02.2016.
 */
public class App {
    private static final File TARGETFILE = new File("data.txt");
    private static final File TARGETFILE2 = new File("data2.txt");
    private static final File TARGETFILE3 = new File("data3.txt");

    public static void main(String[] args) {
        WordSearcher ws = WordSearcher.getInstance();
        ws.createNewSearch(3);
        ws.setWordRepeatLimit(3);
        ws.setCaseInsensitiveMethod(true);
        ws.setIgnorePunctuationMarks(true);
        ws.setDoNotCountEmptyLinesAsWords(true);
        ws.setFile(TARGETFILE, 0);
        ws.setFile(TARGETFILE2, 1);
        ws.setFile(TARGETFILE3, 2);

        StringSearcherTable kapital = ws.getSearcher(1);
        kapital.setWordRepeatLimit(5);

        ws.startSearch();


        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ws.writeStringTable();
    }
}
