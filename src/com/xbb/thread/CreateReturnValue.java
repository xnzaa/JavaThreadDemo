package com.xbb.thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * 
 * W
 * @author ���
 */

public class CreateReturnValue {

	public static void main(String[] args)
	{
		ExecutorService pool =Executors.newCachedThreadPool();		
		Future<Integer> future = pool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        });
		
		Future<String> future1=pool.submit(new MyThread());
		try {
			Thread.sleep(100);
			System.out.println("����ֵΪ��"+future1.get().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class MyThread implements Callable <String>
	{
		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			return "���Ƿ���ֵ��";
		}
		
	}
}
