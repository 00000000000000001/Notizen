
public class Datum {
	private int tag; // 1-31
	private int monat; // 1-12
	private int jahr; // 1-max

	public Datum(int tag, int monat, int jahr) {
		super();
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
	}

	@Override
	public String toString() {
		return String.valueOf((tag < 10) ? "0" + tag : tag) + String.valueOf((monat < 10) ? "0" + monat : monat)
				+ String.valueOf((jahr < 10) ? "0" + jahr : jahr);
	}

	public int getTag() {
		return tag;
	}

	public int getMonat() {
		return monat;
	}

	public int getJahr() {
		return jahr;
	}
	
	

}
