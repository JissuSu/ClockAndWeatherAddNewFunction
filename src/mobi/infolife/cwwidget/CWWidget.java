package mobi.infolife.cwwidget;

public interface CWWidget {
	public void updateView() throws CWRemoteException;
	public void loadData() throws CWRemoteException;
	public void loadPreferences();
}
