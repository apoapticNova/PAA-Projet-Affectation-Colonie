package up.mi.paa.projet.partie1;

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
	
	public void affichageMenu()
	{
		System.out.println("1) Ajouter une relation entre deux colons;"
				+ "2. Ajouter les préferences d'un colon;"
				+ "3) Fin.");
	}
	/**
	 * 
	 * @param saisie
	 * @throws Exception
	 */
	public static void ajouterNbColons(Scanner saisie) throws Exception 
	{
		System.out.println("Saisir la taille de la colonie \n(Attention pour cette premiere version la capacité maximale est de 26 colons): ");
		
		int n = saisie.nextInt();
		if(n<=0)
			throw new Exception("Taille de la colonie ne peut pas être nulle/négative.");
	}
	
}
