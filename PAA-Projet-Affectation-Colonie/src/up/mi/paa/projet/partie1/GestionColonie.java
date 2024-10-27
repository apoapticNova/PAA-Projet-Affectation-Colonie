package up.mi.paa.projet.partie1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * La classe {@code GestionColonie} permet l'interaction entre l'utilisateur
 * et une instance de la classe {@code Colonie}. 
 *
 */
public class GestionColonie {

	/**
	 * Affichage du menu principal
	 */
	
	private Colonie colonie;
	
	public GestionColonie(Scanner sc)
	{
		this.colonie = new Colonie(saisieTailleColonie(sc));
	}
	
	public Colonie getColonie()
	{	return this.colonie;
	}
	public static void affichageDebut()
	{
		System.out.println("********Partage de biens d'une colonie spatiale********\n");
	}
	public static void affichageMenu1()
	{
		System.out.println("\n1) Ajouter une relation entre deux colons;"
				+ "\n2) Ajouter les preferences d'un colon;"
				+ "\n3) Fin.");
	}
	/**
	 * Ajoute le nombre de colons dans la colonie.
	 * Verifie le type entré et le nombre de colons saisi. 
	 * 
	 * Pour cette V1, la capcité est limitée à 26.
	 * 
	 * @param saisie un Scanner
	 */
	public static int saisieTailleColonie(Scanner saisie) 
	{
		int n = -1; 
		while(n<=0)
		{
			System.out.println("\nSaisir la taille de la colonie.\nAttention pour cette premiere version la capacite maximale est de 26 colons:");
			try {
				n = saisie.nextInt();
				if(n<=0)
					System.err.println("Erreur: La colonie ne peut pas avoir une taille inferieure a 0. ");
				else if(n> 26)
				{
					System.err.println("Erreur: La colonie a une capacite limitee a 26...");
					n = 0;
				}
			}catch(InputMismatchException erreur)
			{
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
	public static int saisieChoixMenu1(Scanner saisie)
	{
		int choix = -1;
		while(choix<=0 || choix>=4)
		{
			System.out.println("\nSaisir votre choix parmi (1-3): ");
			affichageMenu1();
			try {
				choix = saisie.nextInt();
				if(choix<=0 || choix>=4)
				{
					System.err.println("Attention. Choix saisi non valide.");
				}
			}catch(InputMismatchException erreur)
			{
				System.err.println("Attention. Saisir un nombre entier.");
				saisie.next();
			}
		}
		return choix;
	}
	/**
	 * Retourne les infiormations de la colonie sur sa gestion et les differentes modifications réalisées par l'user.
	 * 
	 * @return une chaine de caractere donannt des infromations sur la colonie et les derniers mouvements.
	 * 
	 */
	
	public boolean gestionColonie(Scanner saisie)
	{
		int choix = -1;
		boolean processus = true;
		while(processus)
		{
			choix = saisieChoixMenu1(saisie);
			switch(choix)
			{
			case 1:
			{
				//A enlever 
				System.out.println("\nAjout de relations entre 2 colons: ");
				//TODO
				break;
			}
			case 2:
			{
				//A enlever 
				System.out.println("\nAjout de preferences à un colon: ");
				//TODO
				break;
			}
			case 3: 
			{
				System.out.println("Sortie du programme.");
				processus = false;
				break;
			}
			default:
			{
				break;
			}
			}
		}
		return true;
	}
	@Override
	public String toString()
	{
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
	 * main qui permet pour le moment de réaliser les tests. Un main sera de toute façon utilisé pour faire fonctionner l'interface user.
	 */
	public static void main(String[]args)
	{
		affichageDebut();
		Scanner sc = new Scanner(System.in);
		GestionColonie gC = new GestionColonie(sc);
		gC.gestionColonie(sc);
		System.out.println(gC.toString());
		sc.close();
	}
	
}
