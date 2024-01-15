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
    public static void main(String[] args) throws Exception {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filePath="Footage/16_cutout.jpg";
        //Classification du frelon
        Map<String, String> results = HornetClassification.classifyHornet(filePath);

        System.out.println("Résultats de la classification :");
        if(results!=null){
            for (Map.Entry<String, String> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        else System.out.println("Image pas bien defini donc on peut pas faire le traitment.");

        //Affichage visuel (bounding lines) de la zone de traitment et longeur du frelon
        HornetLength.resultPlot(filePath);
        HighGui.waitKey();
    }
}
