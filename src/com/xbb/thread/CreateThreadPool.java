package com.xbb.thread;


/*
 * 
 * 
 * @author ���
 */


import java.util.concurrent.*;

public class CreateThreadPool {

	public static void main(String[] arg)
	{
		//���ε���
//		ExecutorService pool=Executors.newSingleThreadExecutor();//�����̵߳��̳߳�
//		ExecutorService pool=Executors.newFixedThreadPool(2);//���ͬʱ����2���̳߳�
//		ExecutorService pool=Executors.newCachedThreadPool();//�Զ�������С���̳߳�
				
		//���ڵ����߳�
//		ScheduledExecutorService  pool=Executors.newSingleThreadScheduledExecutor();//���������̵߳��̳߳�
		ScheduledExecutorService  pool=Executors.newScheduledThreadPool(2);//2�������̵߳��̳߳�
		pool.scheduleAtFixedRate(new Thread(new MyRunnable(),"t1"),1,1,TimeUnit.SECONDS);//���õ�������
		
		pool.execute(new Thread(new MyRunnable(),"t1"));
		pool.execute(new Thread(new MyRunnable(),"t2"));
		pool.execute(new Thread(new MyRunnable(),"t3"));
//		pool.shutdown();
		System.out.println("hello");
	}
	
	public static class MyRunnable implements Runnable
	{
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()+" is Running");
		}
	}
}
