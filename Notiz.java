

public class Notiz {
	private int id;
	private int timestamp;
	private Datum datum;
	private Uhrzeit uhrzeit;
	private String text;

	public Notiz(int id, int timestamp, Datum datum, Uhrzeit uhrzeit, String text) {
		this.id = id;
		this.timestamp = timestamp;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.text = text;
	}

	@Override
	public String toString() {
		String tag = String.valueOf(datum.getTag());
		String monat = String.valueOf(datum.getMonat());
		String jahr = String.valueOf(datum.getJahr());
		String datum = tag + "." + monat + "." + jahr.substring(2, 4);

		String stunden = String.valueOf(uhrzeit.getStunden());
		String minuten = String.valueOf(uhrzeit.getMinuten());
		String uhrzeit = stunden + ":" + ((minuten.length() == 1) ? "0" + minuten : minuten);

		return datum + ", " + uhrzeit + ": " + text;
	}

//	private String firstLine(String text) {
//		String line = "";
//		int i = 0;
//		while (i < text.length() - 1 && text.charAt(i) != '\n') {
//			line += text.charAt(i++);
//		}
//		return line;
//	}

	public int getId() {
		return id;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Datum getDatum() {
		return datum;
	}

	public Uhrzeit getUhrzeit() {
		return uhrzeit;
	}

}
