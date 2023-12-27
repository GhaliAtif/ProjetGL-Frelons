package HornetLenght;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class HornetLength {

    public static int zeroPixels(Mat line) {
        // Vérifier si l'image est déjà en niveaux de gris
        if (line.channels() > 1) {
            // Convertir l'image en niveaux de gris si elle ne l'est pas déjà
            Imgproc.cvtColor(line, line, Imgproc.COLOR_BGR2GRAY);
        }

        // Compter le nombre de pixels noirs dans la ligne
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

        double verticalNumber = numberOfLines * 0.1;  // Added verticalNumber
        int upperLine = 0;  // Initialize upperLine outside the loop

        while (pixelCount < horizontalNumber && counter < numberOfLines) {
            pixelCount = zeroPixels(arrayImage.row(counter));
            counter++;
        }

        upperLine = counter;  // Assign the value after exiting the loop

        // Recherche de la colonne gauche de la zone d'analyse

        counter = 0;
        pixelCount = 0;
        int leftLine = 0;  // Initialize leftLine outside the loop

        while (pixelCount < verticalNumber && counter < numberOfColumns) {
            pixelCount = zeroPixels(arrayImage.col(counter));
            counter++;
        }

        if (counter < numberOfColumns) {
            leftLine = counter;
        } else {
            System.out.println("No pixels found in the left column");
            // Handle this case appropriately based on your requirements
        }

        System.out.println("Upper line: " + upperLine);
        System.out.println("Lower line: " + lowerLine);
        System.out.println("Left line: " + leftLine);

        // Return the values
        return new int[]{upperLine, lowerLine, leftLine, upperLine, lowerLine, leftLine};
    }


    public static int[] hornetLength(Mat picture) {
        // ...

        int pixelCount = 0;
        int indexMax = 0;
        int[] stingCoordinates = new int[]{0, 0};

        // Génération d'une copie de la matrice de l'image binaire du frelon pour pouvoir la manipuler
        Mat arrayImage = new Mat();
        picture.copyTo(arrayImage);

        // Assurez-vous que l'image est en niveaux de gris
        if (arrayImage.channels() > 1) {
            Imgproc.cvtColor(arrayImage, arrayImage, Imgproc.COLOR_BGR2GRAY);
        }

        // Récupération des dimensions
        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        // Recupération des lignes/colonnes délimitant de la zone d'analyse du frelon
        int[] boundingLinesResult = boundingLines(arrayImage);
        int upperLine = boundingLinesResult[0];
        int lowerLine = boundingLinesResult[1];
        int leftLine = boundingLinesResult[2];

        // Extraction de la zone d'analyse du frelon pour gagner en performance
        Mat extractedArray = arrayImage.rowRange(upperLine, lowerLine).colRange(leftLine, numberOfColumns);

        // Assurez-vous que l'image extraite est en niveaux de gris
        if (extractedArray.channels() > 1) {
            Imgproc.cvtColor(extractedArray, extractedArray, Imgproc.COLOR_BGR2GRAY);
        }

        // Création et remplissage d'un tableau contenant le nombre de pixels noirs par ligne
        int[] pixelCountList = new int[extractedArray.rows()];
        for (int i = 0; i < extractedArray.rows(); i++) {
            pixelCountList[i] = zeroPixels(extractedArray.row(i));
        }

        // Recupération de la ligne de longueur maximale et de son index dans la matrice originale
        // Gestion d'exception dans le cas où aucune ligne n'est détectée
        try {
            MatOfInt pixelCountMat = new MatOfInt(pixelCountList);
            Core.MinMaxLocResult result = Core.minMaxLoc(pixelCountMat);

            if (result.maxVal > 0) {
                pixelCount = (int) result.maxVal;
                indexMax = (int) result.maxLoc.y;
                stingCoordinates = new int[]{leftLine + pixelCount, indexMax + upperLine};
            } else {
                System.out.println("No pixels found");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        System.out.println("Pixel count: " + pixelCount);
        int lengthValue = pixelCount;

        return new int[]{lengthValue, stingCoordinates[0], stingCoordinates[1]};
    }


}

