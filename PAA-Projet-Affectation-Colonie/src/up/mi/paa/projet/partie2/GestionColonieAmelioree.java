package up.mi.paa.projet.partie2;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	private static final String SAVEPATH_DEFAULT = "savedata" + File.separator + "affectation.txt";
	private static final String READPATH_DEFAULT = "savedata" + File.separator + "colonie.txt";
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
		System.out.println("Colonie chargee.");
		return colonie;
	}
	
	/**
	 * Sauvegarde la solution d'affectation trouvée dans un fichier dont le chemin 
	 * est donné par l'utilisateur
	 * 
	 * @param sc scanner pour obtenir le chemin de la part de l'utilisateur
	 */
	private static void sauvegarderSolution(Scanner sc) {
		System.out.println("Indiquez un chemin de sauvegarde (par defaut si vide : savedata/affectation.txt) : ");
		String filePath = sc.nextLine();
		boolean reussie = SauvegardeColonie.sauvegarderAffectation(colonie, filePath.isEmpty()?SAVEPATH_DEFAULT:filePath);
		System.out.println(reussie?"Sauvegarde reussie.":"La sauvegarde a echoue");
	}
	
	/**
	 * Sauvegarde la colonie chargée ou dans le programme dans un fichier dont le chemin
	 * est donné par l'utilisateur
	 * 
	 * @param sc scanner pour obtenir le chemin de la part de l'utilisateur
	 */
	private static void sauvegarderColonie(Scanner sc) {
		System.out.println("Indiquez un chemin de sauvegarde (par defaut si vide : savedata/colonie.txt) : ");
		String filePath = sc.nextLine();
		boolean reussie = SauvegardeColonie.sauvegarderColonie(colonie, filePath.isEmpty()?READPATH_DEFAULT:filePath);
		System.out.println(reussie?"Sauvegarde reussie.":"La sauvegarde a echoue");
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
			afficherCoutSolution();
			choix = lireEntier(sc, "Choisir algorithme: ");
			switch (choix) {
			case 1:
				algorithmeApproximatif(colonie.getTaille()*2);
				System.out.println("Fini.");
				afficherCoutSolution();
				choix = 0;
				break;
			case 2:
				algorithmeHongrois();
				System.out.println("Fini.");
				afficherCoutSolution();
				choix = 0;
				break;
			case 0:
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
			System.out.println("Cout de la solution actuelle : pas encore calcule");
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
		System.out.println("3) Sauvegarder la colonie");
		System.out.println("4) Fin (quitter le programme)");
	}
	
	/**
	 * Affiche le sous-menu du choix d'algorithme pour la résolution automatique
	 */
	private static void menuAlgorithmes() {
		System.out.println("1) Algorithme approximatif (document projet)");
		System.out.println("2) Algorithme Hongrois (Kuhn-Munkres)");
		System.out.println("0) Retour");
	}
	
	/**
	 * Implémentation de l'algorithme approximatif (naïf) tel que décrit dans le document
	 * de la partie 2 du projet
	 * 
	 * <p> Complexité : O(k), sans garantie d'avoir une solution optimale (car les échanges sont fait aléatoirement et sont d'un nombre limité)
	 * <p> On suppose que plus k est grand, plus on s'approche d'une solution optimale
	 * <p> En sachant que pour une colonie de taille n, il existe n! affectations possibles.
	 * Si tous les échanges produisaient des affectations uniques, il faudrait que k >= n! pour
	 * garantir l'obtention d'une solution optimale
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
	 * Implémentation de la méthode hongroise (algorithme de Kuhn-Munkres) pour la résolution
	 * du problème d'affectation en temps polynomial : O(n^4)
	 * 
	 * <p> Le principe de cet algorithme est de simplifier progressivement le coût des n!
	 * affectations possibles sans en changer l'ordre en manipulant une matrice des coûts jusqu'à
	 * obtenir une solution.
	 * 
	 * <p> Cet algorithme présume qu'on dispose d'une matrice des coûts de chaque affectation
	 * colon-ressource de façon individuelle. La difficulté de cette implémentation se trouve dans
	 * la construction de ce coût, car le "coût" tel que définit dans notre implémentation de
	 * la colonie est une vision globale. C'est à dire qu'il n'est pas possible de dire à l'avance
	 * si un colon sera jaloux en connaissant seulement la ressource qui lui a été attribué.
	 * 
	 * <p> On construira le coût d'une affectation en basant sur les listes de préférences du
	 * colon et de ses "ennemis" (ses voisins dans le graphe relationnel).
	 */
	private static void algorithmeHongrois() {
		//Initialisation de la matrice des coûts, appelés "risque", voir risqueAlgoHongrois(Colon)
		int[][] matRisque = new int[colonie.getTaille()][colonie.getTaille()];
		ArrayList<Colon> listeColons = new ArrayList<>(); //On voudra aussi conserver l'ordre des colons, pour plus tard
		for (Colon colon : colonie.getRelations().keySet()) {
			matRisque[listeColons.size()] = risqueAlgoHongrois(colon);
			listeColons.add(colon);
		}
		//La matrice obtenue associe à chaque ligne un colon et à chaque colonne une ressource
		//Un coefficient dans cette matrice est le coût (ou risque, voir risqueAlgoHongrois(Colon)) associé à l'affectation du colon de cette ligne à la ressource de cette colonne
		
		
		//Etape 0: soustraire à chaque coeff de chaque ligne le plus petit élément de cette ligne
		for (int i = 0; i < matRisque.length; i++) {
			int min = min(matRisque[i]);
			if (min != 0) 
				for (int j = 0; j < matRisque[i].length; j++) {
					matRisque[i][j] -= min;
				}
		}
		
		//Etape 0.5: idem pour les colonnes
		for (int i = 0; i < matRisque.length; i++) {
			//Recherche du minimum dans la colonne
			int min = matRisque[0][i];
			for (int j = 1; j < matRisque.length; j++) {
				if (min>matRisque[j][i]) min = matRisque[j][i];
				if (min == 0) break;
			}			
			
			if (min != 0)
				for (int j = 0; j < matRisque.length; j++) {
					matRisque[j][i] -= min;
				}
		}
		
		/* Recherche d'affectation optimale : zeros indépendants
		 * Maintenant qu'on a fait les premières soustractions, on peut commencer à chercher
		 * un ensemble de '0 indépendants'. C'est à dire n coefficients valant 0 dans la matrice
		 * tel que nuls ne partagent une ligne ou une colonne, cet ensemble définit alors une
		 * affectation de coût optimal.
		 */
		
		/**
		 * Classe outil pour simplifier la manipulation de matrices
		 */
		class Coefficient {
			public int ligne;
			public int colonne;
			
			public Coefficient(int ln, int col) {
				ligne = ln;
				colonne = col;
			}
			
			public int getLigne() {
				return ligne;
			}
			public int getColonne() {
				return colonne;
			}
		}
		
		/* Etape 1: Encardrer et barrer des zéros
		 * 1.a ligne qui comporte le moins de zéros non-barrés -> encadrer le premier 0 et barrer les autres zéros sur la meme ligne et colonne
		 * 1.b Répéter a jusqu'à épuisement
		 * CONDITION D'ARRET: Si on obtient n zéros en encadrés: on a obtenu notre affectation, sinon passer à l'étape 1
		 */
		
		/* Etape 2: Marquer et barrer des lignes/colonnes
		 * 2.a : Marquer toutes les lignes sans zero encadré
		 * 2.b : Marquer toute colonne ayant un zero barré sur une ligne marquée
		 * 2.c : Marquer toute ligne ayant un zero encadré dans une colonne marquée
		 * 2.d : Répéter b puis c jusqu'à ce qu'on puisse plus marquer de nouvelle ligne/colonne
		 * 2.e : Selectionner les lignes non marquées et les colonnes marquées
		 */
		
		/* Etape 3: Modification du tableau
		 * 3.a : Inverser la sélection 2.e, on obtient une sous matrice
		 * 3.b : Chercher le coefficient minimal de la sous matrice
		 * 3.c : Soustraire ce coeff aux coefficients de la sous matrice et l'ajouter aux coeffs selectionnés en ligne ET en colonne par l'étape 2.e
		 * 3.d : Revenir à l'étape 1
		 */
		boolean solutionOptimaleTrouvee = false;
		do {
			//Etape 1
			ArrayList<Coefficient> zeros = new ArrayList<>();
			int[] nombreZerosParLigne = new int[matRisque.length];
			for (int i = 0; i < matRisque.length; i++) {
				for (int j = 0; j < matRisque[i].length; j++) {
					if (matRisque[i][j] == 0) {
						zeros.add(new Coefficient(i,j));
						nombreZerosParLigne[i]++;
					}
				}
			}
			//1.a
			ArrayList<Coefficient> encadre = new ArrayList<>();
			ArrayList<Coefficient> barre = new ArrayList<>();
			while (!zeros.isEmpty()) {
				int ligneMin = firstIndexOf(min(nombreZerosParLigne), nombreZerosParLigne);
				for (int i = 0; i < zeros.size(); i++) {
					if (zeros.get(i).getLigne() == ligneMin) {
						Coefficient aEncadre = zeros.remove(i);
						encadre.add(aEncadre);
						ArrayList<Coefficient> toRemove = new ArrayList<>();
						for (Coefficient coef : zeros) {
							if (coef.getLigne() == aEncadre.getLigne() || coef.getColonne() == aEncadre.getColonne()) {
								barre.add(coef);
								toRemove.add(coef);
							}
						}
						zeros.removeAll(toRemove);
					}
				}
				nombreZerosParLigne[ligneMin] = Integer.MAX_VALUE;
			}
			
			//Condition d'arret
			if (encadre.size() == colonie.getTaille()) {
				ArrayList<Colon> affectation = new ArrayList<>();
				for(int i = 0; i < listeColons.size(); i++) {affectation.add(null);}
				for (Coefficient coef : encadre) {
					affectation.set(coef.getColonne(), listeColons.get(coef.getLigne()));
				}
				if (affectation.contains(null)) {
					System.err.println("DEBUG: Null element in assignment list");
				}
				solutionOptimaleTrouvee = true;
			} else {
				//Etape 2
				HashSet<Integer> lignesMarquees = new HashSet<>();
				HashSet<Integer> colonnesMarquees = new HashSet<>();
				
				//2.a
				for (int i = 0; i < matRisque.length; i++) {lignesMarquees.add(i);}
				for (Coefficient coef : encadre) {
					lignesMarquees.remove(coef.getLigne());
				}
				
				
				boolean step2d_a = true;
				boolean step2d_b = true;
				do {
					
					//2.b
					for (Coefficient coef : barre) {
						if (lignesMarquees.contains(coef.getLigne())) {
							step2d_a = colonnesMarquees.add(coef.getColonne());
						}
					}
					//2.c
					for (Coefficient coef : encadre) {
						if (colonnesMarquees.contains(coef.getColonne())) {
							step2d_b = lignesMarquees.add(coef.getLigne());
						}
					}
					
				} while (step2d_a || step2d_b);
				
				//2.e
				HashSet<Integer> selectLigne = new HashSet<>();
				for(int i = 0; i < matRisque.length; i++) {selectLigne.add(i);}
				selectLigne.removeAll(lignesMarquees);
				HashSet<Integer> selectColonne = new HashSet<>(colonnesMarquees);
				
				//Etape 3
				//3.a
				ArrayList<Integer> sousMatriceListe = new ArrayList<>();
				for (int i = 0; i < matRisque.length; i++) {
					for (int j = 0; j < matRisque[i].length; j++) {
						if (!selectLigne.contains(i) && !selectColonne.contains(j)) {
							sousMatriceListe.add(matRisque[i][j]);
						}
					}
				}
				
				//3.b
				int[] sousMatriceTab = new int[sousMatriceListe.size()];
				for (int i = 0; i < sousMatriceTab.length; i++) {
					sousMatriceTab[i] = sousMatriceListe.get(i);
				}
				int minSousMatrice = min(sousMatriceTab);
				
				//3.c
				for (int i = 0; i < matRisque.length; i++) {
					for (int j = 0; j < matRisque[i].length; j++) {
						if (!selectLigne.contains(i) && !selectColonne.contains(j)) {
							matRisque[i][j] -= minSousMatrice;
						} else if (selectLigne.contains(i) && selectColonne.contains(j)) {
							matRisque[i][j] += minSousMatrice;
						}
					}
				}
			}
		} while (!solutionOptimaleTrouvee);
		
	}
	
	/**
	 * Retourne les valeurs du "risque" associés à l'affectation d'un colon aux différentes
	 * ressources de la colonie.
	 * 
	 * <p> Principe : On pose qu'il existe un risque de jalousie d'un colon affecté à
	 * une ressource qui augmente si celui ci reçoit une ressource qui n'est pas la plus haute
	 * dans sa liste de préférence et si l'un de ses voisins la convoite (vue que tous les colons
	 * définissent des préférences, ce coût existe toujours mais est variable)
	 * 
	 * <p> Ce risque a deux composantes additionnées qui varient chacunes entre 0 et n-1 (n : taille de la colonie)
	 */
	private static int[] risqueAlgoHongrois(Colon colon) {
		int[] risque = new int[colonie.getTaille()];
		
		for (int i = 0; i < colonie.getTaille(); i++) {
			Ressource ressource = colonie.getRessources().get(i);
			
			//Coût primaire : rang des ressources dans la liste des préférences
			risque[i] = colon.getPreferences().indexOf(ressource);
			
			//Coût secondaire : rang des ressources dans la liste des préférences des voisins
			//Ce coût est ajusté pour être maximal lorsqu'un colon voisin préfère le plus possible la ressource et minimal à l'inverse
			for (Colon enConflit : colonie.getRelations().get(colon)) {
				risque[i] += (colonie.getTaille() - 1) - enConflit.getPreferences().indexOf(ressource);
			}
		}
		
		return risque;
	}
	
	/**
	 * Retourne le minimum d'une liste d'entiers naturels
	 */
	private static int min(int[] liste) {
		int min;
		try {
			min = liste[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Liste vide!");
			return Integer.MAX_VALUE;
		}
		for (int i = 1; i < liste.length; i++) {
			if (min>liste[i]) min = liste[i];
			if (min == 0) break;
		}
		return min;
	}
	
	/**
	 * Retourne le premier index d'un entier donné dans une liste
	 * Retourne {@code -1} si la valeur est absente de la liste
	 */
	private static int firstIndexOf(int n, int[] liste) {
		for (int i = 0; i < liste.length; i++) {
			if(liste[i] == n) {
				return i;
			}
		}
		return -1;
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
			String choix;
			do {
				System.out.println("Mode manuel (pas de fichier detecte). Acceder a la partie 2 apres la partie 1 ? (y/n)");
				choix = sc.nextLine();
				switch (choix) {
				case "y":
					colonie = restaureColonie(GestionColonie.partie1_main(sc));
					break;
				case "n":
					GestionColonie.partie1_main(sc);
					sc.close();
					System.exit(0);
					break;
				default:
					System.err.println("Choix incorrect : " + choix);
				}
			} while(!choix.equals("y") && !choix.equals("n"));
			
		}
		
		int choix;
		do {
			menu();
			choix = lireEntier(sc, "Choix : ");
			switch (choix) {
			case 1:
				choixAlgorithme(sc);
				break;
			case 2:
				sauvegarderSolution(sc);
				break;
			case 3:
				sauvegarderColonie(sc);
				break;
			case 4:
				break;
			default:
				System.err.println("Choix incorrect : " + choix);
			}
		} while (choix !=4);

		sc.close();
	}
	
	private static Colonie restaureColonie(up.mi.paa.projet.partie1.Colonie col)
	{

		HashMap<up.mi.paa.projet.partie1.Colonie.Colon, HashSet<up.mi.paa.projet.partie1.Colonie.Colon>> relations;
		relations = col.getRelations();
		
		HashMap<up.mi.paa.projet.partie1.Colonie.Colon, Colon> mappingColons = new HashMap<>();
		for (up.mi.paa.projet.partie1.Colonie.Colon colonPartie1 : relations.keySet()) {
	        Colon colonPartie2 = new Colon(colonPartie1.getNom());
	        colonPartie2.setPreferences(new ArrayList<>());
	        mappingColons.put(colonPartie1, colonPartie2);
	    }	

		
		//Relations
		HashMap<Colon, HashSet<Colon>> relationsPartie2 = new HashMap<>();
	    for (var entry : relations.entrySet()) {
	        Colon colonPartie2 = mappingColons.get(entry.getKey());
	        HashSet<Colon> relationsColon = new HashSet<>();
	        for (up.mi.paa.projet.partie1.Colonie.Colon voisin : entry.getValue()) {
	            relationsColon.add(mappingColons.get(voisin));
	        }
	        relationsPartie2.put(colonPartie2, relationsColon);
	    }

	    // Création des ressources pour la colonie partie 2
	    int tailleColonie = relations.size();
	    List<Ressource> ressourcesPartie2 = new ArrayList<>();
	    for(int i=0; i<tailleColonie; i++)
	    	ressourcesPartie2.add(new Ressource("R"+i));
	    
	   

	    for (up.mi.paa.projet.partie1.Colonie.Colon colonPartie1 : relations.keySet()) {
	        Colon colonPartie2 = mappingColons.get(colonPartie1);
	        ArrayList<Integer> indicesPreferences = colonPartie1.getPreferences();
	        ArrayList<Ressource> preferencesPartie2 = new ArrayList<>();
	        for (Integer index : indicesPreferences) {
	            if (index >= 0 && index < ressourcesPartie2.size()) {
	                preferencesPartie2.add(ressourcesPartie2.get(index));
	            }
	        }
	        colonPartie2.setPreferences(preferencesPartie2);
	    }

	    // Création de la colonie partie 2
	    List<Colon> colonsPartie2 = new ArrayList<>(mappingColons.values());
	    Colonie coloniePartie2 = new Colonie(colonsPartie2, ressourcesPartie2);
	    coloniePartie2.setRelations(relationsPartie2);

	    return coloniePartie2;
	}

}
