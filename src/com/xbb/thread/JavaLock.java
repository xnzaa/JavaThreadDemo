package com.xbb.thread;

import java.util.concurrent.locks.Lock;

/*
 * »¥³âËøÄ£Äâ
 * 
 * @author Ğì±ø
 */

public class JavaLock {
	
	public static void main(String[] args)
	{
		Share share=new Share();
		for(int i = 0;i<10;i++)
		{
			Thread t1=new Thread(new MyRunnable(share),"t1");
			t1.start();
		}
	}	
	public static class Share
	{
		private int num=100;
		public int getnum()
		{
			return num;
		}
		
		public synchronized void decnum()
		{
			int num2=num;
			num2-=10;
			try {
				Thread.sleep(1);// do something
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			num=num2;
		}
	}
	
	public static class MyRunnable implements Runnable
	{
		Share share;
		
		public MyRunnable(Share share) {
			this.share=share;
		}
		@Override
		public void run() {
				share.decnum();
				System.out.println(share.getnum());
		}
	}
	
	public static class ShareLock
	{
		private int num=100;
		Lock lock;
		public ShareLock(Lock lock)
		{
			this.lock=lock;
		}
		public int getnum()
		{
			return num;
		}
		
		public synchronized void decnum()
		{
			lock.lock();
			int num2=num;
			num2-=10;
			try {
				Thread.sleep(100);// do something
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			num=num2;
			lock.unlock();
		}
	}
}
