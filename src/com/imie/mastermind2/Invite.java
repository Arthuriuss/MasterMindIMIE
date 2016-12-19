package com.imie.mastermind2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Invite extends User {

	public Invite() {
		login = "invite";
		id = getIdInvite();
	}
	
	/**
	 * Fonction qui retourne l'id du compte invite qui est enregistre en bdd
	 * @return
	 */
	public int getIdInvite()
	{
		int id = 0;
		String query = "SELECT * FROM joueur WHERE login=?";
		try {
			PreparedStatement prepare = ConnexionBDD.getInstance().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepare.setString(1, "invite");
			
			ResultSet result = prepare.executeQuery();
			
			//Je recupere l'id du compte invite
			if(result.first())
				id = result.getInt(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}

}
