package com.xbb.thread;


/*
 * 线程状态改变函数：
 * wait()/当前线程等待，直到有其他线程调用notify*()
 * jion()/将调用线程加到环境线程中，暂停环境线程的执行，直到调用线程执行完毕
 * yeild()/静态方法  暂停当前线程执行，并重新选择执行的线程
 * notify()/唤醒一个后台线程
 * notifyAll()/唤醒所有后台等待的线程
 * sleep()/静态方法，当前线程延时n个单位（ms）
 * 
 * 
 * 线程的6种状态
 * Thread.State.BLOCKED;	阻塞
 * Thread.State.NEW;		新建
 * Thread.State.RUNNABLE;	运行或等待调度
 * Thread.State.TERMINATED;	运行完成
 * Thread.State.TIMED_WAITING;	延时等待
 * Thread.State.WAITING;	等待
 * 
 * @author 徐兵
 */


public class Dispatch {

	
	public static void main(String[] args)
	{
		Dispatch dis=new Dispatch();
		
		Thread t1= new Thread(new  MyRunnable(dis),"线程1");
		Thread t2= new Thread(new  MyThread(dis),"线程2");
		t1.start();
		t2.start();
//		t2.setDaemon(true);//守护线程，可以 中途结束。例子：JVM的垃圾回收、内存管理等线程都是守护线程
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class MyRunnable implements Runnable
	{
		Dispatch dis;
		public MyRunnable(Dispatch dis)
		{
			this.dis=dis;
		}
		@Override
		public  void run() {
			
			System.out.println(Thread.currentThread().getName()+" 启动");
			try {
				Thread.sleep(2000);//单位ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" is Running");

			synchronized(dis){dis.notifyAll();}
			System.out.println(Thread.currentThread().getName()+" 唤醒其他线程");
			}
	}
	
	public static class MyThread extends Thread
	{
		Dispatch dis;
		public MyThread(Dispatch dis)
		{
			this.dis=dis;
		}
		@Override
		public synchronized void run() {
			
			System.out.println(Thread.currentThread().getName()+" 启动");
			try {
				synchronized(dis){dis.wait();}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" 被唤醒");
			Thread.yield();
			System.out.println(Thread.currentThread().getName()+" 重新被调度");
			
		}
	}
	
}
