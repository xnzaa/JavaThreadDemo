package com.xbb.thread;
/*
 * 
 * 
 * @author ���
 */
public class Create {

	public static void main(String[] args)
	{
		System.out.println(Thread.currentThread().getName()+" is Running");
		new Thread((new Create()).new  MyRunnable(),"�߳�1").start();
		new Thread((new Create()).new  MyThread(),"�߳�2").start();
	}
	
	public class MyRunnable implements Runnable
	{
		@Override
		public void run() {
			try {
				Thread.sleep(500);//��λms
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
