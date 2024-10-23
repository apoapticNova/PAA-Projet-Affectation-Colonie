package up.mi.paa.projet.partie1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Implementation d'une colonie spatiale. La classe {@code Colonie} comprend
 * un graphe des relations, une liste d'affectations, une classe interne {@code Colon}
 * et les méthodes nécessaires à la gestion manuelle de la colonie.
 */
public class Colonie {

	private int taille;
	
	/**
	 * {@link HashMap} contenant les instances de {@link Colon} associés 
	 * à cette instance de {@code Colonie} en clés et un {@link HashSet} de
	 * {@code Colon} en valeurs désignant les relations "ne s'aiment pas" entre la clé
	 * et tout élément de la valeur.
	 * 
	 * <p>Cette implémentation du graphe relationnel (liste d'adjacence) 
	 * permet un temps constant en lecture et en écriture.
	 */
	private HashMap<Colon, HashSet<Colon>> relations;
	
	/**
	 * Affectation des ressources pour la {@code Colonie} courante.
	 * Chaque {@code Colon} reçoit la ressource associée à son index dans la liste.
	 * 
	 * <p>(L'implémentation avec ArrayList requiert une garantie de l'unicité de
	 * chaque élément au moment de l'affectation des ressources, une implémentation
	 * avec {@code TreeSet} pour forcer l'unicité serait possible mais peu pertinente)
	 */
	private ArrayList<Colon> affectation;
	
	/**
	 * Instancie une nouvelle {@code Colonie} avec la taille spécifiée.
	 * Si il y a au plus 26 colons, ils seront chacuns nommés par une lettre
	 * de l'alphabet.
	 * 
	 * @param	taille la taille de la colonie
	 * @throws	IllegalArgumentException si la taille est négative
	 */
	public Colonie(int taille) throws IllegalArgumentException {
		if (taille < 0) {
			throw new IllegalArgumentException("Taille initiale de colonie invalide : " + taille);
		}
		
		this.taille = taille;
		this.affectation = new ArrayList<Colon>(); //Colonie initialisée sans affectation
		
		//initialisation du graphe des relations (liste d'adjacence)
		this.relations = new HashMap<Colon, HashSet<Colon>>();
		if (this.taille <= 26) {
			creerColonsAlphabet();
		}
		else {
			for(int i = 0; i<taille; i++) {
				relations.put(new Colon("C"+i+1), new HashSet<Colon>());
			}
		}
		
	}
	
	/**
	 * Ajoute les instances de {@code Colon} en tant que clés dans l'attribut {@code relations}
	 * de l'instance courante de {@code Colonie}.
	 * 
	 * <p>Cette méthode n'est appelée que par le constructeur dans le cas ou la {@code taille}
	 * de la colonie est inférieure ou égale à 26.
	 */
	private void creerColonsAlphabet() {
		for(int i = 0; i<taille; i++) {
			relations.put(new Colon(Character.toString('A'+i)), new HashSet<Colon>());
		}
	}
	
	/**
	 * Ajoute une relation d'animosité ("ne s'aiment pas") entre deux instances de 
	 * {@code Colon} passées en arguments. La liste d'adjacence est mise à jour pour
	 * les deux clés concernées.
	 * 
	 * @param  c1 un {@code Colon}
	 * @param  c2 un {@code Colon}
	 * @return vrai si la relation n'existait pas deja avant
	 * @throws IllegalArgumentException si les deux memes colons sont passés en argument
	 * @throws IllegalArgumentException si les deux colons ne sont pas dans {@code relations.ketSet()}
	 */
	public boolean ajouterRelation(Colon c1, Colon c2) throws IllegalArgumentException {
		if (c1.equals(c2)) {
			throw new IllegalArgumentException("Ne peut pas ajouter de relation entre un colon et lui meme");
		}
		if ( !relations.containsKey(c1) || !relations.containsKey(c2)) {
			throw new IllegalArgumentException("Ne peut pas ajouter de relation entre differentes colonies");
		}
		return relations.get(c1).add(c2) && relations.get(c2).add(c1);
	}
	
