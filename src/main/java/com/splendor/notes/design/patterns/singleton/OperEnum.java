package com.splendor.notes.design.patterns.singleton;

/**
 * @author splendor.s
 * @create 2023/6/19 下午6:52
 * @description
 */
enum OperEnum {
    ADD(1, 2) {
        @Override
        public Integer operate() {
            return this.getA() + this.getB();
        }
    }, MULTIPY(1, 2) {
        @Override
        public Integer operate() {
            return this.getA() * this.getB();
        }
    };

    private Integer a;

    private Integer b;

    OperEnum(Integer a, Integer b) {
        this.a = a;
        this.b = b;
    }

    public abstract Integer operate();

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }
}
