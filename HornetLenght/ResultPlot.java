package HornetLenght;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ResultPlot {

    public static int resultPlot(Mat picture, int lowerLine, int upperLine, int leftLine, int indexMax, int pixelCount,
                                 int numberOfLines, int numberOfColumns, String pictureFile) {

        // Convertir l'image en niveaux de gris
        Imgproc.cvtColor(picture, picture, Imgproc.COLOR_BGR2GRAY);

        // Ligne inférieure
        Imgproc.line(picture, new Point(0, lowerLine), new Point(numberOfColumns, lowerLine), new Scalar(0, 0, 255), 2);

        // Ligne supérieure
        Imgproc.line(picture, new Point(0, upperLine), new Point(numberOfColumns, upperLine), new Scalar(0, 0, 255), 2);

        // Ligne gauche
        Imgproc.line(picture, new Point(leftLine, 0), new Point(leftLine, numberOfLines), new Scalar(0, 0, 255), 2);

        // Ligne droite
        int positionning = (int) (leftLine + pixelCount);
        Imgproc.line(picture, new Point(positionning, 0), new Point(positionning, numberOfLines), new Scalar(0, 0, 255), 2);

        // Ligne de longueur maximale
        Imgproc.line(picture, new Point(leftLine, indexMax + upperLine), new Point(positionning, indexMax + upperLine),
                new Scalar(0, 255, 0), 2);

        // Extrémité de la ligne de longueur maximale
        Imgproc.circle(picture, new Point(positionning, indexMax + upperLine), 10, new Scalar(255, 75, 0), -1);

        HighGui.imshow("Hornet length", picture);

        // Écriture de l'image avec les lignes de recherche dessinées pour démonstration
        String outputfile = pictureFile.substring(0, pictureFile.length() - 4) + "_length.jpg";
        outputfile = outputfile.replace("Footage/", "Footage/");
        System.out.println(outputfile);
        Imgcodecs.imwrite(outputfile, picture);
        HighGui.waitKey(0);

        return 0;
    }
}

