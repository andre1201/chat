package Lists;

	public class Car {
		
		private String name;
		private String marka;
		private int volume;
		
		public Car(String name, String marka, int volume) {
			super();
			this.name = name;
			this.marka = marka;
			this.volume = volume;
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
	}

