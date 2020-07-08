package tspdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TspDemo {

    private static class Location implements HeldKarpSolver.Locatable<Location> {
        Location(String niceName, double latitude, double longitude) {
            this.niceName = niceName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        final String niceName;
        final double latitude;
        final double longitude;

        @Override
        public double distanceTo(Location target) {
            // Distance between geo coordinates using the haversine formula:
            double averageEarthRadiusInKm = 6371;
            double thetaSelf = latitude * Math.PI / 180;
            double thetaTarget = target.latitude * Math.PI / 180;
            double deltaTheta = (target.latitude - latitude) * Math.PI / 180;
            double deltaLambda = (target.longitude - longitude) * Math.PI / 180;
            double a = Math.sin(deltaTheta / 2) * Math.sin(deltaTheta / 2) +
                       Math.cos(thetaSelf) * Math.cos(thetaTarget) *
                       Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return averageEarthRadiusInKm * c;
        }
    }

    private static Location parseLine(String line) {
        // If the input format is incorrect, this will result in an uncaught
        // exception, so avoid feeding garbage into the program.
        Pattern p = Pattern.compile("^([0-9]+),([^,]+),.*,([0-9\\\\.]+),([0-9\\.]+)$");
        Matcher m = p.matcher(line);
        return new Location(m.group(2), Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)));
    }

    public static void main(String[] args) {
        // Parse input from the standard input stream.
        List<Location> locations;
        try (Reader in = new InputStreamReader(System.in, StandardCharsets.UTF_8)) {
            locations = new BufferedReader(in).lines()
                                              .filter(l -> l.length() != 0)
                                              .skip(1)
                                              .map(TspDemo::parseLine)
                                              .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // Solve the problem.
        HeldKarpSolver.Result<Location> result = HeldKarpSolver.solve(locations, 0);

        // Print the results.
        Location lastLocation = locations.get(0);
        System.out.println("                 " + lastLocation.niceName);
        for (Location nextLocation : result) {
            System.out.println(
                    String.format(" -> %7.2fkm -> %s",
                                  lastLocation.distanceTo(nextLocation),
                                  nextLocation.niceName));
            lastLocation = nextLocation;
        }
    }

}
