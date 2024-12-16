package up.mi.paa.projet.partie2;

/**
 * Une ressource peut être affectée à un et un seul colon dans une colonie. Elle est caractérisée
 * par un nom.
 */
public class Ressource {

	private String nom;
	
	public Ressource(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String toString() {
		return nom;
	}
}
