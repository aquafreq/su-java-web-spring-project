package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.Word;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.impl.WordServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordServiceTests extends BaseTest {

    @Autowired
    WordRepository repository;

    Word build;
    WordService wordService;

    ModelMapper modelMapper;
    private final String NAME = "zxc";
    private final String DEFINITION = "top duma";

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        wordService = new WordServiceImpl(modelMapper, repository);
        build = Word.builder().name(NAME).definition(DEFINITION).build();
    }

    @Test
    public void serviceShouldAddWordCorrectly() {
        WordServiceModel map = modelMapper.map(build, WordServiceModel.class);

        WordServiceModel word = wordService.createWord(map);

        assertAll(() -> {
            assertEquals(NAME, word.getName());
            assertEquals(DEFINITION, word.getDefinition());
            assertEquals(1, repository.count());
        });
    }

    @Test
    public void deleteWordById_whenValid_shouldReturnDeleted() {
        WordServiceModel map = modelMapper.map(build, WordServiceModel.class);

        WordServiceModel word = wordService.createWord(map);

        WordServiceModel wordServiceModel = wordService.deleteWordById(word.getId());

        assertEquals(0, repository.count());
        assertEquals(map.getName(), wordServiceModel.getName());
        assertEquals(map.getDefinition(), wordServiceModel.getDefinition());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void deleteWordById_whenInvalid_shouldThrown() {
        exceptionRule.expect(EntityNotFoundException.class);
        exceptionRule.expectMessage("No such word!");
        WordServiceModel wordServiceModel = wordService.deleteWordById("1");
    }

    @Test
    public void addWords_shouldAddWordsCorrectly() {
        WordServiceModel build = new WordServiceModel("name", "def");
        WordServiceModel build1 = new WordServiceModel("name1", "def1");
        WordServiceModel build2 = new WordServiceModel("name2", "def2");
        WordServiceModel build3 = new WordServiceModel("name3", "def3");

        List<WordServiceModel> before = Arrays.asList(build, build1, build2, build3);
        List<WordServiceModel> after = wordService.addWords(before);

        assertEquals(4, repository.count());

        for (int i = 0; i < before.size(); i++) {
            WordServiceModel b = before.get(i);
            WordServiceModel f = after.get(i);

            assertEquals(b.getName(), f.getName());
            assertEquals(b.getDefinition(), f.getDefinition());
        }
    }

    @Test
    public void getWordByNameAndDefinition_shouldReturnIt() {
        wordService.createWord(modelMapper.map(build, WordServiceModel.class));

        WordServiceModel wordByNameAndDefinition = wordService
                .getWordByNameAndDefinition(NAME, DEFINITION);

        assertEquals(NAME, wordByNameAndDefinition.getName());
        assertEquals(DEFINITION, wordByNameAndDefinition.getDefinition());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getWordByNameAndDefinition_withIncorrectName_ShouldThrow() {
        WordServiceModel wordByNameAndDefinition = wordService.getWordByNameAndDefinition(NAME, DEFINITION);

        assertEquals(NAME, "wordByNameAndDefinition.getName()");
        assertEquals(DEFINITION, wordByNameAndDefinition.getDefinition());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getWordByNameAndDefinition_withIncorrectDefinition_ShouldThrow() {
        WordServiceModel wordByNameAndDefinition = wordService.getWordByNameAndDefinition(NAME, DEFINITION);

        assertEquals(NAME, wordByNameAndDefinition.getName());
        assertEquals(DEFINITION, "wordByNameAndDefinition.getDefinition()");
    }


    @Test
    public void getWordByIdOrName_withValidName_shouldReturnWord() {
        WordServiceModel word = wordService.createWord(modelMapper.map(build, WordServiceModel.class));

        WordServiceModel wordByIdOrName = wordService.getWordByIdOrName(word.getId());

        assertEquals(word.getId(), wordByIdOrName.getId());
        assertEquals(word.getName(), wordByIdOrName.getName());
        assertEquals(word.getDefinition(), wordByIdOrName.getDefinition());
    }

    @Test
    public void getWordByIdOrName_withValidId_shouldReturnWord() {
        WordServiceModel word = wordService.createWord(modelMapper.map(build, WordServiceModel.class));

        WordServiceModel wordByIdOrName = wordService.getWordByIdOrName(word.getName());

        assertEquals(word.getId(), wordByIdOrName.getId());
        assertEquals(word.getName(), wordByIdOrName.getName());
        assertEquals(word.getDefinition(), wordByIdOrName.getDefinition());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getWordByIdOrName_withValidInput_shouldThrow() {
        WordServiceModel word = wordService.createWord(modelMapper.map(build, WordServiceModel.class));
        wordService.getWordByIdOrName("word.getId()");
    }

    @Test
    public void getWordsById_shouldReturnWords() {
        WordServiceModel build = new WordServiceModel("name", "def");
        WordServiceModel build1 = new WordServiceModel("name1", "def1");
        WordServiceModel build2 = new WordServiceModel("name2", "def2");
        WordServiceModel build3 = new WordServiceModel("name3", "def3");

        List<WordServiceModel> words = wordService.addWords(Arrays.asList(build, build1, build2, build3));
        List<WordServiceModel> gottenWords = new ArrayList<>(wordService.getWordsById(
                Arrays.asList(
                        words.get(0).getId(),
                        words.get(1).getId(),
                        words.get(2).getId(),
                        words.get(3).getId())
        ));


//        for (int i = 0; i < words.size(); i++) {
//            WordServiceModel word = words.get(i);
//            WordServiceModel gottenWord = gottenWords.get(i);
//
//            assertEquals(word.getId(), gottenWord.getId());
//            assertEquals(word.getName(), gottenWord.getName());
//            assertEquals(word.getDefinition(), gottenWord.getDefinition());
//        }

        assertEquals(words, gottenWords);
    }
}
