package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Commande;
import dao.CommandeDao;
import observer.CommandeSubject;
import observer.CommandeObserver;

public class CommandeUpdateThread extends Thread implements CommandeSubject {
    private Commande commande;
    private CommandeDao commandeDao;
    private boolean running;
    private static final long DELAY = 20000; // 20 secondes
    private List<CommandeObserver> observers;
    private String etatCourant;

    public CommandeUpdateThread(Commande commande) {
        this.commande = commande;
        this.commandeDao = new CommandeDao();
        this.running = true;
        this.observers = new ArrayList<>();
        this.etatCourant = commande.getEtat().getEtat();
    }

    @Override
    public void attach(CommandeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(CommandeObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (CommandeObserver observer : observers) {
            observer.update(etatCourant);
        }
    }

    @Override
    public void setEtat(String etat) {
        this.etatCourant = etat;
        notifyObservers();
    }

    @Override
    public void run() {
        try {
            // Liste des états dans l'ordre
            String[] etats = {"nouvelle", "en_cours", "pret", "livree"};
            int index = 0;

            while (running && index < etats.length) {
                // Mettre à jour l'état de la commande
                commandeDao.mettreAJourEtatCommande(commande.getCommandeId(), etats[index]);
                
                // Mettre à jour l'état de la commande dans la classe
                switch (etats[index]) {
                    case "nouvelle":
                        commande.passerEnNouvelle();
                        break;
                    case "en_cours":
                        commande.passerEnEnCours();
                        break;
                    case "pret":
                        commande.passerEnPret();
                        break;
                    case "livree":
                        commande.passerEnLivree();
                        break;
                }

                // Notifier les observateurs
                notifyObservers();
                
                // Attendre 20 secondes
                Thread.sleep(DELAY);
                
                // Passer à l'état suivant
                index++;
            }
            
            // Si la commande est arrivée à "livree", on arrête le thread
            running = false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        running = false;
    }
}    }
}
