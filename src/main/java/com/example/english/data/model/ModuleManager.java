package com.example.english.data.model;

import com.example.english.data.entity.Word;
import com.example.english.data.entity.WordCategory;

import java.util.*;

public class ModuleManager {
    private Map<Integer, WordCategory> modules;

    public ModuleManager(List<WordCategory> modules) {
        this.modules = new HashMap<>();
        setModules(modules);
    }

    public ModuleManager() {
    }

    public Map<Integer, WordCategory> getModules() {
        return modules;
    }

    public void setModules(Map<Integer, WordCategory> modules) {
        this.modules = modules;
    }

    public void setModules(List<WordCategory> modules) {
        int[] count = {0};
        modules.forEach(m -> this.modules.putIfAbsent(count[0]++, m));
    }

    public WordCategory chooseModule(int moduleNumber) {
        return this.modules.get(moduleNumber);
    }

    public Collection<WordCategory> selectAllModules() {
        return this.modules.values();
    }

    private List<Word> combineAllModules() {
        List<Word> allWords = new ArrayList<>();
        this.modules.forEach((key, value) -> allWords.addAll(value.getWords()));
        return allWords;
    }


    public List<Word> getAllWordsFromModules() {
        return this.combineAllModules();
    }
    public String getAvailableModules() {
        StringBuilder bd = new StringBuilder();
        this.modules.forEach((key, value) -> bd
                .append(key + 1)
                .append(" : ")
                .append(value.getName())
                .append(System.lineSeparator()));

        return bd.toString();
    }
}
