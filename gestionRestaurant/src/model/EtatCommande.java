package model;

public interface EtatCommande {
    void operationPaye();
    void operationNouvelle();
    void operationEnCours();
    void operationLivree();
    void operationPret();
    void doAction();
    String getEtat();
}
