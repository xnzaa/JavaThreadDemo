package com.xbb.thread;


/*
 * 
 * 
 * @author 徐兵
 */


import java.util.concurrent.*;

public class CreateThreadPool {

	public static void main(String[] arg)
	{
		//单次调度
//		ExecutorService pool=Executors.newSingleThreadExecutor();//单个线程的线程池
//		ExecutorService pool=Executors.newFixedThreadPool(2);//最大同时运行2个线程池
//		ExecutorService pool=Executors.newCachedThreadPool();//自动调整大小的线程池
				
		//周期调度线程
//		ScheduledExecutorService  pool=Executors.newSingleThreadScheduledExecutor();//单个周期线程的线程池
		ScheduledExecutorService  pool=Executors.newScheduledThreadPool(2);//2个周期线程的线程池
		pool.scheduleAtFixedRate(new Thread(new MyRunnable(),"t1"),1,1,TimeUnit.SECONDS);//设置调用周期
		
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
