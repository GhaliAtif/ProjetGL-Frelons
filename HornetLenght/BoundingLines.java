package HornetLenght;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class BoundingLines {

    public static int zeroPixels(Mat line) {
        return line.cols() - Core.countNonZero(line);
    }

    public static int[] bounding_lines(Mat arrayImage) {
        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        System.out.println("Number of lines: " + numberOfLines);

        int lowerLine = findLowerLine(arrayImage);
        int upperLine = findUpperLine(arrayImage);
        int leftLine = findLeftLine(arrayImage);

        System.out.println("Upper line: " + upperLine);
        System.out.println("Lower line: " + lowerLine);
        System.out.println("Left line: " + leftLine);

        // Ajout d'un décalage à lowerLine
        int OFFSET = 5;  // Ajustez cette valeur selon vos besoins
        lowerLine += OFFSET;

        return new int[]{upperLine, lowerLine, leftLine};
    }

    private static int findLowerLine(Mat arrayImage) {
        int numberOfLines = arrayImage.rows();
        int horizontalNumber = (int) (arrayImage.cols() * 0.4);

        int counter = numberOfLines - 1;
        int pixelCount = 0;

        while (pixelCount < horizontalNumber && counter > 0) {
            pixelCount = zeroPixels(arrayImage.row(counter));
            counter--;
        }

        return counter;
    }

    private static int findUpperLine(Mat arrayImage) {
        int numberOfLines = arrayImage.rows();
        int horizontalNumber = (int) (arrayImage.cols() * 0.4);

        int counter = 0;
        int pixelCount = 0;

        while (pixelCount < horizontalNumber && counter < numberOfLines) {
            pixelCount = zeroPixels(arrayImage.row(counter));
            counter++;
        }

        return counter;
    }

    private static int findLeftLine(Mat arrayImage) {
        int numberOfColumns = arrayImage.cols();
        int verticalNumber = (int) (arrayImage.rows() * 0.1);

        int counter = 0;
        int pixelCount = 0;

        while (pixelCount < verticalNumber && counter < numberOfColumns) {
            pixelCount = zeroPixels(arrayImage.col(counter));
            counter++;
        }

        return counter;
    }
}

