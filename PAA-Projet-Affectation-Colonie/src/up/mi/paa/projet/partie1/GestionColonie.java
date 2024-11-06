package up.mi.paa.projet.partie1;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import up.mi.paa.projet.partie1.Colonie.Colon;

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
	}
	
	public static void affichageMenu2() {
		System.out.println("1) échanger les ressources de deux colons ;");
		System.out.println("2) afficher le nombre de colons jaloux ;");
		System.out.println("3) Fin.");
	}

	/**
	 * Ajoute le nombre de colons dans la colonie. Verifie le type entré et le
	 * nombre de colons saisi.
	 * 
	 * Pour cette V1, la capacité est limitée à 26.
	 * 
	 * @param saisie un Scanner
	 */
	public static int saisieTailleColonie(Scanner saisie) {
		int n = -1;
		while (n <= 0) {
			System.out.println(
					"\nSaisir la taille de la colonie.\nAttention pour cette premiere version la capacite maximale est de 26 colons:");
			try {
				n = saisie.nextInt();
				if (n <= 0)
					System.err.println("Erreur: La colonie ne peut pas avoir une taille inferieure a 0. ");
				else if (n > 26) {
					System.err.println("Erreur: La colonie a une capacite limitee a 26...");
					n = 0;
				}
			} catch (InputMismatchException erreur) {
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
			System.out.println("\nSaisir votre choix parmi (1-3): ");
			affichageMenu2();
			try {
				choix = saisie.nextInt();
				switch (choix) {
				case 1:
					//choix1
					break;

				case 2:
					//choix2
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
			Colon colon1 = null;
			while (colon1 == null) {
				System.out.println("Saisir le nom du colon de depart: ");
				String nomColon = saisie.next();
				colon1 = colonie.chercherColonViaNom(nomColon);
				if (colon1 == null) {
					System.err.println("Veuillez saisir un nom valide !");
				}
			}
			// Recherche du deuxième colon
			Colon colon2 = null;
			while (colon2 == null) {
				System.out.println("\nSaisir le nom de l'autre colon, qui ne l'aime pas: ");
				String nomColon = saisie.next();
				colon2 = colonie.chercherColonViaNom(nomColon);
				if (colon2 == null) {
					System.err.println("Veuillez saisir un nom valide !");
				}
			}
			
			//Pour arriver ici le programme aura forcément trouvé deux colons dans la même colonie
			//Le seul cas à considérer est celui où les deux colons sont identiques (si ajouterRelation retourne false)
			if(colonie.ajouterRelation(colon1, colon2)) {
				System.out.println("Relation ajoutée !");
			} else {
				System.out.println("Cette relation existe déjà");
			}
			
		} catch (InputMismatchException e) {
			System.err.println(e.getMessage());
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
			System.out.println("Saisir les preferences dans l'ordre séparées d'un espace. Exemple : 1 2 3");
			System.out.println("Attention il doit y avoir exactement " + colonie.getTaille() + " ressources.\n");
			String[] tabPreferences = saisie.nextLine().split(" ", colonie.getTaille()); //StringTokenizer déprécié -> String::split est préféré, on obtient un tableau de String de taille max colonie.taille
			if (tabPreferences.length == colonie.getTaille()) {
				preferences.clear(); // si des mauvaises valeurs ont été entrées à une étape précédente de la boucle, réinitialise la liste
				try {
					int i = 0;
					int ressource = Integer.parseInt(tabPreferences[i]);
					// Cette condition est vraiment longue mais verifie que la donnée est valide et arrete la boucle dès la premiere valeur invalide
					while (ressource >= 0 && ressource < colonie.getTaille() && !preferences.contains(ressource) && i < colonie.getTaille()) {
						preferences.add(ressource);
						ressource = Integer.parseInt(tabPreferences[++i]);
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
		Colon colon = null;
		while (colon == null) {
			System.out.println("Saisir le nom du colon :");
			String nomColon = saisie.next();
			colon = colonie.chercherColonViaNom(nomColon);
			if (colon == null) {
				System.err.println("Veuillez saisir un nom valide !");
			}
		}
		
		colonie.ajouterPreferences(colon, saisiePreferences(saisie));
		System.out.println("Préférences ajoutées pour " + colon.toString());
	}
	
	/**
	 * Méthode qui gère le comportement global du programme
	 * 
	 * @param saisie
	 * @return vrai/faux
	 */
	public void gestionColonie(Scanner saisie) {
		while(!colonie.preferencesCompletes()) {
			System.out.println("Il reste des préférences à saisir");
			menu1(saisie);
		}
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
		affichageDebut();
		Scanner sc = new Scanner(System.in);
		GestionColonie gC = new GestionColonie(sc);
		gC.gestionColonie(sc);
		System.out.println(gC.toString());
		sc.close();
	}

}
