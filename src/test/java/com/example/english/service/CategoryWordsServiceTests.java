package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.CategoryWords;
import com.example.english.data.entity.Word;
import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.CategoryWordsRepository;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.impl.CategoryWordsServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CategoryWordsServiceTests extends BaseTest {

    ModelMapper modelMapper;
    @Autowired
    CategoryWordsRepository repository;
    @MockBean
    WordService wordService;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    CategoryWordsRepository categoryWordsRepository;


    CategoryWordsService service;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        service = new CategoryWordsServiceImpl(modelMapper, repository, wordService);
    }

    @After
    public void cleanup() {
        categoryWordsRepository.deleteAll();
    }


    @Test
    public void addCategory_whenValid_shouldAddCategoryAndIncreaseRepoSize() {
        CategoryWordsServiceModel serviceModel = service.addCategory("zxc");
        assertEquals("zxc", serviceModel.getName());
        assertEquals(1, repository.count());
    }

    @Test
    public void removeCategoryById_shouldRemoveCorrectly() {
        CategoryWordsServiceModel serviceModel = service.addCategory("zxc");
        service.removeCategoryById(serviceModel.getId());
        assertEquals(0, repository.count());
    }

    @Test
    public void removeWordFromCategory_whenValidCategoryAndWord_shouldRemove() {
        Word word = new Word();
        word.setName("zxc");
        word.setDefinition("def");
        Word save = wordRepository.save(word);

        Word word2 = new Word();
        word2.setName("zxc2");
        word2.setDefinition("def2");
        Word save2 = wordRepository.save(word2);

        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("cat");
        categoryWords.getWords().add(save);
        categoryWords.getWords().add(save2);

        CategoryWords save1 = categoryWordsRepository.save(categoryWords);

        when(wordService.getWordByIdOrName(anyString()))
                .thenReturn(modelMapper.map(word2, WordServiceModel.class));

        service.removeWordFromCategory(save1.getId(), save2.getId());

        assertEquals(1, categoryWordsRepository.count());
    }

    @Test
    public void removeWordFromCategory_whenInvalidCategory_shouldThrow() {
        Word word = new Word();
        word.setName("zxc");
        word.setDefinition("def");
        Word save = wordRepository.save(word);

        when(wordService.getWordByIdOrName(anyString()))
                .thenReturn(modelMapper.map(word, WordServiceModel.class));

        assertThrows(
                NoSuchElementException.class,
                () -> service.removeWordFromCategory("save1.getId()", save.getId())
        );
    }

    @Test
    public void deleteWordsInCategory_whenCorrectCategoryAndWords_shouldDelete() {
        Word word = new Word();
        word.setName("zxc");
        word.setDefinition("def");
        Word save = wordRepository.save(word);

        Word word2 = new Word();
        word2.setName("zxc2");
        word2.setDefinition("def2");
        Word save2 = wordRepository.save(word2);

        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("cat");
        categoryWords.getWords().add(save);
        categoryWords.getWords().add(save2);

        CategoryWords save1 = categoryWordsRepository.save(categoryWords);

        List<String> strings = Arrays.asList(word.getId(), word2.getId());

        List<WordServiceModel> collect =
                Stream.of(word, word2)
                        .map(x -> modelMapper.map(x, WordServiceModel.class))
                        .collect(Collectors.toList());


        when(wordService.getWordsById(strings)).thenReturn(collect);

        service.deleteWordsInCategory(save1.getId(), strings);

        assertEquals(0, categoryWordsRepository.findByName("cat").getWords().size());
    }
}
