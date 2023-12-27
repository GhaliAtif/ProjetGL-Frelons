

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class cutout {

    public static Mat cutout(String filename) {
        try {
            // Chargement de l'image
            Mat img = Imgcodecs.imread(filename);

            // Conversion en niveaux de gris et en espace de couleur HSV
            Mat imgGREY = new Mat();
            Imgproc.cvtColor(img, imgGREY, Imgproc.COLOR_BGR2GRAY);

            Mat imgHSV = new Mat();
            Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2HSV);

            Scalar color1 = new Scalar(10, 200, 20);  // Borne sombre du spectre de couleur jaune à garder
            Scalar color2 = new Scalar(45, 255, 255);  // Borne claire du spectre de couleur jaune à garder

            // Création du masque principal (Masque de niveau de gris)
            MatOfFloat threshold = new MatOfFloat(90);
            Mat mask = new Mat();
            Imgproc.threshold(imgGREY, mask, threshold.get(0, 0)[0], 255, Imgproc.THRESH_BINARY);

            // Création du masque secondaire (Masque de couleur jaune)
            Mat tempMask2 = new Mat();
            Core.inRange(imgHSV, color1, color2, tempMask2);
            Mat mask2 = new Mat();
            Core.bitwise_not(tempMask2, mask2);

            // Addition des deux masques
            Mat combinedMask = new Mat();
            Core.bitwise_and(mask, mask2, combinedMask);

            // Dilatation/érosion du masque pour supprimer les petits artefacts
            Mat intermediateSmoothedMask = new Mat();
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.morphologyEx(combinedMask, intermediateSmoothedMask, Imgproc.MORPH_OPEN, kernel);

            Mat smoothedMask = new Mat();
            Imgproc.morphologyEx(intermediateSmoothedMask, smoothedMask, Imgproc.MORPH_CLOSE, new Mat());

            return smoothedMask;

        } catch (Exception e) {
            // Gestion des erreurs, par exemple, l'image n'a pas pu être lue
            e.printStackTrace();
            return null;
        }
    }


}
