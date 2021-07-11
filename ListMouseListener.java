import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ListMouseListener extends MouseAdapter {

	class PopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			if (actionEvent.getActionCommand().equals("Löschen")) {
				// löschen
				DefaultListModel<Notiz> model = (DefaultListModel<Notiz>) list.getModel();
				// - HD
 				NotizUtils.deleteFile(NotizUtils.idToFilename(model.getElementAt(list.getSelectedIndex()).getId()));
				// - Speicher
				int selectedIndex = list.getSelectedIndex();
				if (selectedIndex != -1) {
				    model.remove(selectedIndex);
				}
			}
		}
	}

	private JList<Notiz> list;

	public ListMouseListener(JList<Notiz> list) {
		this.list = list;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		if (!e.isPopupTrigger())
			return;

		int clicked = list.locationToIndex(e.getPoint());
		if (clicked != -1 && list.getCellBounds(clicked, clicked).contains(e.getPoint())) {
			list.setSelectedIndex(clicked);

			JPopupMenu menu = new JPopupMenu();

		    JMenuItem loeschenMenuItem = new JMenuItem("Löschen");
		    loeschenMenuItem.addActionListener(new PopupActionListener());
		    menu.add(loeschenMenuItem);
			menu.show(list, e.getX(), e.getY());
		}
	}
}