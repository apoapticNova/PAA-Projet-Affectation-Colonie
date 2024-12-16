package up.mi.paa.projet.partie2;

import java.io.File;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.List;

import up.mi.paa.projet.partie1.GestionColonie;


/**
 * La classe {@code GestionColonieAmelioree} permet le chargement d'une instance de {@code Colonie}
 * depuis un fichier texte, la résolution automatique du problème d'affectation à coût minimal et
 * la sauvegarde de cette solution dans un fichier texte.
 *
 *<p>Ces actions seront réalisables via des lignes de commandes
 *
 * @author Julie Colliere
 * @author Zakaria Chaker
 */
public class GestionColonieAmelioree {
	
	private static final String SAVEPATH_DEFAULT = "savedata" + File.pathSeparator + "affectation.txt";
	private static final String READPATH_DEFAULT = "savedata" + File.pathSeparator + "colonie.txt";
	private static Colonie colonie;
	
	/**
	 * Charge une colonie depuis un fichier dont le chemin est spécifié par l'utilisateur
	 * 
	 * <p>Si {@code filePath} est vide, le chemin par défaut privilégié sera "savedata/colonie.txt"
	 * 
	 * @param filePath chemin vers le fichier texte décrivant la colonie
	 * @return la colonie telle que décrite par le fichier
	 */
	private static Colonie charger(String filePath) {
		System.out.println("Chargement de votre colonie...");
		Colonie colonie = ParserColonie.parser(filePath.isEmpty()?READPATH_DEFAULT:filePath);
		System.out.println("Colonie chargée.");
		return colonie;
	}
	
	/**
	 * Sauvegarde la solution d'affectation trouvée dans un fichier dont le chemin 
	 * est donné par l'utilisateur
	 * 
	 * @param sc scanner pour obtenir le chemin de la part de l'utilisateur
	 */
	private static void sauvegarderSolution(Scanner sc) {
		System.out.println("Indiquez un chemin de sauvegarde (par défaut si vide : savedata/affectation.txt) : ");
		String filePath = sc.nextLine();
		boolean reussie = SauvegardeColonie.sauvegarderAffectation(colonie, filePath.isEmpty()?SAVEPATH_DEFAULT:filePath);
		System.out.println(reussie?"Sauvegarde reussie.":"La sauvegarde a échoué");
	}
	
	/**
	 * Sous-menu pour le choix d'algorithme (résolution automatique)
	 * 
	 * @param sc scanner pour l'entrée utilisateur
	 */
	private static void choixAlgorithme(Scanner sc) {
		int choix;
		do {
			menuAlgorithmes();
			choix = lireEntier(sc, "Choisir algorithme : ");
			switch (choix) {
			case 1:
				algorithmeApproximatif(colonie.getTaille()*2);
				System.out.println("Fini.");
				afficherCoutSolution();
				choix = 0;
				break;
			case 2:
				//TODO: PAS ENCORE IMPLEMENTE
				break;
			case 0:
				afficherCoutSolution();
				break;
			default:
				System.err.println("Choix incorrect");
			}
		} while(choix != 0);
	}
	
	/**
	 * Affiche le coût de l'affectation actuelle de la colonie (si elle existe)
	 */
	private static void afficherCoutSolution() {
		if (colonie.getAffectation().contains(null) || colonie.getAffectation().size() < colonie.getTaille()) {
			System.err.println("Impossible de calculer le cout de la solution actuelle");
		} else {
			System.out.println("Cout de la solution actuelle : " + colonie.coutAffectation());
		}
	}
	
	/**
	 * Lit un entier entré par l'utilisateur
	 * 
	 * @param sc		scanner récupérant l'entrée utilisateur
	 * @param message	message à afficher
	 * @return entier lu
	 */
	private static int lireEntier(Scanner sc, String message) {
		boolean entierLu = false;
		int nb = 0;
		while (!entierLu) {
			try {
				System.out.print(message);
				nb = sc.nextInt();
				entierLu = true;
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.err.println("Entrez un entier");
				sc.nextLine();
			}
		}
		return nb;
	}
	
	/**
	 * Affiche le menu principal de la partie 2
	 */
	private static void menu() {
		System.out.println("1) Resolution automatique");
		System.out.println("2) Sauvegarder la solution actuelle");
		System.out.println("3) Fin (quitter le programme)");
	}
	
	/**
	 * Affiche le sous-menu du choix d'algorithme pour la résolution automatique
	 */
	private static void menuAlgorithmes() {
		System.out.println("1) Algorithme approximatif (document projet)");
		System.out.println("2) PAS ENCORE IMPLEMENTE");
		System.out.println("0) Retour");
	}
	
	/**
	 * Implémentation de l'algorithme approximatif (naïf) tel que décrit dans le document
	 * de la partie 2 du projet
	 * 
	 * @param k nombre d'échanges de ressources avant l'arrêt de l'algorithme
	 */
	private static void algorithmeApproximatif(int k) {
		Random rng = new Random();
		colonie.affectationNaive();
		List<Colon> affectation = colonie.getAffectation();
		for (int i = 0; i < k; i++) {
			Colon colon1 = affectation.get(rng.nextInt(colonie.getTaille()));
			Colon colon2 = null;
			while(colon1 == (colon2 = affectation.get(rng.nextInt(colonie.getTaille()))));
			
			int cout = colonie.coutAffectation();
			colonie.echangerRessources(colon1, colon2);
			if (cout <= colonie.coutAffectation()) {
				colonie.setAffectation(affectation);
			} else {
				affectation = colonie.getAffectation();
			}
		}
	}

	/**
	 * Méthode d'entrée du programme
	 * 
	 * <p> Un chemin vers un fichier décrivant une colonie est attendu dans args[0] pour
	 * faire fonctionner la partie 2.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		//Dans un premier temps, on veut pouvoir récupérer le chemin d'un fichier dans args[0]
		//Si il n'y en a pas, on passe à la partie 1 
		try {
			colonie = charger(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			GestionColonie.partie1_main(sc);
		}
		
		int choix;
		do {
			menu();
			choix = lireEntier(sc, "Choix : ");
			switch (choix) {
			case 1:
				choixAlgorithme(sc);
				afficherCoutSolution();
				break;
			case 2:
				sauvegarderSolution(sc);
				break;
			case 3:
				break;
			default:
				System.err.println("Choix incorrect : " + choix);
			}
		} while (choix !=3);

		sc.close();
	}

}
