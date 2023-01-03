package com.feng.baseframework.model;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;

public class LombokAnnotationTest {

    @Test
    public void builder() {
        BuilderEntity builderEntity = BuilderEntity.builder().name("test").age(12).build();

        Assert.state(builderEntity != null ,"lombok builder annotation");
    }

    @Test
    public void noArgsConstruct(){
        ConstructEntity constructEntity = new ConstructEntity().setName("test").setAge(12);

        Assert.state(constructEntity != null ,"lombok NoArgsConstructor,Accessors,AllArgsConstructor annotation");
    }

    @Test
    public void allArgsConstruct(){
        UserRole userRole = new UserRole(1l, "test", 12l);

        Assert.state(userRole != null ,"lombok AllArgsConstructor annotation");
    }


    @Test
    public void chain(){
        UserRole userRole = UserRole.of().setRoleId("test").setUserId(1l);

        Assert.state(userRole != null ,"lombok Accessors annotation");
    }

    @Test
    public void staticConstruct(){
        UserRole userRole = UserRole.of().setRoleId("test").setUserId(1l);

        Assert.state(userRole != null ,"lombok RequiredArgsConstructor annotation");
    }
}