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
	 * Pour que le fichier texte soit valide, le fichier doit respecter un ordre précis.
	 * - Il faudra définir d'abord le(s) colon(s) -> les ressources -> la relation potentielle entre deux colons -> les preferences.
	 * - Pour qu'il soit valide il faut avoir 0 fautes d'orthographe et 0 saut de ligne.
	 * - Autant de prereferences que de colons que ressources.
	 * 
	 * @param cheminAcces
	 * @return Colonie construite depuis chemin du fichier
	 * @throws Exception
	 */
	public static boolean fichierTexteValide(String cheminAcces) throws Exception {
	    boolean valide = false;
	    String [] collection = {"colon", "ressource", "deteste", "preferences"};
	   
	    Set<String> colons = new HashSet<>();
	    Set<String> ressources = new HashSet<>();
	    Set<String> detestes = new HashSet<>();
	    Set<String> preferences = new HashSet<>();
	    
	    try(BufferedReader br = new BufferedReader(new FileReader(cheminAcces))) {
	        String ligne = null;
	        int ordre = 0;
	        int nbColons = 0, nbRessources = 0, nbPreferences = 0;
	        
	        while((ligne = br.readLine()) != null) {
	            if(ligne.isEmpty()) {
	                throw new Exception("Erreur: Ligne vide detectee dans le fichier texte");
	            }
	            
	            if (!ligne.endsWith(".")) {
	                throw new Exception("Erreur: Chaque ligne doit se terminer par un point. Voir ligne: " + ligne);
	            }

	            boolean motCle = false;
	            for(int i=0; i<collection.length; i++) {
	                if(ligne.startsWith(collection[i])) {
	                    motCle = true;
	                    if(i<ordre)
	                        throw new Exception("Erreur: Ordre des requetes non respecte. Voir ligne: "+ligne);
	                    
	                    ordre = Math.max(ordre, i);
	                    break;
	                }
	            }
	            if(!motCle)
	                throw new Exception("Erreur: Mot mal saisi ou inconnu. Ligne: "+ligne);
	            
	            if(ligne.startsWith("colon")) {
	                String colon = recupererColon(ligne);
	                if(!colons.add(colon)) {
	                    throw new Exception("Erreur: colon deja saisi (il ne peut pas y avoir de doublons) : "+ligne);
	                }
	                nbColons++;
	            }
	            else if(ligne.startsWith("ressource")) {
	                String ressource = recupererRessources(ligne);
	                if(!ressources.add(ressource)) {
	                    throw new Exception("Erreur: ressource deja saisie (il ne peut pas y avoir de doublons) : "+ligne);
	                }
	                nbRessources++;
	            }
	            else if(ligne.startsWith("deteste")) {
	                String [] elements = recupererDeteste(ligne);
	                if(!colons.contains(elements[0]) || !colons.contains(elements[1])) {
	                    throw new Exception("Erreur: le(s) colon(s) saisi(s) est/sont inconnu ou mal saisi. Ligne: "+ligne);
	                }
	                String chaine  = (elements[0]+","+elements[1]);
	                if(!detestes.add(chaine))
	                    throw new Exception("Erreur: la relation existe déjà. Ligne: "+ligne);
	            }
	            else if(ligne.startsWith("preferences"))
	            {
	                String [] tab = recupererPreferences(ligne);
	                if (tab.length != (nbColons + 1)) {
	                    throw new Exception("Erreur: le nombre de preferences doit etre egal a n+1 (n = nombre de colons). Ligne: " + ligne);
	                }
	               
	                
	                for (String colon: tab) {
	                    if (!colons.contains(colon) && !ressources.contains(colon)) {
	                        throw new Exception("Erreur: le parametre " + colon + " dans les preferences n'a pas deja ete declare.");
	                    }
	                }
	                
	                Set<String> preferencesSet = new HashSet<>(Arrays.asList(tab)); // Utilisation d'un Set pour détecter les doublons
	                if (preferencesSet.size() != tab.length) {
	                    throw new Exception("Erreur: doublon detecte dans les preferences de cette ligne: " + Arrays.toString(tab));
	                }
	                
	                for (String pref : tab) {
	                    if (!preferences.add(pref)) {
	                       
	                    }
	                }
	                nbPreferences++;
	            }
	        }

	        if(nbColons ==0 || nbRessources==0 || nbPreferences ==0) {
	            throw new Exception("Erreur: le fichier doit comporter au moins un colon, une ressource et une preferences");
	        }
	        if(nbColons != nbPreferences) {
	            throw new Exception("Erreur: il doit y avoir autant de colons que de preferences");
	        }
	        if(nbColons != nbRessources) {
	            throw new Exception("Erreur: Il doit y avoir autant de colons que de ressources");
	        }
	        
	        valide = true;
	    } catch(IOException erreur) {
	        throw new Exception("Erreur lors de la lecture du fichier texte: " + erreur.getMessage());
	    }
	        
	    return valide;
	}

	/**
	 * Methode qui permet de recuperer le colon depuis la ligne recupérée.
	 * - Verifie si la ligne est correcte.
	 * - Si elle commence par colon( et se termine par une parenthese ).
	 * - Si la ligne n'est pas correcte erreur.
	 * 
	 * @param ligne
	 * @return chaine colon
	 * @throws Exception
	 */
	public static String recupererColon(String ligne) throws Exception {
	    if (ligne.startsWith("colon(") && ligne.endsWith(").")) {
	    	String colon = ligne.split("\\(")[1].split("\\)")[0].replaceAll("\\.$", "");
	        if (colon.isEmpty()) {
	            throw new Exception("Erreur: nom du colon manquant ou incorrect. Ligne: " + ligne);
	        }
	        return colon;
	    } else {
	        throw new Exception("Erreur: ligne non valide: " + ligne);
	    }
	}
	
	/**
	 * Methode qui permet de recuperer la ressource depuis la ligne recupérée.
	 * - Verifie si la ligne est correcte.
	 * - Si elle commence par ressource( et se termine par une parenthese ).
	 * - Si la ligne n'est pas correcte erreur.
	 * 
	 * @param ligne
	 * @return chaine ressource
	 * @throws Exception
	 */
	public static String recupererRessources(String ligne) throws Exception {
	    if (ligne.startsWith("ressource(") && ligne.endsWith(").")) {
	    	String ressource = ligne.split("\\(")[1].split("\\)")[0].replaceAll("\\.$", "");
	        if (ressource.isEmpty()) {
	            throw new Exception("Erreur: nom de la ressource manquant ou incorect dans la ligne: " + ligne);
	        }
	        return ressource;
	    } else {
	        throw new Exception("Erreur: ligne non valide: " + ligne);
	    }
	}
	/**
	 * Methode qui permet de recuperer un tableau de chaine des relations depuis la ligne recupérée.
	 * - Verifie si la ligne est correcte.
	 * - Si elle commence par deteste( et se termine par une parenthese ).
	 * - Si la ligne n'est pas correcte erreur.
	 * 
	 * @param ligne
	 * @return tableau de chaine deteste
	 * @throws Exception
	 */
	public static String [] recupererDeteste(String ligne) throws Exception {
	    if (ligne.startsWith("deteste(") && ligne.endsWith(").")) {
	    	String chaine = ligne.split("\\(")[1].split("\\)")[0];
	        String [] elements = chaine.split(",");
	        if(elements[0].equals(elements[1]))
	        	throw new Exception("Erreur: Il doit y avoir deux colons differents qui se detestent. Ligne: "+ligne);
	        if (elements.length != 2) {
	            throw new Exception("Erreur: Il doit y avoir exactement deux colons dans la relation. Ligne: " + ligne);
	        }
	        return elements;
	    } else {
	        throw new Exception("Erreur: ligne non valide: " + ligne);
	    }
	}
	
	/**
	 * Methode qui permet de recuperer un tableau de chaine des preferences depuis la ligne recupérée.
	 * - Verifie si la ligne est correcte.
	 * - Si elle commence par preferences( et se termine par une parenthese ).
	 * - Si la ligne n'est pas correcte erreur.
	 * 
	 * @param ligne
	 * @return tableau de chaine preferences
	 * @throws Exception
	 */
	public static String [] recupererPreferences(String ligne) throws Exception {
	    if (ligne.startsWith("preferences(") && ligne.endsWith(").")) {
	        String [] tab = ligne.substring("preferences(".length(), ligne.length() - 2).trim().split(",");
	        if (tab.length == 0) {
	            throw new Exception("Erreur: les preferences ne peuvent pas être vides. Ligne: " + ligne);
	        }
	        return tab;
	    } else {
	        throw new Exception("Erreur: ligne non valide: " + ligne);
	    }
	}
	/**
	 * Apres avoir validé la coherence du fichier texte entré en parametre. Il nous sera possible de construire la colonie sans soucier de verifications supplémentaires et don éviter d'alourdir le code.
	 * - La méthode perettera de retourner une colonie construite en fonction du fichier texte
	 * - La verification des elements entrés en parametre se fera ici.
	 * - Si erreur: message erreur. 
	 * 
	 * @param cheminAcces
	 * @return
	 * @throws Exception
	 */
	public Colonie constructionColonieFichier(String cheminAcces) throws Exception 
	{
		Colonie colonie = null;
		try (BufferedReader br = new BufferedReader(new FileReader(cheminAcces))) {
			if(fichierTexteValide(cheminAcces) == true)
			{
				System.out.println("Syntaxe fichier valide");
				String ligne = null;
				
				if((ligne = br.readLine())!= null)
				{
					
				}
			}
			else 
			{
				System.err.println("Syntaxe fichier non valide");
			}
		}catch(Exception erreur)
		{
			System.err.println(erreur.getMessage());
		}
		return colonie;
	}
		
	public static int getNbColonsFichier(String cheminAcces) throws Exception
	{
		int nbColons = 0;
		  try (BufferedReader br = new BufferedReader(new FileReader(cheminAcces))) 
		  {
			  if(fichierTexteValide(cheminAcces) == true)
			  {
			  String ligne;
			  while ((ligne = br.readLine()) != null) {
		      if (ligne.startsWith("colon")) {
		          nbColons++;
		       }
			  }
		   }
			
		}catch(Exception erreur)
		{
			System.err.println(erreur.getMessage());
		}
		return nbColons;
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
			System.out.println(fichierTexteValide("CheminAcces"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
