
import java.awt.event.*;
	//Cette classe permet de g"rer les clicksouris sur le damier
public class ClickAction extends MouseAdapter {
	//Cette variable permet d'acceder a la fonction jouer de la classe jeu
	private Jeu jeu;
	//variable de fin de partie
	private boolean finPartie;
	
	public ClickAction(Jeu jeu){
		//Initialisation des variables
		this.jeu=jeu;
		finPartie=false;
	}
	//Cette fonction fait appel a la fonction jouer si elle est appelé(lors d'un click souris sur le damier)
	//et si la partie n'est pa finie
	//elle envoie en parametre les coordonées de tableau
	public void mousePressed(MouseEvent event){
		if(!finPartie) this.jeu.jouer(transcription(event.getY()),transcription(event.getX()));
	}
	
	//Cette fonction permet de passer des coordonnées graphiques ( sur le panel) en coordonées de tableau
	public int transcription(int i){
		return (i-(i%Jeu.TAILLEIM))/Jeu.TAILLEIM;
	}
	
	//Cette fonction permet de mettre finPartie a true
	public void setFinPartie(){
		this.finPartie=true;
	}
}