	/**
	 * TODO: documenter cette méthode quand elle sera complètement implémentée
	 * 
	 * @throws IllegalStateException si {@code taille} ne correspond pas à {@code affectation.size()}
	 */
	public int coutAffectation() throws IllegalStateException {
		if (affectation.contains(null)) {
			throw new IllegalStateException("Ne peut pas calculer le cout d'affectation : ressources non-affectees ou taille de la colonie reduite sans re-affectation");
		}
		if (affectation.size() != this.taille) {
			throw new IllegalStateException("Ne peut pas calculer le cout d'affectation : " + ((affectation.size() < this.taille)?"ressources non-affectees":"plus de colons que de ressources"));
		}
		return -1; // TODO: implémenter le calcul du coût d'affectation
	}
	
	/**
	 * Effectue une affectation "naïve" des ressources aux colons de cette instance de
	 * {@code Colonie}. Si elle existe, l'affectation précédente sera écrasée.
	 * 
	 * @throws IllegalStateException si tous les colons n'ont pas une liste de préférences complète
	 */
	public void affectationNaive() throws IllegalStateException {
		//initialisation de la liste d'affectations
		ArrayList<Colon> nouvelleAffectation = new ArrayList<Colon>();
		while (nouvelleAffectation.size() < this.taille) {
			nouvelleAffectation.add(null);
		}
		
		//Pour chaque colon, on récupère sa liste de préférences
		ArrayList<Integer> preferences;
		int ressource;
		for (Colon colon : relations.keySet()) {
			preferences = colon.getPreferences();
			
			// Détection d'exception : on génère un HashSet sur le tas pour s'assurer que
			// les préférences sont suffisantes en nombre ET uniques
			if (new HashSet<Integer>(preferences).size() != taille) {
				throw new IllegalStateException("La liste de preferences pour " + colon.getNom() + "n'est pas complete");
			}
			
			//Pour chaque préférence, on vérifie sa disponibilité
			//Si la ressource est disponible, on l'affecte
			//Plus besoin d'itérer si le colon est affecté (donc dans nouvelleAffectation)
			for(int i = 0; i < this.taille && !nouvelleAffectation.contains(colon); i++) {
				ressource = preferences.get(i);
				//nouvelleAffectation null à l'index ressource ssi la ressource est disponible
				if(nouvelleAffectation.get(ressource) == null) {
					nouvelleAffectation.set(ressource, colon);
				}
			}
		}
		
		//Enregistrement de l'affectation dans l'attribut d'instance (ecrase l'ancienne)
		this.affectation = nouvelleAffectation;
	}
	
	//TODO : méthode pour échanger deux colons
	
	/**
	 * Cette classe interne implémente les attributs et classes nécessaires pour définir
	 * un membre d'une {@code Colonie}.
	 */
	public class Colon {
		
		private String nom;
		
		/**
		 * Préférences des ressources pour l'instance courante de {@code Colon}.
		 * 
		 * La liste est à traiter comme un ordre de priorité descendant
		 * (l'élément à l'index 0 doit être le plus convoité par le {@code Colon}.
		 * 
		 * Cette implémentation comprend les mêmes problèmes d'unicité que pour l'attribut
		 * {@code affectation} de {@code Colonie}. De plus, il faudra s'assurer que chaque
		 * {@code Colon} définit une préférence pour les n ressources.
		 */
		private ArrayList<Integer> preferences;
		
		private Colon(String nom) {
			this.nom = nom;
			this.preferences = new ArrayList<Integer>();
		}
		
		public String getNom() {
			return nom;
		}
		public void setPreferences(ArrayList<Integer> preferences) {
			this.preferences = preferences;
		}
		public ArrayList<Integer> getPreferences() {
			return preferences;
		}
		public boolean equals(Object o) {
			return o == this;
		}
		public String toString() {
			return nom+preferences.toString();
		}
		
		
	}
}
