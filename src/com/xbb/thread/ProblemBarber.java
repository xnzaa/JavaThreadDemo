package com.xbb.thread;

import java.util.concurrent.*;

/*
 * 理发师问题，理发师问题可以理解为一种特殊的生产者、消费者问题。
 * 理发师问题的特殊之处在于，使用了信号量以及互斥量机制。
 * 互斥量使多线程可以互斥第访问信号量
 * 双信号量，记录理发师状态，及顾客数量
 * 
 * @author 徐兵
 */

public class ProblemBarber {

	public static void main(String[] args) {

		Semaphore signal = new Semaphore(10);// 10个空椅子
		Semaphore sleep = new Semaphore(0);// 初始理发师在睡觉（这里的睡觉相当于挂起，而不是执行睡觉）
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
						Thread.sleep(1000);// 1s 服务一名顾客
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1：一名顾客理完发！");
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
				if (signal.tryAcquire())// 获得信号量
				{
					System.out.println("2：新添一名顾客！" + signal.availablePermits());
					if (signal.availablePermits() == 9)
						sleep.release();
				} else {
					System.out.println("2：等待座椅不够,顾客离开！");
				}

			}
		}
	}
}
