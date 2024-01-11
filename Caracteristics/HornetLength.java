package Caracteristics;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
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

        // Recherche le la ligne inférieure de la zone d'analyse
        int counter = numberOfLines;
        int pixelCount = 0;

        double horizontalNumber = numberOfColumns * 0.4;
        while (pixelCount < horizontalNumber && counter > 0) {
            pixelCount = zeroPixels(arrayImage.row(counter - 1));
            counter--;
        }

        int lowerLine = counter;

        // Recherche le la ligne supérieure de la zone d'analyse
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

        return new int[]{upperLine, lowerLine, leftLine};
    }

    /**
     * Calcule la longueur et les coordonnées du frelon basé sur l'image binaire fournie.
     *
     * @param image Image binaire représentant le frelon.
     * @return Un tableau contenant la longueur du frelon et ses coordonnées.
     *         Le format du tableau est {longueur, coordonnéeX, coordonnéeY}.
     *         Si aucun pixel n'est trouvé, la longueur sera 0 et les coordonnées seront (0, 0).
     */

    public static int[] calculer_HornetLength(Mat image) {

        int pixelCount = 0;
        int indexMax = 0;
        int[] stingCoordinates = new int[]{0, 0};

        // Génération d'une copie de la matrice de l'image binaire du frelon pour pouvoir la manipuler
        Mat arrayImage = new Mat();
        image.copyTo(arrayImage);

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

        System.out.println("Longeur du frelon: " + pixelCount);
        int lengthValue = pixelCount;//longeur du frelon
        System.out.println("Sting coordinates"+"( "+stingCoordinates[0]+" ; "+stingCoordinates[1]+" )");
        return new int[]{lengthValue, stingCoordinates[0], stingCoordinates[1]};//longueur, coordonnéeX, coordonnéeY
    }

    /**
     * Méthode pour afficher et dessiner le lignes de boundingLines et la longeur du frelon calculer grace a calculer_HornetLength
     * @param image
     * @param imagePath
     */
    public static void resultPlot(Mat image, String imagePath) {
        Mat inputImage = Imgcodecs.imread(imagePath);

        // calcluler les bounding lines
        int[] lines = boundingLines(inputImage);
        int upperLine = lines[0];
        int lowerLine = lines[1];
        int leftLine = lines[2];

        // Calculer la longeur de frelon
        int[] lengthInfo = calculer_HornetLength(inputImage);
        int stingX = lengthInfo[1];
        int stingY = lengthInfo[2];

        int numberOfLines = image.rows();
        int numberOfColumns = image.cols();

        // Convertir l'image en image a couleurs
        Mat colorPicture = new Mat();
        Imgproc.cvtColor(image, colorPicture, Imgproc.COLOR_GRAY2BGR);


        // Ligne inférieure
        Imgproc.line(colorPicture, new Point(0, lowerLine), new Point(numberOfColumns - 1, lowerLine), new Scalar(0, 0, 255), 2);

        // Ligne supérieure
        Imgproc.line(colorPicture, new Point(0, upperLine), new Point(numberOfColumns - 1, upperLine), new Scalar(0, 0, 255), 2);

        // Ligne gauche
        Imgproc.line(colorPicture, new Point(leftLine, 0), new Point(leftLine, numberOfLines - 1), new Scalar(0, 0, 255), 2);

        // Ligne droite
        int positionX = stingX;  // Assuming the length line starts from the sting coordinates
        Imgproc.line(colorPicture, new Point(positionX, 0), new Point(positionX, numberOfLines - 1), new Scalar(0, 0, 255), 2);

        // Ligne de longueur maximale
        Imgproc.line(colorPicture, new Point(leftLine, stingY), new Point(stingX, stingY), new Scalar(0, 255, 0), 2);

        // Extrémité de la ligne de longueur maximale(POINT DE PUNTE DE FRELON)
        Imgproc.circle(colorPicture, new Point(stingX, stingY), 10, new Scalar(255, 75, 0), -1);

        HighGui.imshow("Zone de traitment et longeur maximale", colorPicture);


        // Écriture de l'image avec les lignes de recherche dessinées pour démonstration
        String outputfile = imagePath.substring(0, imagePath.length() - 4) + "_length.jpg";
        outputfile = outputfile.replace("Footage/", "Footage/");
        System.out.println(outputfile);
        Imgcodecs.imwrite(outputfile, colorPicture);
        HighGui.waitKey(0);

    }



}

