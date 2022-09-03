//Valeur des cases du tableau tmp de la fonction jouer et du tableau damier
//Les dames ne sont pas faites mais c'est pour montrer la raison des valeur que nous avons choisis
// Joueur : pion=1	pionSel=2	dame=3	dameSel=4	pionprendre=5	dameprendre=6
// Ordi	  : pion=-1	pionSel=-2	dame=-3	dameSel=-4

public class Jeu {
	public static final int NBCASES=8;
	public static final int TAILLEIM=62;
    private int[][] damier=new int[NBCASES][NBCASES];
    private ClickAction listener;
    private DamierFrame frame;
    private int tour=0,iTmp=0,jTmp=0;
	private boolean joueur=true;
	private boolean finPartie=false;
	private int[] damierPrendre=new int[1];
	private int[] indPPrend=new int[4];
	private int[] pionPrendre=new int[1];
	private final int profondeur=6;
	//deplacement est un tableau définissant le deplacement futur de l'ordinateur à la suite d'un
	//coup du joueur
	private int[] deplacement=new int[43];
//deplacementInd permet de savoir lors del'appel a la fonction jouer à la suite d'un click souris 
//ou on en est du déplacement de l'ordianteur en cours et de se référer à la bonne partie du tableau précédent
	private int deplacementInd;
	
