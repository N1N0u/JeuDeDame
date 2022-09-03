
import javax.swing.*;
import java.awt.*;
import java.lang.Integer;

public class DamierFrame extends JFrame{

	//Les deux variables de taille de fenetre
	private final static int LARGEUR=504;
	private final static int HAUTEUR=550;
	//damier
	DamierPanel damPanel;
	//fenetres des scores
	JTextField scoreJ,scoreA,nbPionJ,nbPionA;
	//scores
	int entScoreJ,entScoreA,entNbPionJ,entNbPionA;
	
	public DamierFrame(int[][] damier,ClickAction listener) {
		super();
		setSize(LARGEUR,HAUTEUR);
		setResizable(false);
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		//initialisation du damier compris dans la fenetre
		this.damPanel=new DamierPanel(damier,listener);
		damPanel.repaint();
		
		Box scoreBox=Box.createHorizontalBox();
		//Initialisation des fenetres de score et des scores 
		JTextField text1=new JTextField("Score blancs :");
		text1.setEditable(false);
		
		scoreJ=new JTextField("0");
		scoreJ.setEditable(false);
		entScoreJ=0;
		
		JTextField text2=new JTextField("Score noirs :");
		text2.setEditable(false);
		
		scoreA=new JTextField("0");
		scoreA.setEditable(false);
		entScoreA=0;
		
		JTextField text3=new JTextField("Pions blancs :");
		text3.setEditable(false);
		
		nbPionJ=new JTextField("12");
		nbPionJ.setEditable(false);
		entNbPionJ=12;
		
		JTextField text4=new JTextField("Pions noirs :");
		text4.setEditable(false);
		
		nbPionA=new JTextField("12");
		nbPionA.setEditable(false);
		entNbPionA=12;
		
		scoreBox.add(text1);
		scoreBox.add(scoreJ);
		scoreBox.add(text2);
		scoreBox.add(scoreA);
		scoreBox.add(text3);
		scoreBox.add(nbPionJ);
		scoreBox.add(text4);
		scoreBox.add(nbPionA);
		
		//initialisation finale de la fenetre
		panel.add(scoreBox,BorderLayout.SOUTH);
		panel.add(damPanel,BorderLayout.CENTER);
		getContentPane().add(panel);
		
	}
	
	//enleve un point au score du joueur
	public void changeScoreJ(){
		this.entScoreJ+=1;
		scoreJ.setText(Integer.toString(entScoreJ));
	}
	
	//enleve un point au score de l'ordi
	public void changeScoreA(){
		this.entScoreA+=1;
		scoreA.setText(Integer.toString(entScoreA));
	}
	
	//enleve un pion au joueur
	public void changeNbPionJ(){
		this.entNbPionJ-=1;
		nbPionJ.setText(Integer.toString(entNbPionJ));
	}
	
	//enleve un pion a l'ordi
	public void changeNbPionA(){
		this.entNbPionA-=1;
		nbPionA.setText(Integer.toString(entNbPionA));
	}
	
	//permet de récupere le score du joueur
	public int getScoreJ(){
		return this.entNbPionJ+3*this.entScoreJ;
	}
	
	//permet de récupérer le score de l'ordi
	public int getScoreA(){
		return this.entNbPionA+3*this.entScoreA;
	}
	
	//pour rafraichir le damier avec le nouveau tableau
	public void rafraichir(int[][] mat){
		damPanel.rafraichir(mat);
		damPanel.repaint();
	}
}