import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
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
	
	private static enum Action {
		PICKUP, PUTDOWN;
	}
	
	private static enum Side {
		LEFT, RIGHT;

		public static Side getRandomSide() {
			int side = (int) Math.round(Math.random());
			if( side == 0 ) 
				return LEFT;
			else
				return RIGHT;
		}

		public static Side getOppositeSide(Side firstSide) {
			if( firstSide == LEFT ) 
				return RIGHT;
			else
				return LEFT;
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
		
		private Lock used;
		
		private final int id;
		
		public Chopstick(int id) {
			this.id = id;
			 used = new ReentrantLock();
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

		@SuppressWarnings("unused")
		public int getId() {
			return id;
		}
		
	}
	
	
	private class Philosopher implements Runnable{

		private int id;
		private int bitesLeft;
		
		Map<Side, Chopstick> chopsticks;
		
		public Philosopher(int idNumber, int maxBites, Chopstick leftChopstick, Chopstick rightChopstick){
			this.id = idNumber;
			this.bitesLeft = maxBites;
			chopsticks = Collections.synchronizedMap( new EnumMap<Side, Chopstick>(Side.class));
			chopsticks.put(Side.LEFT, leftChopstick);
			chopsticks.put(Side.RIGHT, rightChopstick);
		}
		
		private void announce( Enum<Action> action, Enum<Side> side){ 
			System.out.println("Philosopher " + getId() + " " + 
					( action.equals(Action.PICKUP) ? "picks up" : "puts down" ) + " " + 
					( side.equals(Side.LEFT) ? "left" : "right" ) + " chopstick.");
		}

		@Override
		public void run() {
			while( ! isFull() ) {
				
				Side firstSide = Side.getRandomSide();
				Side secondSide = Side.getOppositeSide(firstSide);
				
				if( chopsticks.get(firstSide).pickUp() ) {
					announce(Action.PICKUP,firstSide);
					if ( chopsticks.get(secondSide).pickUp() ) {
						announce(Action.PICKUP,secondSide);
						eat();
						chopsticks.get(secondSide).putDown();
						announce(Action.PUTDOWN,secondSide);
						chopsticks.get(firstSide).putDown();
						announce(Action.PUTDOWN,firstSide);
					} else {
						chopsticks.get(firstSide).putDown();
						announce(Action.PUTDOWN,firstSide);


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
