package com.dlut.jky.app1.innerClass;

/**
 * Created by jiangkunyou on 15/11/30.
 */
public class DaoTest {
    public Dao myFunction(){
        class inner implements Dao{
            @Override
            public void update() {
                System.out.println("I'm inner class");
            }
        }
        Dao dao = new inner();
        return dao;
    }
    public static void main(String[] args) {
        DaoTest daoTest = new DaoTest();
        Dao dao = daoTest.myFunction();
        dao.update();
    }
}
