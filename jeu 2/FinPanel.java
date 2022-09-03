
import javax.swing.*;
import java.awt.*;
public class FinPanel extends JPanel{
	int score1,score2,joueur;
	public FinPanel(int joueur,int score1,int score2){
		super();
		this.score1=score1;
		this.score2=score2;
		this.joueur=joueur;
	}
	public void paintComponent(Graphics g){
		g.setFont(new Font("SansSerif",Font.PLAIN,25));
		g.drawString("La partie est finie.",140,50);
		if(joueur==0) g.drawString("Il y a EGALITE avec un score de :",80,75);
		else if(joueur==-1) g.drawString("Vous avez PERDU avec un score de :",60,75);
		else g.drawString("Vous avez GAGNE avec un score de :",60,75);
		g.drawString(score1+"/"+score2,220,100);
	}

}
