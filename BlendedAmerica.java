import java.io.File;
import java.util.Scanner;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;

public class BlendedAmerica
{
    private class Subregion
    {
        private String name;
        private int rVotes;
        private int dVotes;
        private int oVotes;
        private double[] xCoordinates;
        private double[] yCoordinates;

        public Subregion(String n)
        {
            this.name = n;
            rVotes = 0; 
            dVotes = 0;
            oVotes = 0;
            xCoordinates = null;
            yCoordinates = null;
        }
    }

    public void visualize(String region, int year) throws Exception
    { 
        HashMap<String, ArrayList<Subregion>> electoralMap = new HashMap<>();
        ArrayList<Subregion> subs = new ArrayList<>();
        electoralMap.put(region, subs);

        File f = new File("./input/" + region +".txt");
        Scanner s = new Scanner(f);

        String[] mins = s.nextLine().split("   ");
        String[] maxs = s.nextLine().split("   ");
        int subregion = s.nextInt();
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

        while(z < subregion) 
        {
            s.nextLine();

            String subname = s.nextLine();
            Subregion newSub = new Subregion(subname);

            s.nextLine();

            int points = s.nextInt();

            s.nextLine();

            double[] x = new double[points];
            double[] y = new double[points];

            int c = 0;
            while (c < points)
            {
                coords = s.nextLine().split("   ");
                x[c] = Double.parseDouble(coords[0]);
                y[c] = Double.parseDouble(coords[1]);
                c++;
            }
            StdDraw.polygon(x , y);

            newSub.xCoordinates = x;
            newSub.yCoordinates = y;

            electoralMap.get(region).add(newSub);

            z++;
        }

        s.close();
        StdDraw.show();

        File f2 = new File("./input/" + region + String.valueOf(year) +".txt");
        Scanner s2 = new Scanner(f2);
        s2.nextLine();

        String[] voteHolder = new String[4];
        int[] votes = new int[3];

        while (s2.hasNextLine())
        {
            voteHolder = s2.nextLine().split(",");
            votes[0] = Integer.parseInt(voteHolder[1]);
            votes[1] = Integer.parseInt(voteHolder[2]);
            votes[2] = Integer.parseInt(voteHolder[3]);
            String key = voteHolder[0];

            for(int i = 0; i < subregion; i++)
            {
                if (electoralMap.get(region).get(i).name.equals(key))
                {
                    electoralMap.get(region).get(i).rVotes = votes[0];
                    electoralMap.get(region).get(i).dVotes = votes[1];
                    electoralMap.get(region).get(i).oVotes = votes[2];
                }
            }
        }
        
        s2.close();

        for(int i = 0; i < subregion; i++)
        {
                double red = (double) electoralMap.get(region).get(i).rVotes;
                double blue = (double) electoralMap.get(region).get(i).dVotes;
                double green = (double) electoralMap.get(region).get(i).oVotes;
                
                double denominator = (double) (red + blue + green);
                
                double redDen = 255*(red/denominator);
                double blueDen = 255*(blue/denominator);
                double greenDen = 255*(green/denominator);
                
                int r = (int) redDen;
                int b = (int) blueDen;
                int g = (int) greenDen;
               
                StdDraw.setPenColor(r, g, b);
                
                StdDraw.filledPolygon(electoralMap.get(region).get(i).xCoordinates, 
                electoralMap.get(region).get(i).yCoordinates);
        }
        
        StdDraw.show();
    }
}
