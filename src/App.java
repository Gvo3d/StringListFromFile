import java.io.File;

/**
 * Created by Gvozd on 03.02.2016.
 */
public class App {
    private static final File TARGETFILE = new File("data.txt");

    public static void main(String[] args) {
        StringSearcherTable sst = StringSearcherTable.getInstance();
        sst.setWordRepeatLimit(3);
        sst.setUseCaseInsensitiveMethod(true);
        sst.setIgnorePunctuationMarks(true);
        sst.setDoNotCountEmptyLinesAsWords(true);
        sst.getRepeatableStringTable(TARGETFILE);
    }
}
