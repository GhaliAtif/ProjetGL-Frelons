package HornetLenght;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MainTest {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Replace this with the path to your test image
        String imagePath = "C:\\Users\\offic\\IdeaProjects\\ProjetGL-Frelons\\Footage\\15_cutout.jpg";

        Mat picture = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);

        if (picture.empty()) {
            System.err.println("Error reading the image");
            return;
        }

        String pictureFile = "HornetImage.jpg";

        // Utilisez la méthode correcte bounding_lines
        int[] lines = BoundingLines.bounding_lines(picture);

        System.out.println("Upper Line: " + lines[0]);
        System.out.println("Lower Line: " + lines[1]);
        System.out.println("Left Line: " + lines[2]);

        // Utilisez les lignes correctes pour calculer la longueur du frelon
        int[] result = HornetLength.hornetLength(picture, pictureFile, lines[0], lines[1], lines[2]);

        System.out.println("Hornet Length: " + result[0]);
        System.out.println("Sting Coordinates: (" + result[1] + ", " + result[2] + ")");

        // Utilisez les lignes correctes pour dessiner les résultats
        ResultPlot.resultPlot(picture, lines[1], lines[0], lines[2], result[2], result[0], picture.rows(), picture.cols(), pictureFile);
    }
}




