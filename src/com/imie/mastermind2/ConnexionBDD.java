package com.imie.mastermind2;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnexionBDD {

	private static Connection connect;
	public static final String URL_BDD = "jdbc:postgresql://localhost:5432/MasterMind";
	private static String user_bdd = "";
	private static String pwd_bdd = "";
	

	public static Connection getInstance()
	{
		if(connect == null)
		{
			try 
			{
				Console cons = System.console();
				Scanner sc = new Scanner(System.in);
				if(user_bdd.isEmpty())
				{
					System.out.print("Login PostgresQL : ");
					user_bdd = sc.nextLine();
				}
				if(pwd_bdd.isEmpty())
				{
					System.out.print("Password PostgresQL : ");
					//ATTENTION l'instruction ci-dessous ne fonctionne pas sous eclipse, seulement dans une vraie console
					//pwd_bdd = new String(cons.readPassword()); //permet de saisir un mot de passe qui n'apparait pas en console
					pwd_bdd = sc.nextLine();
				}
				
				connect = DriverManager.getConnection(URL_BDD, user_bdd, pwd_bdd);
				System.out.println("Connexion à la BDD... OK");
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return connect;
	}
}
