import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PhilosopherRestaurant {

	
	/* 
	 * Settings for this restaurant
	 * NUMBER_OF_PHILOSOPHERS - the number of philosophers in this simulation
	 * MAX_BITES_FOR_PHILOSOPHERS - how many times philosophers want to eat
	 * 
	 */
	private enum Settings {
		NUMBER_OF_PHILOSOPHERS(5),		MAX_BITES_FOR_PHILOSOPHERS(5);
		private final int value;
		private Settings(int value) {
            this.value = value;
        }
		public int getValue() {
            return value;
        }
	}
	
	private Thread[] philosopherThreads;
	
	public static void main(String[] args) {
		PhilosopherRestaurant thinkNEat = newPhilosopherRestaurant();
		thinkNEat.startDinner();
		thinkNEat.waitForPhilosophers();
	}
	
	// Static Factory Method
	public static PhilosopherRestaurant newPhilosopherRestaurant() {
		return new PhilosopherRestaurant();
	}
	
	private void waitForPhilosophers() {
		for( Thread t : philosopherThreads )
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		System.out.println("Dinner is Over!");
		
	}

	private void startDinner() {
		System.out.println("Dinner is starting!\n");
		for( Thread t : philosopherThreads ) 
			t.start();
			
	}

	private PhilosopherRestaurant() {
		philosopherThreads = new Thread[Settings.NUMBER_OF_PHILOSOPHERS.getValue()];
		Chopstick[] forks = new Chopstick[Settings.NUMBER_OF_PHILOSOPHERS.getValue()];
		for(int i = 0 ; i < forks.length ; i ++ )
			forks[i] = new Chopstick(i);
		for(int i = 0 ; i < philosopherThreads.length; i++ )
			philosopherThreads[i] = new Thread(
					new Philosopher(i, Settings.MAX_BITES_FOR_PHILOSOPHERS.getValue(), 
							forks[i], forks[(i+1)%Settings.NUMBER_OF_PHILOSOPHERS.getValue()]));
	}
	
	private class Chopstick {
		
		public Lock used = new ReentrantLock();
		
		private final int id;
		
		public Chopstick(int id) {
			this.id = id;
		}
		
		public boolean pickUp() {
			try {
				return used.tryLock(500, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public void putDown() {
			used.unlock();
		}
		
	}
	
	
	private class Philosopher implements Runnable{

		private int id;
		private int bitesLeft;
		private Chopstick rightChopstick;
		private Chopstick leftChopstick;
		
		
		public Philosopher(int idNumber, int maxBites, Chopstick leftChopstick, Chopstick rightChopstick){
			this.id = idNumber;
			this.bitesLeft = maxBites;
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}
		
		private void announcePickup(String side){ 
			System.out.println("Philosopher " + getId() + " picks up " + side + " chopstick.");
		}

		@Override
		public void run() {
			while( ! isFull() ) {
				if( rightChopstick.pickUp() ) {
					announcePickup("right");
					if ( leftChopstick.pickUp() ) {
						announcePickup("left");
						eat();
						leftChopstick.putDown();
						rightChopstick.putDown();
					} else {
						rightChopstick.putDown();

					}
				}
			}
		}
		
		private void eat() {
			System.out.println("Philosopher " + getId() + " eats.");
			bitesLeft--;
		}
		
		public int getId() {
			return id;
		}
		
		@Override
		public String toString() {
			return "Philosopher number:" + getId() + ". Is full:" + isFull();
		}

		private boolean isFull() {
			return bitesLeft <= 0;
			
		}
		
	}

}
