package up.mi.paa.projet.partie1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	 * {@link HashMap} répertoriant les ressources en valeurs associées à leur
	 * identifiant en clé
	 */
	private HashMap<Integer, Ressource> ressources;
	
	/**
	 * {@link HashMap} contenant les couples d'instances {@link Colon}/{@link Ressource} 
	 * correspondant aux affectations des ressources aux colons de cette colonie 
	 */
	private HashMap<Colon, Ressource> affectations;
	
	/**
	 * Instancie une nouvelle {@code Colonie} avec la taille spécifiée.
	 * Si la {@code taille} entrée est négative ou nulle, initialise une {@code Colonie}
	 * de taille 0.
	 * 
	 * <p>Si il y a au plus 26 colons, ils seront chacuns nommés par une lettre
	 * de l'alphabet.
	 * 
	 * @param	taille la taille de la colonie
	 */
	public Colonie(int taille) {
		this.taille = taille;
		this.relations = new HashMap<Colon, HashSet<Colon>>();
		this.ressources = new HashMap<Integer, Ressource>();
		this.affectations = new HashMap<Colon, Ressource>();
		
		if (this.taille > 0) {
			if (this.taille <= 26) {
				for (int i = 0; i < this.taille; i++) {
					relations.put(new Colon(i, Character.toString('A' + i)), new HashSet<Colon>());
					ressources.put(i, new Ressource(i));
				}
			} else {
				for (int i = 0; i < this.taille; i++) {
					relations.put(new Colon(i), new HashSet<Colon>());
					ressources.put(i, new Ressource(i));
				}
			}
		} else {
			this.taille = 0;
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
	
	public int getTaille() {
		return taille;
	}
	
	/**
	 * Initialise une instance de {@code Colon} et {@code Ressource},
	 * les place dans leurs Map correspondantes, puis incrémente l'attribut {@code taille}
	 */
	public void agrandir() {
		relations.put(new Colon(taille), new HashSet<Colon>());
		ressources.put(taille, new Ressource(taille));
		taille++;
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
	 * @throws IllegalStateException si il y a des ressources/colons non affectés
	 */
	public int coutAffectation() throws IllegalStateException {
		if (!affectations.values().containsAll(ressources.values())) {
			throw new IllegalStateException("Ne peut pas calculer le cout d'affectation : ressource ou colon non-affecte");
		}
		
		int cout = 0;
		/* Pour chaque affectation, si, parmis les relations du colon en question,
		 * il existe colon qu'il n'aime pas avec une ressource notée plus haute dans la
		 * liste des préférences, alors le colon en question est jaloux
		 */
		for(Colon colon : affectations.keySet()) {
			ArrayList<Integer> preferences = colon.getPreferences();
			ArrayList<Ressource> prefere = new ArrayList<>();
			for (Colon relation : relations.get(colon)) {
				//TODO
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
		//TODO
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
		this.affectations = nouvelleAffectation;
	}
	
	/**
	 * Echange l'affectation de deux colons passés en paramètres
	 * 
	 * @throws IllegalArgumentException si les deux colons n'ont pas déjà de ressource affectées
	 */
	public void echangerRessources(Colon c1, Colon c2) throws IllegalArgumentException {
		if (!affectations.containsKey(c1) || !affectations.containsKey(c2)) {
			throw new IllegalArgumentException("Ne peut pas faire l'echange : colon non affecte");
		}
		
		affectations.replace(c1, affectations.replace(c2, affectations.get(c1)));
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
		Colon candidat;
		Iterator<Colon> itColons = relations.keySet().iterator();
		while(itColons.hasNext()) {
			candidat = itColons.next();
			if(candidat.getNom().equals(nom)) {
				colon = candidat;
				break;
			}
		}
		return colon;
	}
	
	/**
	 * @return {@code true} si toutes les listes de préférences sont complètes
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
			sb.append("\tpréfère : ");
			for(Integer ressource : colon.preferences) {
				sb.append(">").append(ressource); // ">" pour la lisibilité lors d'un affichage
			}
			//Sur une ligne : le nom des colons qu'il n'aime pas
			sb.append("\nN'aime pas :");
			for (Colon enConflit : relations.get(colon)) {
				sb.append(" ").append(enConflit.getNom());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Cette classe interne implémente les attributs et méthodes nécessaires pour définir
	 * un membre d'une {@code Colonie}.
	 */
	public class Colon {
		
		private String nom;
		private final int identifiant;
		
		/**
		 * Préférences des ressources pour l'instance courante de {@code Colon}.
		 * 
		 * La liste est à traiter comme un ordre de priorité descendant
		 * (l'élément à l'index 0 doit être le plus convoité par le {@code Colon}.
		 */
		private ArrayList<Integer> preferences; //TODO: Change type to ArrayList<Ressource>
		
		private Colon(int identifiant, String nom) {
			this.identifiant = identifiant;
			this.nom = nom;
			this.preferences = new ArrayList<Integer>();
		}
		
		private Colon(int identifiant) {
			this.identifiant = identifiant;
			this.nom = "C" + identifiant;
		}
		
		public boolean preferencesCompletes(int taille) {
			return new HashSet<Integer>(preferences).size() == taille;
		}
		
		public int getID() {
			return identifiant;
		}
		public String getNom() {
			return nom;
		}
		public void setNom(String nom) {
			this.nom = nom;
		}
		public void setPreferences(ArrayList<Integer> preferences) {
			this.preferences = preferences;
		}
		public ArrayList<Integer> getPreferences() {
			return preferences;
		}
		
		@Override
		public boolean equals(Object o) {
			return (o instanceof Colon) && (o != null) && (((Colon) o).getID() == identifiant);
		}
		
		/**
		 * @return nom suivi des preferences dans l'ordre (séparées d'un espace)
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
	
	/**
	 * Cette classe interne implémente les attributs et méthodes nécéssaires pour définir
	 * une ressource à distribuer dans une {@code Colonie}
	 * 
	 * <p>La définition de cette classe permet une plus grande flexibilité dans la manipulation
	 * de couples {@code Colon}/{@code Ressource}
	 */
	public class Ressource {
		
		private String nom;
		
		/**
		 * Pour faciliter les affectations et autres méthodes manipulant des instances de
		 * {@code Ressource}, il convient de définir un attribut de type {@code int} pour 
		 * accéder rapidement à une ressource de la colonie.
		 * 
		 * <p> Il est important de savoir que cet identifiant doit etre unique dans le cadre de
		 * l'instance englobante de {@code Colonie} associée. On souhaitera que pour une colonie
		 * de taille n, il existe n ressources avec un unique identifiant de {@code 0} à {@code n-1}
		 */
		private final int identifiant;
		
		private Ressource(int identifiant) {
			this.identifiant = identifiant;
			this.nom = "R" + identifiant;
		}
		
		public int getID() {
			return identifiant;
		}
		
		public String getNom() {
			return nom;
		}
		
		public void setNom(String nom) {
			this.nom = nom;
		}
		
		@Override
		public boolean equals(Object o) {
			return (o instanceof Ressource) && (o != null) && (((Ressource) o).getID() == identifiant);
		}
	}
}
