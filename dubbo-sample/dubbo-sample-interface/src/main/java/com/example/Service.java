package com.example;

import com.example.entity.AbstractObject;
import com.example.entity.Color;
import com.example.entity.Printer;
import com.example.entity.Range;
import com.example.entity.Student;
import com.example.entity.Teacher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public interface Service {
    String sayHi(String name);

    List<String> testList();

    int testInt();
    int[] testIntArr();
    Integer testInteger();
    Integer[] testIntegerArr();
    List<Integer> testIntegerList();

    short testShort();
    short[] testShortArr();
    Short testSShort();
    Short[] testSShortArr();
    List<Short> testShortList();

    byte testByte();
    byte[] testByteArr();
    Byte testBByte();
    Byte[] testBByteArr();
    ArrayList<Byte> testByteList();

    float testFloat();
    float[] testFloatArr();
    Float testFFloat();
    Float[] testFloatArray();
    List<Float> testFloatList();

    boolean testBoolean();
    boolean[] testBooleanArr();
    Boolean testBBoolean();
    Boolean[] testBooleanArray();
    List<Boolean> testBooleanList();

    char testChar();
    char[] testCharArr();
    Character testCharacter();
    Character[] testCharacterArr();
    List<Character> testCharacterList();
    List<Character[]> testCharacterListArr();

    String testString();
    String[] testStringArr();
    List<String> testStringList();
    List<String[]> testStringListArr();
    String testNull();

    Date testDate();
    Calendar testCalendar();
    LocalTime testLocalTime();
    LocalDate testLocalDate();
    LocalDateTime testLocalDateTime();
    ZonedDateTime testZoneDateTime();

    Map<Integer, String> testMap();
    Set<Integer> testSet();

    Optional<Integer> testOptionalEmpty();
    Optional<Integer> testOptionalInteger();
    Optional<String> testOptionalString();

    Color testEnum();

    Range testRecord();

    Printer testInterface();

    Teacher testObject();

    List<Teacher> testObjectList();

    Student<Integer> testTemplate();

    InputStream testStream() throws FileNotFoundException;

    Iterator<String> testIterator();

    AbstractObject testAbstract();

}
