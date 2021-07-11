
public class Uhrzeit {
	private int stunden; // 1-24
	private int minuten; // 1-59
	private int sekunden; // 1-59
	public Uhrzeit(int stunden, int minuten, int sekunden) {
		super();
		this.stunden = stunden;
		this.minuten = minuten;
		this.sekunden = sekunden;
	}
	@Override
	public String toString() {
		return String.valueOf((stunden < 10) ? "0" + stunden : stunden) + String.valueOf((minuten < 10) ? "0" + minuten : minuten)
		+ String.valueOf((sekunden < 10) ? "0" + sekunden : sekunden);
	}
	public int getStunden() {
		return stunden;
	}
	public int getMinuten() {
		return minuten;
	}
	public int getSekunden() {
		return sekunden;
	}
	
	

}
