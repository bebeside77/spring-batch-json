package com.bebeside77.spring.batch.json;

import java.util.HashMap;

public interface ElementMapper<T> {
    T mapElement(HashMap<String, Object> elementMap);
}
