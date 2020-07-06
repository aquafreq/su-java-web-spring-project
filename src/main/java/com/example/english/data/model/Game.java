package com.example.english.data.model;

import com.example.english.data.entity.Word;
import com.example.english.data.entity.CategoryWithWords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Game {
    private static final String WORD_GAME = "word";
    private static final String DEFINITION_GAME = "definition";
    private static final String SKIP = "skip";
    private static final String HELP = "help";

    private final Scanner scanner;
    private final ModuleManager moduleManager;

    public Game(ModuleManager moduleManager, Scanner scanner) {
        this.moduleManager = moduleManager;
        this.scanner = scanner;
    }

    private void endGame(String input) {
        if ("end".equals(input)) {
            System.exit(0);
        }
    }

    private void wordGame(List<Word> allWords, String gameType) {
        guessWords(allWords, gameType, allWords.size());
    }

    private void guessWords(List<Word> allWords, String gameType, long count) {
        Word word = null;
        int letters = 0;
        Collections.shuffle(allWords);
        System.out.println();
        while (count != 0) {

            if (word == null || word.isGuessed()) {
                word = generateDefinition(allWords, count);
                askWord(getValue(gameType, word), gameType.equals(DEFINITION_GAME) ? WORD_GAME : DEFINITION_GAME);
            }

            String wordName = scanner.nextLine();

            endGame(wordName);
            if (word.testAnswer(wordName, gameType)) {
                System.out.println("Correct word gj");
                word.setGuessed(true);
                count--;
                letters = 0;
            } else if (SKIP.equals(wordName)) {
                word = generateDefinition(allWords, count);
                askWord(getValue(gameType, word), gameType);
                letters = 0;
            } else if (HELP.equals(wordName)) {
                System.out.println(helpWithAWord(
                        gameType.equals(WORD_GAME) ?
                                word.getName() : word.getDefinition(),
                        ++letters,
                        gameType));
            } else {
                System.out.println("Suck word no gj, try again");
            }
        }
    }


    private String helpWithAWord(String value, int letters, String type) {
        return
                value.length() <= letters ?
                        "Your " + type + " is " + value :
                        "Your " + type + " starts with " + value.substring(0, letters) +
                                value.substring(letters).replaceAll(".", ".");
    }

    private String getValue(String gameType, Word word) {
        return gameType.equals(WORD_GAME) ?
                word.getDefinition()
                : word.getName();
    }

    private Predicate<Word> getWordPredicate() {
        return q -> !q.isGuessed();
    }

    private void openAllWords(List<Word> allWords) {
        allWords.forEach(q -> q.setGuessed(false));
    }

    private Word generateDefinition(List<Word> allWords, long count) {
        return allWords
                .stream()
                .filter(getWordPredicate())
                .skip((long) ((count - 1) * Math.random())
                ).findFirst()
                .orElse(null);
    }

    private void checkIfTheresWords(List<Word> allWords) {
        if (allWords.isEmpty()) {
            System.out.println("There are no words at all.");
            endGame("end");
        }
    }

    public void startGameWithAllModules(String typeGame) {
        List<Word> allWords = moduleManager.getAllWordsFromModules();

        checkIfTheresWords(allWords);

        System.out.println("Your english_project.game starts with the following words:");
        allWords.forEach(System.out::println);
        System.out.println("You can skip your word or get help with it if you enter skip or help.");

        if (WORD_GAME.equals(typeGame)) {
            wordGame(allWords, WORD_GAME);
        } else {
            wordGame(allWords, DEFINITION_GAME);
        }
        openAllWords(allWords);
        afterGamePrint();
        play();
    }

    public void startAGameWithASingleModule(CategoryWithWords module, String gameType) {
        if (module == null || module.getWordsCount()== 0) {
            System.out.println("Select another module, this one has no words." + System.lineSeparator() +
                    "Choose all or a module number:");
        } else {
            System.out.println("Your single module english_project.game has started, the words are:"
                    + System.lineSeparator() + module.getWords() +
                    "You can skip or get help with your word by entering skip or help.");

            List<Word> wordList = new ArrayList<>(module.getWords());

            guessWords(wordList, gameType, wordList.size());
            openAllWords(wordList);
        }
        afterGamePrint();
        play();
    }

    private void afterGamePrint() {
        System.out.println("GJ you completed this exercise." + System.lineSeparator() +
                "Choose what to do next." + System.lineSeparator() +
                "Your options for games are: guess word or definition " + System.lineSeparator() +
                "and for modules are all or specific module by number: "
        );
    }

    private void askWord(String value, String type) {
        if (value != null) {
            System.out.println("Your " + type + " is: " + value);
            System.out.println("Type your answer:");
        }
    }

    private void selectModuleWithGameType(String moduleType, String gameType) {
        if ("all".equals(moduleType)) {
            this.startGameWithAllModules(gameType);
        } else {
            System.out.println("Module " + moduleType + " is chosen");
            CategoryWithWords module = moduleManager.chooseModule(Integer.parseInt(moduleType) - 1);
            this.startAGameWithASingleModule(module, gameType);
        }
    }

    public void play() {
        System.out.println("Enter what you want to guess a word or definition.");
        String gameType = scanner.nextLine();

        System.out.println("Enter module to practice on.");
        System.out.println("Your choices are all modules or one of " + System.lineSeparator() + moduleManager.getAvailableModules());
        String moduleType = scanner.nextLine();
        selectModuleWithGameType(moduleType, gameType);
    }
}