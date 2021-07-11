import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import static javax.swing.ScrollPaneConstants.*;

@SuppressWarnings("serial")
public class MainFrame extends javax.swing.JFrame {
	NotizDefaultListModel listModel = new NotizDefaultListModel();
	NotizDefaultListModel sucheModel = new NotizDefaultListModel();
	JList<Notiz> list = new NotizList(listModel);
	JTextArea text = new JTextArea();
	JScrollPane listScroller = new JScrollPane(list);
	JScrollPane textScroller = new JScrollPane(text);
	JMenuBar bar = new JMenuBar();
	JButton buttonAdd = new JButton("Neu");
	JPanel panelLinks = new JPanel(new BorderLayout());
	JPanel panelRechts = new JPanel(new BorderLayout());
	JTextField suche = new JTextField("suchen");
	Notiz cursor = null;

	public MainFrame() {
		super();

		addComponents();

		addListItems();

		listScroller.setPreferredSize(new Dimension(200, 0));
		listScroller.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		text.setLineWrap(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setTitle("Notizen");
		setSize(700, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		if (list.getModel().getSize() > 0) {
			list.setSelectedIndex(0);
		}
		text.requestFocus();
	}

	private void addListItems() {
		// Notizen laden
		String[] files = NotizUtils.pathNames();
		if (files != null) {
			Notiz[] notizen = new Notiz[files.length];
			for (int i = 0; i < files.length; ++i) {
				try {
					notizen[i] = NotizUtils.readIni(files[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// sortieren nach Timestamp
			Arrays.sort(notizen, new TimestampComparator<>());
			// Liste leeren
			listModel.clear();

			for (Notiz n : notizen) {
				listModel.add(0, n);
			}
		}
	}

	private void addComponents() {
		panelLinks.add(listScroller, BorderLayout.CENTER);
		panelRechts.add(textScroller, BorderLayout.CENTER);
		bar.add(buttonAdd);
		bar.add(suche);

		add(bar, BorderLayout.PAGE_START);
		add(panelLinks, BorderLayout.LINE_START);
		add(panelRechts, BorderLayout.CENTER);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				NotizDefaultListModel model = (NotizDefaultListModel) list.getModel();
				if (list.getSelectedIndex() != -1) {
					if (cursor == null || model.getElementAt(list.getSelectedIndex()).getId() != cursor.getId()) {
						text.setText(model.get(list.getSelectedIndex()).getText());
						text.setCaretPosition(0);
						cursor = model.getElementAt(list.getSelectedIndex());
					}
					text.setEnabled(true);
				} else {
					text.setText("");
					text.setEnabled(false);
				}

			}

		});

		list.addMouseListener(new ListMouseListener(list));

		text.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() >= 0 && e.getKeyChar() <= 255) {
					NotizDefaultListModel model = (NotizDefaultListModel) list.getModel();
					// speichern
					// 1.: Speicher
					model.get(list.getSelectedIndex()).setText(text.getText());
					// 2.: HD
					NotizUtils.editIni(model.getElementAt(list.getSelectedIndex()));
					// aktuelle Notiz nach oben verschieben
					NotizUtils.moveUp(list, model.getElementAt(list.getSelectedIndex()));
					// oberste Notiz wählen
					list.setSelectedIndex(0); //wichtig
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addListItems();
				// neue Notiz anlegen
				// - speicher
				// Datum
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter df;
				df = DateTimeFormatter.BASIC_ISO_DATE; // 20160131
				df = DateTimeFormatter.ofPattern("ddMMyyyy"); // 31.01.2016 20:07
				Datum datum = NotizUtils.stringToDatum(now.format(df));
				df = DateTimeFormatter.ofPattern("kkmmss"); // 31.01.2016 20:07
				Uhrzeit uhrzeit = NotizUtils.stringToUhrzeit(now.format(df));
				@SuppressWarnings("static-access")
				Notiz neu = new Notiz(++listModel.maxID, (int) System.currentTimeMillis(), datum, uhrzeit, "");
				listModel.add(0, neu);
				// - hd
				try {
					NotizUtils.writeIni(neu);
				} catch (IOException e) {
					e.printStackTrace();
				}
				list.setSelectedIndex(0);
				text.requestFocus();
			}

		});

		suche.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				cursor = null;
				if (suche.getText().equals("")) {
					// Liste neu laden
					addListItems();
					// Liste anzeigen
					list.setModel(listModel);
				} else {
					// Ergebnisliste erstellen
					sucheModel = NotizUtils.suche(listModel, suche.getText());
					// Ergebnisliste anzeigen
					list.setModel(sucheModel);
				}
				if (sucheModel.size() > 0) {
					list.setSelectedIndex(0);
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
	}

	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			MainFrame frame = new MainFrame();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
