package up.mi.paa.projet.partie2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserColonie {
	
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
		List<String> listeColons = null;
		try (BufferedReader br = new BufferedReader(new FileReader(cheminAcces))) {
			if(fichierTexteValide(cheminAcces))
			{
				System.out.println("Fichier valide");
				listeColons = new ArrayList<String>();
				String ligne = null;
				
				while ((ligne = br.readLine()) != null) 
				{
					if (ligne.startsWith("colon")) 
					{
						String colon = recupererColon(ligne);
	                    listeColons.add(colon);
	                }}
					if(listeColons.size()>0)
					{
						colonie = new Colonie(listeColons);
					}
					else
					{
						throw new Exception("Aucun colon trouve dans le fichier texte");
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
	/**
	 * Méthode qui permet de retourner le nombre de colons contenu dans notre fichier texte. Cette méthode verifie en amont d'abord si le fichier est valide afin de pouvoir effectuer la construction de la colonie de maniere sereine.
	 * 
	 * @param cheminAcces
	 * @return nbColons trouvé dans fichier Texte
	 * @throws Exception
	 */
	public static int getNbColonsFichier(String cheminAcces) throws Exception
	{
		int nbColons = 0;
		if (!fichierTexteValide(cheminAcces)) 
		{
			throw new Exception("Fichier invalide");
		}
		try(BufferedReader br = new BufferedReader(new FileReader(cheminAcces))) 
		{
			String ligne;
			while ((ligne = br.readLine()) != null) 
			{
				if (ligne.startsWith("colon")) 
				{
					nbColons++;
				}
			}
		}catch(Exception erreur)
		{
			System.err.println(erreur.getMessage());
		}
		return nbColons;
	}
	
	public static void main(String[]args)
	{
		try {
			System.out.println(getNbColonsFichier("D:\\chaker_zakaria\\Universite_Licence\\Universite_Paris_Cite\\L3_info\\S5\\Colonie.txt"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
