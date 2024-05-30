
# Morphing d'image

Projet consistant à produire un morphing d'images entre une image de départ et une image d'arrivée, caractérisé par un gif. 

l'Appli se compose de 3 morphings différents :
* un morphing de formes simples
* un morphing de formes arrondies 
* un morphing d'images

***

## Sommaire

1. Pré-requis
2. Déploiement
3. Exemples d'utilisation
4. Documentation
5. Technologies utilisées
   
***

## Pré-requis

installer les dépendances javafx.controls, fxml, graphics, swing 
***

## Déploiement

Ecrire dans VM arguments de Run Configurations :

```bash
--module-path /home/cytech/Desktop/Java/openjfx-22_linux-x64_bin-sdk/javafx-sdk-22/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.swing
```

accepte les .png et les .jpg

### Choix des images 
Pour chacun des types de morphisme, l'utilisateur peut choisir son image de début et de fin en cliquant respectivement sur "Choisir l'image de gauche" et "Choisir l'image de droite". 

### Points de contrôle 
- Pour le morphing simple : l'utilisateur peut ajouter des points sur l'image de gauche et leurs équivalents apparaissent sur l'image de droite. Sur cette dernière, l'utilisateur peut déplacer les points pour les positionner selon la forme de fin. Il est recommandé de positionner les points sur les sommets des formes ou sur leurs arêtes. 

- Pour le morphing arrondi, les points de contrôle classique fonctionnent de la même manière. Il y a en addition, l'ajout de points intermédiaires entre deux points de contrôle. Ces derniers apparaissent sur les deux images et permettent de dessiner de la courbure de la courbe. L'utilisateur est donc amené à les déplacer pour faire correspondre la courbure de la ligne généré avec la courbure de la forme arrondie de l'image. Il faut également faire une décomposition de la courbe de la forme arrondie en plusieurs points de contrôle pour des soucis de précision.

- Pour le morphing d'images, les points de contrôles fonctionnent comme pour le morphing simple. Il est recommandé de positionner les points sur des éléments caractéristiques de l'image, sur le sujet qui va être déplacé. S'il s'agit de visages, alors positionner sur les yeux, le nez, les oreilles, le haut et le bas du visage. 

***

## Exemples d'utilisation

Exemple d'un morphing entre une forme simple rectangulaire vers une forme simple triangulaire :


Exemple d'un morphing de formes arrondies entre un coeur et une croix :


Exemple d'un morphing d'images entre deux tableaux de Van Gogh :

![](https://github.com/BaptistePlautA/ProjetGL3/blob/Lukas/van_gogh.gif)
***

## Documentation

[Documentation](https://docs.google.com/document/d/1laq1xMgcM8cnwBgvdIpf29Tavy6Ft_AikcwHVvL0pTc/edit?usp=sharing)


[Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html)

[ImageIO pour la conversion en GIF](https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/javax/imageio/ImageIO.html)

[BufferedImage pour le morphing d'images](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)

[JavaFX](https://docs.oracle.com/javase/8/javafx/api/toc.html)

***
## Technologies utilisées

Ci-après une liste des différentes technologies utilisées pour notre projet :
* [Eclipse](https://eclipseide.org/): Version 2023-03 (4.27.0)
* [java](https://www.java.com/fr/): Version 17.0.6
* [javaFX](https://openjfx.io/): Version 22.0.0
* [github](https://github.com/)
***

