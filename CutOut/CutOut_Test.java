package CutOut;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Cette classe sert de classe de test pour la méthode de découpage d'image dans la classe CutOut.
 */
public class CutOut_Test {

    /**
     * Affiche une image dans une fenêtre JFrame.
     *
     * @param image L'objet Mat représentant l'image à afficher.
     * @param title Le titre de la fenêtre JFrame.
     */
    public static void displayImage(Mat image, String title) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        try {
            bufImage = ImageIO.read(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(bufImage);
        JFrame frame = new JFrame(title);
        frame.setLayout(new FlowLayout());
        frame.setSize(400, 400);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Méthode principale de test. Charge une image, effectue le découpage, et affiche le résultat.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "Footage/32.jpg";
        String title= imagePath.substring(8,10);
        Mat result = CutOut.cutOut(imagePath);

        if (result != null) {
            displayImage(result, title);
        }
    }
}