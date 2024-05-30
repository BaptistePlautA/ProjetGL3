
# Morphing d'image

Ce projet consiste à produire un morphing d'images, une technique qui permet de transformer progressivement une image de départ en une image d'arrivée en utilisant des points de contrôle. 
Ces points de contrôle sont placés sur les images pour guider la transformation. 
Le résultat final est un GIF animé qui illustre cette transition de manière fluide et dynamique, montrant chaque étape de la transformation guidée par les points de contrôle.

L'application se compose de 3 morphings différents :
* un morphing de formes simples : on traite des lignes brisées et la transition se fait en déplaçant linéairement les points de contrôles.
* un morphing de formes arrondies : on traite des courbes et la transition reste la même. 
* un morphing d'images : en utilisant la méthode de triangulation de Delaunay.

***

## Sommaire

1. Pré-requis
2. Déploiement
3. Instructions
4. Exemples d'utilisation
5. Documentation
6. Technologies utilisées
   
***

## Pré-requis

Installer les dépendances javafx.controls, fxml, graphics, swing. 
***

## Déploiement

Écrire la ligne suivante dans VM arguments de Run Configurations :

```bash
--module-path /home/cytech/Desktop/Java/openjfx-22_linux-x64_bin-sdk/javafx-sdk-22/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.swing
```

## Instructions

### Choix des images 
Pour chacun des types de morphisme, l'utilisateur peut choisir son image de début et de fin en cliquant respectivement sur "Choisir l'image de gauche" et "Choisir l'image de droite". 
Attention, le nom de l'image ne doit pas comporter d'espaces et les extensions acceptées sont : png et jpg. 
Pour tester l'application, si besoin, vous trouverez des images dans le dossier Annexes/ImagesPourTester/.

### Points de contrôle 
- Pour le morphing simple : l'utilisateur peut ajouter des points sur l'image de gauche et les équivalents apparaissent sur l'image de droite. Sur cette dernière, l'utilisateur peut déplacer les points pour les positionner selon la forme de fin. Il est recommandé de positionner les points sur les sommets ou les arêtes des formes. 

- Pour le morphing arrondi, les points de contrôle classique fonctionnent de la même manière que pour le morphing simple. En plus, il y a l'ajout de deux points intermédiaires entre deux points de contrôle. Ces derniers apparaissent sur les deux images et permettent de dessiner de la courbure de la courbe. L'utilisateur doit déplacer ces points intermédiaires pour faire correspondre la courbure de la ligne généré avec la courbure de la forme arrondie de l'image. Pour plus de précision, il est également nécessaire de décomposer la courbe de la forme arrondie en plusieurs points de contrôle.

- Pour le morphing d'images, les points de contrôles fonctionnent comme pour le morphing simple. Il est recommandé de positionner les points sur des éléments caractéristiques de l'image, sur le sujet qui va être transformé. Par exemple, pour des visages, placez les points sur les yeux, le nez, les oreilles, ou encore le haut et le bas du visage.

***

## Exemples d'utilisation

Exemple d'un morphing entre une forme simple rectangulaire vers une forme simple triangulaire :

![](https://github.com/BaptistePlautA/ProjetGL3/blob/Lukas/simple.gif)

Exemple d'un morphing de formes arrondies entre un coeur et une croix :

![](https://github.com/BaptistePlautA/ProjetGL3/blob/Lukas/arrondi.gif)

Exemple d'un morphing d'images entre deux tableaux de Van Gogh :

![](https://github.com/BaptistePlautA/ProjetGL3/blob/Lukas/van_gogh.gif)
***

## Documentation

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

