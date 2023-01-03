package com.feng.baseframework.mapper;

import com.feng.baseframework.model.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentMapper {

    @Insert("insert into students(id,name,age) values(#{id},#{name},#{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addStudent(Student student);

    @Update("update students set name=#{name},age=#{age} where id=#{id}")
    public void updateStudent(Student student);

    @SelectProvider(type = ProviderMethod.class,method = "studentsLikeName")
    public List<Student> getStudentsLikeName(String name);

    @Delete("delete from students where id in(#{ids})")
    @Lang(SimpleLangDriver.class)
    public void deleteStudents(@Param("ids") List<Integer> ids);

    @Select("SELECT id, name, age FROM students WHERE id = #{id}")
    @Results(id = "userMap", value = { @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "name", property = "name", javaType = String.class),
            @Result(column = "age", property = "age", javaType = Integer.class) })
    public Student getStudentById(Integer id);
}
