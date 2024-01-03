package HornetLenght;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.HighGui;

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

        int[] result = HornetLength.hornetLength(picture, pictureFile);

        System.out.println("Hornet Length: " + result[0]);
        System.out.println("Sting Coordinates: (" + result[1] + ", " + result[2] + ")");

        ResultPlot.resultPlot(picture, result[1], result[2], 0, result[2] - result[0] / 2, result[0], picture.rows(), picture.cols(), pictureFile);
    }
}


