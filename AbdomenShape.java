import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour savoir si le frelon possede un abdomen pointu ou rondu et le classifier eventuellement
 */
public class AbdomenShape {
    /**
     * Fonction pour savoir si le frelon possede un abdomen pointu ou rondu et le classifier eventuellement
     * @param pictureArray
     * @param stingCoordinates
     * @return
     */
    public static String abdomenShape(Mat pictureArray, Point stingCoordinates) {
        List<Double> coefficients1, coefficients2 ;
        // Taille de l'image
        Size size = pictureArray.size();
        int longueurTot = (int) size.height;
        int largeurTot = (int) size.width;

        // Zoom sur la partie superiure de l'abdomen
        Mat imStingUpper = pictureArray.submat(
                (int) (stingCoordinates.y - largeurTot * 0.05),
                (int) stingCoordinates.y,
                (int) (stingCoordinates.x - longueurTot * 0.05),
                (int) stingCoordinates.x);

        // Zoom sur la partie inferiure de l'abdomen
        Mat imStingLower = pictureArray.submat(
                (int) stingCoordinates.y, (int) (stingCoordinates.y + longueurTot * 0.05),
                (int) (stingCoordinates.x - largeurTot * 0.05), (int) stingCoordinates.x);
        //Proceder a dessiner la partie superieur
        processAbdomenHalf(imStingUpper, "Footage/Contour_dard_haut.jpg",largeurTot,longueurTot);

        // Proceder a dessiner la partie inferiure
        processAbdomenHalf(imStingLower, "Footage/Contour_dard_bas.jpg",largeurTot,longueurTot);

        // Partie Calcul
       coefficients1 = findPoints("Footage/Contour_dard_haut.jpg");
        if (coefficients1.isEmpty()) {
            return "";
        }
        // Calculate
        coefficients2 = findPoints("Footage/Contour_dard_bas.jpg");
        if (coefficients2.isEmpty()) {
            return "";
        }

        // Remove temporary files
        new File("Footage/Contour_dard_haut.jpg").delete();
        new File("Footage/Contour_dard_bas.jpg").delete();

        //TODO refaire la fonction car affiche pas de resultat
        //TODO faire test unitaire
        // Calcul de l'angle ( binome de l'année derniere)
        double angle = findAngle(coefficients1, coefficients2);

        // Determine the shape of the abdomen
        if (angle <= 30) {
            System.out.println("pointu d'après l'angle");
            return "pointu";
        } else {
            System.out.println("rond d'après l'angle");
            return "rond";
        }

    }

    /**
     * Cherche les points sur l'image
     * @param picturePath
     * @return
     */
    public static List<Double> findPoints(String picturePath) {
        try {
            BufferedImage image = ImageIO.read(new File(picturePath));
            int largeur = image.getWidth();
            int hauteur = image.getHeight();

            List<Double> X = new ArrayList<>();
            List<Double> Y = new ArrayList<>();

            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < hauteur; y++) {
                    int pixel = image.getRGB(x, y);
                    if (pixel == 0) {
                        X.add((double) x);
                        Y.add((double) y);
                    }
                }
            }

            List<Double> coefficients = new ArrayList<>();
            coefficients.addAll(X);
            coefficients.addAll(Y);

            return coefficients;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Trouver les coefficients pour faire eventuels calculs
     * @param X
     * @param Y
     * @return
     */
    public static List<Double> findCoeffs(List<Double> X, List<Double> Y) {
        SimpleRegression regression = new SimpleRegression();

        for (int i = 0; i < X.size(); i++) {
            regression.addData(X.get(i), Y.get(i));
        }

        // Get the slope (coefficient for x)
        double slope = regression.getSlope();

        // Get the intercept
        double intercept = regression.getIntercept();

        List<Double> coefficients = List.of(slope, intercept);
        return coefficients;
    }

    /**
     * Fonction pour calculer l'angle a partir des coefficients
     * @param coeff1
     * @param coeff2
     * @return
     */
    public static double findAngle(List<Double> coeff1, List<Double> coeff2) {
        double tan = (coeff1.get(0) - coeff2.get(0)) / (1 + coeff1.get(0) * coeff2.get(0));
        double arctan = Math.atan(tan);
        return arctan * 180 / Math.PI;
    }

   /* private static Mat loadImage(String imagePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            Mat mat = bufferedImageToMat(bufferedImage);
            return mat;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Mat bufferedImageToMat(BufferedImage img) {
        int rows = img.getHeight();
        int cols = img.getWidth();
        Mat mat = new Mat(rows, cols, CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }*/

    /**
     * Methode action pour proceder a dessiner la partie inferieure et superieure de l'abdomen
     * @param im_sting_half
     * @param outputFilePath
     * @param largeurTot
     * @param longueurTot
     */
   private static void processAbdomenHalf(Mat im_sting_half, String outputFilePath,int largeurTot, int longueurTot) {
       // Création des contours
           Mat edged = new Mat();
           Imgproc.Canny(im_sting_half, edged, 30, 200);

           List<MatOfPoint> contours = new ArrayList<>();
           Mat hierarchy = new Mat();
           Imgproc.findContours(edged, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

           // Création d'une image blanche
           Mat whiteBlankImage = Mat.ones(new Size(longueurTot * 0.05, largeurTot * 0.05), CvType.CV_8UC3);
           whiteBlankImage.setTo(new Scalar(255, 255, 255));

           // Dessin des contours sur l'image blanche
           Imgproc.drawContours(whiteBlankImage, contours, -1, new Scalar(0, 0, 0), 1);

           Imgcodecs.imwrite(outputFilePath, whiteBlankImage);

   }
}

