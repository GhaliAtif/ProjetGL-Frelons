package HornetLenght;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ResultPlot {

    public static int resultPlot(Mat picture, int lowerLine, int upperLine, int leftLine, int indexMax, int pixelCount,
                                 int numberOfLines, int numberOfColumns, String picturefile) {

        // Drawing search lines
        Mat colorPicture = new Mat();
        Imgproc.cvtColor(picture, colorPicture, Imgproc.COLOR_GRAY2BGR);

        // Lower line
        Imgproc.line(colorPicture, new org.opencv.core.Point(0, lowerLine),
                new org.opencv.core.Point(numberOfColumns, lowerLine), new Scalar(0, 0, 255), 2);

        // Upper line
        Imgproc.line(colorPicture, new org.opencv.core.Point(0, upperLine),
                new org.opencv.core.Point(numberOfColumns, upperLine), new Scalar(0, 0, 255), 2);

        // Left line
        Imgproc.line(colorPicture, new org.opencv.core.Point(leftLine, 0),
                new org.opencv.core.Point(leftLine, numberOfLines), new Scalar(0, 0, 255), 2);

        // Right line
        int positionning = (int) (leftLine + pixelCount);
        Imgproc.line(colorPicture, new org.opencv.core.Point(positionning, 0),
                new org.opencv.core.Point(positionning, numberOfLines), new Scalar(0, 0, 255), 2);

        // Maximum length line
        Imgproc.line(colorPicture, new org.opencv.core.Point(leftLine, indexMax + upperLine),
                new org.opencv.core.Point(positionning, indexMax + upperLine), new Scalar(0, 255, 0), 2);

        // Endpoint of the maximum length line
        Imgproc.circle(colorPicture, new org.opencv.core.Point(positionning, indexMax + upperLine), 10,
                new Scalar(255, 75, 0), -1);

        Imgcodecs.imwrite("HornetLength.jpg", colorPicture);
        return 0;
    }
}

