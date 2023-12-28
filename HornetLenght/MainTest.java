package HornetLenght;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.HighGui;

public class MainTest {

    public static void main(String[] args) {
        // Charger l'image depuis un fichier
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "C:/Users/offic/IdeaProjects/ProjetGL-Frelons/Footage/15_cutout.jpg";
        Mat image = Imgcodecs.imread(imagePath);

        // Vérifier si l'image est chargée correctement
        if (image.empty()) {
            System.err.println("Erreur lors du chargement de l'image.");
            System.exit(1);
        }

        // Check the number of channels in the input image
        System.out.println("Number of channels: " + image.channels());

        // Appliquer l'analyse de la longueur du frelon
        int[] lengthResult = HornetLength.hornetLength(image);

        // Afficher la longueur du frelon
        System.out.println("Longueur du frelon : " + lengthResult[0]);

        // Appliquer la visualisation des résultats
        ResultPlot.resultPlot(image, lengthResult[0], lengthResult[1], lengthResult[2],
                0, 0, image.rows(), image.cols(), imagePath);

        // Attendre la fermeture de la fenêtre graphique
        HighGui.waitKey();
    }
}


