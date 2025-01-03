Projet de programmation – PAA

Date de rendu limite : 22/12/2024

Partage de biens d’une colonie

**Contexte:**

Après chaque mission de ravitaillement, le commandant d’une colonie spatiale doit repartir des ressources critiques entre les colons. Parmi ces ressources, chaque colon doit recevoir une de ces ressources critiques (e.g., un ´équipement ou une ration de vivres essentiels). Pour maintenir l’harmonie dans la colonie, le commandant demande à chaque membre de lui soumettre ses préférences sur les ressources allouées et doit tenter de les respecter au mieux. En plus de ces préférences, il doit tenir compte des relations entre les colons. Certains s’entendent mal, et une mauvaise répartition des ressources pourrait entraîner des conflits ou mettre en danger la survie de la colonie...

**Groupe:**

CHAKER Zakaria et COLLIERE Julie

**Description du projet:**

Ce projet vise à modéliser une colonie de colons, à gérer leurs relations et préférences, et à trouver une répartition optimale de ressources et surtout en minimisant les conflits entre colons.

La deuxième partie du projet automatise la configuration de la colonie et intègre un algorithme de résolution automatique pour améliorer les affectations. Le programme repose sur une interface texte avec gestion des fichiers et des erreurs utilisateur.

**Structure du projet:**  
Le projet est organisé en deux packages situés dans le dossier src:

- up.mi.paa.projet.partie1
- up.mi.paa.projet.partie2

**\*Partie 1:**

Ce package contient les classes principales pour gérer une colonie :

1. **Colonie.java** :
    - Construit une colonie à partir de sa taille. Si la taille de la colonie est inférieure ou égale à 26. Les colons sont nommés par des lettres: A, B, ..., Z. Si strictement supérieure à 26 les colons sont nommés C0, C1, ..., Cn.
    - Les ressources sont générées automatiquement en fonction de la taille de la colonie. Ce sont des entiers numérotés de 0 à n-1.
2. **GestionColonie.java** :
    - Implémente les fonctionnalités de gestion de la colonie, notamment l'ajout des préférences et des relations entre colons.
    - Propose une solution pour répartir les ressources en minimisant les conflits.

\***Partie 2:**

Cette partie introduit des fonctionnalités avancées et des classes supplémentaires :

1. **Colon.java** :
    - Modélise un colon avec un nom unique et des préférences.
2. **Colonie.java** :
    - Extension de Colonie (Partie1), introduit de nouvelles fonctionnalités permettant la lecture de Colonies depuis des fichiers texte.
3. **GestionColonieAméliorée.java** :
    - Fournit une gestion optimisée des colons et des ressources.
4. **ParserColonie.java** :
    - Permet de lire et d'analyser les données de colonie à partir de fichiers texte et vérifier la validité du fichier entré .
5. **Ressource.java** :
    - Modélise les ressources utilisées dans la colonie.
6. **SauvegardeColonie.java** :
    - Permet de sauvegarder la colonie sous forme de fichier texte.

**Fonctionnalités principales**

**1\. Lecture et validation d'un fichier de configuration**

Le fichier doit respecter le format suivant :

colon(nom_colon_1). colon(nom_colon_2). ressource(nom_ressource_1). ressource(nom_ressource_2). deteste(nom_colon_1,nom_colon_2). preferences(nom_colon_1,nom_ressource_1,nom_ressource_2). preferences(nom_colon_2,nom_ressource_2,nom_ressource_1).

**2\. Menu**

Une fois le fichier chargé avec succès, le programme propose trois options :

- **Résolution automatique**:
  - Utilise un algorithme approximatif pour trouver une affectation réduisant les conflits.
- **Sauvegarde de la solution**:
  - Enregistre l’affectation courante dans un fichier texte au format:

nom_colon_1:nom_ressource_1 nom_colon_2:nom_ressource_2

- **Fin du programme** :
  - Sortie du programme

**3\. Algorithme de résolution**

L'algorithme proposé par le document de projet repose sur les principes suivants :

- Initialisation avec une solution naïve (premier choix préféré).
- Optimisation par échanges locaux entre deux colons.
- Calcul du coût (évaluer le nombre de colons jaloux).

Un autre algorithme a été implémenté : l'algorithme hongrois ou algorithme de Kuhn-Munkres. Il repose sur les principes suivants :

- Manipulation de "matrice de coûts" pour obtenir une solution optimale
- Substitution du coût (nombre de colons jaloux) par le risque (possibilité que le colon soit jaloux)
- Plus de détails dans la javadoc du code (classe GestionColonieAmelioree -> algorithmeHongrois())
  - Références ayant servie à l'implémentation : 
    - [COURS SUR LA METHODE HONGROISE (KUHN).avi - Marc BEVERAGGI (via youtube)](https://www.youtube.com/watch?v=mwsaZ18caaM)
    - [Algorithme hongrois - Wikipedia](https://fr.wikipedia.org/wiki/Algorithme_hongrois)

**4\. Gestion des erreurs utilisateur**

- Messages clairs pour indiquer les erreurs :
  - Erreurs dans le fichier d’entrée (avec la ligne concernée).
  - Mauvaise saisie au menu (e.g., option invalide).
  - Gestion des exceptions (valeurs non numériques, fichiers inexistants).

**Exécution du programme**

1. **Classe principale** :

La classe contenant la méthode main est: up.mi.paa.projet.partie2.GestionColonieAmelioree

1. **Compilation/Exécution** :

La compilation requiert au moins Java 17.

`javac up/mi/paa/projet/partie2/GestionColonieAmelioree.java` (depuis la racine du projet)

Puis : 
  - `java GestionColonieAmelioree [cheminFichier]`
OU
  - `java GestionColonieAmelioree ""` pour utiliser le chemin de chargement par défaut (savedata/colonie.txt)
OU
  - `java GestionColonieAmelioree` pour accéder à la partie 1

**Fonctionnalités supplémentaires**

- Il est possible dans ce programme de sauvegarder la colonie construite manuellement dans un fichier texte en spécifiant le chemin d’accès.
