package Caracteristics;
import java.util.Calendar;

/*
 * Auteur : Quentin Tollot et Matheo
 * Description : la classe permet de detecter en fonction du mois et saison si le frelon est ouvriere ou fondatrice
 * Integrée dans notre code pour la detection de la caste du frelon
 */
public class CasteDetector {
    public static String estimerCasteFrelon(double longueur_reelle) {
        // Obtenir le mois actuel
        int moisActuel = Calendar.getInstance().get(Calendar.MONTH) + 1;

        // Déterminer la saison en fonction du mois actuel
        String saison;
        if (moisActuel >= 3 && moisActuel <= 5) {
            saison = "printemps";
        } else if (moisActuel >= 6 && moisActuel <= 8) {
            saison = "été";
        } else if (moisActuel >= 9 && moisActuel <=12) {
            saison = "automne";
        }
        else {
            saison = "autre";
        }
        // Printemps
        if (saison.equalsIgnoreCase("printemps")) {
            if (longueur_reelle >= 25 && longueur_reelle <= 35) {
                return "Reine";
            } else if (longueur_reelle >= 14 && longueur_reelle <= 18) {
                return "Ouvrière";
            }
        }
        // Été
        else if (saison.equalsIgnoreCase("été")) {
            if (longueur_reelle >= 35 && longueur_reelle <= 40) {
                return "Reine";
            } else if (longueur_reelle >= 25 && longueur_reelle <= 33) {
                return "Ouvrière";
            }
        }
        // Automne
        else if (saison.equalsIgnoreCase("automne")) {
            if (longueur_reelle >=25 && longueur_reelle <= 33) {
                return "Ouvrière";
            }
            else if (longueur_reelle > 33 && longueur_reelle <43) {
                return "Reine";
            }
        }
        // Hiver
        else {
            if (longueur_reelle >=25 && longueur_reelle <= 40) {
                return "Reine";
            }
        }
        return "Non déterminée";
    }

}

