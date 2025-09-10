package br.com.erudio.unittests.mapper;
import static br.com.erudio.mapper.ObjectMapper.parseObject;
import static br.com.erudio.mapper.ObjectMapper.parseListObjects;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.erudio.data.dto.BookDTO;
import br.com.erudio.model.Book;
import br.com.erudio.unittests.mapper.mocks.MockBook;

public class ObjectMapperBookTests {
    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToDTOTest() {
        BookDTO output = parseObject(inputObject.mockEntity(), BookDTO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Author Test0", output.getAuthor());
//        assertEquals(new Date(), output.getLaunchDate());
        assertEquals(25D, output.getPrice());
        assertEquals("Title of the book0", output.getTitle());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<BookDTO> outputList = parseListObjects(inputObject.mockEntityList(), BookDTO.class);
        BookDTO outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Author Test0", outputZero.getAuthor());
//        assertEquals(new Date(), outputZero.getLaunchDate());
        assertEquals(25D, outputZero.getPrice());
        assertEquals("Title of the book0", outputZero.getTitle());

        BookDTO outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Author Test7", outputSeven.getAuthor());
//        assertEquals(new Date(), outputSeven.getLaunchDate());
        assertEquals(25D, outputSeven.getPrice());
        assertEquals("Title of the book7", outputSeven.getTitle());

        BookDTO outputEight = outputList.get(8);
        assertEquals(Long.valueOf(8L), outputEight.getId());
        assertEquals("Author Test8", outputEight.getAuthor());
//        assertEquals(new Date(), outputEight.getLaunchDate());
        assertEquals(25D, outputEight.getPrice());
        assertEquals("Title of the book8", outputEight.getTitle());
    }

    @Test
    public void parseDTOToEntityTest() {
        Book output = parseObject(inputObject.mockDTO(), Book.class);
        
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Author Test0", output.getAuthor());
//        assertEquals(new Date(), output.getLaunchDate());
        assertEquals(25D, output.getPrice());
        assertEquals("Title of the book0", output.getTitle());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Book> outputList = parseListObjects(inputObject.mockDTOList(), Book.class);
        Book outputZero = outputList.get(0);
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Author Test0", outputZero.getAuthor());
//        assertEquals(new Date(), outputZero.getLaunchDate());
        assertEquals(25D, outputZero.getPrice());
        assertEquals("Title of the book0", outputZero.getTitle());

        Book outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Author Test7", outputSeven.getAuthor());
//        assertEquals(new Date(), outputSeven.getLaunchDate());
        assertEquals(25D, outputSeven.getPrice());
        assertEquals("Title of the book7", outputSeven.getTitle());

        Book outputEight = outputList.get(8);
        assertEquals(Long.valueOf(8L), outputEight.getId());
        assertEquals("Author Test8", outputEight.getAuthor());
//        assertEquals(new Date(), outputEight.getLaunchDate());
        assertEquals(25D, outputEight.getPrice());
        assertEquals("Title of the book8", outputEight.getTitle());
    }
}