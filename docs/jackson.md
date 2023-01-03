# 序列化时忽略某些字段

## 只序列化不为空字段
```text
new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
```

## 反序列化忽略类中不存在属性
```text
new ObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
```

## 忽略序列化和反序列化

### @JsonIgnore
```text
@JsonIgnore
private String author;
```

### @JsonIgnoreProperties
```text
@JsonIgnoreProperties({ "summary", "author" })
public class ArticleIgnoreProperties {
 
	private String title;
	private String summary;
	private String content;
	private String author;
 
}
```

### @JsonIgnoreType
> 忽略某个类型序列化
```text
public class AnimalIgnoreType {
 
	private String name;
	private Date date;
	private Address address;
 
	@JsonIgnoreType
	public static class Address {
		private String city;
	}
}
```

### addMixIn
> 将注解应用于目标类或方法
> ObjectMapper#addMixIn(Class<?> target, Class<?> mixinSource)

将@JsonIgnoreType注解应用于目标类
```text
@JsonIgnoreType
public class IgnoreType {}

public class AnimalIgnoreType {
 
	private String name;
	private Date date;
	private Address address;
 
	public static class Address {
		private String city;
		
	}
 
}

/**
 * 调用ObjectMapper的addMixIn方法，将@JsonIgnoreType注解应用于任意目标类.
 * 
 * @throws JsonProcessingException
 */
@Test
public void mixIn() throws JsonProcessingException {
	AnimalIgnoreType animal = new AnimalIgnoreType();
	animal.setName("sam");
	animal.setDate(new Date());
	
	AnimalIgnoreType.Address address = new AnimalIgnoreType.Address();
	address.setCity("gz");
	animal.setAddress(address);
	
	ObjectMapper mapper = new ObjectMapper();
	mapper.addMixIn(Date.class, IgnoreType.class);
	mapper.addMixIn(Address.class, IgnoreType.class);
	//{"name":"sam"}
	System.out.println(mapper.writeValueAsString(animal));
}
```

将@JsonIgnore注解应用于目标方法
```text
abstract class MixIn {
    @JsonIgnore
    abstract public void setSourceCodePaths(SourceCodePath... sourcePaths);
}

public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.addMixIn(ApiConfig.class, MixIn.class);

        StringBuffer jsonBuffer = new StringBuffer(1024);
        FileUtils.readToBuffer(jsonBuffer, Thread.currentThread().getContextClassLoader().getResourceAsStream("smart-doc.json"));
        ApiConfig config = objectMapper.readValue(jsonBuffer.toString(), ApiConfig.class);
}
```