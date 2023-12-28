package AbdomenShape;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour savoir si le frelon possede un abdomen pointu ou rondu et le classifier eventuellement
 */
public class AbdomenShape {
    /**
     * Fonction pour déterminer si le frelon possède un abdomen pointu ou rond et le classifier éventuellement.
     * @param pictureArray Matrice représentant le masque binaire du frelon
     * @param stingCoordinates Coordonnées de l'extrémité de l'abdomen
     * @return Résultat de la forme de l'abdomen ("pointu" ou "rond")
     */
    public static String abdomenShape(Mat pictureArray, Point stingCoordinates) {
        int longueurTot = pictureArray.rows();
        int largeurTot = pictureArray.cols();

        // Zoom sur la moitié haute de l'abdomen
        Rect roiTop = new Rect((int) (stingCoordinates.x - longueurTot * 0.05), (int) (stingCoordinates.y - largeurTot * 0.05),
                (int) (longueurTot * 0.05), (int) (largeurTot * 0.05));
        Mat imStingTop = new Mat(pictureArray, roiTop);

        // Création des contours
        Mat edgedTop = new Mat();
        Imgproc.Canny(imStingTop, edgedTop, 50, 150);
        Imgproc.dilate(edgedTop, edgedTop, new Mat(), new Point(-1, -1), 2); // Dilatation des contours
        List<MatOfPoint> contoursTop = new ArrayList<>();
        Imgproc.findContours(edgedTop, contoursTop, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        // Création d'une image blanche
        Mat whiteBlankImageTop = Mat.ones(new Size(longueurTot, largeurTot), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image blanche
        double seuilMin = 100; // Ajustez la valeur du seuil selon vos besoins
        List<MatOfPoint> filteredContoursTop = new ArrayList<>();
        for (MatOfPoint contour : contoursTop) {
            if (Imgproc.contourArea(contour) > seuilMin) {
                filteredContoursTop.add(contour);
            }
        }
        Imgproc.drawContours(whiteBlankImageTop, filteredContoursTop, -1, new Scalar(0, 0, 0), 1);

        Imgcodecs.imwrite("Footage/Contour_dard_haut.jpg", whiteBlankImageTop);

        // Calculs
        Mat coordsTop = findPoints("Footage/Contour_dard_haut.jpg");
        if (coordsTop.empty()) return null;
        double m1 = findCoeffs(coordsTop);

        // Zoom sur la moitié basse de l'abdomen
        Rect roiBottom = new Rect((int) (stingCoordinates.x - longueurTot * 0.05), (int) stingCoordinates.y,
                (int) (longueurTot * 0.05), (int) (largeurTot * 0.05));
        Mat imStingBottom = new Mat(pictureArray, roiBottom);

        // Création des contours
        Mat edgedBottom = new Mat();
        Imgproc.Canny(imStingBottom, edgedBottom, 50, 150); // Ajustez les valeurs 50 et 150 selon vos besoins
        Imgproc.dilate(edgedBottom, edgedBottom, new Mat(), new Point(-1, -1), 2); // Dilatation des contours
        List<MatOfPoint> contoursBottom = new ArrayList<>();
        Imgproc.findContours(edgedBottom, contoursBottom, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        // Création d'une image blanche
        Mat whiteBlankImageBottom = Mat.ones(new Size(roiBottom.width, roiBottom.height), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image blanche
        List<MatOfPoint> filteredContoursBottom = new ArrayList<>();
        for (MatOfPoint contour : contoursBottom) {
            if (Imgproc.contourArea(contour) > seuilMin) {
                filteredContoursBottom.add(contour);
            }
        }
        Imgproc.drawContours(whiteBlankImageBottom, filteredContoursBottom, -1, new Scalar(0, 0, 0), 1);

        Imgcodecs.imwrite("Footage/Contour_dard_bas.jpg", whiteBlankImageBottom);

        // Calculs
        Mat coordsBottom = findPoints("Footage/Contour_dard_bas.jpg");
        if (coordsBottom.empty()) return null;
        double m2 = findCoeffs(coordsBottom);

        // Suppression des fichiers temporaires
        new File("Footage/Contour_dard_haut.jpg").delete();
        new File("Footage/Contour_dard_bas.jpg").delete();

        // Calcul de l'angle
        double angle = findAngle(m1, m2);

        // Détermination de la forme de l'abdomen
        if (angle <= 60) {
            System.out.println("pointu d'après l'angle");
            return "pointu";
        } else {
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
    public static double findCoeffs(Mat coords) {
        int size = (int) coords.total();
        MatOfPoint2f points = new MatOfPoint2f();

        for (int i = 0; i < size / 2; i++) {
            double x = coords.get(0, i)[0];
            double y = coords.get(1, i)[0];
            points.push_back(new MatOfPoint2f(new Point(x, y)));
        }

        RotatedRect ellipse = Imgproc.fitEllipse(points);
        double m = Math.tan(Math.toRadians(ellipse.angle));
        return m;
    }
    /**
     * Fonction pour calculer l'angle à partir des coefficients.
     * @param m1 Coefficient de la première droite superieure
     * @param m2 Coefficient de la deuxième droite inferieure
     * @return Angle entre les deux droites
     */
    public static double findAngle(double m1, double m2) {
        double tan = (m1 - m2) / (1 + m1 * m2);
        double arctan = Math.atan(tan);
        double degree = Math.toDegrees(arctan);
        return degree;
    }

}
