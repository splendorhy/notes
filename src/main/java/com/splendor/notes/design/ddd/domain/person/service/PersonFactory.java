package com.splendor.notes.design.ddd.domain.person.service;

import com.splendor.notes.design.ddd.domain.person.entity.Person;
import com.splendor.notes.design.ddd.domain.person.repository.facade.PersonRepository;
import com.splendor.notes.design.ddd.domain.person.repository.po.PersonPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author splendor.s
 * @create 2022/11/28 上午10:01
 * @description
 */
@Service
public class PersonFactory {

    @Autowired
    PersonRepository personRepository;

    public PersonPO createPersonPO(Person person){
        PersonPO personPO = new PersonPO();
        personPO.setPersonId(person.getPersonId());
        personPO.setPersonName(person.getPersonName());
        personPO.setRoleLevel(person.getRoleLevel());
        personPO.setPersonType(person.getPersonType());
        personPO.setCreateTime(person.getCreateTime());
        personPO.setLastModifyTime(person.getLastModifyTime());
        return personPO;
    }

    public Person createPerson(PersonPO po){
        Person person = new Person();
        person.setPersonId(po.getPersonId());
        person.setPersonType(po.getPersonType());
        person.setRoleLevel(po.getRoleLevel());
        person.setPersonName(po.getPersonName());
        person.setStatus(po.getStatus());
        person.setCreateTime(po.getCreateTime());
        person.setLastModifyTime(po.getLastModifyTime());
        return person;
    }

    public Person getPerson(PersonPO personPO){
        personPO = personRepository.findById(personPO.getPersonId());
        return createPerson(personPO);
    }

}
