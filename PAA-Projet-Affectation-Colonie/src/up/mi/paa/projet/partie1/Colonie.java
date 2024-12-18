package up.mi.paa.projet.partie1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Implementation d'une colonie spatiale. La classe {@code Colonie} comprend
 * un graphe des relations, une liste d'affectations, une classe interne {@code Colon}
 * et les méthodes nécessaires à la gestion manuelle de la colonie.
 * 
 * @author Julie Colliere
 * @author Zakaria Chaker
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
		this.affectation = new ArrayList<Colon>(); 
		
		this.relations = new HashMap<Colon, HashSet<Colon>>();
		if (this.taille <= 26) {
			creerColonsAlphabet();
		}
		else {
			for(int i = 0; i<taille; i++) {
				relations.put(new Colon("C"+i), new HashSet<Colon>());
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
	 * Retourne une HashMap<Colon, Hashset<Colon>>
	 * 
	 * @return les relations de la colonie actuelle. 
	 */
	public HashMap<Colon, HashSet<Colon>> getRelations() {
		return this.relations;
	}
	
	/**
	 * Ajoute une relation d'animosité ("ne s'aiment pas") entre deux instances de 
	 * {@code Colon} passées en arguments. La liste d'adjacence est mise à jour pour
	 * les deux clés concernées.
	 * 
	 * @param  c1 un {@code Colon}
	 * @param  c2 un {@code Colon}
	 * @return {@code true} si la relation a bien été ajoutée
	 * @throw IllegalArgumentException si les deux memes colons sont passés en argument
	 * @throw IllegalArgumentException si les deux colons ne sont pas dans {@code relations.ketSet()}
	 */
	public boolean ajouterRelation(Colon c1, Colon c2) throws IllegalArgumentException {
		if (c1.equals(c2)) {
			throw new IllegalArgumentException("Ne peut pas ajouter de relation entre un colon et lui meme.");
		}
		if ( !relations.containsKey(c1) || !relations.containsKey(c2)) {
			throw new IllegalArgumentException("Colon n'appartenant pas à la colonie");
		}
		
		// retourne FALSE si l'ajout à échoué (cela inclue quand la relation existe deja)
		return relations.get(c1).add(c2) && relations.get(c2).add(c1);
	}
	
	/**
	 * Ajoute les préférences d'un colon
	 * 
	 * @param c instance de {@code Colon}
	 * @param preferences liste des préférences
	 */
	public void ajouterPreferences(Colon c, ArrayList<Integer> preferences) throws IllegalArgumentException {
		if (!relations.containsKey(c)) {
			throw new IllegalArgumentException("Le colon ne fait pas partie de cette colonie");
		}
		
		c.setPreferences(preferences);
	}
	
	/**
	 * Détermine le cout d'affectation en vérifiant le nombre de colon dont une ou plusieurs
	 * ressources convoitées ont été affectées à des colons qu'il n'aime pas. 
	 * 
	 * @return le cout d'affectation (le nombre de colons jaloux)
	 * @throws IllegalStateException si {@code taille} ne correspond pas à {@code affectation.size()}
	 */
	public int coutAffectation() throws IllegalStateException {
		if (affectation.contains(null)) {
			throw new IllegalStateException("Ne peut pas calculer le cout d'affectation : ressources non-affectees ou taille de la colonie reduite sans re-affectation");
		}
		if (affectation.size() != this.taille) {
			throw new IllegalStateException("Ne peut pas calculer le cout d'affectation : " + ((affectation.size() < this.taille)?"ressources non-affectees":"plus de colons que de ressources"));
		}
		
		int cout = 0;
		
		for(Colon colon : relations.keySet()) {
			boolean estJaloux = false;
			ArrayList<Integer> preferences = colon.getPreferences();
			int ressource = affectation.indexOf(colon);
			for(int i = preferences.indexOf(ressource); i>0; i--) {
				if (relations.get(colon).contains(affectation.get(preferences.get(i-1)))) {
					estJaloux = true;
				}
			}
			
			if (estJaloux) {
				cout++;
			}
		}
		
		return cout;
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
				throw new IllegalStateException("La liste de preferences pour " + colon.getNom() + " n'est pas complete");
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
	
	/**
	 * Echange l'affectation de deux colons passés en paramètres
	 * 
	 * @throws IllegalArgumentException si les deux colons n'ont pas déjà de ressource affectées
	 */
	public void echangerRessources(Colon c1, Colon c2) throws IllegalArgumentException {
		if (!affectation.contains(c1) || !affectation.contains(c2)) {
			throw new IllegalArgumentException("Ne peut pas faire l'echange: ressource non affectee");
		}
		
		int ressource_c1 = affectation.indexOf(c1);
		int ressource_c2 = affectation.indexOf(c2);
		affectation.set(ressource_c1, c2);
		affectation.set(ressource_c2, c1);
	}
	
	/**
	 * Méthode qui permet de retourner la taille de la colonie.
	 * 
	 * @return taille de la colonie
	 */
	public int getTaille() {
		return taille;
	}
	
	/**
	 * Trouve un colon dans {@code relations.ketSet()} dont le nom correspond à celui passé en paramètre
	 * 
	 * <p> L'unicité des noms est normalement assurée par le constructeur de Colonie
	 * 
	 * @return le premier colon trouvé avec le nom correspondant, {@code null} si non-trouvé
	 */
	public Colon chercherColonViaNom(String nom) {
		Colon colon = null;
		boolean trouve = false;
		Colon candidat;
		Iterator<Colon> itColons = relations.keySet().iterator();
		while(itColons.hasNext() && !trouve) {
			candidat = itColons.next();
			if(candidat.getNom().equals(nom)) {
				colon = candidat;
				trouve = true;
			}
		}
		return colon;
	}
	/*
	 * @return true si toutes les listes de préférences sont complètes
	 */
	public boolean preferencesCompletes() {
		boolean preferencesCompletes = true;
		for(Colon colon : relations.keySet()) {
			ArrayList<Integer> preferences = colon.getPreferences();
			if(new HashSet<Integer>(preferences).size() != taille) {
				preferencesCompletes = false;
			}
		}
		return preferencesCompletes;
	}
	
    /**
     * Méthode toString() qui permet de retourner le détail de la colonie actuelle:
     * Liste des colons
     * Relations entre colons
     * 
     */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Colonie de taille: ");
		sb.append(this.taille);
		sb.append("\nListe des colons: ");
		for(Colon colon : relations.keySet()) {
			//Sauter une ligne
			sb.append("\n\n");
			//Sur une ligne : le nom du colon et ses préférences
			sb.append(colon.getNom());
			sb.append("\tprefere: ");
			for(Integer ressource : colon.preferences) {
				sb.append(">").append(ressource); // ">" pour la lisibilité lors d'un affichage
			}
			//Sur une ligne : le nom des colons qu'il n'aime pas
			sb.append("\nN'aime pas: ");
			for (Colon enConflit : relations.get(colon)) {
				sb.append(" ").append(enConflit.getNom());
			}
		}
		
		if (affectation.size() == this.taille) {
			sb.append("\n\nAffectation actuelle :\n");
			for(int i = 0; i < affectation.size(); i++) {
				sb.append("\nRessource ");
				sb.append(i);
				sb.append(" : ");
				sb.append(affectation.get(i));
			}
		}
		return sb.toString();
	}
	
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
		
		/**
		 * Methode toString() retourne les preferences du colon
		 * 
		 * @return les preferences du colon suivi d'un espace
		 */
		@Override
		public String toString() { 
			StringBuffer sb = new StringBuffer();
			sb.append(nom);
			for (int ressource : this.preferences) {
				sb.append(" ");
				sb.append(ressource);
			}
			return sb.toString();
		}
	}
	
}
