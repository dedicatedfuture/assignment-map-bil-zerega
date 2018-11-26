package com.billzerega.android.assignment_maps_bil_zerega;

    public class MapLocation {
        private double lattitude;
        private String location;
        private double longitude;

        public MapLocation(double lattitude, String location, double longitude) {
            this.lattitude = lattitude;
            this.location = location;
            this.longitude = longitude;
        }

        public double getLattitude() {
            return lattitude;
        }

        public void setLattitude(double lattitude) {
            this.lattitude = lattitude;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
