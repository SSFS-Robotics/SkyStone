package beestbot.vision;

import android.graphics.Bitmap;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import beestbot.io.FileManager;
import beestbot.vision.Objects.Detected;
import beestbot.vision.Objects.Foundation;
import beestbot.vision.Objects.Stone;

// to use OpenCV, external storage: https://blog.csdn.net/nugongahou110/article/details/48154859
// OpenCVRepackaged: https://github.com/OpenFTC/OpenCV-Repackaged

public class SkyStoneOpenCVPipeline {

    private static final int regionSideClipExtensionLength = 120; //120

    public static Mat resizedImage = new Mat();

    public static List<Foundation> foundations = new ArrayList<Foundation>();
    public static List<Stone> stones = new ArrayList<Stone>();

    //debug steps
    public static Mat red      = new Mat();
    public static Mat blue     = new Mat();
    public static Mat yellow   = new Mat();
    public static Mat black    = new Mat();
    public static int blackcut =0;


    private void pipeline(VuforiaLocalizer vuforia) {

        Bitmap image = getImage(vuforia);
        if (image != null) {
            Mat matImg = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(image, matImg);
            process(matImg);
        }
    }

    private Bitmap getImage(VuforiaLocalizer vuforia) {
        try {
            VuforiaLocalizer.CloseableFrame closeableFrame = vuforia.getFrameQueue().take();
            for (int i = 0; i < closeableFrame.getNumImages(); i++) {
                Image image = closeableFrame.getImage(i);

                if (image.getFormat() == PIXEL_FORMAT.RGB565) {
                    Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
                    bitmap.copyPixelsFromBuffer(image.getPixels());
                    return bitmap;
                }
            }
            closeableFrame.close();
        } catch (InterruptedException e) {
        }
        return null;
    }

    private static Mat process(Mat source0) {
        System.gc();
        System.runFinalization();

        resizedImage = new Mat();
        Imgproc.resize(source0, resizedImage, new Size(640*1, 480*1), 0.0, 0.0, Imgproc.INTER_LINEAR);

        //white balance
        /*
        compute.forEach(resizedImage,
        	(double[] d) -> {
        		//b,g,r
        		d[0]*=1.15;
        		d[2]*=1;

        		return d;
        	});
         */

        Mat original = resizedImage.clone();

        //set ranges
        double blackCutOff = Compute.getHistogramfast(resizedImage);
        blackcut = (int) blackCutOff;

        //For yellow
        double[] yellowRange = {80, 105};

        //For Blue
        double[] blueRange1 = {170, 180};
        double[] blueRange2 = {0, 10};

        //for Red
        double[] redRange = {110, 120};

        double[] satRange = {60, 255};
        double[] valRange = {blackCutOff * 0.7, 255};

        Mat redOutput = Compute.threshold(resizedImage, redRange, satRange, valRange);

        Mat blueOutput = Compute.combine(
                Compute.threshold(resizedImage, blueRange1, satRange, valRange),
                Compute.threshold(resizedImage, blueRange2, satRange, valRange));

        Mat blackOutput = Compute.threshold(resizedImage,
                new double[]{0, 180},//hue  0, 180
                new double[]{0, 180},//sat  0, 180
                new double[]{0, blackCutOff});//val

        //yellow stones face sideways, so there is less glare
        //thus the saturation minumun can be higher
        Mat yellowOutput = Compute.threshold(
                resizedImage,
                yellowRange,
                new double[]{170, 255}, valRange);

        //For debug display
        red = redOutput.clone();
        blue = blueOutput.clone();
        yellow = yellowOutput.clone();
        black = blackOutput.clone();

        //stones = computeStones(yellowOutput, original);
        foundations = computeFoundations(redOutput, blueOutput, yellowOutput, blackOutput, original);

        for (Stone s : stones) {
            s.draw(original);
        }

        for (Foundation f : foundations) {
            f.draw(original);
        }

        redOutput.release();
        blueOutput.release();
        yellowOutput.release();
        blackOutput.release();

        return original;
    }

