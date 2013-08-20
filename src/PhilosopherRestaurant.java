import PingPongManager.PingPongPlayer;


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
		// TODO Auto-generated method stub
		PhilosopherRestaurant thinkNEat = new PhilosopherRestaurant();
		thinkNEat.startDinner();
		thinkNEat.waitForPhilosophers();
	}
	
	private void waitForPhilosophers() {
		// TODO Auto-generated method stub
		
	}

	private void startDinner() {
		System.out.println("Dinner is starting!\n");
	}

	private PhilosopherRestaurant() {
		philosopherThreads = new Thread[Settings.NUMBER_OF_PHILOSOPHERS.getValue()];
		for(int i = 0 ; i < philosopherThreads.length; i++ )
			philosopherThreads[i] = new Thread(
					new Philosopher(i, Settings.MAX_BITES_FOR_PHILOSOPHERS.getValue()));
	}
	
	
	private class Philosopher implements Runnable{

		private int id;
		private int bitesLeft;
		
		public Philosopher(int idNumber, int maxBites){
			this.id = idNumber;
			this.bitesLeft = maxBites;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
		public int getId() {
			return id;
		}
		
		@Override
		public String toString() {
			return "Philosopher number:" + getId() + ". Is full:" + isFull();
		}

		private boolean isFull() {
			return bitesLeft > 0;
			
		}
		
	}

}