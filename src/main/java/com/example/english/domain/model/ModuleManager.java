package com.example.english.domain.model;

import com.example.english.domain.entity.Word;
import com.example.english.domain.entity.CategoryWithWords;

import java.util.*;

public class ModuleManager {
    private Map<Integer, CategoryWithWords> modules;

    public ModuleManager(List<CategoryWithWords> modules) {
        this.modules = new HashMap<>();
        setModules(modules);
    }

    public ModuleManager() {
    }

    public Map<Integer, CategoryWithWords> getModules() {
        return modules;
    }

    public void setModules(Map<Integer, CategoryWithWords> modules) {
        this.modules = modules;
    }

    public void setModules(List<CategoryWithWords> modules) {
        int[] count = {0};
        modules.forEach(m -> this.modules.putIfAbsent(count[0]++, m));
    }

    public CategoryWithWords chooseModule(int moduleNumber) {
        return this.modules.get(moduleNumber);
    }

    public Collection<CategoryWithWords> selectAllModules() {
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
