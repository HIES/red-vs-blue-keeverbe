import java.io.File;
import java.util.Scanner;
import java.lang.Exception;
import java.util.ArrayList;

public class EmptyMap
{
    public static void visualize(String region) throws Exception
    {
        File f = new File("./input/" + region +".txt");
        Scanner s = new Scanner(f);
        s.useDelimiter("   ");
        System.out.println("delim is: " + s.delimiter());
        String[] limits = new String[4];
        //s.nextLine().split(" ");
        limits[0] = s.next();
        System.out.println(limits[0]);
        limits[1] = s.next();
        System.out.println(limits[1]);
        //s.nextLine().split(" ");
        limits[2] = s.next();
        System.out.println(limits[2]);
        limits[3] = s.next();
        System.out.println(limits[3]);
        double [] mapLimits = new double[4];
        String[] coordinates = new String[2];
        double [] xy = new double[2];

        for (int i = 0; i < 4; i++)
        {
            mapLimits[i] = Double.parseDouble(limits[i]);
            System.out.println(mapLimits[i]);
        }

        StdDraw.setCanvasSize((int) (512*(((mapLimits[2]-mapLimits[0])/(mapLimits[3]-mapLimits[1])))), 512);
        StdDraw.setXscale(mapLimits[0],mapLimits[2]);
        StdDraw.setYscale(mapLimits[1],mapLimits[3]);

        StdDraw.enableDoubleBuffering();

        while(s.hasNextLine())
        {
            if (s.hasNextDouble())
            {
            coordinates = s.nextLine().split("   ");
            xy[0] = Double.parseDouble(coordinates[0]);
            xy[1] = Double.parseDouble(coordinates[1]);
            StdDraw.point(xy[0],xy[1]);
           } 
        }

        StdDraw.show();
        s.close();
    }
}
