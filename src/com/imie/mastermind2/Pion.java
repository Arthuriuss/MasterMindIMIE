package com.imie.mastermind2;

public class Pion {

	private int couleur;
	
	public Pion(int couleur) {
		this.couleur = couleur;
	}

	public String toString() {
		return Integer.toString(couleur);
	}

	/**
	 * @return the couleur
	 */
	public int getCouleur() {
		return couleur;
	}

	/**
	 * @param couleur the couleur to set
	 */
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	

}
