package up.mi.paa.projet.partie2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class SauvegardeColonie {
	
	/**
	 * Sauvegarde une solution (affectation) trouvée pour une colonie dans un fichier texte
	 * 
	 * @param colonie instance de colonie à sauvegarder
	 * @param filePath chemin du fichier
	 */
	public static boolean sauvegarderAffectation(Colonie colonie, String filePath) {
		boolean sauvegardeReussie = false;
		try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){

			List<Colon> affectation = colonie.getAffectation();
			for (Colon c: affectation) {

				Ressource ressource = colonie.getRessources().get(colonie.getAffectation().indexOf(c));
				pW.println(c + ":" + ressource);

			}
			sauvegardeReussie = true;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return sauvegardeReussie;
	}
	

	
}
