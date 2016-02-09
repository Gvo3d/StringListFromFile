package SST;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Gvozd on 10.02.2016.
 */
public class WordSearcher {
    private ArrayList<StringSearcherTable> arrayOfThreads = new ArrayList<>();
    private boolean[] searchEnd = new boolean[0];
    private static WordSearcher instance;
    private int wordRepeatLimit = 2;
    ConcurrentHashMap<String, Integer> dataset = new ConcurrentHashMap<>();

    private WordSearcher() {
    }

    public static WordSearcher getInstance() {
        if (instance == null) {
            instance = new WordSearcher();
        }
        return instance;
    }

    public void createNewSearch() {
        arrayOfThreads.add(new StringSearcherTable(this, arrayOfThreads.size()));
        searchEnd = new boolean[arrayOfThreads.size()];
    }

    public void createNewSearch(int numberOfSearches) {
        for (int i = 0; i < numberOfSearches; i++) {
            createNewSearch();
        }
    }

    public void setFile(File filename, int searcherId) {
        StringSearcherTable temp = arrayOfThreads.get(searcherId);
        temp.getRepeatableStringTable(filename);
    }

    public void setWordRepeatLimit(int limit) {
        wordRepeatLimit = limit;
        for (StringSearcherTable temp : arrayOfThreads) {
            temp.setWordRepeatLimit(limit);
        }
    }

    public void setCaseInsensitiveMethod(boolean setMethod) {
        for (StringSearcherTable temp : arrayOfThreads) {
            temp.setUseCaseInsensitiveMethod(setMethod);
        }
    }

    public void setIgnorePunctuationMarks(boolean setIgnore) {
        for (StringSearcherTable temp : arrayOfThreads) {
            temp.setIgnorePunctuationMarks(setIgnore);
        }
    }

    public void setDoNotCountEmptyLinesAsWords(boolean setEmpty) {
        for (StringSearcherTable temp : arrayOfThreads) {
            temp.setDoNotCountEmptyLinesAsWords(setEmpty);
        }
    }

    private void startSearch(int searcherId) {
        StringSearcherTable temp = arrayOfThreads.get(searcherId);
        Thread searchThread = new Thread(temp);
        searchThread.start();
    }

    public void startSearch() {
        for (int i = 0; i < arrayOfThreads.size(); i++) {
            startSearch(i);
        }
    }

    public void writeStringTable() {
        boolean notEnded = false;
        for (int i = 0; i < arrayOfThreads.size() - 1; i++) {
            if (searchEnd[i] == false) {
                System.out.println("Search is not ended");
                notEnded = true;
            }
        }
        if (!notEnded) {
            for (HashMap.Entry<String, Integer> entry : dataset.entrySet()) {
                if (entry.getValue() >= wordRepeatLimit) {
                    System.out.println("Слово \"" + entry.getKey() + "\" повторяется " + entry.getValue() + " раз.");
                }
            }
        }
    }

    void searchEnded(int searcherId) {
        searchEnd[searcherId] = true;
    }

    public StringSearcherTable getSearcher(int number) {
        return arrayOfThreads.get(number);
    }
}
