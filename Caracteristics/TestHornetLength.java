package Caracteristics;


import Caracteristics.HornetLength;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TestHornetLength {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Chemin vers l'image
        String imagePath = "Footage/CutoutResult of Cutout.jpg";

        // Chargement de l'image
        Mat image = Imgcodecs.imread(imagePath);

        // Vérification si l'image est lue correctement
        if (image.empty()) {
            System.err.println("Erreur lors de la lecture de l'image.");
            return;
        }

        // Convertir l'image en niveaux de gris si elle n'est pas déjà en niveaux de gris
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        }

        // Test de la fonction hornetLength
        testHornetLengthForImage(image, imagePath);
    }

    public static void testHornetLengthForImage(Mat image, String imagePath) {
        // Appel de la fonction hornetLength avec l'image et le chemin de l'image
        int[] result = HornetLength.calculer_HornetLength(image);

        // Affichage des résultats
        System.out.println("Longueur du frelon : " + result[0]);
        System.out.println("Coordonnées de l'extrémité de l'abdomen : (" + result[1] + ", " + result[2] + ")");

        // Appel de la fonction resultPlot avec les résultats
        HornetLength.resultPlot(image, result[1], result[2], result[2], result[0], 0, image.rows(), image.cols(), imagePath);
    }
}
