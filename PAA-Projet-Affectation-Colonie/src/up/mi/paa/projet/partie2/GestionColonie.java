package up.mi.paa.projet.partie2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import up.mi.paa.projet.partie2.Colonie.Colon;


/**
 * La classe {@code GestionColonie} permet l'interaction entre l'utilisateur et
 * une instance de la classe {@code Colonie}.
 *
 * @author Julie Colliere
 * @author Zakaria Chaker
 */
public class GestionColonie {

	private Colonie colonie;

	public GestionColonie(Scanner sc) {
		this.colonie = new Colonie(saisieTailleColonie(sc));
	}

	public Colonie getColonie() {
		return this.colonie;
	}

	public static void affichageDebut() {
		System.out.println("********Partage de biens d'une colonie spatiale********\n");
	}

	public static void affichageMenu1() {
		System.out.println("1) Ajouter une relation entre deux colons;");
		System.out.println("2) Ajouter les preferences d'un colon;");
		System.out.println("3) Fin.");
		System.out.println("0) Quitter le programme");
	}
	
	public static void affichageMenu2() {
		System.out.println("1) echanger les ressources de deux colons;");
		System.out.println("2) afficher le nombre de colons jaloux;");
		System.out.println("3) Fin. (Quitter le programme)");
	}

	/**
	 * Ajoute le nombre de colons dans la colonie. Verifie le type entré et le
	 * nombre de colons saisi.
	 * 
	 * La capacité de la colonie peut dépasser 26 pour cette v2.
	 * 
	 * @param saisie un Scanner
	 */
	public static int saisieTailleColonie(Scanner saisie) {
		int n = -1;
		while (n <= 0) {
			System.out.println("Saisir la taille de la colonie: ");
			try {
				n = saisie.nextInt();
				if (n <= 0)
					System.err.println("Erreur: La colonie ne peut pas avoir une taille inferieure a 0. ");
				}
			 catch (InputMismatchException erreur) {
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.nextLine();
			}
		}
		return n;
	}

	/**
	 * Propose à l'utilisateur les actions à réaliser pour l'étape 1 de constitution de la colonie
	 * 
	 * @param saisie
	 * @return int
	 */
	private void menu1(Scanner saisie) {
		int choix = -1;
		while (choix != 3) {
			System.out.println("\nSaisir votre choix parmi (1-3): ");
			affichageMenu1();
			try {
				choix = saisie.nextInt();
				switch (choix) {
				case 1:
					relationsEntreColons(saisie);
					break;

				case 2:
					preferencesColons(saisie);
					break;

				case 3:
					System.out.println("Fin des modifications de la colonie");
					break;
					
				case 0:
					System.exit(0);
					break;

				default:
					System.err.println("Choix invalide !");
					break;

				}
			} catch (InputMismatchException e) {
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.nextLine();
			}
		}
	}
	
	/**
	 * Propose à l'utilisateur les actions à réaliser pour l'étape 2 de constitution de la colonie
	 * 
	 * @param saisie
	 * @return int
	 */
	private void menu2(Scanner saisie) {
		int choix = -1;
		while (choix != 3) {
			System.out.println(colonie.toString());
			System.out.println("\nSaisir votre choix parmi (1-3): ");
			affichageMenu2();
			try {
				choix = saisie.nextInt();
				switch (choix) {
				case 1:
					echangerColons(saisie);
					break;

				case 2:
					System.out.println(colonie.coutAffectation() + " colons jaloux");
					break;

				case 3:
					System.out.println("Sortie du programme");
					break;

				default:
					System.err.println("Choix invalide !");
					break;

				}
			} catch (InputMismatchException e) {
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.nextLine();
			}
		}
	}

