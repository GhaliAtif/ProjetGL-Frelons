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

public class cutouTest {
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

        // Sauvagarde de l'image resultat
        String outputPath = "Footage/Cutout"+title+".jpg";
        Imgcodecs.imwrite(outputPath, image);
        System.out.println("Result saved as: " + outputPath);
    }

    public static void main(String[] args) {
        // Example usage
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "Footage/frelon16.jpg";
        Mat result = cutout.cutout(imagePath);

        if (result != null) {
            displayImage(result, "Result of Cutout");
        }
    }
}
