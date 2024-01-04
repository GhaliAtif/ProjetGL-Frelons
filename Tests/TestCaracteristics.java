package Tests;
import Caracteristics.AbdomenShape;
import Caracteristics.HornetLength;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.image.*;
import java.io.File;


import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestCaracteristics {

    public void run() {
        //nu.pattern.OpenCV.loadLocally();
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

        // Cas de test avec une image partiellement noire
        Mat partialBlackImage = new Mat(3, 3, CvType.CV_8UC1);
        partialBlackImage.put(0, 0, 255);  // Pixel blanc
        partialBlackImage.put(1, 0, 0);    // Pixel noir
        partialBlackImage.put(2, 0, 0);    // Pixel noir

        // Print the contents of the image
        System.out.println("Partial Black Image:");
        System.out.println(partialBlackImage.dump());

        assertEquals(8, HornetLength.zeroPixels(partialBlackImage));

        // Cas de test avec une image en couleur (doit être convertie en niveaux de gris)

        Mat colorImage = new Mat(3, 3, CvType.CV_8UC3, new Scalar(0, 0, 255));
        Mat grayImage = new Mat();

        // Check the result of the zeroPixels method
        int zeroPixelsCount = HornetLength.zeroPixels(grayImage);
        System.out.println("Zero Pixels Count: " + zeroPixelsCount);
        assertEquals(0, HornetLength.zeroPixels(grayImage));
    }

    /**
     * Teste le bon positionnement des lignes de délimitation de la zone d'intérêt.
     * L'idée ici est de vérifier avec des exemples simples que la fonction renvoie bien les bonnes valeurs.
     */
    @Test
    public void testBoundingLines() {
        run();
        // Cas de test : S'arrête à la dernière ligne
       /* Mat emptyImage = new Mat(1920, 1080, CvType.CV_8UC1);
        int[] resultEmpty = HornetLength.boundingLines(emptyImage);
        System.out.print("Results: ");
        for (int i = 0; i < resultEmpty.length; i++) {
            System.out.print(resultEmpty[i] + " ");
        }
        System.out.println();

        assertArrayEquals(new int[]{1920, 0, 1080, 1920, 0, 1080}, resultEmpty);*/

        // Cas de test : S'arrête à la première ligne de pixels noirs
       /* Mat blackImage = new Mat(1920, 1080, CvType.CV_8UC1, new Scalar(0, 0, 0));
        int[] resultBlack = HornetLength.boundingLines(blackImage);
        System.out.print("Results: ");
        for (int i = 0; i < resultBlack.length; i++) {
            System.out.print(resultBlack[i] + " ");
        }
        System.out.println();
        assertArrayEquals(new int[]{1, 1919, 1, 1, 1919, 1}, resultBlack);*/

        // Cas de test : Image simple
       /* String filepathIMG1 = "Footage/16_cutout.jpg";
        Mat simpleImage = Imgcodecs.imread(filepathIMG1);
        int[] resultSimple = HornetLength.boundingLines(simpleImage);
        System.out.print("Results: ");
        for (int i = 0; i < resultSimple.length; i++) {
            System.out.print(resultSimple[i] + " ");
        }
        System.out.println();
        assertArrayEquals(new int[]{408, 687, 96, 408, 687, 96}, resultSimple);*/

        // Cas de test : Image étalon du projet
        String filepathIMG2 = "Footage/15_cutout.jpg";
        Mat projectImage = Imgcodecs.imread(filepathIMG2);
        int[] resultProject = HornetLength.boundingLines(projectImage);
        System.out.print("Results: ");
        for (int i = 0; i < resultProject.length; i++) {
            System.out.print(resultProject[i] + " ");
        }
        System.out.println();
        assertArrayEquals(new int[]{430, 621, 168, 430, 621, 168}, resultProject);
    }
   /*@Test
    public void testBoundingLines() {
        // Cas de test : S'arrête à la dernière ligne
        Mat emptyImageMock = mock(Mat.class);
        when(emptyImageMock.rows()).thenReturn(1920);
        when(emptyImageMock.cols()).thenReturn(1080);

        int[] resultEmpty = HornetLength.boundingLines(emptyImageMock);
        assertArrayEquals(new int[]{1920, 0, 1080, 1920, 0, 1080}, resultEmpty);

        // Cas de test : S'arrête à la première ligne de pixels noirs
        Mat blackImageMock = mock(Mat.class);
        when(blackImageMock.rows()).thenReturn(1920);
        when(blackImageMock.cols()).thenReturn(1080);
        when(blackImageMock.get(anyInt(), anyInt())).thenAnswer(invocation -> new Scalar(0));

        int[] resultBlack = HornetLength.boundingLines(blackImageMock);
        assertArrayEquals(new int[]{1, 1919, 1, 1, 1919, 1}, resultBlack);

        // Cas de test : Image simple
        Mat simpleImageMock = mock(Mat.class);
        when(simpleImageMock.rows()).thenReturn(408);
        when(simpleImageMock.cols()).thenReturn(687);

        int[] resultSimple = HornetLength.boundingLines(simpleImageMock);
        assertArrayEquals(new int[]{408, 687, 96, 408, 687, 96}, resultSimple);

        // Cas de test : Image étalon du projet
        Mat projectImageMock = mock(Mat.class);
        when(projectImageMock.rows()).thenReturn(430);
        when(projectImageMock.cols()).thenReturn(621);

        int[] resultProject = HornetLength.boundingLines(projectImageMock);
        assertArrayEquals(new int[]{430, 621, 168, 430, 621, 168}, resultProject);
    }*/


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
        Mat im4 = Imgcodecs.imread("Footage/hornetcube_cutout.jpg");
        Mat im5 = Imgcodecs.imread("Footage/15_cutout.jpg");

        // Tests
        assertEquals(1919, HornetLength.calculer_HornetLength(im1)[0]); // Image totalement noire
       // assertEquals(1, HornetLength.calculer_HornetLength(im2)[0]); // Image totalement blanche
        assertEquals(959, HornetLength.calculer_HornetLength(im3)[0]); // Image à 50% de blanc et 50% de noir
        assertEquals(874, HornetLength.calculer_HornetLength(im4)[0]); // Image simpliste
        assertEquals(704, HornetLength.calculer_HornetLength(im5)[0]); // Image étalon du projet
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

        // Tests (on arrondi car la fonction appelée donne des résultats avec une précision de 10^-15)
        // Exemple : Donne 1.9999999999999998 au lieu de 2

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

    // Méthode pour convertir une matrice OpenCV en un tableau d'entiers
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