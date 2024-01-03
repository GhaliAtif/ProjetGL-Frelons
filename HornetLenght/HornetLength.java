package HornetLenght;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static HornetLenght.BoundingLines.boundingLines;
import static HornetLenght.BoundingLines.zeroPixels;

public class HornetLength {

    public static int[] hornetLength(Mat picture, String pictureFile) {
        Mat arrayImage = new Mat();
        picture.copyTo(arrayImage);

        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        int[] lines = boundingLines(arrayImage);

        int upperLine = lines[0];
        int lowerLine = lines[1];
        int leftLine = lines[2];

        Mat extractedArray = new Mat(arrayImage, new org.opencv.core.Rect(leftLine, upperLine,
                numberOfColumns - leftLine, lowerLine - upperLine));

        List<Integer> pixelCountList = new ArrayList<>();
        for (int i = 0; i < extractedArray.rows(); i++) {
            Mat line = extractedArray.row(i);
            pixelCountList.add(zeroPixels(line));
        }

        int pixelCount = pixelCountList.stream().max(Integer::compare).orElse(0);
        int indexMax = pixelCountList.indexOf(pixelCount);

        int lengthValue = pixelCount;
        int[] stingCoordinates = new int[]{leftLine + (int) pixelCount, indexMax + upperLine};

        return new int[]{lengthValue, stingCoordinates[0], stingCoordinates[1]};
    }


}

