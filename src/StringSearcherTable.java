import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Gvozd on 03.02.2016.
 */
public class StringSearcherTable {
    private static StringSearcherTable instance;
    private HashMap<String, Integer> dataset;
    private int wordRepeatLimit = 2;
    private boolean ignorePunctuationMarks = false;
    private boolean useCaseInsensitiveMethod = false;
    private boolean doNotCountEmptyLinesAsWords = false;
    private static final char[] PUNCTUATIONDELIMETERS = {';', ',', '.', '!', '?', '—', ':', '-'};
    private long lineCount;
    private long wordCount;
    private String textName;

    public void setUseCaseInsensitiveMethod(boolean useCaseInsensitiveMethod) {
        this.useCaseInsensitiveMethod = useCaseInsensitiveMethod;
    }

    public void setDoNotCountEmptyLinesAsWords(boolean countEmptyLines) {
        this.doNotCountEmptyLinesAsWords = countEmptyLines;
    }

    public void setIgnorePunctuationMarks(boolean ignorePunctuationMarks) {
        this.ignorePunctuationMarks = ignorePunctuationMarks;
    }

    public void setWordRepeatLimit(int wordRepeatLimit) {
        this.wordRepeatLimit = wordRepeatLimit;
    }

    private StringSearcherTable() {
    }

    public static StringSearcherTable getInstance() {
        if (instance == null) {
            instance = new StringSearcherTable();
        }
        return instance;
    }

    public void getRepeatableStringTable(File targetFile) {
        try {
            if (targetFile.isDirectory()) throw new NotAFileException();

            ArrayList<String> stringData = new ArrayList<>();
            Scanner scanner = new Scanner(new BufferedReader((new FileReader(targetFile))));
            while (scanner.hasNext()) {
                stringData.add(scanner.nextLine());
            }
            getRepeatableStringTable(stringData);
        } catch (NotAFileException e) {
            e.printStackTrace();
        } catch (NotAStringObjectException e2) {
            e2.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void getRepeatableStringTable(List stringArray) throws NotAStringObjectException {
        String tempLine;
        lineCount = 0;
        wordCount = 0;
        textName = null;
        dataset = new HashMap<>();
        try {
            for (Object stringObject : stringArray) {
                tempLine = (String) stringObject;
                if (textName == null) textName = tempLine;
                lineCount++;
                for (String wordString : tempLine.split(" ")) {
                    if (ignorePunctuationMarks) {
                        wordString = deletingPunctuationMarks(wordString);
                    }
                    if (useCaseInsensitiveMethod) {
                        wordString = wordString.toLowerCase();
                    }
                    if (wordString == "" && doNotCountEmptyLinesAsWords) {
                    } else {
                        wordCount++;
                        addWordIntoSet(wordString);
                    }
                }
            }
        } catch (ClassCastException e) {
            throw new NotAStringObjectException();
        }
        writeStringTable(dataset);
    }

    private void addWordIntoSet(String wordForAdding) {
        if (dataset.containsKey(wordForAdding)) {
            updateWordCounterInASet(wordForAdding);
        } else {
            dataset.put(wordForAdding, 1);
        }
    }

    private String deletingPunctuationMarks(String forPunctuationDeleting) {
        String result = "";
        char[] stringToCharForDelimetation = forPunctuationDeleting.toCharArray();
        for (char chr : stringToCharForDelimetation) {
            boolean doNotAdd = false;
            for (char delimeter : PUNCTUATIONDELIMETERS) {
                if (chr == delimeter) {
                    doNotAdd = true;
                }
            }
            if (!doNotAdd) result = result + chr;
        }
        return result;
    }

    private void updateWordCounterInASet(String insertableWord) {
        dataset.replace(insertableWord, dataset.get(insertableWord), (dataset.get(insertableWord) + 1));
    }

    private void writeStringTable(HashMap<String, Integer> dataset) {
        System.out.println("Текст \"" + textName + "\" содержит " + lineCount + " строк и " + wordCount + " слов");
        System.out.println("Нижний порог количества повторений: " + wordRepeatLimit);
        for (HashMap.Entry<String, Integer> entry : dataset.entrySet()) {
            if (entry.getValue() >= wordRepeatLimit) {
                System.out.println("Слово \"" + entry.getKey() + "\" повторяется " + entry.getValue() + " раз.");
            }
        }
    }
}
