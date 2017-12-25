package com.bebeside77.spring.batch.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class JacksonJsonStreamItemReaderTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private JacksonJsonStreamItemReader<Book> reader;

    @Before
    public void setUp() {
        reader = new JacksonJsonStreamItemReader<Book>();
    }

    @After
    public void tearDown() {
        reader.close();
    }

    @Test
    public void readArray() throws Exception {
        reader.setMappingClass("com.bebeside77.spring.batch.json.Book");
        ClassPathResource classPathResource = new ClassPathResource("book_array.json");
        reader.setFilePath(classPathResource.getFile().getAbsolutePath());
        reader.open(new ExecutionContext());

        int count = getCount();

        assertEquals(5, count);
    }

    @Test
    public void readObject() throws Exception {
        reader.setMappingClass("com.bebeside77.spring.batch.json.Book");
        ClassPathResource classPathResource = new ClassPathResource("book_object.json");
        reader.setFilePath(classPathResource.getFile().getAbsolutePath());
        reader.setArrayName("books");
        reader.open(new ExecutionContext());

        int count = getCount();

        assertEquals(5, count);
    }

    @Test
    public void readObjectWithRowMapper() throws Exception {
        reader.setElementMapper(new ElementMapper<Book>() {
            public Book mapElement(HashMap<String, Object> elementMap) {
                Book book = new Book();
                book.setId((Integer) elementMap.get("id"));
                book.setName((String) elementMap.get("name"));
                book.setAuthor((String) elementMap.get("author"));
                return book;
            }
        });

        ClassPathResource classPathResource = new ClassPathResource("book_object.json");
        reader.setFilePath(classPathResource.getFile().getAbsolutePath());
        reader.setArrayName("books");
        reader.open(new ExecutionContext());

        int count = getCount();

        assertEquals(5, count);
    }

    private int getCount() throws Exception {
        int count = 0;

        while (true) {
            Book book = reader.read();
            log.info("book: {}", book);

            if (book == null) {
                break;
            }

            count++;
        }
        return count;
    }
}