package Caracteristics;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour savoir si le frelon possede un abdomen pointu ou rond et le classifier eventuellement
 */
public class  AbdomenShape {
    /**
     * Fonction pour déterminer si le frelon possède un abdomen pointu ou rond et le classifier éventuellement.
     * @param pictureArray Matrice représentant le masque binaire du frelon
     * @param stingCoordinates Coordonnées de l'extrémité de l'abdomen
     * @return Résultat de la forme de l'abdomen ("pointu" ou "rond")
     */
    public static String abdomenShape(Mat pictureArray, Point stingCoordinates) {
        int longueurTot = pictureArray.rows();
        int largeurTot = pictureArray.cols();

        // Vérifier si les coordonnées de la ROI sont dans les limites de l'image
        if (stingCoordinates.x < 0 || stingCoordinates.y < 0 ) {
            System.out.println("Coordonnées de la ROI non valides.");
            return null;
        }

        // Zoom sur la moitié haute de l'abdomen
        Rect roiTop = new Rect((int) (stingCoordinates.x - longueurTot * 0.07), (int) (stingCoordinates.y - largeurTot * 0.07),
                (int) (longueurTot * 0.07), (int) (largeurTot * 0.07));


        // Vérifier si la ROI est dans les limites de l'image
        if (roiTop.x < 0 || roiTop.y < 0 || roiTop.width <= 0 || roiTop.height <= 0) {
            System.out.println("Dimensions de la ROI non valides.");
            return null;
        }

        Mat imStingTop = new Mat(pictureArray, roiTop);

        Imgcodecs.imwrite("Footage/ZoomTOP.jpg", imStingTop);
        // Création des contours
        Mat edgedTop = new Mat();
        Imgproc.Canny(imStingTop, edgedTop, 50, 150);
        Imgproc.dilate(edgedTop, edgedTop, new Mat(), new Point(0, 0), 1); // Dilatation des contours
        List<MatOfPoint> contoursTop = new ArrayList<>();
        Imgproc.findContours(edgedTop, contoursTop, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);


        // Création d'une image blanche
        Mat whiteBlankImageTop = Mat.ones(new Size(100, 100), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image blanche
        double seuilMin = 100;
        List<MatOfPoint> filteredContoursTop = new ArrayList<>();
        for (MatOfPoint contour : contoursTop) {
            if (Imgproc.contourArea(contour) > seuilMin) {
                filteredContoursTop.add(contour);
            }
        }

        Imgproc.drawContours(whiteBlankImageTop, filteredContoursTop, 0, new Scalar(255, 255, 255), 1);

        Imgcodecs.imwrite("Footage/Contour_dard_haut.jpg", whiteBlankImageTop);

        // Calculs
        Mat coordsTop = findPoints("Footage/Contour_dard_haut.jpg");
        if (coordsTop.empty()) return null;
        double[] m1 = findCoeffs(coordsTop);
        System.out.println("Coefficients M1: ");
        for (double coeffM1 : m1) {
            System.out.println(coeffM1);
        }

        // Zoom sur la moitié basse de l'abdomen
        Rect roiBottom = new Rect((int) (stingCoordinates.x - longueurTot * 0.07), (int) stingCoordinates.y,
                (int) (longueurTot * 0.07), (int) (largeurTot * 0.07));


        // Vérifier si les coordonnées de la ROI sont dans les limites de l'image
        if (roiBottom.x < 0 || roiBottom.y < 0 || roiBottom.width <= 0 || roiBottom.height <= 0) {
            System.out.println("Dimensions de la ROI inférieure non valides.");
            return null;
        }

        Mat imStingBottom = new Mat(pictureArray, roiBottom);
        Imgcodecs.imwrite("Footage/ZoomDOWN.jpg", imStingBottom);

        // Création des contours
        Mat edgedBottom = new Mat();
        Imgproc.Canny(imStingBottom, edgedBottom, 50, 150); // Ajustez les valeurs 50 et 150 selon vos besoins
        Imgproc.dilate(edgedBottom, edgedBottom, new Mat(), new Point(0, 0), 1); // Dilatation des contours
        List<MatOfPoint> contoursBottom = new ArrayList<>();
        Imgproc.findContours(edgedBottom, contoursBottom, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        // Création d'une image blanche
        Mat whiteBlankImageBottom = Mat.ones(new Size(100, 100), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image blanche
        List<MatOfPoint> filteredContoursBottom = new ArrayList<>();
        for (MatOfPoint contour : contoursBottom) {
            if (Imgproc.contourArea(contour) > seuilMin) {
                filteredContoursBottom.add(contour);
            }
        }
        Imgproc.drawContours(whiteBlankImageBottom, filteredContoursBottom, 0, new Scalar(255, 255, 255), 1);


        Imgcodecs.imwrite("Footage/Contour_dard_bas.jpg", whiteBlankImageBottom);

        // Calculs
        Mat coordsBottom = findPoints("Footage/Contour_dard_bas.jpg");
        if (coordsBottom.empty()) return null;
        double[] m2 = findCoeffs(coordsBottom);
        System.out.println("Coefficients M2: ");
        for (double coeffM2 : m2) {
            System.out.println(coeffM2);
        }

        // Suppression des fichiers temporaires
        /*new File("Footage/Contour_dard_haut.jpg").delete();
        new File("Footage/Contour_dard_bas.jpg").delete();
        new File("Footage/ZoomTOP.jpg").delete();
        new File("Footage/ZoomDOWN.jpg").delete();*/

        // Calcul de l'angle
        double angle = Math.round(findAngle(m1[1], m2[1]));

        // Détermination de la forme de l'abdomen
        if (angle > 60.0) {
            System.out.println("Angle   "+angle);
            System.out.println("m1  "+m1[1]);
            System.out.println("m2  "+m2[1]);
            System.out.println("pointu d'après l'angle");
            return "pointu";
        } else {
            System.out.println("Angle   "+angle);
            System.out.println("m1  "+m1[1]);
            System.out.println("m2  "+m2[1]);
            System.out.println("rond d'après l'angle");
            return "rond";
        }

    }



    /**
     * Cherche les points sur l'image.
     * @param picturePath Chemin de l'image avec les contours de l'abdomen
     * @return Matrice des coordonnées des points (abscisse et ordonnée)
     */

    public static Mat findPoints(String picturePath) {
        Mat image = Imgcodecs.imread(picturePath);
        int largeur = image.cols();
        int hauteur = image.rows();

        Mat coords = new Mat(2, largeur * hauteur, CvType.CV_32FC1);
        int count = 0;

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                double[] pixel = image.get(y, x);
                if (pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0) {
                    coords.put(0, count, x);
                    coords.put(1, count, y);
                    count++;
                }
            }
        }

        return coords.colRange(0, count);
    }

    /**
     * Trouver les coefficients pour faire d'éventuels calculs.
     * @param coords Matrice des coordonnées des points (abscisse et ordonnée)
     * @return Coefficient de la fonction représentant la droite de l'abdomen
     */
  public static double[] findCoeffs(Mat coords) {
        int size = (int) coords.total();
        WeightedObservedPoints points = new WeightedObservedPoints();

        for (int i = 0; i < size / 2; i++) {
            double x = coords.get(0, i)[0];
            double y = coords.get(1, i)[0];
            points.add(x, y);
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
        double[] coeffs = fitter.fit(points.toList());

        return coeffs;
    }
    /**
     * Fonction pour calculer l'angle à partir des coefficients.
     * @param m1 Coefficient de la première droite superieure
     * @param m2 Coefficient de la deuxième droite inferieure
     * @return Angle entre les deux droites
     */
    public static double findAngle(double m1, double m2) {
        if (Double.compare(m1, m2) == 0) {
            return Double.NaN; //ou cas ou on a des lignes paralleles
        }
        double tan = (m1 - m2) / (1 + m1 * m2);
        double arctan = Math.atan(tan);
        double degree = Math.toDegrees(arctan);
        return degree;
    }

}
