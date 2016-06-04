package com.xbb.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 
 * 读者-写着模型，分3种情况：
 * 1：读者优先  （读操作占有写锁，使得后续的写操作挂起，读操作优先执行）
 * 
 * 2：顺序操作 （写操作占有读锁，使得所有在写操作后面的线程均在写操作后执行）
 * 
 * 3：写着优先 （增加互斥量write_mutex，使得读操作的等待队列中，最多只有一个获得读锁，
 *    从而确保写操作优先获得读锁，延迟其他读操作的进行，确保写操作优先！是最常见的模型）
 * 
 * @author 徐兵
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
		Semaphore readerNum = new Semaphore(0);//信号量：读者总数
		Semaphore writerNum = new Semaphore(1);//信号量：写着总数
		Semaphore mutexReader = new Semaphore(1);//读者互斥锁
		Semaphore mutexWriter = new Semaphore(1);//写者互斥锁
		
		Mode mode = Mode.READFIRST; //模式选择
		
		Lock lock = new ReentrantLock();
		
		
		ExecutorService pool = Executors.newCachedThreadPool();

		//3种模式如果同时运行，因存在同步问题，会有意想不到的结果
		//最好还是分开运行

		switch(mode)
		{
			case READFIRST:
				System.out.println("读优先:");
				pool.execute(new Reader1(readerNum,writerNum,lock));
				pool.execute(new Writer1(readerNum,writerNum,lock));
				pool.execute(new Reader1(readerNum,writerNum,lock));
				pool.execute(new Reader1(readerNum,writerNum,lock));
				break;
			case NORMAL:
				System.out.println("顺序操作:");
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				pool.execute(new Writer2(readerNum,writerNum,mutexReader));
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				pool.execute(new Reader2(readerNum,writerNum,mutexReader));
				break;
			case WRITEFRIST:	
				System.out.println("写优先:");
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
				readerNum.release();//读者+1
				if(readerNum.availablePermits()==1)
					try {
						writerNum.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			System.out.println("我在读"+readerNum.availablePermits());
			try {
				Thread.sleep(1000);
				synchronized (readerNum) {
					readerNum.acquire() ;//有可能挂起
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
				System.out.println("我在写");
				writerNum.release();
		}
	}
	
	
	
	
	
	
	
	//顺序操作，添加了一个信号量：读锁
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
				readerNum.release();//读者+1
				if(readerNum.availablePermits()==1)
					try {
						writerNum.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			System.out.println("我在读"+readerNum.availablePermits());
			try {
				Thread.sleep(1000);
				synchronized (readerNum) {
					readerNum.acquire() ;//有可能挂起
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
				System.out.println("我在写");
				writerNum.release();
				mutexReader.release();
		}
	}
	
	
	
	
	
	
	
	
	
	
	//写优先操作，添加了2个信号量：读锁，写锁
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
					readerNum.release();//读者+1
					if(readerNum.availablePermits()==1)
						try {
							writerNum.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
				System.out.println("我在读"+readerNum.availablePermits());
				try {
					Thread.sleep(1000);
					synchronized (readerNum) {
						readerNum.acquire() ;//有可能挂起
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
					System.out.println("我在写");
					writerNum.release();
					mutexReader.release();
			}
		}
}
