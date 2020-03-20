package com.tianhy.dto;

/**
 * {@link}
 *
 * @Desc: DTO(Data Transfer Object) 数据传输对象
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
