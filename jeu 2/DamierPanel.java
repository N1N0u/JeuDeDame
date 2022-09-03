import javax.swing.*;
import java.awt.*;


public class DamierPanel extends JPanel {
	//tableau de type damier qui permet l'afichage du damier graphique
	int[][] mat;
	//différentes images
	// S=Sélectionné	P=qui peut Prendre
	Image noir,blanc,pionN,pionB,pionBS,pionNS,pionBP;

	public DamierPanel(int[][] mat,ClickAction listener){
		//initialisation des variables
		this.mat=mat;
		//permet de gérer la gestion des click dans la classe ClickAction
		addMouseListener(listener);
		Toolkit kit=Toolkit.getDefaultToolkit();
		MediaTracker tracker=new MediaTracker(this);
		this.blanc=kit.getImage("blancµ.jpg");
		
		/**/
		this.blanc=kit.getImage("blanc.jpg");
		this.noir=kit.getImage("noir.jpg");
		this.pionB=kit.getImage("pionB.jpg");
		this.pionN=kit.getImage("pionN.jpg");
		this.pionBS=kit.getImage("pionBS.jpg");
		this.pionNS=kit.getImage("pionNS.jpg");
		this.pionBP=kit.getImage("pionBP.jpg");
		tracker.addImage(blanc,0);
		tracker.addImage(noir,0);
		tracker.addImage(pionB,0);
		tracker.addImage(pionN,0);
		try {tracker.waitForID(0);}
		catch(InterruptedException e){}
	}

	public void paintComponent(Graphics g){

		int i,j;
		//Pour chaque ligne
		for (i=0;i<Jeu.NBCASES;i++){
			//pour chaque colonne
			for (j=0;j<Jeu.NBCASES;j++){
				//-10 -> case blanche
				if (this.mat[i][j]==-10){
					g.drawImage(this.blanc,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				0 -> case noir
				else if(this.mat[i][j]==0){
					g.drawImage(this.noir,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				1 -> pion blanc
				else if(this.mat[i][j]==1){
					g.drawImage(this.pionB,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				-1 -> pion noir
				else if(this.mat[i][j]==-1){
					g.drawImage(this.pionN,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				2 -> pion Joueur selectionné
				else if(this.mat[i][j]==2){
					g.drawImage(this.pionBS,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				-2 -> pion Ordi sélectionné
				else if(this.mat[i][j]==-2){
					g.drawImage(this.pionNS,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}
//				5 -> pion blanc qui peut prendre
				else if(this.mat[i][j]==5){
					g.drawImage(this.pionBP,j*Jeu.TAILLEIM,i*Jeu.TAILLEIM,null);
				}

			}
		}
		repaint();
		
	}
	//permet de réinitiliser la variable de damier
	public void rafraichir(int[][] mat){
		this.mat=mat;
	}

}
