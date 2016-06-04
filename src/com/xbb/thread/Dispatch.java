package com.xbb.thread;


/*
 * �߳�״̬�ı亯����
 * wait()/��ǰ�̵߳ȴ���ֱ���������̵߳���notify*()
 * jion()/�������̼߳ӵ������߳��У���ͣ�����̵߳�ִ�У�ֱ�������߳�ִ�����
 * yeild()/��̬����  ��ͣ��ǰ�߳�ִ�У�������ѡ��ִ�е��߳�
 * notify()/����һ����̨�߳�
 * notifyAll()/�������к�̨�ȴ����߳�
 * sleep()/��̬��������ǰ�߳���ʱn����λ��ms��
 * 
 * 
 * �̵߳�6��״̬
 * Thread.State.BLOCKED;	����
 * Thread.State.NEW;		�½�
 * Thread.State.RUNNABLE;	���л�ȴ�����
 * Thread.State.TERMINATED;	�������
 * Thread.State.TIMED_WAITING;	��ʱ�ȴ�
 * Thread.State.WAITING;	�ȴ�
 * 
 * @author ���
 */


public class Dispatch {

	
	public static void main(String[] args)
	{
		Dispatch dis=new Dispatch();
		
		Thread t1= new Thread(new  MyRunnable(dis),"�߳�1");
		Thread t2= new Thread(new  MyThread(dis),"�߳�2");
		t1.start();
		t2.start();
//		t2.setDaemon(true);//�ػ��̣߳����� ��;���������ӣ�JVM���������ա��ڴ������̶߳����ػ��߳�
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
			
			System.out.println(Thread.currentThread().getName()+" ����");
			try {
				Thread.sleep(2000);//��λms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" is Running");

			synchronized(dis){dis.notifyAll();}
			System.out.println(Thread.currentThread().getName()+" ���������߳�");
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
			
			System.out.println(Thread.currentThread().getName()+" ����");
			try {
				synchronized(dis){dis.wait();}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" ������");
			Thread.yield();
			System.out.println(Thread.currentThread().getName()+" ���±�����");
			
		}
	}
	
}
