package dfgfdg;

/*//An incorrect implementation of a producer and consumer. 
class Q {
	int n;

	synchronized int get() {
		System.out.println("Got: " + n);
		return n;
	}

	synchronized void put(int n) {
		this.n = n;
		System.out.println("Put: " + n);
	}
}*/

class Q {
	int n;
	boolean valueSet = false;

	synchronized int get() {
		if (!valueSet)
			try {
				System.out.println("get waiting");
				wait();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
			}
		System.out.println("Got: " + n);
		valueSet = false;
		notify();
		System.out.println("get notify");
		return n;
	}

	synchronized void put(int n) {
		if (valueSet)
			try {
				System.out.println("put waiting");
				wait();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
			}
		this.n = n;
		valueSet = true;
		System.out.println("Put: " + n);
		System.out.println("put notify");
		notify();
	}
}

class Producer implements Runnable {
	Q q;

	Producer(Q q) {
		this.q = q;
		new Thread(this, "Producer").start();
	}

	public void run() {
		int i = 0;
		while (true) {
			q.put(i++);
			if (i>5) {
				break;
			}
		}
	}
}

class Consumer implements Runnable {
	Q q;

	Consumer(Q q) {
		this.q = q;
		new Thread(this, "Consumer").start();
	}

	public void run() {
		while (true) {
			q.get();
		}
	}
}

public class PC {
	public static void main(String args[]) {
		Q q = new Q();
		new Producer(q);
		new Consumer(q);
		System.out.println("Press Control-C to stop.");
	}
}