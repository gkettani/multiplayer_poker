import java.util.*;

public class Combinaison{
	
	public String nom;  //Nom de la combinaison
	public int points = 0;						//Chaque combinaison attribut des points
	private ArrayList<Cartes> listeCombinaison = new ArrayList<Cartes>();   //On range ici les cartes d'une combinaison lorsque celle-ci est vérifée
	private ArrayList<Cartes> listeCouleur = new ArrayList<Cartes>();       //On range dans cette liste les cartes de la combinaison couleur
	private boolean doublePaires = false;

	
	
	public Combinaison(String nom){
		this.nom = nom;
	}

	public boolean couleur(ArrayList<Cartes>listeDeCartes) {
		int compteur1 = 0;
		int compteur2 = 0;
		int compteur3 = 0;
		int compteur4 = 0;
		for (Cartes c : listeDeCartes){ 
			if(c.signe == "coeur"){
				compteur1++;
				if(compteur1 == 5){
					for(Cartes a : listeDeCartes){
						if(a.signe == "coeur"){
							listeCouleur.add(a);
						}
					}
				}
			}
			else if(c.signe == "trefle"){
				compteur2++;
				if(compteur2 == 5){
					for(Cartes a : listeDeCartes){
						if(a.signe == "trefle"){
							listeCouleur.add(a);
						}
					}
				}
			}
			else if(c.signe == "pique"){
				compteur3++;
				if(compteur3 == 5){
					for(Cartes a : listeDeCartes){
						if(a.signe == "pique"){
							listeCouleur.add(a);
						}
					}
				}
			}
			else if(c.signe == "carreau"){
				compteur4++;
				if(compteur4 == 5){
					for(Cartes a : listeDeCartes){
						if(a.signe == "carreau"){
							listeCouleur.add(a);
						}
					}
				}
			}
		}
		if((compteur1 >=5) || (compteur2 >=5) || (compteur3 >=5) || (compteur4 >=5))	{
			points = 75; 
			return true;
		}
		else {
		 	listeCouleur.clear();
		 	return false;
		 }
	 }
	 
	public boolean paire(ArrayList<Cartes> listeDeCartes){                                 
		boolean b = false;
		int compteur = 0;
		listeCombinaison.clear(); 
		Collections.sort(listeDeCartes);
		for(int i=0; i<listeDeCartes.size()-1   ; i++){
			if(listeDeCartes.get(i).compareTo(listeDeCartes.get(i+1)) == 0  ){			//Si deux cartes de meme valeur se suivent
				compteur++;
				listeCombinaison.add(listeDeCartes.get(i));								
				listeCombinaison.add(listeDeCartes.get(i+1));
				if(i<listeDeCartes.size()-2 && listeDeCartes.get(i+1).compareTo(listeDeCartes.get(i+2)) == 0){       //Si jamais c'est un brelan ou un carré on ne prend pas en compte 
					compteur --;                                                         							//(necessaire pour le full car full = paire + brelan)
					listeCombinaison.remove(listeDeCartes.get(i));								
					listeCombinaison.remove(listeDeCartes.get(i+1));
					i++;
				}
			}
		}if (compteur == 1){
			b = true;
			points += 15 + listeCombinaison.get(0).valeur;
		} 
		if(compteur >= 2) {  //Si on a plusieurs paires
			doublePaires = true;
			points = 30 + listeCombinaison.get(listeCombinaison.size()-1).valeur;
		}
		return b;
	}
	
	public boolean suite(ArrayList<Cartes> listeDeCartes){   
		boolean b = false;
		int compteur = 0;
		int i = listeDeCartes.size() - 1;
		listeCombinaison.clear();
		Collections.sort(listeDeCartes); 

		while(i > 0 ){
			if(listeDeCartes.get(i).valeur -1 == listeDeCartes.get(i-1).valeur  ){
				compteur ++;
				listeCombinaison.add(listeDeCartes.get(i));
			}
			else if(listeDeCartes.get(i).compareTo(listeDeCartes.get(i-1)) != 0){
				compteur = 0;
				listeCombinaison.clear();
			}
			if(compteur == 4){
				listeCombinaison.add(listeDeCartes.get(i-1));
				break;
			} 
			i--;
		}
		if(compteur >= 4) {
			points += 60 + listeCombinaison.get(0).valeur;    //On rajoute à 60 la valeur de la carte la plus haute de la suite (liste triée decroissante)
			b = true;
		} else {
			listeCombinaison.clear();
		}
			
		return b;
	}
	
