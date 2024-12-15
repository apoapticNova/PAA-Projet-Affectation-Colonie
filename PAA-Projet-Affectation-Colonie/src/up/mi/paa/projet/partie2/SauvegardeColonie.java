package up.mi.paa.projet.partie2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class SauvegardeColonie {

	/**
	 * Sauvegarde une colonie dans un fichier texte
	 * 
	 * @param colonie instance de colonie à sauvegarder
	 * @param filePath chemin du fichier
	 */
	public static void sauvegarder(Colonie colonie, String filePath) {
		//TODO: FACULTATIF
	}
	
	/**
	 * Sauvegarde une solution (affecation) trouvée pour une colonie dans un fichier texte
	 * 
	 * @param colonie instance de colonie à sauvegarder
	 * @param filePath chemin du fichier
	 */
	public static void sauvegarderAffectation(Colonie colonie, String filePath) {
		try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){
			//TODO
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1); //TODO: Pas sûr de garder ça
		}
	}
}
