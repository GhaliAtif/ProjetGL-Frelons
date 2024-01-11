package Results;

import Caracteristics.AbdomenShape;
import Caracteristics.HornetClassification;
import Caracteristics.HornetLength;
import CutOut.CutOut;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Cette Classe est utilisée pour afficher sur la console le differentes caracteristiques du frelon et permettre un test visuel en meme temps.
 */
public class ResultantlyCharacteristics {
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
        String outputPath = "Footage/"+title+"_cutout.jpg";
        Imgcodecs.imwrite(outputPath, image);
        System.out.println("Result saved as: " + outputPath);
    }
    public static void main(String[] args) throws Exception {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filePath="Footage/33_cutout.jpg";
        //Classification du frelon
        Map<String, String> results = HornetClassification.classifyHornet(filePath);

        System.out.println("Résultats de la classification :");
        if(results!=null){
            for (Map.Entry<String, String> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        else System.out.println("No results CutOut is not well defined");

        //Affichage visuel (bounding lines) de la zone de traitment et longeur du frelon
        HornetLength.resultPlot(filePath);
        HighGui.waitKey();
    }
}
