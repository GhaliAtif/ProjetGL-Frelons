package HornetLenght;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class BoundingLines {

    public static int zeroPixels(Mat line) {
        // Convert the line to grayscale
        Imgproc.cvtColor(line, line, Imgproc.COLOR_BGR2GRAY);

        // Count the number of black pixels in the line
        int blackPixelCount = line.rows() * line.cols() - Core.countNonZero(line);

        return blackPixelCount;
    }

    public static int[] boundingLines(Mat arrayImage) {
        // Récupération du nombre de lignes et de colonnes de la matrice de l'image binaire du frelon
        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        System.out.println("Number of lines: " + numberOfLines);

        // Recherche de la ligne inférieure de la zone d'analyse
        int counter = numberOfLines;
        int pixelCount = 0;

        double horizontalNumber = numberOfColumns * 0.4;
        while (pixelCount < horizontalNumber && counter > 0) {
            pixelCount = zeroPixels(arrayImage.row(counter - 1));
            counter--;
        }

        int lowerLine = counter;

        // Recherche de la ligne supérieure de la zone d'analyse

        counter = 0;
        pixelCount = 0;

        while (pixelCount < horizontalNumber && counter < numberOfLines) {
            pixelCount = zeroPixels(arrayImage.row(counter));
            counter++;
        }

        int upperLine = counter;

        // Recherche de la colonne gauche de la zone d'analyse

        counter = 0;
        pixelCount = 0;

        double verticalNumber = numberOfLines * 0.1;
        while (pixelCount < verticalNumber && counter < numberOfColumns) {
            pixelCount = zeroPixels(arrayImage.col(counter));
            counter++;
        }

        int leftLine = counter;

        System.out.println("Upper line: " + upperLine);
        System.out.println("Lower line: " + lowerLine);
        System.out.println("Left line: " + leftLine);

        return new int[]{upperLine, lowerLine, leftLine};
    }
}

