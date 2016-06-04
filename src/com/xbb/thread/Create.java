package com.xbb.thread;
/*
 * 
 * 
 * @author 徐兵
 */
public class Create {

	public static void main(String[] args)
	{
		System.out.println(Thread.currentThread().getName()+" is Running");
		new Thread((new Create()).new  MyRunnable(),"线程1").start();
		new Thread((new Create()).new  MyThread(),"线程2").start();
	}
	
	public class MyRunnable implements Runnable
	{
		@Override
		public void run() {
			try {
				Thread.sleep(500);//单位ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" is Running");
		}
	}
	
	public class MyThread extends Thread
	{
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()+" is Running");
		}
	}
}
