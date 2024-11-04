package up.mi.paa.projet.partie1;

import java.util.InputMismatchException;
import java.util.Scanner;

import up.mi.paa.projet.partie1.Colonie.Colon;

/**
 * La classe {@code GestionColonie} permet l'interaction entre l'utilisateur et
 * une instance de la classe {@code Colonie}.
 *
 */
public class GestionColonie {

	/**
	 * Affichage du menu principal
	 */

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
		System.out.println("\n1) Ajouter une relation entre deux colons;" + "\n2) Ajouter les preferences d'un colon;"
				+ "\n3) Fin.");
	}

	/**
	 * Ajoute le nombre de colons dans la colonie. Verifie le type entré et le
	 * nombre de colons saisi.
	 * 
	 * Pour cette V1, la capcité est limitée à 26.
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
				saisie.next();
			}
		}
		return n;
	}

	/**
	 * Retourne le choix de l'utilisateur.
	 * 
	 * @param saisie
	 * @return int
	 */
	public static int saisieChoixMenu1(Scanner saisie) {
		int choix = -1;
		while (choix <= 0 || choix >= 4) {
			System.out.println("\nSaisir votre choix parmi (1-3): ");
			affichageMenu1();
			try {
				choix = saisie.nextInt();
				if (choix <= 0 || choix >= 4) {
					System.err.println("Attention. Choix saisi non valide.");
				}
			} catch (InputMismatchException erreur) {
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.next();
			}
		}
		return choix;
	}

	/**

	 * Méthode relationsEntreColons retourne si la methode s'est bien déroulée ou pas
	 * Elle permet de saisir une relation entre un colon A et un colon B
	 * Les exceptions sont normalement toutes prises en compte...

	 * Retourne les infiormations de la colonie sur sa gestion et les differentes
	 * modifications réalisées par l'user.
	 * @param saisie
	 * @return vrai ou faux si la methode se passe bien ou pas necessaire pour le switch case principal...
	 * @return une chaine de caractere donannt des infromations sur la colonie et
	 *         les derniers mouvements.
	 * 
	 */

	public boolean relationsEntreColons(Scanner saisie)
	{
		boolean val = false;
		
		while(!val)
		{
			try
			{
				Colon colonDepart = null, colonArrivee = null;
				String colon1, colon2;
				System.out.println("Saisir le nom du colon de depart: ");
				colon1 = saisie.next();
				System.out.println("\nSaisir le nom de l'autre colon, qui ne l'aime pas: ");
				colon2 = saisie.next();
				
				for(Colon c: this.colonie.getRelations().keySet())
				{
					if(c.getNom().equals(colon1))
						colonDepart = c;
					
					if(c.getNom().equals(colon2))
						colonArrivee = c;
					
				}
				
				val = this.colonie.ajouterRelation(colonDepart, colonArrivee);
			}catch(Exception e)
			{
				System.err.println(e.getMessage());
				val = false;
			}
		}
		return val;
		
	}
	
	
	public boolean preferencesColons(Scanner saisie)
	{
		boolean val = false;
		int cpt = 0;
		/**
		 * Saisir les preferences d'un colon
		 * Ex: A 1 2 3 4 
		 * Verifier qu'il y a bien exactement n ressources correspondant à n colons 
		 * 
		 */
		while(!val)
		{
			try {
				System.out.println(((cpt ==0) ? "S" : "A nouveau, s")+("aisir le nom du colon: "));
				String colon;
				colon = saisie.next();
				//TODO System.ou
			}catch(Exception e)
			{
				System.err.println(e.getMessage());
				cpt++;
				val = false;
			}
		}
		return val;
	}
	/**
	 * Méthode principale qui gere la colonie avec le switch case principal
	 * 
	 * @param saisie
	 * @return vrai/faux 
	 */

	public boolean gestionColonie(Scanner saisie) {
		int choix = -1;

		boolean processus = true, choixReboot = true;
		while(processus)
		{
			try
			{
				if(choixReboot)
					choix = saisieChoixMenu1(saisie);
			switch(choix)
			{
				case 1:
				{
					choixReboot = this.relationsEntreColons(saisie);
					break;
				}
				case 2:
				{
					System.out.println("\nAjout de preferences à un colon: ");
					break;
			    }
				case 3: 
				{
					System.out.println("Sortie du programme.");
					processus = false;
					break;
				}
				default: {
					break;
				}
			}
			}catch(InputMismatchException e)
			{
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.next();
			}
		}
		return true;
	}
	/**
	 * Retourne les infiormations de la colonie sur sa gestion et les differentes modifications réalisées par l'user.
	 * 
	 * @return une chaine de caractere donannt des infromations sur la colonie et les derniers mouvements.
	 */
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Etat actuel de la colonie: \n\n");
		sb.append(this.colonie.toString());
		/**
		 * TODO
		 * 
		 */
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
