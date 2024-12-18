package up.mi.paa.projet.partie2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParserColonie {

	/**
	 * Cree une colonie à partir d'un fichier texte
	 * 
	 * @param cheminFichier le chemin du fichier décrivant la colonie 
	 * @return	la nouvelle colonie créée
	 */
	public static Colonie parser(String cheminFichier) {
		Colonie colonie = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
			String ligne = null;
			int etape = 0;
			
			ArrayList<Colon> colons = new ArrayList<>();
			ArrayList<Ressource> ressources = new ArrayList<>();
			
			while ((ligne = reader.readLine()) != null) {
				if(ligne.matches("^(colon|ressource|deteste|preferences)\\((\\w+,)*\\w+\\)\\.$")) {
					switch(ligne.substring(0, ligne.indexOf("("))) {
					
					case "colon":
						if (etape != 0) {
							throw new IOException("Ligne incorrecte, colon inattendu : " + ligne);
						}
						if (ligne.matches("^.+\\(\\w+\\)\\.$")) {
							colons.add(parserColon(ligne));
						} else throw new IOException("Ligne incorrecte, trop d'arguments (max 1) : " + ligne);
						break;
						
					case "ressource":
						if (etape == 0 && colons.size() > 0) {
							etape++;
						}
						if (etape == 1 && colons.size() > ressources.size()) {
							if (ligne.matches("^.+\\(\\w+\\)\\.$")) {
								ressources.add(parserRessource(ligne));
							} else throw new IOException("Ligne incorrecte, trop d'arguments (max 1) : " + ligne);
						} else throw new IOException("Ligne incorrecte, ressource inattendue : " + ligne);
						break;
					
					case "deteste":
						if (etape == 1 && colons.size() == ressources.size()) {
							etape++;
							colonie = new Colonie(colons, ressources);
						}
						if (etape == 2) {
							if (ligne.matches("^.+\\(\\w+,\\w+\\)\\.$")) {
								String[] relation = parserRelation(ligne);
								Colon colon1 = colonie.chercherColonViaNom(relation[0]);
								Colon colon2 = colonie.chercherColonViaNom(relation[1]);
								try {
									colonie.ajouterRelation(colon1, colon2);
								} catch (IllegalArgumentException e) {
									throw new IOException("Ligne incorrecte, colons invalides pour ajout de relation: " + ligne, e);
								}
							} else throw new IOException("Ligne incorrecte, pas assez/trop d'arguments (attendu : 2): " + ligne);
						} else throw new IOException("Ligne incorrecte, relation inattendue: " + ligne);
						break;
					
					case "preferences":
						if (etape >= 1 && colons.size() == ressources.size()) {
							etape = 3;
							if (colonie == null) colonie = new Colonie(colons, ressources);
						}
						if (etape == 3) {
							if (ligne.matches("^.+\\(\\w+(,\\w+){" + colonie.getTaille() + "}\\)\\.$")) {
								String[] donnees = parserPreferences(ligne);
								Colon colon = colonie.chercherColonViaNom(donnees[0]);
								ArrayList<Ressource> preferences = new ArrayList<Ressource>();
								if (colon == null) {
									throw new IOException("Ligne incorrecte, colon inconnu: " + ligne);
								}
								for (int i = 1; i < donnees.length; i++) {
									Ressource ressource = colonie.chercherRessourceViaNom(donnees[i]);
									if (ressource == null) {
										throw new IOException("Ligne incorrecte, ressource inconnue : " + ligne);
									}
									preferences.add(ressource);
								}
								try {
									colonie.ajouterPreferences(colon, preferences);
								} catch (IllegalArgumentException e) {
									throw new IOException("Ligne incorrecte, liste de préférences incomplète : " + ligne,e);
								}
							} else throw new IOException("Ligne incorrecte, pas assez/trop d'arguments (attendu : " + (colonie.getTaille()+1) + ") : " + ligne);
						} else throw new IOException("Ligne incorrecte, preferences inattendues : " + ligne);
						break;
						
					default:
						throw new IOException("Unexpected regex edge-case (you're not supposed to see this): " + ligne);
					}
				} else throw new IOException("Ligne incorrecte: " + ligne);
			}
			
			if (!colonie.preferencesCompletes()) {
				throw new IOException("Erreur durant le chargement de la colonie, pas assez de lignes 'preferences(...)' pour une colonie de taille: " + colonie.getTaille());
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return colonie;
	}
	
	
	/**
	 * Cree un colon à partir d'une ligne
	 * 
	 * <p>Format d'une ligne décrivant un colon : "{@code colon(nom_colon).}"
	 * 
	 * @param ligne chaine contenant les données décrivant le colon (nom)
	 * @return chaine colon
	 */
	public static Colon parserColon(String ligne) {
	    String nom = ligne.split("\\(")[1].split("\\)")[0];
	    return new Colon(nom);
	}
	
	/**
	 * Cree une ressource à partir d'une ligne
	 * 
	 * <p>Format d'une ligne décrivant une ressource : "{@code ressource(nom_ressource).}"
	 * 
	 * @param ligne chaine décrivant la ressource
	 * @return la nouvelle ressource créée
	 */
	public static Ressource parserRessource(String ligne) {
	    String nom = ligne.split("\\(")[1].split("\\)")[0];
	    return new Ressource(nom);
	}
	
	/**
	 * Cree une 'relation' (couple de deux colons) à partir d'une ligne
	 * 
	 * <p>Format d'une relation : "{@code deteste(nom_colon1,nom_colon2)}"
	 * 
	 * @param ligne chaine décrivant la relation
	 * @return tableau de longueur 2 contenant les deux noms de colons de la relation
	 */
	public static String [] parserRelation(String ligne) {
	    String[] noms = ligne.split("\\(")[1].split("\\)")[0].split(",");
	    return noms;
	}
	
	/**
	 * Cree une liste de préférences à partir d'une ligne
	 * 
	 * <p>Format d'une liste de préférences : "{@code preferences(nom_colon,nom_ressource1,nom_ressource2,...).}"
	 * 
	 * @param ligne chaine décrivant la liste de préférences
	 * @return tableau de chaines, le premier element est le colon et ceux qui suivent sa liste de préférences
	 */
	public static String [] parserPreferences(String ligne) {
	    String[] donnees = ligne.split("\\(")[1].split("\\)")[0].split(",");
	    return donnees;
	}
	
}
