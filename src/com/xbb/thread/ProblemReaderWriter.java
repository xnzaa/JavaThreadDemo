package com.xbb.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 
 * ����-д��ģ�ͣ���3�������
 * 1����������  ��������ռ��д����ʹ�ú�����д�������𣬶���������ִ�У�
 * 
 * 2��˳����� ��д����ռ�ж�����ʹ��������д����������߳̾���д������ִ�У�
 * 
 * 3��д������ �����ӻ�����write_mutex��ʹ�ö������ĵȴ������У����ֻ��һ����ö�����
 *    �Ӷ�ȷ��д�������Ȼ�ö������ӳ������������Ľ��У�ȷ��д�������ȣ��������ģ�ͣ�
 * 
 * @author ���
 */
public class ProblemReaderWriter{

	public static enum Mode
	{
		READFIRST,
		WRITEFRIST,
		NORMAL;
	}
	
	
	public static void main(String[] args)
	{
		Semaphore readerNum = new Semaphore(0);//�ź�������������
		Semaphore writerNum = new Semaphore(1);//�ź�����д������
		Semaphore mutexReader = new Semaphore(1);//���߻�����
		Semaphore mutexWriter = new Semaphore(1);//д�߻�����
		
		Mode mode = Mode.READFIRST; //ģʽѡ��
		
		Lock lock = new ReentrantLock();
		
		
		ExecutorService pool = Executors.newCachedThreadPool();

		//3��ģʽ���ͬʱ���У������ͬ�����⣬�������벻���Ľ��
		//��û��Ƿֿ�����

		switch(mode)
		{
			case READFIRST:
				System.out.println("������:");
				pool.execute(new Reader1(readerNum,writerNum,lock));
				pool.execute(new Writer1(readerNum,writerNum,lock));
				pool.execute(new Reader1(readerNum,writerNum,lock));
				pool.execute(new Reader1(readerNum,writerNum,lock));
				break;
			case NORMAL:
				System.out.println("˳�����:");
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				pool.execute(new Writer2(readerNum,writerNum,mutexReader));
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				break;
			case WRITEFRIST:	
				System.out.println("д����:");
				pool.execute(new Reader3(readerNum,writerNum,mutexReader,mutexWriter));
				pool.execute(new Reader3(readerNum,writerNum,mutexReader,mutexWriter));
				pool.execute(new Reader3(readerNum,writerNum,mutexReader,mutexWriter));
				pool.execute(new Writer3(readerNum,writerNum,mutexReader,mutexWriter));
				break;
		}
		
		
		pool.shutdown();
		
	}
	
	
	static class Reader1 implements Runnable
	{
		Semaphore readerNum;
		Semaphore writerNum;
		Lock lock;
		
		public Reader1(Semaphore readerNum,Semaphore writerNum,Lock lock) {
			this.readerNum=readerNum;
			this.writerNum=writerNum;
			this.lock=lock;
		}
		
		@Override
		public void run() {
			
			synchronized (readerNum) {
				readerNum.release();//����+1
				if(readerNum.availablePermits()==1)
					try {
						writerNum.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			System.out.println("���ڶ�"+readerNum.availablePermits());
			try {
				Thread.sleep(1000);
				synchronized (readerNum) {
					readerNum.acquire() ;//�п��ܹ���
					if(readerNum.availablePermits()==0)
						writerNum.release();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static class Writer1 implements Runnable
	{		
		Semaphore readerNum;
		Semaphore writerNum;
		Lock lock;
		
		public Writer1(Semaphore readerNum,Semaphore writerNum,Lock lock) {
			this.readerNum=readerNum;
			this.writerNum=writerNum;
			this.lock=lock;
		}
		
		@Override
		public void run() {
			try {
				writerNum.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				System.out.println("����д");
				writerNum.release();
		}
	}
	
	
	
	
	
	
	
	//˳������������һ���ź���������
	static class Reader2 implements Runnable
	{
		Semaphore readerNum;
		Semaphore writerNum;
		Semaphore mutexReader;
		
		public Reader2(Semaphore readerNum,Semaphore writerNum,Semaphore lock) {
			this.readerNum=readerNum;
			this.writerNum=writerNum;
			this.mutexReader=lock;
		}
		
		@Override
		public void run() {

			try {
				mutexReader.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			synchronized (readerNum) {
				readerNum.release();//����+1
				if(readerNum.availablePermits()==1)
					try {
						writerNum.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			System.out.println("���ڶ�"+readerNum.availablePermits());
			try {
				Thread.sleep(1000);
				synchronized (readerNum) {
					readerNum.acquire() ;//�п��ܹ���
					if(readerNum.availablePermits()==0)
					{
						writerNum.release();
					}
				}
				mutexReader.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static class Writer2 implements Runnable
	{		
		Semaphore readerNum;
		Semaphore writerNum;
		Semaphore mutexReader;
		
		public Writer2(Semaphore readerNum,Semaphore writerNum,Semaphore lock) {
			this.readerNum=readerNum;
			this.writerNum=writerNum;
			this.mutexReader=lock;
		}
		
		@Override
		public void run() {
			try {
				mutexReader.acquire();
				writerNum.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				System.out.println("����д");
				writerNum.release();
				mutexReader.release();
		}
	}
	
	
	
	
	
	
	
	
	
	
	//д���Ȳ����������2���ź�����������д��
		static class Reader3 implements Runnable
		{
			Semaphore readerNum;
			Semaphore writerNum;
			Semaphore mutexReader;
			Semaphore mutexWriter;
			
			public Reader3(Semaphore readerNum,Semaphore writerNum,Semaphore lock,Semaphore mutexWriter) {
				this.readerNum=readerNum;
				this.writerNum=writerNum;
				this.mutexReader=lock;
				this.mutexWriter=mutexWriter;
			}
			
			@Override
			public void run() {

				try {
					mutexWriter.acquire();
					mutexReader.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				synchronized (readerNum) {
					readerNum.release();//����+1
					if(readerNum.availablePermits()==1)
						try {
							writerNum.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
				System.out.println("���ڶ�"+readerNum.availablePermits());
				try {
					Thread.sleep(1000);
					synchronized (readerNum) {
						readerNum.acquire() ;//�п��ܹ���
						if(readerNum.availablePermits()==0)
						{
							writerNum.release();
						}
					}
					mutexReader.release();
					mutexWriter.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		static class Writer3 implements Runnable
		{		
			Semaphore readerNum;
			Semaphore writerNum;
			Semaphore mutexReader;
			Semaphore mutexWriter;
			
			public Writer3(Semaphore readerNum,Semaphore writerNum,Semaphore lock,Semaphore mutexWriter) {
				this.readerNum=readerNum;
				this.writerNum=writerNum;
				this.mutexReader=lock;
				this.mutexWriter=mutexWriter;
			}
			
			@Override
			public void run() {
				try {
					mutexReader.acquire();
					writerNum.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					System.out.println("����д");
					writerNum.release();
					mutexReader.release();
			}
		}
}
