package AbdomenShape;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;


public class AbdomenShapeTest {

    public static void main(String[] args) throws Exception {
        // Loading OpenCV library......
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        String filepath="Footage/triangle.png";
        Mat pictureArray = Imgcodecs.imread(filepath);
        if (pictureArray.width() < 1 || pictureArray.height() < 1) {
            throw new CvException("OpenCV cannot read the image ");
        }
        // Exemple
        Point stingCoordinates = new Point(998, 455);

        // Test  abdomenShape methode
        String result= AbdomenShape.abdomenShape(pictureArray, stingCoordinates);

        System.out.println("RESULT:   "+result);
    }
}