    private static List<Stone> computeStones(Mat yellowOutput, Mat canvas){
        Mat dTrans = Compute.distanceTransform(yellowOutput,12);
        List<MatOfPoint> stonesContour = Compute.findHulls(dTrans);

        List<Stone> stones = new ArrayList<Stone>();

        for(MatOfPoint con : stonesContour) {
            Stone d = new Stone(con);
            if(!d.isBastard) {
                stones.add(d);
            }
        }

        return stones;
    }

    /*
     * Takes in red, blue, yellow, black masks, and image to annotate on
     * spits out list of Foundations
     */
    private static List<Foundation> computeFoundations(Mat redOutput, Mat blueOutput, Mat yellowOutput, Mat blackOutput, Mat canvas){
        //Find Contours
        List<MatOfPoint> hullsRed = Compute.findHulls(redOutput);
        List<MatOfPoint> hullsBlue = Compute.findHulls(blueOutput);
        List<MatOfPoint> hullsYellow = Compute.findHulls(yellowOutput);

        //populate array of detected (color only)
        List<Detected> detected = new ArrayList<Detected>();
        //we will segregate the blacks
        List<Detected> blacks = new ArrayList<Detected>();

        for (MatOfPoint p : hullsRed) {
            Detected toadd = new Detected(p, Detected.Color.RED);
            if (!toadd.isBastard) {
                detected.add(toadd);
            }
        }
        for (MatOfPoint p : hullsBlue) {
            Detected toadd = new Detected(p, Detected.Color.BLUE);
            if (!toadd.isBastard) {
                detected.add(toadd);
            }
        }
        for (MatOfPoint p : hullsYellow) {
            Detected toadd = new Detected(p, Detected.Color.YELLOW);
            if (!toadd.isBastard) {
                detected.add(toadd);
            }
        }

        //cut sides of color contours. Field walls are bad.
        for(Detected d:detected) {
            Point one = new Point(d.bounds.x,d.bounds.y+d.bounds.height*0.5);
            Point two = new Point(d.bounds.x,d.bounds.y+d.bounds.height*0.5+regionSideClipExtensionLength);
            Imgproc.line(blackOutput,one, two,new Scalar(new double[] {0,0,0}),1);

            one = new Point(d.bounds.x+d.bounds.width,d.bounds.y+d.bounds.height*0.5);
            two = new Point(d.bounds.x+d.bounds.width,d.bounds.y+d.bounds.height*0.5+regionSideClipExtensionLength);
            Imgproc.line(blackOutput,one, two,new Scalar(new double[] {0,0,0}),1);
        }

        ArrayList<MatOfPoint> hullsBlack = Compute.findHulls(blackOutput);

        for (MatOfPoint p : hullsBlack) {
            Detected toadd = new Detected(p, Detected.Color.BLACK);
            if (!toadd.isBastard) {
                blacks.add(toadd);
            }
        }

        for (Detected d : detected) {
            d.draw(canvas);
        }for (Detected d : blacks) {
            d.draw(canvas);
        }

        //process sandwiches, populate foundation ArrayList
        List<Foundation> foundations = new ArrayList<Foundation>();

        Imgproc.putText(canvas, String.valueOf(blackcut), new Point(20,20), 0, 0.6, new Scalar(0,0,0), 7);
        Imgproc.putText(canvas, String.valueOf(blackcut), new Point(20,20), 0, 0.6, new Scalar(255,255,0), 2);

        for (Detected d : blacks) {
            for (Detected j : detected) {
                if (Math.abs(d.x - j.x) < 120 &&
                        d.bounds.y > j.bounds.y && d.bounds.y < j.bounds.y+j.bounds.height+30 &&
                        Math.abs(d.bounds.width*1.0/j.bounds.width-1)  <  0.6)   {
                    foundations.add(Foundation.createFoundation(d, j));
                }
            }
        }

        return foundations;
    }
}
