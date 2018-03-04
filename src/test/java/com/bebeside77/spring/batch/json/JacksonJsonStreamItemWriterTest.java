package com.bebeside77.spring.batch.json;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JacksonJsonStreamItemWriterTest {
	private JacksonJsonStreamItemWriter<Book> writer;

	private TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() {
		writer = new JacksonJsonStreamItemWriter<>();
	}

	@Test
	public void write() throws IOException {
		temporaryFolder.create();
		File outputFile = temporaryFolder.newFile("output.txt");

		writer.setEncoding("UTF8");
		writer.setOutputFilePath(outputFile.getAbsolutePath());
		writer.open(new ExecutionContext());

		writer.write(createBookList());
		writer.close();

		String outputContents = FileUtils.readFileToString(outputFile, "utf-8");
		outputContents = StringUtils.trimAllWhitespace(outputContents);

		ClassPathResource classPathResource = new ClassPathResource("book_array.json");
		String expectedContents = FileUtils.readFileToString(classPathResource.getFile(), "utf-8");
		expectedContents = StringUtils.trimAllWhitespace(expectedContents);

		assertThat(outputContents, is(expectedContents));
	}

	private List<Book> createBookList() {
		List<Book> bookList = new ArrayList<>();
		Book book1 = new Book();
		book1.setId(1);
		book1.setName("cosmos");
		book1.setAuthor("cal");
		Book book2 = new Book();
		book2.setId(2);
		book2.setName("The secret");
		book2.setAuthor("cal");
		Book book3 = new Book();
		book3.setId(3);
		book3.setName("Thinking in java");
		book3.setAuthor("cal");
		Book book4 = new Book();
		book4.setId(4);
		book4.setName("Effective java");
		book4.setAuthor("cal");
		Book book5 = new Book();
		book5.setId(5);
		book5.setName("Clean code");
		book5.setAuthor("cal");

		bookList.add(book1);
		bookList.add(book2);
		bookList.add(book3);
		bookList.add(book4);
		bookList.add(book5);
		return bookList;
	}
}