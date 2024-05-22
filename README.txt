
En ce qui concerne l'alerte il y a 1 classe AttenteMorphisme dans laquelle il y a les 2 fonctions statiques:	


-	public static void creerFenetre();

-	public static void fermerFenetre();



Pour l'utiliser il faut juste mettre :


-	AttenteFinMorphisme.creerFenetre(); 		dans les EventHandler qui gèrent le lancement du Morphing

-	AttenteFinMorphisme.fermerFenetre(); 		à l'endroit ou le morphing est sensé être terminé


(Je le rajoute dans le programme final dans la journée normalement)


***************************************************************************

J'ai aussi mis une version de PointsControleHandler.java dans laquelle
j'ai juste remplacé les 'é' par des 'e' dans "coordonnées" (problèmes d'encodages)
Il faudrait le remplacer dans le main (je le fais pas moi car j'ai peur de faire de la merde aec gitHub :) )

