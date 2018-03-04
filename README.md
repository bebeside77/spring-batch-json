# spring-batch-json
Spring Batch custom extension to handle json.
It use jackson streaming api to save memory use. It is useful when you need to handle big size of items or json file.

## JacksonJsonStreamItemReader
You can read json object or array.

### Example
#### Read array format json file
##### json file
```json
[
  {
    "id": 1,
    "name": "cosmos",
    "author": "cal"
  },
  {
    "id": 2,
    "name": "The secret",
    "author": "cal"
  }
]
```

##### bean configuration
```xml
<bean class="com.bebeside77.spring.batch.json.JacksonJsonStreamItemReader">
  <property name="filePath" value="./some_json_file.json"/>
  <property bame="mappingClass" value="com.bebeside77.spring.batch.json.Book"/>
</bean>
```

#### Read object format json file
##### json file
```json
{
  "type": "CREATE",
  "books": [
    {
      "id": 1,
      "name": "cosmos",
      "author": "cal"
    },
    {
      "id": 2,
      "name": "The secret",
      "author": "cal"
    }
  ]
}
```

##### bean configuration
```xml
<bean class="com.bebeside77.spring.batch.json.JacksonJsonStreamItemReader">
  <property name="filePath" value="./some_json_file.json"/>
  <property name="arrayName" value="books"/>
  <property bame="mappingClass" value="com.bebeside77.spring.batch.json.Book"/>
</bean>
```

## JacksonJsonStreamItemWriter
You can write items to json file.
It support only json array format.

### Example
##### bean configuration
```xml
<bean class="com.bebeside77.spring.batch.json.JacksonJsonStreamItemWriter">
  <property name="outputFilePath" value="./output_file.json"/>
  <property name="encoding" value="utf8"/>
</bean>
```

## Constraint
- Minimum Java version : 1.8
