package com.splendor.notes.design.ddd.domain.person.entity;

/**
 * @author splendor.s
 * @create 2022/11/25 下午6:03
 * @description
 */
import com.splendor.notes.design.ddd.domain.person.entity.valueobject.PersonStatus;
import com.splendor.notes.design.ddd.domain.person.entity.valueobject.PersonType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Person {

    String personId;
    String personName;
    PersonType personType;
    List<Relationship> relationships;
    int roleLevel;
    Date createTime;
    Date lastModifyTime;
    PersonStatus status;

    public Person create(){
        this.createTime = new Date();
        this.status = PersonStatus.ENABLE;
        return this;
    }

    public Person enable(){
        this.lastModifyTime = new Date();
        this.status = PersonStatus.ENABLE;
        return this;
    }

    public Person disable(){
        this.lastModifyTime = new Date();
        this.status = PersonStatus.DISABLE;
        return this;
    }
}
