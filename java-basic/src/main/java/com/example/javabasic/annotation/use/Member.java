package com.example.javabasic.annotation.use;

import com.example.javabasic.annotation.define.*;
import lombok.Data;

@Data
@DBTable(name = "MEMBER")
public class Member {
    @SQLString(30)
    private String firstName;

    @SQLString(50)
    private String lastName;

    @SQLInteger(name = "age")
    private Integer age;

    @SQLString(value = 30, constrains = @Constraints(primaryKey = true))
    private String handle;
}