	public boolean brelan(ArrayList<Cartes> listeDeCartes){
		boolean b = false;
		Collections.sort(listeDeCartes);
		for(int i=0; i<listeDeCartes.size() - 2; i++){
			if(listeDeCartes.get(i).compareTo(listeDeCartes.get(i+1))==0
			&& listeDeCartes.get(i+1).compareTo(listeDeCartes.get(i+2))==0){
				b = true;
				points += 45 + listeDeCartes.get(i).valeur;
				listeCombinaison.add(listeDeCartes.get(i));
				listeCombinaison.add(listeDeCartes.get(i+1));
				listeCombinaison.add(listeDeCartes.get(i+2));
			}
		}
		return b;	
	}
	
	public boolean carre(ArrayList<Cartes> listeDeCartes){
		boolean b = false;
		Collections.sort(listeDeCartes);
		listeCombinaison.clear();
		for(int i=0; i<listeDeCartes.size() - 3; i++){
			if(listeDeCartes.get(i).compareTo(listeDeCartes.get(i+1))==0
			&& listeDeCartes.get(i+1).compareTo(listeDeCartes.get(i+2))==0
			&& listeDeCartes.get(i+2).compareTo(listeDeCartes.get(i+3))==0){
				b = true;
				points = 103 + listeDeCartes.get(i).valeur;
				listeCombinaison.add(listeDeCartes.get(i));
				listeCombinaison.add(listeDeCartes.get(i+1));
				listeCombinaison.add(listeDeCartes.get(i+2));
				listeCombinaison.add(listeDeCartes.get(i+3));
			}
		}
		return b;
		
	}

	public boolean doublePaires(ArrayList<Cartes> listeDeCartes){
		paire(listeDeCartes);
		if (doublePaires) return true;
		else return false;
	}
	
	
	public boolean full(ArrayList<Cartes> listeDeCartes){ 
		if(paire(listeDeCartes) && brelan(listeDeCartes)){
			points += 15;    
			return true;
		}else return false;
	}
	
	public boolean quinteFlush(ArrayList<Cartes> listeDeCartes){
		if(couleur(listeDeCartes)){
			if (suite(listeCouleur)){
				return true;

			} else return false;
			
		}else return false;
	}
	
	public boolean quinteFlushRoyal(ArrayList<Cartes> listeDeCartes){
		boolean b = false;
		if(quinteFlush(listeDeCartes) && listeCombinaison.get(0).valeur == 14){
			b = true;
			points += 1;
		}
		return b;
	}

	/**
		Si aucune combinaison n'est vérifiée, on attribut un nombre de points égal à la valeur de la carte haute du joueur
	**/

	public void valeurCarteHaute(ArrayList<Cartes> listeDeCartes){  
		if(listeDeCartes.get(0).compareTo(listeDeCartes.get(1)) > 0){    //On compare les 2 cartes dans la main du joueur 
			points = listeDeCartes.get(0).valeur;
		}else{
			points = listeDeCartes.get(1).valeur;
		}	
	}

	public void addAs(ArrayList<Cartes> listeDeCartes){      // Si la liste contient un/des AS (valeur=14) on ajoute une/des carte avec la valeur 1          
		for(int i=0; i<listeDeCartes.size();i++){
			if(listeDeCartes.get(i).valeur == 14){
				listeDeCartes.add(new Cartes(1,listeDeCartes.get(i).signe));
			}
		}
	}

	public void removeAs(ArrayList<Cartes> listeDeCartes){      // retir le(s) 1 s'il y'en a                                        
		for(int i=0; i<listeDeCartes.size();i++){
			if(listeDeCartes.get(i).valeur == 1){
				listeDeCartes.remove(listeDeCartes.get(i));
				i--;
			}
		}
	}
	
}
