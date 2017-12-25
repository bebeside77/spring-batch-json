package com.bebeside77.spring.batch.json;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JacksonJsonStreamItemReader <T> extends AbstractItemStreamItemReader {
    private String filePath;
    private JsonParser jsonParser;
    private String arrayName;
    private ElementMapper<T> elementMapper;
    private String mappingClass;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public ElementMapper<T> getElementMapper() {
        return elementMapper;
    }

    public void setElementMapper(ElementMapper<T> elementMapper) {
        this.elementMapper = elementMapper;
    }

    public String getMappingClass() {
        return mappingClass;
    }

    public void setMappingClass(String mappingClass) {
        this.mappingClass = mappingClass;
    }

    public T read() throws Exception {
        if (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            if (elementMapper != null) {
                HashMap<String, Object> nodeMap = jsonParser.readValueAs(HashMap.class);
                return elementMapper.mapElement(nodeMap);
            } else {
                Class<T> clazz = (Class<T>) Class.forName(mappingClass);
                return jsonParser.readValueAs(clazz);
            }
        } else {
            return null;
        }
    }

    private JsonParser createJsonParser() throws IOException {
        JsonFactory jsonFactory = new MappingJsonFactory();
        return jsonFactory.createJsonParser(new File(filePath));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            jsonParser = createJsonParser();

            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                moveParserOnTargetElement();
            }
        } catch (IOException e) {
            throw new ItemStreamException("Exception occurred while create JsonParser.", e);
        }
    }

    private void moveParserOnTargetElement() throws IOException {
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();

            if (arrayName.equals(fieldName)) {
                jsonParser.nextToken();
                break;
            }
        }
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            jsonParser.close();
        } catch (IOException e) {
            throw new ItemStreamException("Exception occurred while close JsonParser.", e);
        }
    }
}
