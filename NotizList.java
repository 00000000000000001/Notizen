import javax.swing.DefaultListModel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class NotizList extends JList<Notiz>{
	static private int maxID = 0;

	public NotizList(DefaultListModel<Notiz> listModel) {
		super(listModel);
	}

	public static int getMaxID() {
		return maxID;
	}
	
	
	
}
