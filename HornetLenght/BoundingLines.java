package HornetLenght;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class BoundingLines {

    public static int zeroPixels(Mat line) {
        return line.cols() - Core.countNonZero(line);
    }

    public static int[] boundingLines(Mat arrayImage) {
        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        System.out.println("Number of lines: " + numberOfLines);

        int counter = numberOfLines;
        int pixelCount = 0;

        double horizontalNumber = arrayImage.cols() * 0.4;
        while (pixelCount < horizontalNumber && counter > 0) {
            pixelCount = zeroPixels(arrayImage.row(counter - 1));
            counter--;
        }

        int lowerLine = counter;

        counter = 0;
        pixelCount = 0;

        while (pixelCount < horizontalNumber && counter < numberOfLines) {
            pixelCount = zeroPixels(arrayImage.row(counter));
            counter++;
        }

        int upperLine = counter;

        counter = 0;
        pixelCount = 0;

        double verticalNumber = arrayImage.rows() * 0.1;
        while (pixelCount < verticalNumber && counter < numberOfColumns) {
            pixelCount = zeroPixels(arrayImage.col(counter));
            counter++;
        }

        int leftLine = counter;

        System.out.println("Upper line: " + upperLine);
        System.out.println("Lower line: " + lowerLine);
        System.out.println("Left line: " + leftLine);

        return new int[]{upperLine, lowerLine, leftLine};
    }
}

