package up.mi.paa.projet.partie2;

import up.mi.paa.projet.partie1.GestionColonie;


/**
 * La classe {@code GestionColonie} permet l'interaction entre l'utilisateur et
 * une instance de la classe {@code Colonie}.
 *
 * @author Julie Colliere
 * @author Zakaria Chaker
 */
public class GestionColonieAmelioree {

	private Colonie colonie;

	public GestionColonieAmelioree() {
		
	}

	public Colonie getColonie() {
		return this.colonie;
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
		//Dans un premier temps, on veut pouvoir récupérer le chemin d'un fichier dans args[0]
		//Si il n'y en a pas, on peut demander à l'utilisateur d'en entrer un ou directement passer à la partie 1
		
		try {
			String cheminFichier = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			GestionColonie.partie1_main(); //Passage direct à la partie 1
		}
		
		try {
			System.out.println(ParserColonie.fichierTexteValide("CheminAcces"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
