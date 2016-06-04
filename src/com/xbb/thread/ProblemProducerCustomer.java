package com.xbb.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * ���������������⣬Ҳ�����޻�������
 * 
 * ��һ�ֱȽϳ����Ĳ������⣬�������ϵͳ����ͨ�ŵģ���Ϣ���й���
 * 
 * @author ���
 */
public class ProblemProducerCustomer {

	public static void main(String[] args) {
		WareHouse warehouse = new WareHouse();
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new Customer(50, warehouse));
		pool.execute(new Producer(20, warehouse));
		pool.execute(new Producer(30, warehouse));
		pool.execute(new Producer(20, warehouse));

		pool.shutdown();
	}

	public static class WareHouse {
		int num = 0;
		final int maxnum = 100;

		// ��ǰ�̲߳����е�ǰ���������Դ��ʱ�򣬵���obj.wait()����;����obj.notify()����������obj.notifyAll()�����������쳣
		public synchronized void product(int x) {
			while (num + x > maxnum) {
				try {
					System.out.println("�����������ȴ�����~");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			num = num + x;
			System.out.println("������~");
			notifyAll();
		}

		public synchronized void consume(int x) {
			while (num - x < 0) {
				try {
					System.out.println("�������㣬�ȴ�����~");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			num = num - x;
			System.out.println("������~");
			notifyAll();
		}

		public int getnum() {
			return num;
		}
	}

	public static class Producer implements Runnable {
		int speed;
		WareHouse warehouse;

		public Producer(int speed, WareHouse warehouse) {
			this.speed = speed;
			this.warehouse = warehouse;
		}

		@Override
		public void run() {
			warehouse.product(speed);
		}
	}

	public static class Customer implements Runnable {
		int speed;
		WareHouse warehouse;

		public Customer(int speed, WareHouse warehouse) {
			this.speed = speed;
			this.warehouse = warehouse;
		}
		@Override
		public void run() {
			warehouse.consume(speed);
		}

	}

}
