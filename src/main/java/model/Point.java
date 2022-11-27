package model;

public record Point(String name, double lng, double lat) {

    public double distanceTo(Point other){
        return Math.sqrt(Math.pow(this.lng - other.lng, 2.0) + Math.pow(this.lat - other.lat, 2.0));
    }

    @Override
    public String toString(){
        return "(" + this.lng + "," + this.lat + ")";
    }
    public boolean equals(Point p){
        return (this.lng() == p.lng() && this.lat() == p.lat());
    }
}
