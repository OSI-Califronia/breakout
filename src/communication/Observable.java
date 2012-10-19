package communication;

import java.util.List;
import java.util.Vector;

public class Observable {
	
	protected List<IObserver> observerList = new Vector<IObserver>();
	
	public Observable() {
		super();
	}

	public void addObserver(IObserver obs) {
		observerList.add(obs);
	}
	
	public void removeObserver(IObserver obs) {
		observerList.remove(obs);
	}
	
	public void notifyRepaintPlayGrid() {		
		for (IObserver obs : observerList) {
			obs.updateRepaintPlayGrid();
		}		
	}
	
}
