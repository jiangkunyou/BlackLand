package com.dlut.jky.app1.thread;

/**
 * Created by jiangkunyou on 15/11/25.
 */
public class myRunnable implements Runnable {

    String name = null;

    @Override
    public void run() {

        for(int i = 0; i < 10; i++) {
//            String name = Thread.currentThread().getName();
            name = Thread.currentThread().getName();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": " + name);
        }
    }

//    public static void main(String [] args){
//        myRunnable myRunnable = new myRunnable();
//        new Thread(myRunnable, "AAA").start();
//        new Thread(myRunnable, "BBB").start();
//        new Thread(myRunnable, "CCC").start();
//    }
}
