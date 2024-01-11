package TestUnitaires;
import Caracteristics.AbdomenShape;
import Caracteristics.HornetLength;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;


import static org.junit.Assert.*;

public class TestCaracteristics {

    public void run() {
        //Chargement de la librairie
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Section 1 : Test des fonctions de recherche de la longueur

    /**
     * Teste la fonction zeroPixels qui compte le nombre de pixels noirs dans une ligne de pixels.
     * L'idée est ici de vérifier que la fonction compte bien tous les pixels noirs sans s'arrêter au premier pixel blanc rencontré.
     */

    @Test
    public void testZeroPixels() {
        run();
        // Cas de test avec une image blanche (tous les pixels blancs)
        Mat whiteImage = new Mat(3, 3, CvType.CV_8UC1, new Scalar(255));
        assertEquals(0, HornetLength.zeroPixels(whiteImage));

        // Cas de test avec une image noire (tous les pixels noirs)
        Mat blackImage = new Mat(3, 3, CvType.CV_8UC1, new Scalar(0));
        assertEquals(9, HornetLength.zeroPixels(blackImage));

    }

    /**
     * Teste le bon positionnement des lignes de délimitation de la zone d'intérêt.
     * L'idée ici est de vérifier avec des exemples simples que la fonction renvoie bien les bonnes valeurs.
     */
    @Test
    public void testBoundingLines() {
        run();
        // Cas de test : Ou il s'arrête à la dernière ligne
        Mat emptyImage = new Mat(1920, 1080, CvType.CV_8UC1);
        int[] resultEmpty = HornetLength.boundingLines(emptyImage);
        assertArrayEquals(new int[]{1, 1919, 1}, resultEmpty);

        // Cas de test : Image frelon 2023
        String filepathIMG2 = "Footage/16_cutout.jpg";
        Mat projectImage = Imgcodecs.imread(filepathIMG2);
        int[] resultProject = HornetLength.boundingLines(projectImage);
        assertArrayEquals(new int[]{409, 895, 121}, resultProject);
    }

    /**
     * Teste le calcul de la longueur du frelon.
     * La fonction s'appuie sur zeroPixels et boundingLines pour calculer la longueur du frelon. On vérifie que l'association de ces fonctions est bien réalisée et que la longueur est bien calculée.
     */
    @Test
    public void testHornetLength() {

        run();
        // Chargement des images de référence
        Mat im1 = Imgcodecs.imread("Footage/black.png");
        Mat im2 = Imgcodecs.imread("Footage/white.png");
        Mat im3 = Imgcodecs.imread("Footage/bw50.png");
        Mat im4 = Imgcodecs.imread("Footage/21_cutout.jpg");
        Mat im5 = Imgcodecs.imread("Footage/16_cutout.jpg");

        // Tests sur les images de reference
        assertEquals(1919, HornetLength.calculer_HornetLength(im1)[0]); // Image totalement noire
        assertEquals(959, HornetLength.calculer_HornetLength(im3)[0]); // Image à 50% de blanc et 50% de noir
        assertEquals(812, HornetLength.calculer_HornetLength(im4)[0]); // Image simpliste
        assertEquals(1269, HornetLength.calculer_HornetLength(im5)[0]); // Image frelon de la base 2023
    }



    // Section 2 : Test des fonctions de recherche de la forme de l'abdomen
    @Test
    public void testFindPoints() {
        run();
        // Teste la fonction de recherche des points de contours de l'abdomen.
        // On cherche à vérifier que la fonction renvoie bien les bonnes séries de valeurs pour des images de référence.

        // Chargement des images de référence
        String im1 = "Footage/abdomen_up.jpg";
        String im2 = "Footage/abdomen_down.jpg";
        String im3 = "Footage/white.png";

        // Tests
        // Tests sur l'image de référence du projet
        //Partie superieure de l'abdomen
        int[][] mat1Test ={{4, 10, 14, 15, 45, 55, 61, 62, 63, 65, 66, 75, 81, 83, 84, 93, 95},{19, 20, 22, 24, 36, 48, 53, 54, 56, 57, 58, 67, 77, 79, 82, 91, 97}};
        // Conversion de la matrice OpenCV en un tableau d'entiers
        int[][] matActuelUP = convertMatToArray(AbdomenShape.findPoints(im1));
        assertEquals(mat1Test, matActuelUP);
        //Partie inferieure de l'abdomen
        int[][] mat2Test ={{10, 15, 21, 27, 34, 42, 49, 51, 61, 66, 69, 72, 74, 75, 78, 96},{75, 73, 71, 69, 67, 65, 57, 55, 49, 44, 42, 39, 37, 36, 33, 15}};
        int[][] matActuelDOWN = convertMatToArray(AbdomenShape.findPoints(im2));
        assertEquals(mat2Test, matActuelDOWN);

        // Teste bien que la fonction renvoie une liste vide si l'image est totalement blanche
        int[][] mat3Test = new int[0][];
        int[][] matActuelIM3 = convertMatToArray(AbdomenShape.findPoints(im3));
        assertEquals(mat3Test, matActuelIM3);

    }

    /**
           Teste la fonction de recherche des coefficients de la droite de l'abdomen.
           Avec des images de référence, on vérifie que la fonction renvoie bien les bonnes valeurs de coefficients.
     */
    @Test
    public void testFindCoeffs() {
        run();
        //Cas de test avec differentes matrice pour tester le resultat de FindCoeff
        // Cas 1
        Mat inputMat1 = new Mat(2, 3, CvType.CV_64FC1);
        inputMat1.put(0, 0, 1, 2, 3);  // Première ligne
        inputMat1.put(1, 0, 1, 2, 3);  // Deuxième ligne
        double[] resultMat1 = AbdomenShape.findCoeffs(inputMat1);
        assertEquals(0, Math.round(resultMat1[0])); // Coefficient directeur
        assertEquals(1, Math.round(resultMat1[1])); // Ordonnée à l'origine
        inputMat1.release();

        // Cas 2
        Mat inputMat2 = new Mat(2, 3, CvType.CV_64FC1);
        inputMat2.put(0, 0, 1, 2, 3);  // Première ligne
        inputMat2.put(1, 0, 2, 4, 6);  // Deuxième ligne
        double[] resultMat2 = AbdomenShape.findCoeffs(inputMat2);
        assertEquals(0, Math.round(resultMat2[0])); // Coefficient directeur
        assertEquals(2, Math.round(resultMat2[1])); // Ordonnée à l'origine
        inputMat2.release();

        // Cas 3
        Mat inputMat3 = new Mat(2, 3, CvType.CV_64FC1);
        inputMat3.put(0, 0, 1, 2, 3);  // Première ligne
        inputMat3.put(1, 0, 4, 7, 10); // Deuxième ligne
        double[] resultMat3 = AbdomenShape.findCoeffs(inputMat3);
        assertEquals(1, Math.round(resultMat3[0])); // Coefficient directeur
        assertEquals(3, Math.round(resultMat3[1])); // Ordonnée à l'origine
        inputMat3.release();

        // Cas 4
        Mat inputMat4 = new Mat(2, 3, CvType.CV_64FC1);
        inputMat4.put(0, 0, 4, 10, 12.5);  // Première ligne
        inputMat4.put(1, 0, 0, 12, 17);    // Deuxième ligne
        double[] resultMat4 = AbdomenShape.findCoeffs(inputMat4);
        assertEquals(-8, Math.round(resultMat4[0])); // Coefficient directeur
        assertEquals(2, Math.round(resultMat4[1])); // Ordonnée à l'origine
        inputMat4.release();
    }
    /**
     * Test macro de la fonction de recherche de la forme de l'abdomen. Vérifie que
     * les fonctions fonctionnent ensemble et mènent bien à la bonne forme.
     */
    @Test
    public void testAbdomenShape() {

        run();
        // Chargement des images de référence
        Mat im1 = Imgcodecs.imread("Footage/16_cutout.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Mat im2 = Imgcodecs.imread("Footage/white.png", Imgcodecs.IMREAD_GRAYSCALE);
        Mat im3 = Imgcodecs.imread("Footage/black.png", Imgcodecs.IMREAD_GRAYSCALE);
        Mat im4 = Imgcodecs.imread("Footage/32_cutout.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Mat im5 = Imgcodecs.imread("Footage/33_cutout.jpg", Imgcodecs.IMREAD_GRAYSCALE);

        // Tests
        assert AbdomenShape.abdomenShape(im1, new Point(1390 , 713)).equals("pointu"); // Vraie image de frelon pointu
        assert AbdomenShape.abdomenShape(im2, new Point(971, 533)) == null; // Pas de forme (tout blanc)
        assert AbdomenShape.abdomenShape(im3, new Point(500, 500)) == null; // Pas de forme (tout noir)
        assert AbdomenShape.abdomenShape(im4, new Point(373, 318)).equals("rond"); // Forme ronde simple
        assert AbdomenShape.abdomenShape(im5, new Point(396, 350)).equals("pointu"); // Forme triangulaire simple
    }


    /**
     * Méthode pour convertir une matrice OpenCV en un tableau d'entiers
     * @param mat objet de type matrice
     * @return result un array de array(matrice) de int
     */
    private int[][] convertMatToArray(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();

        int[][] result = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = (int) mat.get(i, j)[0];
            }
        }

        return result;
    }


}