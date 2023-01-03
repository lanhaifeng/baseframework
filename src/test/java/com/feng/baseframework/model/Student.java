package com.feng.baseframework.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.feng.baseframework.jackson.CustomStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

@Data
@JsonSerialize(using = CustomStringSerializer.class)
public class Student implements Serializable, Cloneable {
    private Integer id;
    private String name;
    private Integer age;
    private Teacher teacher;

    public String hobby;
    public static String type = "person";

    public static String getTestType(){
        return type.toUpperCase();
    }

    public  String getPrivateName(String name) {
        if (check()){
            return "private 被mock 了";
        }
        return name;
    }

    public Optional<Integer> getAgeTest() {
        return Optional.ofNullable(age);
    }

    private Boolean check(){
        return false;
    }

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(Integer id, String name, Integer age, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher = teacher;
    }

    public String nameToUpperCase() {
        this.name = this.name.toUpperCase();
        return this.name;
    }

    //浅拷贝
    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student)super.clone();
    }

    //深拷贝
    /*@Override
    public Student clone() throws CloneNotSupportedException {
        Student s = (Student)super.clone();
        if(s.getTeacher() != null){
            s.setTeacher(s.getTeacher().clone());
        }
        return s;
    }*/
}
