import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JList;

abstract public class NotizUtils {
	static private String path = getProgramPath() + "/Notizen/";

	public static String getProgramPath() {
		String currentdir = "";
		try {
			currentdir = System.getProperty("user.dir");
			currentdir = currentdir.replace("\\", "/");
		} catch (Exception e) {
			System.out.println(e);
		}
		return currentdir;

	}

	// Bsp.: 1 -> 0000000001
	static public String idToFilename(int id) {
		String res;
		res = String.valueOf(id);
		while (res.length() < 10) {
			res = "0" + res;
		}
		return res;
	}

	// Bsp.: 0000000001 -> 1
	static public int filenameToID(String filename) {
		return Integer.parseInt(filename);
	}

	// Alle Dateien im Ordner Notizen
	static public String[] pathNames() {
		String[] res;
		File f = new File(path);
		res = f.list();
		return res;
	}

	static public void writeIni(Notiz n) throws IOException {
		String fileName = String.valueOf(path + idToFilename(n.getId()));
		String selection = String.valueOf(n.getDatum()) + String.valueOf(n.getUhrzeit());
		String timestamp = String.valueOf(n.getTimestamp());
		String wert = n.getText();
		String ini = "[" + selection + "]\nTimestamp=" + timestamp + "\nText=" + wert;
		if (wert != "" && wert != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
				writer.write(ini);

				writer.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	static public Notiz readIni(String file) throws IOException {
		int id;
		int tag = 0;
		int monat = 0;
		int jahr = 0;
		int stunden = 0;
		int minuten = 0;
		int sekunden = 0;
		int timestamp = 0;
		String text = "";

		BufferedReader reader = new BufferedReader(new FileReader(path + file));

		String line = reader.readLine();
		// Datum
		tag = Integer.parseInt(line.substring(1, 3));
		monat = Integer.parseInt(line.substring(3, 5));
		jahr = Integer.parseInt(line.substring(5, 9));
		// Uhrzeit
		stunden = Integer.parseInt(line.substring(9, 11));
		minuten = Integer.parseInt(line.substring(11, 13));
		sekunden = Integer.parseInt(line.substring(13, 15));
		// Timestamp
		line = reader.readLine();
		timestamp = Integer.parseInt(line.substring(line.indexOf('=') + 1, line.length()));
		// Text
		line = reader.readLine();
		text = line.substring(line.indexOf('=') + 1, line.length());

		line = reader.readLine();
		while (line != null) {
			text += "\n" + line;
			line = reader.readLine();
		}
		id = NotizUtils.filenameToID(file);
		reader.close();
		// maximale ID ermitteln
		if (id > NotizDefaultListModel.maxID) {
			NotizDefaultListModel.maxID = id;
		}
		return new Notiz(id, timestamp, new Datum(tag, monat, jahr), new Uhrzeit(stunden, minuten, sekunden), text);
	}

	public static Datum stringToDatum(String format) {
		int tag;
		int monat;
		int jahr;

		tag = Integer.parseInt(format.substring(0, 2));
		monat = Integer.parseInt(format.substring(2, 4));
		jahr = Integer.parseInt(format.substring(4, 8));
		Datum ret = new Datum(tag, monat, jahr);

		return ret;
	}

	public static Uhrzeit stringToUhrzeit(String format) {
		int stunden;
		int minuten;
		int sekunden;

		stunden = Integer.parseInt(format.substring(0, 2));
		minuten = Integer.parseInt(format.substring(2, 4));
		sekunden = Integer.parseInt(format.substring(4, 6));
		Uhrzeit ret = new Uhrzeit(stunden, minuten, sekunden);

		return ret;
	}

	public static void saveAll(JList<Notiz> list) {
		for (int i = 0; i < list.getModel().getSize(); ++i) {
			try {

				writeIni(list.getModel().getElementAt(i));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteFile(String filename) {
		File file = new File(path + filename);
		if (file.delete()) {
			System.out.println("Deleted the file: " + file.getName());
		} else {
			System.out.println("Failed to delete the file.");
		}
	}

	public static void editIni(Notiz n) {
		String fileName = String.valueOf(path + idToFilename(n.getId()));
		String selection = String.valueOf(n.getDatum()) + String.valueOf(n.getUhrzeit());
		String timestamp = String.valueOf((int) System.currentTimeMillis());
		String wert = n.getText();
		String ini = "[" + selection + "]\nTimestamp=" + timestamp + "\nText=" + wert;
		if (wert != "" && wert != null) {
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(fileName));
				writer.write(ini);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void moveUp(JList<Notiz> list, Notiz elementAt) {
		int index = list.getSelectedIndex();
		DefaultListModel<Notiz> model = (DefaultListModel<Notiz>) list.getModel();

		for (int i = index; i > 0; --i) {
			model.set(i, model.getElementAt(i - 1));
		}
		model.set(0, elementAt);
	}

	public static NotizDefaultListModel suche(NotizDefaultListModel listModel, String text) {
		NotizDefaultListModel res = new NotizDefaultListModel();
		for (int i = 0; i < listModel.size(); ++i) {
			if (listModel.getElementAt(i).getText().toLowerCase().contains(text.toLowerCase())) {
				res.addElement(listModel.getElementAt(i));
			}
		}
		return res;
	}

}
