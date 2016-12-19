package com.imie.mastermind2;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MasterMind2 {

	private Pion[] combinaison;
	private Pion[] copieCombinaison;
	private Pion[] proposition;
	private User joueur;
	private int bienPlaces, malPlaces, nbTentatives;
	private boolean abandon, replay;

	public MasterMind2(int n, int nbTentativesMax, User j)
	{
		joueur = j;
		combinaison = new Pion[n];
		copieCombinaison = new Pion[n];
		proposition = new Pion[n];
		int bienPlaces, malPlaces, nbTentatives;
		boolean abandon, replay;

		
		do
		{
			System.out.println("\nBienvenue dans le jeu du MasterMind " + j.getLogin() + "! ");
			System.out.println("Les règles du jeu sont simple. Vous devez trouver une combinaison mystère de " + n + " chiffres dans le bon ordre. \n"
					+ "Vous avez le droit à " + nbTentativesMax + " tentatives maximum. \n"
					+ "Chaque chiffre est compris entre 1 et 8. Vous pouvez taper le chiffre 0 si vous souhaitez abandonner. \n\nBon jeu !\n");

			//On cree la combinaison a trouver
			combinaison = combinaisonAleatoire(n);
			
			//Decommenter la ligne ci-dessous si on veut afficher la solution au départ
			System.out.println(Arrays.toString(combinaison));

			bienPlaces = malPlaces = nbTentatives = 0;
			abandon = replay = false;
			
			do
			{
				//on recopie le tableau de la combinaison originale à trouver pour garder l'originale intacte
				copieCombinaison = recopieTableau(combinaison);

				//on demande au joueur de saisir sa proposition
				abandon = saisirProposition(n, proposition);

				//on verifie que le joueur n'a pas voulu abandonner
				if(!abandon)
				{
					//on regarde le nombre de bien places et de mal places
					bienPlaces = bienPlaces(combinaison, proposition, copieCombinaison);

					//maintenant les mal places
					malPlaces = malPlaces(copieCombinaison, proposition);

					//on affiche les resultats
					afficheResultat(bienPlaces, malPlaces);

					//on a fait une tentative de plus
					nbTentatives++;
					System.out.println("Il vous reste " + (nbTentativesMax-nbTentatives) + " tentatives.");
				}

			}
			while(!abandon && bienPlaces != n && nbTentatives < nbTentativesMax);

			//on regarde maintenant si on a arreter a cause d'un abandon ou d'une victoire ou d'une defaite
			System.out.println();
			if(abandon)
				System.out.println("Vous êtes un looser !!!!");
			else if(bienPlaces == n)
				System.out.println("Bravo, vous êtes un champion ! Vous avez trouvé en " + nbTentatives + " tentatives.");
			else
				System.out.println("Dommage, vous avez perdu ! Vous avez épuisé toutes vos tentatives.");

			//On demande au joueur s'il veut rejouer
			replay = ouiOuNon("Souhaitez-vous rejouer (\"o\" pour oui, \"n\" pour non) : ");
			System.out.println();
		}
		while(replay);
		
		System.out.println("Aurevoir " + j.getLogin() + "!");

	}

	/**
	 * Fonction qui compte le nombre de chiffres bien places
	 * @param combinaison combinaison à trouver
	 * @param proposition proposition du joueur
	 * @param combinaisonModifiee tableau qui contient la combinaison mais avec les chiffres bien places remplaces par des zero
	 * @return renvoie le nombre de chiffres bien places
	 */
	public static int bienPlaces(Pion[] combinaison, Pion[] proposition, Pion[] combinaisonModifiee)
	{
		int bienPlaces = 0;

		//On compare chaque case de meme indice pour trouver les bien places
		for(int i=0; i<combinaison.length; ++i)
		{
			if(combinaison[i].getCouleur() == proposition[i].getCouleur())
			{
				combinaisonModifiee[i].setCouleur(0); //on retient que cette case est un chiffre bien place
				proposition[i].setCouleur(0); //idem
				bienPlaces++;
			}
			else
				combinaisonModifiee[i].setCouleur(combinaison[i].getCouleur()); //sinon on recopie le chiffre initial qui n'est pas un bien place
		}

		return bienPlaces;
	}

	/**
	 * Fonction qui renvoie le nombre de mal places. Attention a appeler cette fonction avec les tableaux qui contiennent les 0
	 * mémorisant les chiffres deja bien places qu'on ne doit pas traiter.
	 * @param combinaisonModifie la combinaison originale ou chaque case contenant un zero est un chiffre qui a ete bien place, donc a ne pas traiter
	 * @param propositionModifie proposition du joueur modifie en fonction de la fonction bienPlace avec des case à zero
	 * @return renvoie le nombre de chiffres mal places
	 */
	public static int malPlaces(Pion[] combinaisonModifie, Pion[] propositionModifie)
	{
		Pion[] copieCombinaison = new Pion [combinaisonModifie.length]; //tableau qui recopie la combinaison afin de ne pas perdre l'originale
		int malPlaces  = 0;

		//On peut maintenant avec ce tableau trouver le nombre de mal places en remplacant par des zero les cases deja traites
		//On compare chaque case de combinaisonModifie...
		for(int i = 0; i<combinaisonModifie.length; ++i)
		{
			//...avec chaque case du tableau proposition
			for(int j=0; j<combinaisonModifie.length; ++j)
			{
				//si notre case n'est pas un zero c'est qu'il s'agit peut-etre d'un chiffre mal place
				if(combinaisonModifie[i].getCouleur() != 0 && propositionModifie[j].getCouleur() != 0 && combinaisonModifie[i].getCouleur() == propositionModifie[j].getCouleur())
				{
					//on memorise notre chiffre mal place qu'on ne doit plus traite, ATTENTION aux i et j !
					combinaisonModifie[i].setCouleur(0);
					propositionModifie[j].setCouleur(0);
					malPlaces++; //un chiffre de plus mal place
				}
			}
		}

		return malPlaces;
	}

	/**
	 * Genere un tableau de taille n de maniere aleatoire. Chaque case a une valeur comprise entre 1 et 8.
	 * @param n taille de notre combinaison
	 * @return Renvoie le tableau genere
	 */
	public static Pion[] combinaisonAleatoire(int n)
	{
		Pion[] combi = new Pion[n];
		Random rand = new Random();
		//initialisation de chaque case de maniere aleatoire (chiffre entre 1 et 8)
		for(int i=0; i<n; ++i)
		{
			combi[i] = new Pion(rand.nextInt(8)+1);
		}

		return combi;
	}



	/**
	 * Fonction pour saisir une entier entre 0 et 8. Quand le joueur tape 0 il a decide de quitter.
	 * @param n numero du chiffre a saisir
	 * @return renvoie l'entier saisie par le joueur.
	 */
	public static int saisirEntierEntre0Et8(int n)
	{		
		Scanner sc = new Scanner(System.in);
		int choix;
		boolean choixValide, quit;
		choixValide = false; //booleen pour savoir si le chiffre saisi est compris entre 1 et 8
		quit = false; //booleen pour savoir si le joueur souhaite quitter le jeu ou pas (si il a tape un 0)

		do{
			System.out.print("Chiffre " + n + " : ");
			choix = sc.nextInt();

			//on verifie si le choix est valide ou pas, sinon on informe le joueur de son erreur
			if(choix >= 1 && choix <= 8)
				choixValide = true;
			//Si il a tape un 0 on lui demande de confirmer au cas ou ce n'etait pas volontaire
			else if(choix == 0)
			{
				quit = ouiOuNon("Voulez-vous vraiment abandonner (\"o\" pour oui, \"n\" pour non) : ");
			}
			//Sinon erreur de saisie
			else
				System.out.println("Attention, vous devez saisir un entier entre 1 et 8, ou 0 pour quitter.");
		}
		while(!choixValide && !quit); //tant que le choix n'est pas valide ou qu'il n'a pas demander a quitter

		return choix;
	}

	/**
	 * Fonction pour saisir la proposition du joueur
	 * @param n taille de la combinaison
	 * @param proposition place la proposition du joueur dans ce tableau
	 * @return renvoie true si le joueur souhaite abandonner, sinon false
	 */
	public static boolean saisirProposition(int n, Pion[] proposition)
	{
		boolean quit = false;
		int i=0;

		System.out.println("Saisissez une combinaison de " + n + " chiffres compris entre 1 et 8 (0 pour abandonner).");
		while(!quit && i<n)
		{
			proposition[i] = new Pion(saisirEntierEntre0Et8(i+1));

			//on verifie si le joueur a voulu quitter
			if(proposition[i].getCouleur() == 0)
				quit = true;

			i++;
		}

		return quit;
	}

	/**
	 * Fonction qui recopie le tableau donne en parametre et renvoie une copie de ce tableau
	 * @param tableau tableau a recopie
	 * @return renvoie le nouveau tableau copie
	 */
	public static Pion[] recopieTableau(Pion[] tableau)
	{
		Pion[] copie = new Pion [tableau.length];

		for(int i=0; i<tableau.length; ++i)
		{
			copie[i] = new Pion(tableau[i].getCouleur());
		}

		return copie;
	}

	/**
	 * Affiche le nombre bien places et mal places en fonction des parametres donnes
	 * @param b nb de bien places
	 * @param m nb de mal places
	 */
	public static void afficheResultat(int b, int m)
	{
		System.out.println("\n" + b + " chiffre(s) bien placé(s) \n" + m + " chiffre(s) mal placé(s) \n");
	}

	public static boolean ouiOuNon(String question)
	{
		Scanner sc = new Scanner(System.in);
		String rp;
		do {
			System.out.print(question);
			rp = sc.nextLine();
		}
		while(!rp.equals("o") && !rp.equals("n")); //on continue tant qu'il n'a pas saisie "o" ou "n"

		//On verifie sa reponse, si oui on memorise son choix dans le booleen quit
		if(rp.equals("o"))
			return true;
		else 
			return false;
	}
	
}
