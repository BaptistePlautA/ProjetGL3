
En ce qui concerne l'alerte il y a 1 classe AttenteMorphisme dans laquelle il y a les 2 fonctions statiques:	


-	public static void creerFenetre();

-	public static void fermerFenetre();



Pour l'utiliser il faut juste mettre :


-	AttenteFinMorphisme.creerFenetre(); 		dans les EventHandler qui gèrent le lancement du Morphing

-	AttenteFinMorphisme.fermerFenetre(); 		à l'endroit ou le morphing est sensé être terminé



***************************************************************************

J'ai aussi mis une version de PointsControleHandler.java dans laquelle
j'ai juste remplacé les 'é' par des 'e' dans "coordonnées" (problèmes d'encodages)
Il faudrait le remplacer dans le main (je le fais pas moi car j'ai peur de faire de la merde aec gitHub)

***************************************************************************


Cela fonctionne bien, les seuls changements qu'il y a à faire
(que je ferais quand les parties seront terminées) sont : 


* Ajouter AttenteFinMorphisme dans le projet

* Modifier MorphingSimpleHandler en rajoutant 3 threads qui attendent que le précédent se termine 
avant de se lancer : le premier pour lancer la fenêtre d'attente,
le deuxième pour faire le traitement du morphing
le troisième pour fermer la fenêtre d'attente

* Ajouter les librairies manquantes

* changer le chemin des images si soucis

à noter que la version que je push n'est pas la dernière, il suffit de changer
ce qu'il y a dans le thread 2

