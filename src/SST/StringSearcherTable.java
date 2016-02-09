package SST;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Gvozd on 03.02.2016.
 */
public class StringSearcherTable implements Runnable {
    private int thisId;
    private ArrayList<String> textdata;
    private WordSearcher parent;
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

    public StringSearcherTable(WordSearcher parent, int thisId) {
        this.parent = parent;
        this.thisId = thisId;
    }

    public void getRepeatableStringTable(File targetFile) {
        try {
            if (targetFile.isDirectory()) throw new NotAFileException();

            ArrayList<String> stringData = new ArrayList<>();
            Scanner scanner = new Scanner(new BufferedReader((new FileReader(targetFile))));
            while (scanner.hasNext()) {
                stringData.add(scanner.nextLine());
            }
            textdata = stringData;
        } catch (NotAFileException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void getRepeatableStringTable(List stringArray) throws NotAStringObjectException {
        String tempLine;
        lineCount = 0;
        wordCount = 0;
        textName = null;
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
        System.out.println("Текст \"" + textName + "\" содержит " + lineCount + " строк и " + wordCount + " слов");
        System.out.println("Нижний порог количества повторений: " + wordRepeatLimit);
        System.out.println();
        parent.searchEnded(thisId);
    }

    private void addWordIntoSet(String wordForAdding) {
        if (parent.dataset.containsKey(wordForAdding)) {
            updateWordCounterInASet(wordForAdding);
        } else {
            parent.dataset.put(wordForAdding, 1);
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
        parent.dataset.replace(insertableWord, parent.dataset.get(insertableWord), (parent.dataset.get(insertableWord) + 1));
    }

    @Override
    public void run() {
        if (textdata != null) try {
            getRepeatableStringTable(textdata);
        } catch (NotAStringObjectException e) {
            e.printStackTrace();
        }
    }
}
