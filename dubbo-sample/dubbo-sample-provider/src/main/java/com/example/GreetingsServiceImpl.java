package com.example;

import com.example.entity.AbstractObject;
import com.example.entity.AbstractObjectImpl;
import com.example.entity.Color;
import com.example.entity.Printer;
import com.example.entity.PrinterImpl;
import com.example.entity.Range;
import com.example.entity.Student;
import com.example.entity.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.*;
import java.util.*;

@DubboService
@Slf4j
public class GreetingsServiceImpl implements Service {

    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }

    @Override
    public List<String> testList() {
        return new ArrayList<>(Arrays.asList("hello", "hi"));
    }

    @Override
    public int testInt() {
        int res = 0;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public int[] testIntArr() {
        int[] res = new int[]{1, 2, 3, 4};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public Integer testInteger() {
        Integer res = 4;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Integer[] testIntegerArr() {
        Integer[] res = new Integer[]{1, 2, 3, 4};
        log.info("ori: ", (Object) res);
        return res;
    }

    @Override
    public List<Integer> testIntegerList() {
        List<Integer> res =  new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public short testShort() {
        short res = 12;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public short[] testShortArr() {
        short[] res = new short[]{34, 67, 99};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public Short testSShort() {
        Short res = 2;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Short[] testSShortArr() {
        Short[] res = new Short[]{1, 45, 99};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public List<Short> testShortList() {
        List<Short> res = new ArrayList<>(Arrays.asList((short)1, (short)2, (short)3));
        log.info("ori: " + res);
        return res;
    }


    @Override
    public byte testByte() {
        byte res = (byte)23;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public byte[] testByteArr() {
        byte[] res = new byte[]{(byte)12, (byte)22, (byte)33};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }


    @Override
    public Byte testBByte() {
        Byte res = (byte)33;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Byte[] testBByteArr() {
        Byte[] res = new Byte[]{(byte)11, (byte)22, (byte)33};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public ArrayList<Byte> testByteList() {
        ArrayList<Byte> res = new ArrayList<>(Arrays.asList((byte)22, (byte)33));
        log.info("ori: " + res);
        return res;
    }


    @Override
    public float testFloat() {
        float res = 1.4F;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public float[] testFloatArr() {
        float[] res = new float[]{1.2F, 4.5F};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public Float testFFloat() {
        Float res = 3.9F;
        log.info("ori: " + res);
        return res;
    }


    @Override
    public Float[] testFloatArray() {
        Float[] res = new Float[]{2.3F, 4.5F};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }


    @Override
    public List<Float> testFloatList() {
        List<Float> res = new ArrayList<>(Arrays.asList(3.2F, 4.5F, 9.F));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public boolean testBoolean() {
        boolean res = false;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public boolean[] testBooleanArr() {
        boolean[] res = new boolean[]{false, true};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public Boolean testBBoolean() {
        Boolean res = true;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Boolean[] testBooleanArray() {
        Boolean[] res = new Boolean[]{true, false};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public List<Boolean> testBooleanList() {
        List<Boolean> res = new LinkedList<>(Arrays.asList(true, false));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public char testChar() {
        char res = 'a';
        log.info("ori: " + res);
        return res;
    }

    @Override
    public char[] testCharArr() {
        char[] res = new char[0];
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public Character testCharacter() {
        Character res = 'w';
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Character[] testCharacterArr() {
        Character[] res = new Character[2];
        res[0] = 'a';
        res[1] = 'w';
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public List<Character> testCharacterList() {
        List<Character> res = new ArrayList<>(Arrays.asList('a', 'w'));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public List<Character[]> testCharacterListArr() {
        Character[] res1 = new Character[2];
        res1[0] = 'a';
        res1[1] = 'w';
        Character[] res2 = new Character[2];
        res2[0] = 'r';
        res2[1] = 'y';
        List<Character[]> res = new ArrayList<>();
        res.add(res1);
        res.add(res2);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public String testString() {
        String res = "dubbo";
        log.info("ori: " + res);
        return res;
    }

    @Override
    public String[] testStringArr() {
        String[] res = new String[]{"dubbo", "wearefamali"};
        log.info("ori: " + Arrays.toString(res));
        return res;
    }

    @Override
    public List<String> testStringList() {
        List<String> res = new ArrayList<>(Arrays.asList("dubbo", "china"));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public List<String[]> testStringListArr() {
        String[] res1 = new String[]{"dubbo1", "wearefamali"};
        String[] res2 = new String[]{"dubbo2", "wearefamali"};
        List<String[]> res = new ArrayList<>(Arrays.asList(res1, res2));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public String testNull() {
        String res = null;
        log.info("ori: " + res);
        return null;
    }

    @Override
    public Date testDate() {
        Date res = new Date();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Calendar testCalendar() {
        Calendar res = new GregorianCalendar();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public LocalTime testLocalTime() {
        LocalTime res = LocalTime.now();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public LocalDate testLocalDate() {
        LocalDate res = LocalDate.now();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public LocalDateTime testLocalDateTime() {
        LocalDateTime res = LocalDateTime.now();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public ZonedDateTime testZoneDateTime() {
        ZonedDateTime res = ZonedDateTime.of(2023,
                7, 11, 23, 45, 59,
                1234,
                ZoneId.of("UTC+8"));
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Map<Integer, String> testMap() {
        Map<Integer, String> res = new HashMap<>();
        res.put(1, "one");
        res.put(2, "two");
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Set<Integer> testSet() {
        Set<Integer> res = new HashSet<>();
        res.add(23);
        res.add(45);
        res.add(100);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Optional<Integer> testOptionalEmpty() {
        Optional<Integer> res = Optional.empty();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Optional<Integer> testOptionalInteger() {
        Optional<Integer> res = Optional.of(12);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Optional<String> testOptionalString() {
        Optional<String> res = Optional.of("dubbo");
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Color testEnum() {
        Color res = Color.RED;
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Range testRecord() {
        Range res = new Range(2, 3);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Printer testInterface() {
        Printer res = new PrinterImpl();
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Teacher testObject() {
        Teacher res = new Teacher("dubbo", 23);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public List<Teacher> testObjectList() {
        Teacher res1 = new Teacher("dubbo1", 23);
        Teacher res2 = new Teacher("dubbo2", 25);
        List<Teacher> res = new ArrayList<>();
        res.add(res1);
        res.add(res2);
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Student<Integer> testTemplate() {
        Student<Integer> res = new Student<>(2, "dubbo");
        log.info("ori: " + res);
        return res;
    }

    @Override
    public InputStream testStream() throws FileNotFoundException {
        InputStream res = new FileInputStream("C:\\Users\\25942\\Desktop\\GSoC 2023\\test.txt");
        log.info("ori: " + res);
        return res;
    }

    @Override
    public Iterator<String> testIterator() {
        List<String> res = new ArrayList<>(Arrays.asList("dubbo", "hello", "hi"));
        log.info("ori: " + res.iterator());
        return res.iterator();
    }

    @Override
    public AbstractObject testAbstract() {
        AbstractObject res = new AbstractObjectImpl();
        log.info("ori: " + res);
        return res;
    }
}
