package com.example;

// import com.alibaba.fastjson.JSON;

import lombok.Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@Data
public class JsonType {
    // 整型相关
    // private int intVar = 1;
    // private int[] intArray = {1, 2, 3, 4};

    // private Integer integerVar = 1;
    // private Integer[] integerArray = {1, 2, 3, 4, 5};
    // private List<Integer> integerList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    //
    // private byte[] byteArray = {1, 2, 3, 4};
    // private short[] shortArray = {1, 2, 3, 4};
    //
    // // 浮点型相关
    // private float floatVar = 3.F;
    // private float[] floatArray = {1.F, 2.F, 3.F, 4.F};
    //
    // private Float floatVarB = 1.F;
    // private Float[] floatArrayB = {1F, 2F, 3F, 4F};
    // private List<Float> floatList = new ArrayList<>(Arrays.asList(1F, 2F, 3F, 4F));

    // BigDecimal bigDecimal = new BigDecimal("3.8");

    //
    // // 布尔值相关
    // private boolean booleanVar = false;
    // private boolean[] booleanArray = {true, false};
    //
    // private Boolean booleanVarB = true;
    // private Boolean[] booleanArrayB = {true, false};
    // private List<Boolean> booleanList = new ArrayList<>(Arrays.asList(true, false, true));
    //
    // // char 相关
    // private char charVar = 'a';
    // private char[] charArray = {'w', 'a'};
    //
    // private Character characterVar = 'a';
    // private Character[] characterArray = {'a', 's'};
    // private List<Character> characterList = new ArrayList<>(Arrays.asList('a', 's'));
    //
    // // String 相关
    // private String stringVar = "hejianfei";
    // private String[] stringArray = {"hejianfei", "hjf"};
    // private List<String> stringList = new ArrayList<>(Arrays.asList("hejianfei", "hjf"));
    // StringBuilder sb = new StringBuilder(10);
    // String string = null;
    //
    // // null
    // private Integer nullVar = null;
    //
    // // date
    // private Date dataVar = new Date();

    // // map
    // private Map<Integer, String> map = new HashMap<>();
    //
    // // set
    // private Set<String> set = new HashSet<>();
    //
    // private T templateVar;

    // // 创建List集合
    // Set<String> set = new HashSet<>();
    //
    // // Stream 流
    // Stream<String> stream;

    // Iterator<String> it;

    // Time 相关
    // Calendar calendar = new GregorianCalendar();
    // LocalTime localTime = LocalTime.now();
    // LocalDate localDate = LocalDate.now();
    // LocalDateTime localDateTime = LocalDateTime.now();
    // ZonedDateTime zonedDateTime = ZonedDateTime.of(2023, 7, 11, 23, 45, 59, 1234, ZoneId.of("UTC+8"));

    // Optional<Integer> optionalNull = Optional.empty();
    // Optional<Integer> optionalInteger = Optional.of(2);
    // Optional<String> optionalString = Optional.of("hjf");

    // public enum Color {
    //     RED("red", 1), BLUE("blue", 2),
    //     GREEN("green", 3), BLACK("black", 4);
    //
    //     public String getName() {
    //         return name;
    //     }
    //
    //     public void setName(String name) {
    //         this.name = name;
    //     }
    //
    //     public int getIndex() {
    //         return index;
    //     }
    //
    //     public void setIndex(int index) {
    //         this.index = index;
    //     }
    //
    //     @Override
    //     public String toString() {
    //         return "Color{" +
    //                 "name='" + name + '\'' +
    //                 ", index=" + index +
    //                 '}';
    //     }
    //
    //     private String name;
    //     private int index;
    //     Color(String name, int value) {
    //         this.name = name;
    //         this.index = value;
    //     }
    // }
    //
    // Color color = Color.BLUE;

    // record 关键字
    // public record range(int start, int end){};
    // range rangeVar = new range(12, 67);

    // public sealed class SealedClass {
    // }
    //
    // public final class Ellipse extends SealedClass {
    //     private String name = "hjf-sealed-class";
    //
    //     public String getName() {
    //         return name;
    //     }
    //
    //     public void setName(String name) {
    //         this.name = name;
    //     }
    // }
    //
    // Ellipse ellipse = new Ellipse();

    InputStream object1 = new FileInputStream("C:\\Users\\25942\\Desktop\\GSoC 2023\\test.txt");

    public JsonType() throws FileNotFoundException {
        // this.map.put(1, "111");
        // this.map.put(2, "222");
        // this.set.add("hjf");
        // this.set.add("hjyp");
        // this.templateVar = template;
        // set.add("张老三");
        // set.add("张小三");
        // // stream = set.stream();
        // it = set.iterator();
        // sb.append('w');
        // sb.append("hjfwer");
        // string = sb.toString();
    }

    public void getTemplateType() {
        Field[] fields = JsonType.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> curFieldType = field.getType();
            if (curFieldType.equals(Set.class)) {
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    Class<?> actualType = (Class<?>) pt.getActualTypeArguments()[0];
                    System.out.println(actualType.getSimpleName());
                }
            }
            System.out.println(field.getName());
        }

    }

    public void jsonSerialize() {
        var a = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        // JsonType<Float> jsonType = new JsonType<>(a);
        // JsonType jsonType = new JsonType();
        // jsonType.getTemplateType();

        // String jsonString = JSON.toJSONString(jsonType);
        // System.out.println(jsonString);
        // System.out.println(JSON.toJSONString(optionalString));
    }


    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {

        JsonType jsonType = new JsonType();
    }
}
