package com.imie.mastermind2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Joueur extends User {

	private String mdp;
	private String nom;
	private String prenom;
	private String mail;
	
	
	/**
	 * Constructeur d'un joueur
	 */
	public Joueur(boolean creation) {
		saisirIdentifiants(creation);
	}
	
	/**
	 * Fonction qui demande a l'utilisateur de saisir ses identifiants
	 */
	public void saisirIdentifiants(boolean creation)
	{
		boolean pwdConfirm = false;
		Scanner sc = new Scanner(System.in);
		System.out.print("login : ");
		login = sc.nextLine();
		do {
			System.out.print("pwd : ");
			mdp = sc.nextLine();
			
			//Si on est dans une creation de compte, on doit confirmer le mdp
			if(creation)
			{
				System.out.print("confirm pwd : ");
				
				//On confirme le mot de passe
				if(!sc.nextLine().equals(mdp))
					System.out.println("Attention, vous avez entré deux mots de passe différents, veuillez recommencer svp.");
				else
					pwdConfirm = true;
			}
			
		}while(!pwdConfirm && creation);
		
		//Si c'est une creation de compte
		if(creation)
		{
			System.out.print("nom : ");
			nom = sc.nextLine();
			System.out.print("prenom : ");
			prenom = sc.nextLine();
		}
	}
	
	/**
	 * Fonction qui creer un joueur en BDD
	 * @return true si tout s'est bien passe, faux sinon
	 */
	public boolean creerJoueur()
	{
		boolean res = false;
		String query = "INSERT INTO joueur (login, pwd, nom, prenom) VALUES (?,?,?,?) RETURNING id_user;";
		try {
			PreparedStatement prepare = ConnexionBDD.getInstance().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			prepare.setString(1, login);
			prepare.setString(2, mdp);
			prepare.setString(3, nom);
			prepare.setString(4, prenom);
			
			//On execute la requete
			prepare.execute();
				
			//Si la requete s'est bien passee, on recupere le id_user qui a ete genere
			ResultSet result = prepare.getResultSet();
			if(result.first())
			{
				id = result.getInt(1);
				res = true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Fonction qui verifie les identifiants saisis par l'utilisateur
	 * @return true si tout est ok, false sinon
	 */
	public boolean verifierIdentifiants()
	{
		String query = "SELECT * FROM joueur WHERE login=?";
		String query2 = "SELECT * FROM joueur WHERE login=? AND pwd=?";
		try {
			PreparedStatement prepare = ConnexionBDD.getInstance().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepare.setString(1, login);
			
			ResultSet result = prepare.executeQuery();
			
			//Si login existe
			if(result.first())
			{
				PreparedStatement prepare2 = ConnexionBDD.getInstance().prepareStatement(query2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				prepare2.setString(1, login);
				prepare2.setString(2, mdp);
				
				ResultSet result2 = prepare2.executeQuery();
				//user identifie
				if(result2.first())
				{
					System.out.println("\nIdentification OK...\n");
					
					//On remplit nos attibuts
					id = result2.getInt("id_user");
					if(result2.getString("nom") != null)
						nom = result2.getString("nom");
					if(result2.getString("prenom") != null)
						prenom = result2.getString("prenom");
					
					return true;
				}
				//Mot de passe non valide !
				else
				{
					System.out.println("\nAttention le mot de passe est invalide !\n");
					return false;
				}
			}
			else
			{
				System.out.println("\nAttention le login saisi n'existe pas !\n");
				return false;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * @return the mdp
	 */
	public String getMdp() {
		return mdp;
	}

	/**
	 * @param mdp the mdp to set
	 */
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	

}
