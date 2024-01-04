package HornetLenght;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static HornetLenght.BoundingLines.zeroPixels;

public class HornetLength {

    public static int[] hornetLength(Mat picture, String pictureFile, int upperLine, int lowerLine, int leftLine) {
        Mat arrayImage = new Mat();
        picture.copyTo(arrayImage);

        int numberOfLines = arrayImage.rows();
        int numberOfColumns = arrayImage.cols();

        List<Integer> pixelCountList = new ArrayList<>();
        for (int i = 0; i < arrayImage.rows(); i++) {
            Mat line = arrayImage.row(i);
            pixelCountList.add(BoundingLines.zeroPixels(line));
        }

        int pixelCount = pixelCountList.stream().max(Integer::compare).orElse(0);
        int indexMax = pixelCountList.indexOf(pixelCount);

        int lengthValue = pixelCount;
        int[] stingCoordinates = new int[]{leftLine + (int) pixelCount, indexMax + upperLine};

        return new int[]{lengthValue, stingCoordinates[0], stingCoordinates[1]};
    }


}

