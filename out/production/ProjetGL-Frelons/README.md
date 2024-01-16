# ProjetGL-Frelons 
#Projet de Génie Logiciel : Réecriture en java a partir de l'existant en python et detection de la forme de l'abdomen de frelon et la  caste (fondatrice,ouvrière ou male)

## Lancement de l'Application :

- L'utilisateur exécute le programme en lançant la classe `InterfaceFrelon`.
- La fenêtre de l'interface utilisateur apparaît avec un champ de texte, un bouton, et une étiquette.

## Entrée du Numéro de l'Image :

- L'utilisateur entre un numéro dans le champ de texte. Ce numéro représente une image spécifique du frelon dans le dossier 'Footage/numeroImage.jpg'.
- Ensuite, la méthode `cutOut` est appelée pour créer le masque binaire de cette image, qui est sauvegardé au format 'Footage/numeroImage_cutout.jpg'.

## Traitement de l'Image :

- L'utilisateur clique sur le bouton "Appuyer pour traitement".
- Le programme récupère le numéro entré, construit le chemin d'accès à l'image correspondante, puis utilise la méthode `classifyHornet` pour traiter l'image.

## Affichage des Résultats :

- Les caractéristiques du frelon extraites sont affichées dans l'interface utilisateur.
- En cas d'erreur (par exemple, si l'utilisateur n'entre pas un nombre valide ou le nombre n’est pas présent dans le dossier 'Footage'), un message d'erreur est affiché.

## Répétition du Processus :

- L'utilisateur peut répéter le processus en entrant différents numéros d'image et en appuyant sur le bouton pour visualiser les caractéristiques correspondantes.

