
public class PhilosopherRestaurant {

	/**
	 * @param args
	 */
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
		// TODO Auto-generated method stub
		
	}

	private PhilosopherRestaurant() {
		
	}
	
	
	private class Philosopher implements Runnable{

		private int number;
		private int timesEaten;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String toString() {
			return "Philosopher number:" + this.number + ". Times Eaten: " + this.timesEaten;
		}
		
	}

}
