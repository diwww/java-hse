package ru.hse;


import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {
        List<? super B> list1 = new ArrayList<B>();
        list1.add(new C());
        list1.add(new B());
//        list1.add(new A());
        throw new Throwable();
    }
}

class A {

}

class B extends A {
}

class C extends B {
}