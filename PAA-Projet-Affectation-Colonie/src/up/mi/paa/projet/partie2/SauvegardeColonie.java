package up.mi.paa.projet.partie2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;


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
			
			for (Colon colon: colonie.getAffectation()) {
				Ressource ressource = colonie.getRessources().get(colonie.getAffectation().indexOf(colon));
				pW.println(colon + ":" + ressource);
			}
			
			sauvegardeReussie = true;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return sauvegardeReussie;
	}
	
	public static boolean sauvegarderColonie(Colonie colonie, String filePath) {
		boolean sauvegardeReussie = false;
		try (PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){
			//lignes colon(nom).
			for (Colon colon : colonie.getRelations().keySet()) {
				pW.println("colon(" + colon + ").");
			}
			
			//lignes ressource(nom).
			for (Ressource ressource : colonie.getRessources()) {
				pW.println("ressource(" + ressource + ").");
			}
			
			//lignes deteste(nom1,nom2).
			HashSet<Colon> ignore = new HashSet<>();
			for (Colon colon : colonie.getRelations().keySet()) {
				for (Colon enConflit : colonie.getRelations().get(colon)) {
					if (!ignore.contains(enConflit))
						pW.println("deteste(" + colon + "," + enConflit + ").");
				}
				ignore.add(colon);
			}
			
			//lignes preferences(nom,nom_ressource1,nom_ressource2,...).
			for (Colon colon : colonie.getRelations().keySet()) {
				pW.print("preferences(" + colon);
				for (Ressource ressource : colon.getPreferences()) {
					pW.print("," + ressource);
				}
				pW.println(").");
			}
			
			sauvegardeReussie = true;
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return sauvegardeReussie;
	}
	
}
