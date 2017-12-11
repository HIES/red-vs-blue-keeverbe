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
        System.out.println("delim is: " + s.delimiter());

        String[] mins = s.nextLine().split("   ");
        String[] maxs = s.nextLine().split("   ");
        int regions = s.nextInt();
        s.nextLine();

        double [] mapLims = new double[4];
        mapLims[0] = Double.parseDouble(mins[0]);
        mapLims[1] = Double.parseDouble(mins[1]);
        mapLims[2] = Double.parseDouble(maxs[0]);
        mapLims[3] = Double.parseDouble(maxs[1]);   
        StdDraw.setCanvasSize((int) (512*(((mapLims[2]-mapLims[0])/(mapLims[3]-mapLims[1])))), 512);
        StdDraw.setXscale(mapLims[0],mapLims[2]);
        StdDraw.setYscale(mapLims[1],mapLims[3]);

        StdDraw.enableDoubleBuffering();
        String[] coords = new String[2];
        int z = 0;
        while(z < regions) 
        {
            s.nextLine();//Advance past blank
            s.nextLine();
            s.nextLine();
            int subregions = s.nextInt();
            s.nextLine();
            int c = 0;
            double[] x = new double[subregions];
            double[] y = new double[subregions];
            while (c < subregions)
            {
                coords = s.nextLine().split("   ");
                x[c] = Double.parseDouble(coords[0]);
                y[c] = Double.parseDouble(coords[1]);
                c++;
            }
            StdDraw.polygon(x , y);
            z++;
        }
        System.out.println(z);
        StdDraw.show();
        s.close();
    }
}
