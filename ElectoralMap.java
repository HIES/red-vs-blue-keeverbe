import java.io.File;
import java.util.Scanner;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;

public class ElectoralMap
{
    HashMap<String, HashMap<String, ArrayList<Subregion>>> electoralMap = new HashMap<>();
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

    public void setScale(String region) throws Exception
    {
        File f = new File("./input/" + region +".txt");
        Scanner s = new Scanner(f);

        String[] mins = s.nextLine().split("   ");
        String[] maxs = s.nextLine().split("   ");
        int count = s.nextInt();
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
    }

    public void geoData(String region) throws Exception
    {
        setScale(region);
        File f = new File("./input/" + region +".txt");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.nextLine();
        int countTo = s.nextInt();
        s.nextLine();
        s.nextLine();

        String[] coords = new String[2];
        int count = 0;

        while(count < countTo) 
        {
            String countyName = s.nextLine();
            if (countyName.indexOf(" Parish") > -1)
            {
                countyName = countyName.substring(0, countyName.indexOf(" Parish"));
                System.out.println(countyName);
            }
            else if (countyName.indexOf(" city") > -1)
            {
                countyName = countyName.substring(0, countyName.indexOf(" city"));
                System.out.println(countyName);
            }
            countyName = countyName.toLowerCase();    
            String stateName = s.nextLine();

            if (electoralMap.containsKey(stateName) == false)
            {
                HashMap<String, ArrayList<Subregion>> innerMap = new HashMap<>();
                ArrayList<Subregion> counties = new ArrayList<>();
                innerMap.put(stateName, counties);
                electoralMap.put(stateName, innerMap);
            }

            Subregion county = new Subregion(countyName);

            int numPoints = s.nextInt();
            s.nextLine();
            double[] x = new double[numPoints];
            double[] y = new double[numPoints];

            int pointCounter = 0;
            while (pointCounter < numPoints)
            {
                coords = s.nextLine().split("   ");
                x[pointCounter] = Double.parseDouble(coords[0]);
                y[pointCounter] = Double.parseDouble(coords[1]);
                pointCounter++;
            }
            StdDraw.polygon(x , y);
            s.nextLine();

            county.xCoordinates = x;
            county.yCoordinates = y;

            electoralMap.get(stateName).get(stateName).add(county);

            count++;
        }
        s.close();
        StdDraw.show();
    }

    public void votingData(String region, int year) throws Exception
    {
        geoData(region);
        Object[] keys = electoralMap.keySet().toArray();
        String[] stringKeys = new String[keys.length];
        for (int i = 0; i < keys.length; i++)
        {
            stringKeys[i] = keys[i].toString();
        }
        for (int i = 0; i < stringKeys.length; i++)
        {
            File f = new File("./input/" + stringKeys[i] + String.valueOf(year) +".txt");
            Scanner s = new Scanner(f);
            s.nextLine();

            String[] voteHolder = new String[4];
            int[] votes = new int[3];
            int count = 0;
            while (s.hasNextLine())
            {
                voteHolder = s.nextLine().split(",");
                String countyname = voteHolder[0].toLowerCase();

                votes[0] = Integer.parseInt(voteHolder[1]);
                votes[1] = Integer.parseInt(voteHolder[2]);
                votes[2] = Integer.parseInt(voteHolder[3]);
                for (int j = 0; j < electoralMap.get(stringKeys[i]).get(stringKeys[i]).size(); j++)
                {
                    if (electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).name.equals(countyname))
                    {
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).rVotes = votes[0];
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).dVotes = votes[1];
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).oVotes = votes[2];
                    }
                    count++;
                }

            }
            s.close();
        }
    }

    public void visualize(String region, int year) throws Exception
    { 
        votingData(region, year);
        Object[] keys = electoralMap.keySet().toArray();
        String[] stringKeys = new String[keys.length];
        for (int i = 0; i < keys.length; i++)
        {
            stringKeys[i] = keys[i].toString();
        }

        for(int i = 0; i < stringKeys.length; i++)
        {
            for(int j = 0; j < electoralMap.get(stringKeys[i]).get(stringKeys[i]).size(); j++)
            {
                if (electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).rVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).dVotes && 
                electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).rVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).oVotes)
                {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.filledPolygon(electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).xCoordinates, 
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).yCoordinates);
                }
                else  if (electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).dVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).rVotes && 
                electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).dVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).oVotes)
                {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.filledPolygon(electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).xCoordinates, 
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).yCoordinates);
                }
                else  if (electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).oVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).dVotes && 
                electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).oVotes > electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).rVotes)
                {
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledPolygon(electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).xCoordinates, 
                        electoralMap.get(stringKeys[i]).get(stringKeys[i]).get(j).yCoordinates);
                }
            }
        }

        StdDraw.show();
    }
}

