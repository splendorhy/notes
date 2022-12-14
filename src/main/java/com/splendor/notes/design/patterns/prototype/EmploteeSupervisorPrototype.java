package com.splendor.notes.design.patterns.prototype;

import lombok.Data;

/**
 * @author splendor.s
 * @create 2022/11/28 下午3:59
 * @description 需要注意浅拷贝带来的问题
 */
@Data
public class EmploteeSupervisorPrototype implements Cloneable {
    private String name;
    private SupervisorPrototype supervisorPrototype;

    @Override
    public EmploteeSupervisorPrototype clone() {
        EmploteeSupervisorPrototype emploteeSupervisorPrototype = null;
        try {
            emploteeSupervisorPrototype = (EmploteeSupervisorPrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return emploteeSupervisorPrototype;
    }

    public static void main(String[] args) {
        SupervisorPrototype supervisorPrototype = new SupervisorPrototype();
        supervisorPrototype.setName("splendor 1");

        EmploteeSupervisorPrototype emploteeSupervisorPrototype1 = new EmploteeSupervisorPrototype();
        emploteeSupervisorPrototype1.setName("zhouyi 1");
        emploteeSupervisorPrototype1.setSupervisorPrototype(supervisorPrototype);

        EmploteeSupervisorPrototype emploteeSupervisorPrototype2 = emploteeSupervisorPrototype1.clone();
        emploteeSupervisorPrototype2.setName("zhouyi 2");
        emploteeSupervisorPrototype2.getSupervisorPrototype().setName("splendor 2");

        System.out.println("员工" + emploteeSupervisorPrototype1.getName() + "的上司是：" + emploteeSupervisorPrototype1.getSupervisorPrototype().getName());
        System.out.println("员工" + emploteeSupervisorPrototype2.getName() + "的上司是：" + emploteeSupervisorPrototype2.getSupervisorPrototype().getName());

    }
}
