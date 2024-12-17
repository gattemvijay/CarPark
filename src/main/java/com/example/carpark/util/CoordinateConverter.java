package com.example.carpark.util;

public class CoordinateConverter {

    private static final double RAD_RATIO = Math.PI / 180.0; // Convert degrees to radians
    private static final double WGS84_EQUATORIAL_RADIUS = 6378137.0; // Semi-major axis
    private static final double WGS84_FLATTENING = 1 / 298.257223563; // Flattening
    private static final double SVY21_FALSE_NORTHING = 38744.572;
    private static final double SVY21_FALSE_EASTING = 28001.642;
    private static final double CENTRAL_MERIDIAN = 103.83333333333333; // Longitude of origin
    private static final double SCALE_FACTOR = 1.0;

    // Origin latitude of SVY21 projection (1° 22' N in decimal degrees)
    private static final double LATITUDE_OF_ORIGIN = 1.366666;

    public static double[] svy21ToWGS84(double northing, double easting) {
        double a = WGS84_EQUATORIAL_RADIUS;
        double f = WGS84_FLATTENING;
        double b = a * (1 - f); // Semi-minor axis
        double e2 = (a * a - b * b) / (a * a); // Square of eccentricity

        double nPrime = northing - SVY21_FALSE_NORTHING;
        double ePrime = easting - SVY21_FALSE_EASTING;

        double sinLatOrigin = Math.sin(LATITUDE_OF_ORIGIN * RAD_RATIO);
        double tOrigin = Math.tan(LATITUDE_OF_ORIGIN * RAD_RATIO);
        double vOrigin = a / Math.sqrt(1 - e2 * sinLatOrigin * sinLatOrigin);

        double M0 = getM(LATITUDE_OF_ORIGIN * RAD_RATIO, e2, a, b);
        double M = M0 + nPrime / SCALE_FACTOR;

        double latitudeRad = getLatitude(M, e2, a, b);
        double sinLat = Math.sin(latitudeRad);
        double v = a / Math.sqrt(1 - e2 * sinLat * sinLat);
        double rho = (v * (1 - e2)) / (1 - e2 * sinLat * sinLat);

        double psi = v / rho;
        double t = Math.tan(latitudeRad);

        double secLat = 1.0 / Math.cos(latitudeRad);

        double term1 = ePrime / (v * SCALE_FACTOR);
        double term2 = (ePrime * ePrime) / (2 * rho * v * SCALE_FACTOR * SCALE_FACTOR);
        double term3 = (ePrime * ePrime * ePrime) / (6 * Math.pow(v, 3) * SCALE_FACTOR * SCALE_FACTOR * SCALE_FACTOR);

        double latitude = latitudeRad / RAD_RATIO;

        double longitude = CENTRAL_MERIDIAN
                + (term1 - term2 * (1 + 2 * t * t + psi)) * secLat / RAD_RATIO;

        return new double[] { latitude, longitude };
    }

    private static double getLatitude(double M, double e2, double a, double b) {
        double phi = M / a;
        double deltaPhi;
        do {
            double sinPhi = Math.sin(phi);
            double rho = a * (1 - e2) / Math.pow((1 - e2 * sinPhi * sinPhi), 1.5);
            double nu = a / Math.sqrt(1 - e2 * sinPhi * sinPhi);
            double mPrime = getM(phi, e2, a, b);
            deltaPhi = (M - mPrime) / (rho + nu);
            phi += deltaPhi;
        } while (Math.abs(deltaPhi) > 1e-11); // Precision check
        return phi;
    }

    private static double getM(double latitude, double e2, double a, double b) {
        double n = (a - b) / (a + b);
        double n2 = n * n;
        double n3 = n2 * n;

        double A0 = a * (1 - n + 5 * (n2 - n3) / 4 + 81 * (n2 * n2 - 4 * n3 * n2) / 64);
        double B0 = 3 * a * n / 2 * (1 - n + 7 * (n2 - n3) / 8 + 55 * n2 * n2 / 64);
        double C0 = 15 * a * n2 / 16 * (1 - n + 3 * (n2 - n3) / 4);
        double D0 = 35 * a * n3 / 48;

        return A0 * latitude - B0 * Math.sin(2 * latitude) + C0 * Math.sin(4 * latitude) - D0 * Math.sin(6 * latitude);
    }
}
