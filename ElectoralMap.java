import java.io.File;
import java.util.Scanner;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
public class ElectoralMap
{
    private class Subregion
    {
        private String name;
        private int rVotes;
        private int dVotes;
        private int oVotes;
        private double[] xCoordinates;
        private double[] yCoordinates;
        private String color;
        public Subregion(String n)
        {
            this.name = n;
            rVotes = 0; 
            dVotes = 0;
            oVotes = 0;
            xCoordinates = null;
            yCoordinates = null;
            color = "";
        }
    }
    //Had to make visualize not static, as I was referencing a non-static and it wouldn't compile
    public void visualize(String region, int year) throws Exception
    { 
        //Make a map (region = key, arraylist of subregions = values)
        HashMap<String, ArrayList<Subregion>> electoralMap = new HashMap<>();
        //Make arraylistof subregions
        ArrayList<Subregion> subregions = new ArrayList<>();
        //Put the region and subregion arrayList into map
        electoralMap.put(region, subregions);
        
        //Open geographic data
        File f = new File("./input/" + region +".txt");
        Scanner s = new Scanner(f);

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
            //Save Subregion name
            String n = s.next();
            //Make new subregion a to be added into the arraylist
            Subregion a = new Subregion(n);
            s.nextLine();//Advance past subregion
            s.nextLine();//Advance past region
            int points = s.nextInt();
            s.nextLine();//Advance past no. data points
            int c = 0;
            double[] x = new double[points];
            double[] y = new double[points];
            while (c < points)
            {
                coords = s.nextLine().split("   ");
                x[c] = Double.parseDouble(coords[0]);
                y[c] = Double.parseDouble(coords[1]);
                c++;
            }
            //Assign xCoordinates and yCoordinates to a fields
            a.xCoordinates = x;
            a.yCoordinates = y;
            //Close geographic file
            //s.close();
            
            //Open electoral data file using subregion name and make scanner
            File f2 = new File("./input/" + region + String.valueOf(year) +".txt");
            Scanner s2 = new Scanner(f2);
            s2.nextLine();
            String[] stuff = new String[4];
            int rSum = 0;
            int dSum = 0;
            int oSum = 0;
            while (s2.hasNextLine())
            {
                stuff = s2.nextLine().split(",");
                rSum+= Integer.parseInt(stuff[1]);
                dSum+= Integer.parseInt(stuff[2]);
                oSum+= Integer.parseInt(stuff[3]);
            }
            //Assign vote counts for subregion
            a.rVotes = rSum;
            a.dVotes = dSum;
            a.oVotes = oSum;
            //Assign color for subregion
            if (rSum > dSum && rSum > oSum)
            {
                a.color = "RED";
                StdDraw.setPenColor(StdDraw.RED);
            }
            else if (dSum > rSum && dSum > oSum)
            {
                a.color = "BLUE";
                StdDraw.setPenColor(StdDraw.BLUE);
            }
            else
            {
                a.color = "GREEN";
                StdDraw.setPenColor(StdDraw.GREEN);
            }
            System.out.println(a.color);
            s2.close();
            subregions.add(a);
  
            StdDraw.filledPolygon(x , y);
            z++;
        }
        electoralMap.put(region, subregions);
        s.close();
        StdDraw.show();
    }
}