package Lists;

	public class Car {
		
		private String name;
		private String marka;
		private int volume;
		private float speed;
		
		public Car(String name, String marka, int volume) {
			super();
			this.name = name;
			this.marka = marka;
			this.volume = volume;
		}
		
		@Override
		public String toString() {
			return "Car [name=" + name + ", marka=" + marka + ", volume="
					+ volume + ", speed=" + getSpeed() + "]";
		}

		public Car(String name, String marka, int volume, float speed) {
			super();
			this.name = name;
			this.marka = marka;
			this.volume = volume;
			this.setSpeed(speed);
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMarka() {
			return marka;
		}
		public void setMarka(String marka) {
			this.marka = marka;
		}
		public int getVolume() {
			return volume;
		}
		public void setVolume(int volume) {
			this.volume = volume;
		}

		public float getSpeed() {
			return speed;
		}

		public void setSpeed(float speed) {
			this.speed = speed;
		}
	}