	public static void main(String[] args){
		Jeu jeu=new Jeu();
	}
	public Jeu(){
		this.damierPrendre[0]=1;
		this.pionPrendre[0]=1;
	    initialiserJeu();
	    listener=new ClickAction(this);
	    this.frame=new DamierFrame(damier,listener);
	    this.frame.show();
	}
	public void jouer(int i,int j){
		
		//tmp est un tableau copiant le tableau damier mais permettant de rentrer d'autres valeurs tels que
		//celles des pions sélectionnés pour l'affichage du damier
		int[][] tmp=new int[NBCASES][NBCASES];
		initialiserTmp (tmp);
		
	if(joueur){
			if(this.damierPrendre[0]==1){
				//Si on est au premier tour(tour==0 -> premier tour) ou qu'on est au deuxieme tour
				//mais que la case selectionnée contient un pion blanc (donc du joueur)
				//On stock les indices  dans itmp et jTmp puis on met la valeur d'un pion sélectionner
				//dans le tableau d'affichage
				if((this.tour==0 || this.tour==1) && this.damier[i][j]==1){
					this.iTmp=i;this.jTmp=j;this.tour=1;
					tmp[i][j]=2;
					
				}
				//Si on est au second tour et que la nouvelle case sélectionner est vide et est juste devant
				//le pion préalablement sélectionné on effectue le déplacement
				//puis on réinitialise les variables nécessaires pour le tour de l'ordinateur et
				//on calcule le coup de l'ordi
			    else if(this.tour==1 && this.damier[i][j]==0 && i==this.iTmp-1 && (j==this.jTmp+1 || j==this.jTmp-1)){
					//teste si le pion est arrivé a l'autre bout du damier
			    	if(i!=0){
						this.damier[i][j]=1;tmp[i][j]=1;
					}
			    	//Si c'est le cas il est retiré du jeu et un point est ajouter au joueur
			    	else{
			    		this.damier[i][j]=3;tmp[i][j]=0;
			    		frame.changeScoreJ();
			    	}
				
					this.damier[this.iTmp][this.jTmp]=0;tmp[this.iTmp][this.jTmp]=0;
					this.tour=0;
					this.frame.rafraichir(tmp);
					//Au tour de l'ordi
					this.joueur=false;
					//Calcul du coup de l'ordi
					jouerOrdi(0,this.joueur,damier,this.profondeur,new int[19],0,new int[43],0);
					//initialisation des variables
					this.deplacementInd=1;
					//Pour selectionner le pion que l'ordi va jouer
					tmp[this.deplacement[1]][this.deplacement[2]]=-2;
					
					//Teste si l'ordinateur peut jouer sinon on arrete la partie
					if(!peutJouer(joueur)) finPartie();
				}
			    //Dans les cas ou on selectionne une case autre et qu'on est au deuxieme tour
			    //on recommence a 0
			    else if(this.tour==1)this.tour=0;
			}
			//Si des pions peuvent prendre, alors ils sont obligés de le faire
			else{
				//Si c'est le premier tour
				if(tour==0){
					//vérifie si le pion sélectionné est un pion qui peut prendre
					//Si c'est le cas, tous les indices des prises qui lui sont possible
					//dans damier prendre dont stockés dans indPPrend
					this.indPPrend=new int[6];
					this.indPPrend[0]=0;
					for(int x=1;x<=this.damierPrendre[0];x+=6){
						if(i==this.damierPrendre[x] && j==this.damierPrendre[x+1]){
							this.indPPrend[0]+=1;
							this.indPPrend[indPPrend[0]]=x;
						}
					}
					//Si le pion sélectionner peut bien prendre
					//On stock les indices de damier et on selectionne le pion
					if(indPPrend[0]!=0){
						this.iTmp=i;this.jTmp=j;
					    afficherPrendre(tmp,damierPrendre);
						tmp[i][j]=2;
						tour=1;
					}
				}
				//Si il s'agit du deuxieme tour et qu'un pion a donc été sélectionner
				else if(this.tour==1){
					//on vérifie que la case sélectionnée permet bien la prise d'un pion
					//grace au tableau des prises posibles et les indices stockés précedement
					//et on stock l'indice correspondant a cet unique coup si c'est le cas
					int x=-1;
					for(int y=1;y<=indPPrend[0];y++){
						if(i==damierPrendre[indPPrend[y]+4] && j==damierPrendre[indPPrend[y]+5]){
							x=y;
						}
					}
					//Si cela correspond bien a une prise possible alors on modifie le damier en conséquence
					if(x!=-1){
						damier[damierPrendre[indPPrend[x]]][damierPrendre[indPPrend[x]+1]]=0;
						damier[damierPrendre[indPPrend[x]+2]][damierPrendre[indPPrend[x]+3]]=0;
						tmp[damierPrendre[indPPrend[x]]][damierPrendre[indPPrend[x]+1]]=0;
						tmp[damierPrendre[indPPrend[x]+2]][damierPrendre[indPPrend[x]+3]]=0;
//						On enleve un pion a l'ordinateur
						frame.changeNbPionA();
						//On vérifie si une prise ulterieur est possible avec le meme pion
						this.pionPrendre=prendrePion(this.damier,damierPrendre[indPPrend[x]+4],damierPrendre[indPPrend[x]+5],this.joueur);
						//Si ce n'est pas le cas alors on fait tous les tests de fin de coup et on
						//initialise les variables pour le coup de l'ordinateur
						if(this.pionPrendre[0]==1){
//							teste si le pion est arrivé a l'autre bout du damier
					    	if(i!=0){
					    		damier[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=1;
					    		tmp[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=1;
							}
					    	//Si c'est le cas il est retiré du jeu et un point est ajouté au joueur
					   	else{
					    		damier[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=0;
					    		tmp[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=0;
	//System.out.println(damierPrendre[indPPrend[x]+4]+"//"+damierPrendre[indPPrend[x]+5]);
					    		frame.changeScoreJ();
					    	}
					    	//C'est au tour de l'ordinateur
							this.joueur=false;
							this.tour=0;
							jouerOrdi(0,this.joueur,damier,this.profondeur,new int[19],0,new int[43],0);
							this.deplacementInd=1;
							//Pour selectionner le pion que l'ordinateur va jouer
							tmp[this.deplacement[1]][this.deplacement[2]]=-2;
							if(!peutJouer(joueur)) finPartie();
						}
						//Sinon on fini cette partie de coup puis on ajoute un a tour
						else{
							damier[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=1;
							tmp[damierPrendre[indPPrend[x]+4]][damierPrendre[indPPrend[x]+5]]=2;
							this.tour+=1;
						}
					}
					//Si il s'agit du premier tour mais que la case cochée ne permet pas de prise
					//alors on refait appel a jouer() comme au premier tour
					//avec les meme indices
					else{
						this.tour=0;
						jouer(i,j);
					}
				}
				//Si il s'agit de la suite d'une prise, donc qu'on est au moins au troisieme tour
				else{
					//On vérifie que la case sélectionner correspond bien a une case de prise avec le pion
					//et si c'est le cas on conserve l'indice de cette nouvelle prise
					int x=-1;
					for(int y=1;y<=this.pionPrendre[0]-6;y+=6){
						if(i==this.pionPrendre[y+4] && j==this.pionPrendre[y+5]){
							x=y;
						}						
					}
					//Si c'est le cas on effectue le déplacement
					if(x!=-1){
						int pionEnCoursI=pionPrendre[x+4];
						int pionEnCoursJ=pionPrendre[x+5];
						damier[pionPrendre[x+4]][pionPrendre[x+5]]=damier[pionPrendre[x]][pionPrendre[x+1]];
						damier[pionPrendre[x]][pionPrendre[x+1]]=0;
						damier[pionPrendre[x+2]][pionPrendre[x+3]]=0;
						tmp[pionPrendre[x]][pionPrendre[x+1]]=0;
						tmp[pionPrendre[x+2]][pionPrendre[x+3]]=0;
						//On enleve un pion a l'ordinateur
						frame.changeNbPionA();
						//On teste si il existe encore des prises ulterieurs et on les stocks dans pionPrendre
						this.pionPrendre=prendrePion(damier,pionPrendre[x+4],pionPrendre[x+5],this.joueur);
						//Si c'est la fin d'un coup : pas d'autres prises possibles
						if(pionPrendre[0]==1){
							//teste si le pion est arrivé a l'autre bout du damier
							if(i!=0){
								damier[pionEnCoursI][pionEnCoursJ]=1;
								tmp[pionEnCoursI][pionEnCoursJ]=1;
							}
					    	//Si c'est le cas il est retiré du jeu et un point est ajouter au joueur
					   	else{
					    		damier[pionEnCoursI][pionEnCoursJ]=0;
								tmp[pionEnCoursI][pionEnCoursJ]=0;
					    		frame.changeScoreJ();
					    	}
							//C'est au tour de l'ordinateur
							joueur=false;
							tmp[pionEnCoursI][pionEnCoursJ]=1;
							tour=0;
							jouerOrdi(0,this.joueur,damier,this.profondeur,new int[19],0,new int[43],0);
							this.deplacementInd=1;
							//Pour sélectionner le pion que l'ordinateur va jouer
							tmp[this.deplacement[1]][this.deplacement[2]]=-2;
							if(!peutJouer(joueur)) finPartie();
						}
						//Si il existe encore des prises ulterieurs
						else{
							tour+=1;
							damier[pionPrendre[x+4]][pionPrendre[x+5]]=1;
							tmp[pionEnCoursI][pionEnCoursJ]=2;
						}
					}
					//Si la case sélectionnée n'est pas correct on ne fait rien sinon réafficher un pion
					//sélectionner au même endroit
					else tmp[pionPrendre[1]][pionPrendre[2]]=2;
				}
			}
				
		}
		//Si c'est au tour de l'ordi de jouer
		else{
			//supprime le pion qui va etre bouger sur le damier
			    this.damier[deplacement[deplacementInd]][deplacement[deplacementInd+1]]=0;
			    tmp[deplacement[deplacementInd]][deplacement[deplacementInd+1]]=0;
			    //Si il s'agit d'un coup simple
			    if(this.deplacement[0]==5){
			    	//teste si le pion est arrivé a l'autre bout du damier et effectue le déplacement
			    	//en consequence
					if(deplacement[deplacementInd+2]!=NBCASES-1){
						this.damier[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=-1;
						tmp[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=-1;
					}
			    	//Si c'est le cas il est retiré du jeu et un point est ajouter au joueur
			    	else{
			    		this.damier[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
			    		tmp[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
			    		frame.changeScoreA();
			    	}
				//C'est au tour du joueur
				//Initialisation des vaiables : damierPrendre contenant la liste des prises possibles
				//et appel de afficherprendre pour l'afichage des ces pion qui peuvent prendre
			    this.joueur=true;
			    damierPrendre=prendreMat(damier,joueur);
			    afficherPrendre(tmp,damierPrendre);
			    //Test si on est arriver a la fin
			    if(!peutJouer(joueur)) finPartie();
			}
			//Si il s'agit d'un coup avec prises
			else{
				//Si cette partie du coup n'est pas la derniere, c'est a dire qu'apres cette prise il y
				//en aura d'autres
				if(this.deplacementInd<deplacement[0]-6){
					//On effectue le déplacement
					this.damier[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-1;
					this.damier[deplacement[deplacementInd]][deplacement[deplacementInd+1]]=0;
					this.damier[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
					tmp[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-2;
				    tmp[deplacement[deplacementInd]][deplacement[deplacementInd+1]]=0;
				    tmp[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
				    this.deplacementInd+=4;
//				  On enleve un pion au joueur
					frame.changeNbPionJ();
					
				}
				//Si c'est la fin d'un coup avec prise, qu'aucune autre prise ne suit celle ci
				else{
					//On effectue le déplacement
					this.damier[deplacement[deplacementInd]][deplacement[deplacementInd+1]]=0;
					this.damier[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
					tmp[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-1;
						this.damier[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-1;
				    tmp[deplacement[deplacementInd+2]][deplacement[deplacementInd+3]]=0;
//					On enleve un pion au joueur
					frame.changeNbPionJ();
//				  	teste si le pion est arrivé a l'autre bout du damier
					if(deplacement[deplacementInd+4]!=NBCASES-1){
						this.damier[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-1;
						tmp[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=-1;
					}
			    	//Si c'est le cas il est retiré du jeu et un point est ajouter au joueur
			    	else{
			    		this.damier[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=0;
						tmp[deplacement[deplacementInd+4]][deplacement[deplacementInd+5]]=0;
			    		frame.changeScoreA();
			    	}
					//Puis on réinitialise les variables pour le tour du joueur
				    this.deplacementInd+=4;
				    this.joueur=true;
				    this.damierPrendre=prendreMat(this.damier,joueur);
				    afficherPrendre(tmp,damierPrendre);
				    if(!peutJouer(joueur)) finPartie();
				}
			}
			    //reinitialisation de pionPrendre apres le tour de l'ordi
			this.pionPrendre[0]=1;
		}
		//On rafraichit la fenetre apres les modification apportés par l'appel a jouer() en lui envoyant
		//comme parametre le tableau d'affichage
		this.frame.rafraichir(tmp);
		
		
		
		//pour jouer ordi : penser a initialiser deplacement temporaire
	}
	//Pour cela elle utilise plusieurs parametres
	//valeur: valeur associé a chaque coup
	//joueur: joueur en cours pour le coup
	//tab: tableau du jeu
	//profondeur: nbre de fois qu on appellera la fonction jouerOrdi=nbre de coups calculés
	//temp:tableau permettant de stocker les deplacements ulterieurs pour un meme coup
	//     ces deplacements sont tous stockés dans ce tableau
	//temp2: variable permettant de savoir a partir de quel indice du tableau on choisi les indices de déplacement
	//		si temp2=0 il n'y a pa de coups ulterieurs
	//deplacement:variable permettant de stocker les premiers coups et qui est remplacée chaque fois qu'um meilleur premier
	//coup est trouvé (taille=43 soit 10 prises possibles pour 1 coup)
	public int jouerOrdi(int valeur,boolean joueur,int[][] tab,int profondeur,int[] temp,int indtemp,int[] deplacement,int appel){
		int pJ,pA;int[] nbsouffler;int valeurtemp,valtemp;
		//Ces valeurs temporaires sont volontairement démesuré pour que au moins une valeur soit renvoyée
		//a chaque fois,celle du premier coup calculé si elle est la meme pour chaque coup
		if (joueur) {pJ=1;pA=-1;valeurtemp=2000;}
		else {pJ=-1;pA=1;valeurtemp=-2000;}
		//Si on arrive au max choisi de l'arbre des possibilités
		if(profondeur==0) valeurtemp=0;
		else {
			//Si il s'agit de la continuité d'un coup
			if(indtemp!=0){
				//sauvegarde de la valeur du pion pris
				int pionpris=tab[temp[indtemp+2]][temp[indtemp+3]];
				//effectuer le deplacement
				tab[temp[indtemp+2]][temp[indtemp+3]]=0;
				tab[temp[indtemp+4]][temp[indtemp+5]]=tab[temp[indtemp]][temp[indtemp+1]];
				tab[temp[indtemp]][temp[indtemp+1]]=0;
				
				int[] tmp=prendrePion(tab,temp[indtemp+4],temp[indtemp+5],joueur);

				//Si pas d'autres prises possibles avec le meme pion
				if(tmp[0]==1){
					//Verifie si on arrive au bout du damier et ajoute ou soustrait 2 points si c'est le cas
					if(joueur && temp[indtemp+4]==0) valtemp=-1;
					else if(!joueur && temp[indtemp+4]==NBCASES-1) valtemp=1;
					else valtemp=0;
					
					int val=jouerOrdi(pionpris+valtemp,!joueur,tab,profondeur-1,tmp,0,deplacement,appel+1);
					if(joueur && val<valeurtemp) valeurtemp=val;
					else if(!joueur && val>valeurtemp) valeurtemp=val;
				}
					
				//Si autres prises possibles avec le meme pion
				else{
					for(int x=1;x<=tmp[0]-6;x+=6){
						int val=jouerOrdi(pionpris,joueur,tab,profondeur,tmp,x,deplacement,appel+1);
						if(joueur && val<valeurtemp) valeurtemp=val;
						else if(!joueur && val>valeurtemp) {
							valeurtemp=val;
							if(profondeur==this.profondeur){
								if(deplacement[0]<11+4*appel)	deplacement[0]=11+4*appel;
								deplacement[11+4*(appel-1)]=tmp[x+2];deplacement[11+4*(appel-1)+1]=tmp[x+3];
								deplacement[11+4*(appel-1)+2]=tmp[x+4];deplacement[11+4*(appel-1)+3]=tmp[x+5];
								
							}
						}
					}
				}
					
				//defaire le deplacement
				tab[temp[indtemp]][temp[indtemp+1]]=tab[temp[indtemp+4]][temp[indtemp+5]];
				tab[temp[indtemp+4]][temp[indtemp+5]]=0;
				tab[temp[indtemp+2]][temp[indtemp+3]]=pionpris;
			}
			else{
				//Pour verifier qu'il n'y a aucun pion a prendre
				//les pions qui peuvent prendre sont stockés dans nbsouffler
				nbsouffler=prendreMat(tab,joueur);
				//Si il existent des pions pouvant prendre
				if (nbsouffler[0] != 1){
					//Pour chaque pion pouvant souffler
					for(int i=1;i<=nbsouffler[0]-6;i+=6){
						//sauvegarde de la valeur du pion pris
						int pionpris=tab[nbsouffler[i+2]][nbsouffler[i+3]];
						//effectuer le deplacement
						tab[nbsouffler[i+4]][nbsouffler[i+5]]=tab[nbsouffler[i]][nbsouffler[i+1]];
						tab[nbsouffler[i+2]][nbsouffler[i+3]]=0;
						tab[nbsouffler[i]][nbsouffler[i+1]]=0;
						//stocker le deplacement si il s'agit du premier appel à jouerOrdi
						if(profondeur==this.profondeur){
							deplacement[0]=7;
							deplacement[1]=nbsouffler[i];deplacement[2]=nbsouffler[i+1];
							deplacement[3]=nbsouffler[i+2];deplacement[4]=nbsouffler[i+3];
							deplacement[5]=nbsouffler[i+4];deplacement[6]=nbsouffler[i+5];
						}
						int[] tmp=prendrePion(tab,nbsouffler[i+4],nbsouffler[i+5],joueur);
						//Si il n'y a pas d'autres prises ulterieur possibles avec le meme pion
						if (tmp[0]==1){
//							Verifie si on arrive au bout du damier et ajoute ou soustrait 2 points si c'est le cas
							if(joueur && nbsouffler[i+4]==0) valtemp=-1;
							else if(!joueur && nbsouffler[i+4]==NBCASES-1) valtemp=1;
							else valtemp=0;
							
							int val=jouerOrdi(pionpris+valtemp,!joueur,tab,profondeur-1,tmp,0,deplacement,appel+1);
							if(joueur && val<valeurtemp) valeurtemp=val;
							else if(!joueur && val>valeurtemp) {
								valeurtemp=val;
								//Si le deplacement stocké est meilleur que l'ancien deplacement de
								//la variable d'instance ou que cette variable d'instance n'a pas 
								//encore été utilisée, mettre ce deplacement dans la variable d'instance
								if(profondeur==this.profondeur) this.deplacement=deplacement;
							}
						}
						//Si il existe une ou plusieurs autres prises ulterieur possibles avec le meme pion
						//Conserver la meme profondeur car il s'agit en fait du meme coup
						else{
							for(int x=1;x<=tmp[0]-6;x+=6){
								if(profondeur==this.profondeur){
									deplacement[0]=11;
									deplacement[deplacement[0]-4]=tmp[x+2];deplacement[deplacement[0]-3]=tmp[x+3];
									deplacement[deplacement[0]-2]=tmp[x+4];deplacement[deplacement[0]-1]=tmp[x+5];
								}
								int val=jouerOrdi(pionpris,joueur,tab,profondeur,tmp,x,deplacement,appel+1);
								if(joueur && val<valeurtemp) valeurtemp=val;
								else if(!joueur && val>valeurtemp){
									valeurtemp=val;
									//Si le déplacement est meilleurs que le précédent on stock le résultat
									//dans la variable d'instance déplacement
									if(profondeur==this.profondeur) this.deplacement=deplacement;
								}
							}
						}
//						defaire le deplacement
						tab[nbsouffler[i]][nbsouffler[i+1]]=tab[nbsouffler[i+4]][nbsouffler[i+5]];
						tab[nbsouffler[i+2]][nbsouffler[i+3]]=pionpris;
						tab[nbsouffler[i+4]][nbsouffler[i+5]]=0;
					}
				}
				//Si aucun pion ne peut prendre, on effectue tous les déplacements normaux qu'il est possible de faire
				else {
					for(int i=0;i<NBCASES;i++){
						for(int j=0;j<NBCASES;j++){
							if(tab[i][j]==pJ){
								if (i>0){
									if(j>0){
										if (joueur && tab[i-1][j-1]==0){
											//effectuer deplacement
											tab[i-1][j-1]=tab[i][j];
											tab[i][j]=0;
											//regarder si le deplacement est meilleurs que le précedent
//											Verifie si on arrive au bout du damier et ajoute ou soustrait 2 points si c'est le cas
											if(i-1==0) valtemp=-1;
											else valtemp=0;
											int val=jouerOrdi(valtemp,!joueur,tab,profondeur-1,temp,0,deplacement,appel+1);
											if (val<valeurtemp) valeurtemp=val;
											//defaire deplacement
											tab[i][j]=tab[i-1][j-1];
											tab[i-1][j-1]=0;
										}
									}
									if(j<NBCASES-1){
										if(joueur && tab[i-1][j+1]==0){
											
											//effectuer deplacement
											tab[i-1][j+1]=tab[i][j];
											tab[i][j]=0;
											//regarder si le deplacement est meilleurs que le précedent
//											Verifie si on arrive au bout du damier et ajoute ou soustrait 2 points si c'est le cas
											if(i-1==0) valtemp=-1;
											else valtemp=0;
											int val=jouerOrdi(valtemp,!joueur,tab,profondeur-1,temp,0,deplacement,appel+1);
											if (val<valeurtemp) valeurtemp=val;
											//defaire deplacement
											tab[i][j]=tab[i-1][j+1];
											tab[i-1][j+1]=0;
										}
									}
								}
								if(i<NBCASES-1){
									if(j>0){
										if(!joueur && tab[i+1][j-1]==0){
											//effectuer deplacement
											tab[i+1][j-1]=tab[i][j];
											tab[i][j]=0;
											//regarder si le deplacement est meilleur que le précedent
//											Verifie si on arrive au bout du damier et ajoute 2 points si c'est le cas
											if(i+1==NBCASES-1) valtemp=1;
											else valtemp=0;
											int val=jouerOrdi(valtemp,!joueur,tab,profondeur-1,temp,0,deplacement,appel+1);
											if (val>valeurtemp){
												valeurtemp=val;
												//stocker le deplacement si premier appel a jouerOrdi
												if(profondeur==this.profondeur){
													this.deplacement[0]=5;this.deplacement[1]=i;this.deplacement[2]=j;
													this.deplacement[3]=i+1;this.deplacement[4]=j-1;
												}
											}
//											defaire deplacement
											tab[i][j]=tab[i+1][j-1];
											tab[i+1][j-1]=0;
										}
									}
										
									if(j<NBCASES-1){
										if(!joueur && tab[i+1][j+1]==0){
											//effectuer deplacement
											tab[i+1][j+1]=tab[i][j];
											tab[i][j]=0;
											//regarder si le deplacement est meilleur que le précedent
//											Verifie si on arrive au bout du damier et ajoute ou soustrait 2 points si c'est le cas
											if(i+1==NBCASES-1) valtemp=1;
											else valtemp=0;
											int val=jouerOrdi(valtemp,!joueur,tab,profondeur-1,temp,0,deplacement,appel+1);
											if (val>valeurtemp){
												valeurtemp=val;
												//stocker le deplacement
												if(profondeur==this.profondeur){
													this.deplacement[0]=5;this.deplacement[1]=i;this.deplacement[2]=j;
													this.deplacement[3]=i+1;this.deplacement[4]=j+1;
												}
											}
//											defaire deplacement
											tab[i][j]=tab[i+1][j+1];
											tab[i+1][j+1]=0;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//Si valeur temporaire n'a pas été remplacée,cela signifie que le joueur en cours ne peu
		//plus effectuer aucun déplacement
		if(valeurtemp<-1900 || valeurtemp>1900)valeurtemp=0;
		return valeur+valeurtemp;
	}
	public int[] prendreMat(int[][] tab,boolean joueur){

		int[] ret=new int[31];
		ret[0]=1;	
		if (joueur){
		for(int i=2;i<NBCASES;i++){
			for(int j=2;j<NBCASES;j++){
				if (tab[i][j]==1){
					if(tab[i-1][j-1]==-1 && tab[i-2][j-2]==0){			
						ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
						ret[ret[0]-4]=i-1;ret[ret[0]-3]=j-1;
						ret[ret[0]-2]=i-2;ret[ret[0]-1]=j-2;
					}
					
				}
			}
		}
		for(int i=2;i<NBCASES;i++){
			for(int j=0;j<NBCASES-2;j++){
				if (tab[i][j]==1){
					if(tab[i-1][j+1]==-1 && tab[i-2][j+2]==0){
						ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
						ret[ret[0]-4]=i-1;ret[ret[0]-3]=j+1;
						ret[ret[0]-2]=i-2;ret[ret[0]-1]=j+2;
					}
				}
			}
		}
	}	
		else if (! joueur){
		for(int i=0;i<NBCASES-2;i++){
			for(int j=2;j<NBCASES;j++){
				if (tab[i][j]==-1){
					if(tab[i+1][j-1]==1 && tab[i+2][j-2]==0){
						ret[0]+=6;
						ret[ret[0]-6]=i;
						ret[ret[0]-5]=j;
						ret[ret[0]-4]=i+1;
						ret[ret[0]-3]=j-1;
						ret[ret[0]-2]=i+2;
						ret[ret[0]-1]=j-2;
				
					}
				}
			}
		}
		for(int i=0;i<NBCASES-2;i++){
			for(int j=0;j<NBCASES-2;j++){
				if (tab[i][j]==-1){
					if(tab[i+1][j+1]==1 && tab[i+2][j+2]==0){
						ret[0]+=6;
						ret[ret[0]-6]=i;
						ret[ret[0]-5]=j;
						ret[ret[0]-4]=i+1;
						ret[ret[0]-3]=j+1;
						ret[ret[0]-2]=i+2;
						ret[ret[0]-1]=j+2;
					}
				}
			}
		}
	}	
		return ret;
	}
	public int[] prendrePion(int[][] mat, int i,int j,boolean joueur){

		int[] ret=new int[19];
		ret[0]=1;
		if (joueur){
		if (i>1){
			if(j>1){
				if(mat[i-1][j-1]==-1 && mat[i-2][j-2]==0){
					ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
					ret[ret[0]-4]=i-1;ret[ret[0]-3]=j-1;
					ret[ret[0]-2]=i-2;ret[ret[0]-1]=j-2;
				}
			}
			if(j<NBCASES-2){
				if(mat[i-1][j+1]==-1 && mat[i-2][j+2]==0){
					ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
					ret[ret[0]-4]=i-1;ret[ret[0]-3]=j+1;
					ret[ret[0]-2]=i-2;ret[ret[0]-1]=j+2;
				}
			}
		}}
		else if (! joueur){
		if(i<NBCASES-2){
			if(j>1){
				if(mat[i+1][j-1]==1 && mat[i+2][j-2]==0){
					ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
					ret[ret[0]-4]=i+1;ret[ret[0]-3]=j-1;
					ret[ret[0]-2]=i+2;ret[ret[0]-1]=j-2;
				}
			}
			if(j<NBCASES-2){
				if(mat[i+1][j+1]==1 && mat[i+2][j+2]==0){
					ret[0]+=6;ret[ret[0]-6]=i;ret[ret[0]-5]=j;
					ret[ret[0]-4]=i+1;ret[ret[0]-3]=j+1;
					ret[ret[0]-2]=i+2;ret[ret[0]-1]=j+2;
				}
			}
		}}
		return ret;
	}
	
	//Cette fonction qui prend en parametre un tableau de type damier et un tableau de type prendreMat()
	//permet de transformer les valeurs des pions qui peuvent sélectionner pour l'affichage dans le tableau
	//de type damier
	public void afficherPrendre(int[][] mat,int[] cases){
		for(int i=1;i<cases[0];i+=6){
			mat[cases[i]][cases[i+1]]=5;
		}
	}
    public void initialiserJeu() {
        int i,j;
        int zoneNoirs = 3;
        int zoneBlancs = 6;
        for ( i=1; i<=8; i++ ) {
            for ( j=1; j<=8; j++ ) {
                if ( ((i%2 == 0)&&(j%2 == 0)) || ((i%2 !=0) && (j%2 != 0))) { 
                	if ( i <= zoneNoirs ) this.damier[i-1][j-1]=-1;
                    else if ( i >= zoneBlancs ) this.damier[i-1][j-1]=1;
                    else this.damier[i-1][j-1]=0;	
                }
                else if ( (i%2 == 0)&&(j%2 != 0) ) { this.damier[i-1][j-1]=-10;
                            }
                else if ( (i%2 != 0)&&(j%2 == 0) ) {
                	this.damier[i-1][j-1]=-10;
                                }                                           
            }                
        }
	} 
    public boolean peutJouer(boolean joueur){
    	boolean peutjouer=false;
    	if (prendreMat(damier,joueur)[0]!=1) peutjouer=true;
    	else{
    		for (int i=0;i<NBCASES;i++){
    			for(int j=0;j<NBCASES;j++){
    				if((damier[i][j]==1) || (damier[i][j]==-1)){
    					if(joueur && i>0){
    						if(j<NBCASES-1){
    							if(damier[i-1][j+1]==0) peutjouer=true;
    						}
    						if(j>0){
    							if(damier[i-1][j-1]==0) peutjouer=true;
    						}
    					}
    					else if(!joueur && i<NBCASES-1){
    						if(j<NBCASES-1){
    							if(damier[i+1][j+1]==0) peutjouer=true;
    						}
    						if(j>0){
    							if(damier[i+1][j-1]==0) peutjouer=true;
    						}
    					}
    				}
    			}
    		}
    	}
    	return peutjouer;
    }
    public void finPartie(){
    	listener.setFinPartie();
    	FinFrame frame;
    	int scoreJ=this.frame.getScoreJ();
    	int scoreA=this.frame.getScoreA();
    	if(scoreJ<scoreA) frame=new FinFrame(-1,scoreJ,scoreA);
    	else if(scoreJ==scoreA) frame= new FinFrame(0,scoreJ,scoreA);
    	else frame=new FinFrame(1,scoreJ,scoreA);
    }
    //il sert uniquement pour la variable d'affichage a chaque appel de la fonction jouer
    public void initialiserTmp(int[][] tmp){
    	for(int i=0;i<NBCASES;i++){
    		for(int j=0;j<NBCASES;j++){
    			tmp[i][j]=this.damier[i][j];
    		}
    	}
    }
}