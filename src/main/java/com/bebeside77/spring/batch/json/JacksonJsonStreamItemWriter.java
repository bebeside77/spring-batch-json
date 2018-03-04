package com.bebeside77.spring.batch.json;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Spring batch ItemWriter to create json file with items.
 * It is useful when you need to handle big size of items.
 *
 * @param <T>
 */
public class JacksonJsonStreamItemWriter<T> extends AbstractItemStreamItemWriter<T> {
	private JsonGenerator jsonGenerator;
	private String outputFilePath;
	private String encoding;

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void write(List<? extends T> items) {
		items.forEach((Consumer<T>) item -> {
			try {
				jsonGenerator.writeObject(item);
			} catch (IOException e) {
				throw new ItemStreamException(String.format("Error occurred while write object, item: %s", item), e);
			}
		});
	}

	@Override
	public void open(ExecutionContext executionContext) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = new MappingJsonFactory(objectMapper);

		try {
			File outputFile = new File(outputFilePath);
			jsonGenerator = jsonFactory.createJsonGenerator(outputFile, JsonEncoding.valueOf(encoding));
			jsonGenerator.useDefaultPrettyPrinter();
			jsonGenerator.writeStartArray();
		} catch (IOException e) {
			throw new ItemStreamException("Fail to create JsonGenerator.", e);
		}
	}

	@Override
	public void close() {
		try {
			jsonGenerator.writeEndArray();
			jsonGenerator.close();
		} catch (IOException e) {
			throw new ItemStreamException("Fail to close JsonGenerator.", e);
		}
	}
}
