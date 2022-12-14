package com.splendor.notes.design.patterns.prototype;

import lombok.Data;

/**
 * @author splendor.s
 * @create 2022/11/28 下午3:59
 * @description 通过深拷贝解决浅拷贝所带来的问题
 */
@Data
public class EmploteeSupervisorWithDeepPrototype implements Cloneable {

    private String name;
    private SupervisorPrototype supervisorPrototype;

    @Override
    public EmploteeSupervisorWithDeepPrototype clone() {
        EmploteeSupervisorWithDeepPrototype emploteeSupervisorWithDeepPrototype = null;
        try {
            emploteeSupervisorWithDeepPrototype = (EmploteeSupervisorWithDeepPrototype) super.clone();
            SupervisorPrototype supervisorPrototype = this.supervisorPrototype.clone();
            emploteeSupervisorWithDeepPrototype.setSupervisorPrototype(supervisorPrototype);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return emploteeSupervisorWithDeepPrototype;
    }

    public static void main(String[] args) {
        SupervisorPrototype supervisorPrototype = new SupervisorPrototype();
        supervisorPrototype.setName("splendor 1");

        EmploteeSupervisorWithDeepPrototype emploteeSupervisorWithDeepPrototype1 = new EmploteeSupervisorWithDeepPrototype();
        emploteeSupervisorWithDeepPrototype1.setName("zhouyi 1");
        emploteeSupervisorWithDeepPrototype1.setSupervisorPrototype(supervisorPrototype);

        EmploteeSupervisorWithDeepPrototype emploteeSupervisorWithDeepPrototype2 = emploteeSupervisorWithDeepPrototype1.clone();
        emploteeSupervisorWithDeepPrototype2.setName("zhouyi 2");
        emploteeSupervisorWithDeepPrototype2.getSupervisorPrototype().setName("splendor 2");

        System.out.println("员工" + emploteeSupervisorWithDeepPrototype1.getName() + "的上司是：" + emploteeSupervisorWithDeepPrototype1.getSupervisorPrototype().getName());
        System.out.println("员工" + emploteeSupervisorWithDeepPrototype2.getName() + "的上司是：" + emploteeSupervisorWithDeepPrototype2.getSupervisorPrototype().getName());

    }
}
