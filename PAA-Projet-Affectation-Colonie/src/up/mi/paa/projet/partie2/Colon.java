package up.mi.paa.projet.partie2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Un colon est un membre d'une colonie caractérisé par un nom et une liste de préférences.
 */
public class Colon {
	private String nom;
	
	/**
	 * Préférences des ressources pour l'instance courante de {@code Colon}.
	 * 
	 * La liste est à traiter comme un ordre de priorité descendant
	 * (l'élément à l'index 0 doit être le plus convoité par le {@code Colon}.
	 */
	private ArrayList<Ressource> preferences;
	
	public Colon(String nom) {
		this.nom = nom;
		this.preferences = new ArrayList<Ressource>();
	}
	
	/**
	 * Vérifie si le colon admet bien n préférences uniques
	 * 
	 * @param n le nombre de préférences (taille de la colonie) conditionnant le test
	 * @return vrai si et seulement si le colon admet n préférences unique
	 */
	public boolean admetPreferences(int n) {
		return new HashSet<Ressource>(preferences).size() == n;
	}
	
	public String getNom() {
		return nom;
	}
	public void setPreferences(ArrayList<Ressource> preferences) {
		this.preferences = preferences;
	}
	public ArrayList<Ressource> getPreferences() {
		return preferences;
	}
	public boolean equals(Object o) {
		return (o instanceof Colon)?((Colon) o).getNom().equals(this.nom):false;
	}
	
	@Override
	public String toString() {
		return nom;
	}
}
