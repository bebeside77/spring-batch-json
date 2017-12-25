package com.bebeside77.spring.batch.json;

import org.codehaus.jackson.JsonGenerator;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

public class JacksonJsonStreamItemWriter extends AbstractItemStreamItemWriter {
    private JsonGenerator jsonGenerator;

    public void write(List items) throws Exception {


    }
}
