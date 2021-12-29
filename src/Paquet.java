import java.util.Random;

public class Paquet{
	
	//Attributs : tableauDeCartes correspond au Paquet de base 
	protected Cartes[] tableauDeCartes;

	public Paquet(String ordre) {     
		tableauDeCartes = new Cartes[52];
		int valeur = 2;
		switch (ordre) {
			case "valeur" :                  //Creation d'un paquet dont les cartes sont classées par ordre croissant
				for (int i=0; i<52; i+=4){
					tableauDeCartes[i] = new Cartes(valeur, "pique");
					tableauDeCartes[i+1] = new Cartes(valeur, "trefle");
					tableauDeCartes[i+2] = new Cartes(valeur, "coeur");
					tableauDeCartes[i+3] = new Cartes(valeur, "carreau");
					valeur++;
				}
				break;
			
			case "signe" :                   //Creation d'un paquet dont les cartes sont regroupées par signe et classées par ordre croissant
				for (int i=0; i<13; i+=1){
					tableauDeCartes[i] = new Cartes(i+2, "pique");
					tableauDeCartes[i+13] = new Cartes(i+2, "trefle");
					tableauDeCartes[i+26] = new Cartes(i+2, "coeur");
					tableauDeCartes[i+39] = new Cartes(i+2, "carreau");
				}
				break;
			
			default :
				break;
		}	
	}

	public void melanger() {   
		Random r = new Random(); 
		for (int loop=0; loop < 15; loop++) {
			for (int i=0; i<52 ; i++) {
				int numero = r.nextInt(52);   //On choisie un numero aléatoire
				Cartes carte = tableauDeCartes[i];	//On intervertie ensuite les cartes du paquet
				tableauDeCartes[i] = tableauDeCartes[numero];  
				tableauDeCartes[numero] = carte;
			}
		}
	}

	public int getSize(){
		return tableauDeCartes.length;
	}

	public String toString(){       //Affichage du Paquet créé
		String res;
		res = "Les cartes sont :";
		for(int i=0; i<tableauDeCartes.length; i++){
			res += " (" + tableauDeCartes[i].valeur + "," + tableauDeCartes[i].signe + ")";
		}
		return res; 
	}

}
