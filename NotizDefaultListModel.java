import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class NotizDefaultListModel extends DefaultListModel<Notiz>{
	static int maxID = 0;
	
	public void add(int index, Notiz notiz) {
		super.add(index, notiz);
	}
}
