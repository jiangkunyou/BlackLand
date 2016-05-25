package com.dlut.jky.app1.beans;

import com.dlut.jky.app1.reflection.AgeValidator;
import lombok.*;

/**
 * Created by jiangkunyou on 15/11/2.
 */

@NoArgsConstructor
@AllArgsConstructor
public class Person {
    public Integer getAge() {
        return age;
    }

    // 使用自定义注解 限制年龄必须在18~35之间才能赋值成功
    @AgeValidator(min = 18, max = 35)
    public void setAge(Integer age) {
        this.age = age;
    }

    private Integer age;
    @Setter @Getter private String name;

}
