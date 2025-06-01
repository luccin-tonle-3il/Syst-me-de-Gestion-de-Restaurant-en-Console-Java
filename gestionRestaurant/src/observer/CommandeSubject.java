package observer;

public interface CommandeSubject {
	void attach(CommandeObserver observer);
    void detach(CommandeObserver observer);
    void notifyObservers();
    void setEtat(String etat);
}
