
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

Le gif généré est à retrouver dans :

```bash
./src/ressources
```

### Formes simples

Possibilité de choisir l'image de départ et l'image d'arrivée en cliquant sur "Choisir l'image de gauche" et "Choisir l'image de droite". Le but ici est de générer un morphing entre deux formes simples n'ayant que des lignes brisées.

#### Image gauche

Cliquer sur les sommets de la forme simple, un point de contrôle non déplaçable apparait et son équivalent sur l'image de droite apparait également.
Si besoin de recommencer la disposition des points, cliquer sur "réinitialiser".

#### Image droite

Déplacer les points de contrôles sur les sommets de la figure de droite ou sur les arêtes à défaut.


### Formes arrondies

Possibilité de choisir l'image de départ et l'image d'arrivée en cliquant sur "Choisir l'image de gauche" et "Choisir l'image de droite". Le but ici est de générer un morphing entre deux formes qui peuvent avoir des arrondis.

#### Image de gauche

Ici les points de contrôle vont par 4. cliquer sur un sommet ou une arête pour qu'un point de contrôle apparaisse. tous les deux points de contôles, deux autres points de contôles intermédiaires de couleur bleu apparaissent, ainsi qu'une ligne matérialisant la courbe de départ. Les deux points bleus peuvent être déplacés dans l'image de gauche. Attention, il faut faire correspondre la courbure de la ligne généré avec la courbure de la Forme arrondie de l'image gauche. Les points bleus ne sont donc pas nécéssairement sur la figure.
Il est aussi important de prévoir une décomposition de la courbe de la forme arrondie en plusieurs points de contrôle pour des soucis de précision.

#### Image de droite

Même principe que pour l'image gauche, il faut faire correspondre la ligne entre les 4 points avec les courbes potentielles de la forme arrondie de l'image droite.


### Morphing d'images

Possibilité de choisir l'image de départ et l'image d'arrivée en cliquant sur "Choisir l'image de gauche" et "Choisir l'image de droite". Le but ici est de générer un morphing entre deux images png ou jpg.

#### Image de gauche
mettre les points de contrôles sur des points caractéristiques de l'image comme les yeux, la bouche, ou sur un paysages des caractéristiques notables (un pic de montagne, un élément particulier, etc ...)

#### Image de droite
déplacer les points générés sur les points cractéristiques de l'image gauche

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

