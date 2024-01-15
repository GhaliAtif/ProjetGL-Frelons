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
 * Classe pour savoir si le frelon possede un abdomen pointu ou rond.
 */
public class  AbdomenShape {
    /**
     * Fonction pour déterminer si le frelon possède un abdomen pointu ou rond.
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


        // Création d'une image noire
        Mat whiteBlankImageTop = Mat.ones(new Size(100, 100), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image noire
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

        // Création d'une image noire
        Mat whiteBlankImageBottom = Mat.ones(new Size(100, 100), CvType.CV_8U);

        // Dessin des contours filtrés sur l'image noire
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
     * Cette méthode prend en entrée le chemin d'une image et retourne une matrice contenant les coordonnées
     * des points dont la couleur est noire dans l'image.
     *
     * @param picturePath Le chemin de l'image à analyser.
     * @return Une matrice (Mat) contenant les coordonnées (x, y) des points noirs dans l'image.
     */
    public static Mat findPoints(String picturePath) {
        // Charger l'image depuis le chemin spécifié
        Mat image = Imgcodecs.imread(picturePath);

        // Récupérer les dimensions de l'image (largeur et hauteur)
        int largeur = image.cols();
        int hauteur = image.rows();

        // Créer une matrice pour stocker les coordonnées des points noirs
        Mat coords = new Mat(2, largeur * hauteur, CvType.CV_32FC1);

        // Compteur pour suivre le nombre de points noirs trouvés
        int count = 0;

        // Parcourir tous les pixels de l'image
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                // Récupérer la valeur du pixel à la position (x, y)
                double[] pixel = image.get(y, x);

                // Vérifier si le pixel est noir (RGB : 0, 0, 0)
                if (pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0) {
                    // Stocker les coordonnées du point noir dans la matrice
                    coords.put(0, count, x);
                    coords.put(1, count, y);
                    // Incrémenter le compteur de points noirs
                    count++;
                }
            }
        }

        // Retourner la sous-matrice contenant les coordonnées des points noirs
        return coords.colRange(0, count);
    }

    /**
     * Cette méthode prend en entrée une matrice de coordonnées (x, y) et utilise une régression polynomiale
     * d'ordre 1 pour calculer les coefficients du polynôme qui représente la relation entre x et y.
     *
     * @param coords La matrice de coordonnées (x, y) des points à utiliser pour la régression polynomiale.
     * @return Un tableau de doubles contenant les coefficients du polynôme d'ordre 1.
     */
    public static double[] findCoeffs(Mat coords) {
        // Obtenir le nombre total de points dans la matrice
        int size = (int) coords.total();

        // Créer un conteneur pour les points pondérés
        WeightedObservedPoints points = new WeightedObservedPoints();

        // Ajouter les points à partir de la matrice aux points pondérés
        for (int i = 0; i < size / 2; i++) {
            // Récupérer les coordonnées (x, y) du point dans la matrice
            double x = coords.get(0, i)[0];
            double y = coords.get(1, i)[0];
            // Ajouter le point aux points pondérés
            points.add(x, y);
        }

        // Utiliser la bibliothèque Apache Commons Math pour effectuer la régression polynomiale d'ordre 1
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
        // Calculer les coefficients du polynôme
        double[] coeffs = fitter.fit(points.toList());

        // Retourner les coefficients calculés
        return coeffs;
    }

    /**
     * Cette méthode prend deux pentes de lignes en entrée et calcule l'angle en degrés entre ces deux lignes.
     * En cas de lignes parallèles, la méthode renvoie Double.NaN (indéfini).
     *
     * @param m1 La pente de la première droite superieure.
     * @param m2 La pente de la deuxième droite inferieure.
     * @return L'angle en degrés entre les deux lignes, ou Double.NaN en cas de lignes parallèles.
     */
    public static double findAngle(double m1, double m2) {
        // Vérifier si les pentes sont égales (lignes parallèles)
        if (Double.compare(m1, m2) == 0) {
            return Double.NaN; // Cas où les lignes sont parallèles, angle indéfini
        }

        // Calculer la tangente de l'angle entre les deux lignes
        double tan = (m1 - m2) / (1 + m1 * m2);

        // Calculer l'arc tangente et convertir le résultat en degrés
        double arctan = Math.atan(tan);
        double degree = Math.toDegrees(arctan);

        // Retourner l'angle calculé
        return degree;
    }

}
