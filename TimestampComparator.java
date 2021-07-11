import java.util.Comparator;

public class TimestampComparator<T> implements Comparator<Notiz> {

	@Override
	public int compare(Notiz n1, Notiz n2) {
		return n1.getTimestamp() - n2.getTimestamp();
	}



}
