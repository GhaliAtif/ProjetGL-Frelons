package Caracteristics;


import Caracteristics.HornetLength;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TestHornetLength {


    public static void main(String[] args) {
        // Charger l'image depuis un fichier
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "Footage/16_cutout.jpg";
        Mat image = Imgcodecs.imread(imagePath);

        // Vérifier si l'image est chargée correctement
        if (image.empty()) {
            System.err.println("Erreur lors du chargement de l'image.");
            System.exit(1);
        }

        // Convertir l'image en niveaux de gris si elle n'est pas déjà en niveaux de gris
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
       }
        // Check the number of channels in the input image
        System.out.println("Number of channels: " + image.channels());

        // Appliquer l'analyse de la longueur du frelon
        int[] lengthResult = HornetLength.calculer_HornetLength(image);

        // Afficher la longueur du frelon
        System.out.println("Longueur du frelon : " + lengthResult[0]);

        // Appliquer la visualisation des résultats
        HornetLength.resultPlot(image, imagePath);

        // Attendre la fermeture de la fenêtre graphique
        HighGui.waitKey();
    }

   /* public static void testHornetLengthForImage(Mat image, String imagePath) {
        // Appel de la fonction hornetLength avec l'image et le chemin de l'image
        int[] result = HornetLength.calculer_HornetLength(image);
       // int[] resultBoundingLines= HornetLength.boundingLines(image);

        // Affichage des résultats
        System.out.println("Longueur du frelon : " + result[0]);
        System.out.println("Coordonnées de l'extrémité de l'abdomen : (" + result[1] + ", " + result[2] + ")");

        // Appel de la fonction resultPlot avec les résultats
        //HornetLength.resultPlot(image, result[1], result[2], result[2], result[0], 0, image.rows(), image.cols(), imagePath);
    }*/


}
