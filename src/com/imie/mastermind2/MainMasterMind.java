package com.imie.mastermind2;

import java.util.Scanner;

public class MainMasterMind {

	public MainMasterMind() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		//Init de la connexion a la bdd, si on s'est bien connecte c'est parti
		if(ConnexionBDD.getInstance() != null)
			menuPrincipal();
		else
			System.out.println("La connexion à la BDD a échoué ! Aurevoir !");
	}
	
	public static void menuPrincipal()
	{
		Scanner sc = new Scanner(System.in);
		int choix;
		
		do {
			System.out.println("----------- MENU PRINCIPAL ------------");
			System.out.println(" 1 - Connexion");
			System.out.println(" 2 - Connexion en mode invité");
			System.out.println(" 3 - Création d'un nouveau compte");
			System.out.println(" 4 - Quitter");
			
			choix = saisirEntierEntre1Etn(4, "\nChoisir une option : ");
			
			switch(choix) {
			case 1:
				Joueur j = new Joueur(false);
				//verification des identifiants saisis, si OK alors on lance le mastermind avec le joueur en parametre
				if(j.verifierIdentifiants())
					new MasterMind2(4, 15, j);
				break;
			case 2:
				Invite inv = new Invite();
				//Lancement du mastermind avec le joueur invite
				new MasterMind2(4, 15, inv);
				break;
			case 3:
				System.out.println("-------- CREATION D'UN NOUVEAU COMPTE ---------");
				j = new Joueur(true);
				
				//On cree le compte du joueur et on verifie en meme temps si tout s'est bien passe
				if(j.creerJoueur())
				{
					System.out.println("Création du compte... OK !\nVous êtes connecté en tant que " + j.getLogin() + "\n\n");
					new MasterMind2(4, 15, j);
				}
				else
					System.out.println("Oups, une erreur est survenue, veuillez recommencer...");
				break;
			case 4:
				break;
			}
		}
		while(choix != 4);
		
		System.out.println("Bye bye !");
		
	}
	
	/**
	 * Methode qui permet de saisir un entier entre 1 et n et affiche le message message donne en parametre avant de saisir l'entier
	 * @param n : entier entre 1 et n
	 * @param message : message a afficher avant de saisir l'entier 
	 * @return l'entier saisi
	 */
	public static int saisirEntierEntre1Etn(int n, String message)
	{		
		Scanner sc = new Scanner(System.in);
		int choix;
		boolean choixValide;
		choixValide = false; //booleen pour savoir si le chiffre saisi est compris entre 1 et n

		do{
			System.out.print(message);
			choix = sc.nextInt();

			//on verifie si le choix est valide ou pas, sinon on informe le joueur de son erreur
			if(choix >= 1 && choix <= n)
				choixValide = true;
			//Sinon erreur de saisie
			else
				System.out.println("Attention, vous devez saisir un entier entre 1 et " + n + ".");
		}
		while(!choixValide); //tant que le choix n'est pas valide ou qu'il n'a pas demander a quitter

		return choix;
	}

}
