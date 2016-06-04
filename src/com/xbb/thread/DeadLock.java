package com.xbb.thread;

/*
 * ����ģ��,��ѧ�ҾͲ�����ļ�ʵ��
 * 
 * 3�ֽ�������ǣ�������������Դ�ּ�����Chandy/Misra������ѧ�ҹ�ͨ����
 * @author ���
 */

public class DeadLock {
	public static void main(String arg[])
	{
		DeadLock dead = new DeadLock();
		Thread t1=new Thread((new DeadLock()).new MyRunnable1(dead),"t1");
		Thread t2=new Thread((new DeadLock()).new MyRunnable1(dead),"t2");
		t1.start();
		t2.start();
	}
	
	public class MyRunnable1 implements Runnable
	{
		DeadLock dead =new DeadLock();
		public  MyRunnable1(DeadLock dead) {
			this.dead=dead;
		}
		
		@Override
		public void run() {
			dead.write(1, 2);
			dead.read();
		}
	}
	
	public class MyRunnable2 implements Runnable
	{
		DeadLock dead =new DeadLock();
		public  MyRunnable2(DeadLock dead){
			this.dead=dead;
		}
		@Override
		public void run() {
			dead.read();
			dead.write(1, 2);
		}
	}

    private String resourceA =new String();//��Դ��ע�⣺ҪΪͬһ����
    private String resourceB =new String();

    public void  read() {
        synchronized (resourceA) {
    		System.out.println("getA");
        	try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            synchronized (resourceB) {
        		System.out.println("getB");
            } 
        } 
    } 

    public void write(int a,int b) { 
        synchronized (resourceB) {
    		System.out.println("getB ");
        	try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
            synchronized (resourceA) {
        		System.out.println("getA");
            } 
        } 
    } 
}
