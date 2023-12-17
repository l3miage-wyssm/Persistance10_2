# Projet PC 
## Description du Projet
Le but de ce projet est d'enrichir et d'améliorer les différentes fonctionnalités de l'interface graphique utilisateur fournie. Le projet utilise principalement le langage de programmation Java. Afin d'améliorer la qualité et la maintenabilité du code, nous avons utilisé Sonar qui détecte et corrige efficacement les problèmes dans le code, garantissant sa robustesse et sa sécurité. Et aussi, nous avons utilisé Github pour une collaboration efficace entre les binômes, il assure le bon déroulement du projet.

## Installation
La commande pour importer le package jar *ComposantImportExport* :
```
mvn install:install-file -Dfile="lib/ComposantImportExport-0.0.1-SNAPSHOT.jar" -DgroupId="edu.uga.miage.m1" -DartifactId="edu.uga.miage.m1.polygons.gui.exportImport" -Dversion="0.0.1" -Dpackaging=jar -DgeneratePom=true

```
Puis il faut que vous lancez la fichier ```App.java```


## Fonctionnalités Réalisées
### Exportation des Données
- Utilisation de la bibliothèque JsonObjectBuilder de Java pour le format JSON et DocumentBuilderFactory pour le format XML.
- Permet une exportation facile des données dans le format JSON ou XML.
### Sélection et Mouvement des Shapes
- Possibilité de changer le type d'événement de la souris en cliquant sur le bouton correspondant dans la barre d'outils.
- Lorsque le bouton de la souris est sélectionné, le curseur peut être déplacé pour sélectionner la forme souhaitée.
- Déplacement de la forme en la faisant glisser, avec les coordonnées finales déterminées par la position où le bouton gauche de la souris est relâché.
### Annulation des Actions
- Les commandes sont divisées en deux catégories : CommandMove et CommandShape.
- CommandMove est dédié aux opérations de déplacement, avec les attributs startX et startY stockant les coordonnées avant le déplacement.
- La méthode undo() permet de revenir aux coordonnées précédentes.
- Dans CommandShape, le dernier élément de shapeList est retiré lors de l'appel de undo().
### Importation des fichers JSON
Vous pouvez cliquer sur le bouton "importer fichier" et ensuite sélectionner parmi vos fichiers au format .JSON. Ensuite, vous pourrez importer le contenu du fichier et effectuer des opérations sur les formes graphiques.
## Document de conception
Veuillez voir dans le fichier ```documentDeConception.pdf```.
