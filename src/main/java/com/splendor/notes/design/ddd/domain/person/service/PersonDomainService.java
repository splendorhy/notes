package com.splendor.notes.design.ddd.domain.person.service;

import com.splendor.notes.design.ddd.domain.person.entity.Person;
import com.splendor.notes.design.ddd.domain.person.repository.facade.PersonRepository;
import com.splendor.notes.design.ddd.domain.person.repository.po.PersonPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author splendor.s
 * @create 2022/11/28 上午10:02
 * @description
 */
@Service
@Slf4j
public class PersonDomainService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonFactory personFactory;


    public void create(Person person) {
        PersonPO personPO = personRepository.findById(person.getPersonId());
        if (null == personPO) {
            throw new RuntimeException("Person already exists");
        }
        person.create();
        personRepository.insert(personFactory.createPersonPO(person));
    }

    public void update(Person person) {
        person.setLastModifyTime(new Date());
        personRepository.update(personFactory.createPersonPO(person));
    }

    public void deleteById(String personId) {
        PersonPO personPO = personRepository.findById(personId);
        Person person = personFactory.getPerson(personPO);
        person.disable();
        personRepository.update(personFactory.createPersonPO(person));
    }

    public Person findById(String userId) {
        PersonPO personPO = personRepository.findById(userId);
        return personFactory.getPerson(personPO);
    }

    /**
     * find leader with applicant, if leader level bigger then leaderMaxLevel return null, else return Approver from leader;
     *
     * @param applicantId
     * @param leaderMaxLevel
     * @return
     */
    public Person findFirstApprover(String applicantId, int leaderMaxLevel) {
        PersonPO leaderPO = personRepository.findLeaderByPersonId(applicantId);
        if (leaderPO.getRoleLevel() > leaderMaxLevel) {
            return null;
        } else {
            return personFactory.createPerson(leaderPO);
        }
    }

    /**
     * find leader with current approver, if leader level bigger then leaderMaxLevel return null, else return Approver from leader;
     *
     * @param currentApproverId
     * @param leaderMaxLevel
     * @return
     */
    public Person findNextApprover(String currentApproverId, int leaderMaxLevel) {
        PersonPO leaderPO = personRepository.findLeaderByPersonId(currentApproverId);
        if (leaderPO.getRoleLevel() > leaderMaxLevel) {
            return null;
        } else {
            return personFactory.createPerson(leaderPO);
        }
    }

}
