package com.xbb.thread;

import java.util.concurrent.*;

/*
 * ��ʦ���⣬��ʦ����������Ϊһ������������ߡ����������⡣
 * ��ʦ���������֮�����ڣ�ʹ�����ź����Լ����������ơ�
 * ������ʹ���߳̿��Ի���ڷ����ź���
 * ˫�ź�������¼��ʦ״̬�����˿�����
 * 
 * @author ���
 */

public class ProblemBarber {

	public static void main(String[] args) {

		Semaphore signal = new Semaphore(10);// 10��������
		Semaphore sleep = new Semaphore(0);// ��ʼ��ʦ��˯���������˯���൱�ڹ��𣬶�����ִ��˯����
		Thread barber = new Thread(new Barber(signal, sleep));

		barber.start();
		try {
			Thread.sleep(1000);

			while (true) {
				Thread customer = new Thread(new Customer(signal, sleep));
				customer.start();
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class Barber implements Runnable {
		Semaphore signal;
		Integer num;
		Semaphore sleep;

		public Barber(Semaphore signal, Semaphore sleep) {
			this.signal = signal;
			this.sleep = sleep;
		}

		@Override
		public void run() {
			synchronized (this) {
				try {
					sleep.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				while (signal.availablePermits() > 0) {
					try {
						Thread.sleep(1000);// 1s ����һ���˿�
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1��һ���˿����귢��");
					signal.release();
				}
			}
		}
	}

	public static class Customer implements Runnable {
		Semaphore signal;
		Semaphore sleep;

		public Customer(Semaphore signal, Semaphore sleep) {
			this.signal = signal;
			this.sleep = sleep;
		}
		@Override
		public void run() {

			synchronized (this) {
				if (signal.tryAcquire())// ����ź���
				{
					System.out.println("2������һ���˿ͣ�" + signal.availablePermits());
					if (signal.availablePermits() == 9)
						sleep.release();
				} else {
					System.out.println("2���ȴ����β���,�˿��뿪��");
				}

			}
		}
	}
}