	/**
	 * Méthode relationsEntreColons retourne si la methode s'est bien déroulée ou pas
	 * Elle permet de saisir une relation entre un colon A et un colon B
	 * 
	 * <p>Retourne les informations de la colonie sur sa gestion et les differentes
	 * modifications réalisées par l'user.
	 * 
	 * @param saisie
	 * @return vrai ou faux si la methode se passe bien ou pas necessaire pour le switch case principal...
	 */
	public void relationsEntreColons(Scanner saisie) {
		try {
			// Recherche du premier colon
			Colon colon1 = saisirColon(saisie, "Saisir le nom du colon de depart :");
			// Recherche du deuxième colon
			Colon colon2 = saisirColon(saisie, "Saisir le nom de l'autre colon, qui ne l'aime pas :");
			
			//Pour arriver ici le programme aura forcément trouvé deux colons dans la même colonie
			//Le seul cas à considérer est celui où les deux colons sont identiques (si ajouterRelation retourne false)
			if(colonie.ajouterRelation(colon1, colon2)) {
				System.out.println("Relation ajoutee !");
			} else {
				System.out.println("Cette relation existe deja");
			}
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Instancie une {@code ArrayList<Integer>} pour une assignation à l'attribut {@code preferences}
	 * du colon passé en argument de {@code preferencesColons}.
	 * 
	 * <p> La méthode s'appuie sur les données entrées par l'utilisateur et ne retournera que
	 * lorsque les données entrées seront valides. Cette implémentation impose que la liste soit
	 * complète (cohérente avec la taille de la colonie associée).
	 */
	public ArrayList<Integer> saisiePreferences(Scanner saisie) {
		ArrayList<Integer> preferences = new ArrayList<Integer>();
		// Saisir les preferences d'un colon Ex: A 1 2 3 4
		// Verifier qu'il y a bien exactement n ressources correspondant à n colons
		do {
			System.out.println("Saisir les preferences dans l'ordre separees d'un espace, numerotees en partant de 0 inclu. Exemple : 0 1 2");
			System.out.println("Attention il doit y avoir exactement " + colonie.getTaille() + " ressources.\n");
			saisie.nextLine(); //pour une raison inconnue si on ne fait pas nextLine() ici la prochaine instruction s'exécute une première fois sans attendre d'entrée utilisateur
			String[] tabPreferences = saisie.nextLine().split(" ", colonie.getTaille()); //StringTokenizer déprécié -> String::split est préféré, on obtient un tableau de String de taille max colonie.taille
			if (new HashSet<String>(Arrays.asList(tabPreferences)).size() == colonie.getTaille()) { //Génère un HashSet sur le tas pour vérifier l'unicité des valeurs
				preferences.clear(); // si des mauvaises valeurs ont été entrées à une étape précédente de la boucle, réinitialise la liste
				try {
					//à partir de là on sait que les valeurs de tabPreferences sont toutes uniques, reste à savoir si elles sont valides
					//RAPPEL: Entree valide = k in [0;colonie.taille[ <=> colonie.taille ressources
					for(int i = 0; i < tabPreferences.length; i++) {
						int ressource = Integer.parseInt(tabPreferences[i]);
						if (ressource >= 0 && ressource < colonie.getTaille()) {
							preferences.add(ressource);
						} else {
							break; //si la ressource est invalide -> sortie de boucle
						}
					}
				} catch (NumberFormatException e) {
					System.err.println("Veuillez entrer une liste valide !");
				}
			}
		} while (preferences.size() != colonie.getTaille());

		return preferences;
	}
	
	/**
	 * A l'aide des informations entrées par l'utilisateur, trouve un colon, appelle
	 * {@code saisiePreferences()} pour obtenir une liste de préférences à assigner
	 * 
	 */
	public void preferencesColons(Scanner saisie) {
		//Recherche du colon à modifier
		Colon colon = saisirColon(saisie, "Saisir le nom du colon :");
		colonie.ajouterPreferences(colon, saisiePreferences(saisie));
		System.out.println("Preferences ajoutees pour " + colon.toString());
	}
	
	public void echangerColons(Scanner saisie) {
		Colon colon1 = saisirColon(saisie, "Saisir le premier colon du duo à echanger");
		Colon colon2 = saisirColon(saisie, "Saisir le deuxieme colon du duo à echanger");
		
		colonie.echangerRessources(colon1, colon2);
		System.out.println("Echange effectue !");
	}
	
	/**
	 * Utilise les données entrées par l'utilisateur pour trouver un colon via
	 * {@code chercherColonViaNom(nom)}
	 * 
	 * <p> AssertionError si la colonie a une taille de 0 (aucun colon ne pourrait etre trouvé) 
	 * 
	 * @return le colon trouvé
	 */
	public Colon saisirColon(Scanner saisie, String message) {
		assert colonie.getTaille() > 0;
		Colon colon = null;
		while (colon == null) {
			System.out.println(message);
			String nomColon = saisie.next();
			colon = colonie.chercherColonViaNom(nomColon);
			if (colon == null) {
				System.err.println("Veuillez saisir un nom valide !");
			}
		}
		return colon;
	}
	
	
	/**
	 * Méthode qui gère le comportement global du programme
	 * 
	 * @param saisie
	 * @return vrai/faux
	 */
	public void gestionColonie(Scanner saisie) {
		while(!colonie.preferencesCompletes()) {
			System.out.println("Il reste des preferences a saisir");
			menu1(saisie);
		}
		colonie.affectationNaive();
		menu2(saisie);

	}
	
	/**
	 * Retourne les informations de la colonie sur sa gestion et les differentes modifications réalisées par l'user.
	 * 
	 * @return une chaine de caractere donannt des infromations sur la colonie et les derniers mouvements.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Etat actuel de la colonie: \n\n");
		sb.append(this.colonie.toString());
		return sb.toString();
	}

	/**
	 * main provisoire qui permet pour le moment de réaliser les tests. Un main sera de toute façon utilisé pour faire fonctionner l'interface user.
	 */
	public static void main(String[] args) {
		/**
		affichageDebut();
		 
		Scanner sc = new Scanner(System.in);
		GestionColonie gC = new GestionColonie(sc);
		gC.gestionColonie(sc);
		System.out.println(gC.toString());
		sc.close();
		
		**/
	    //test si fichier valide 
		try {
			System.out.println(ParserColonie.fichierTexteValide("CheminAcces"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
