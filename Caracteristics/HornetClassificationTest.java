package Caracteristics;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashMap;
import java.util.Map;

public class HornetClassificationTest {
    public static void main(String[] args) throws Exception {
        // Loading OpenCV library......
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String filepath="Footage/32_cutout.jpg";
        Mat pictureArray = Imgcodecs.imread(filepath);
        if (pictureArray.width() < 1 || pictureArray.height() < 1) {
            throw new CvException("OpenCV cannot read the image ");
        }
        Map<String, String> results = HornetClassification.classifyHornet(pictureArray);

        System.out.println("Résultats de la classification :");
        if(results!=null){
            for (Map.Entry<String, String> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        else System.out.println("No results CutOut is not well defined");

    }
}
