package model;

public record Point(double lng, double lat) {

    public double distanceTo(Point other){
        return Math.sqrt(Math.pow(this.lng - other.lng, 2.0) + Math.pow(this.lat - other.lat, 2.0));
    }

    @Override
    public String toString(){
        return "(" + this.lng + "," + this.lat + ")";
    }
}