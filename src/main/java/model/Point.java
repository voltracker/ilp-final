package model;

public record Point(String name, double lng, double lat){

    public double distanceTo(Point other){
        return Math.sqrt(Math.pow(this.lng - other.lng, 2.0) + Math.pow(this.lat - other.lat, 2.0));
    }

    public Point makeMove(CompassDirection direction){
        double radians = Math.toRadians(direction.bearing);
        double newLat = this.lat + (0.00015 * Math.sin(radians));
        double newLng = this.lng + (0.00015 * Math.cos(radians));
        return new Point(this.name, newLng, newLat);
    }

    public boolean closeTo(Point p){
        return this.distanceTo(p) < 0.00015;
    }
    @Override
    public String toString(){
        return "(" + this.lng + "," + this.lat + ")";
    }
    public boolean equals(Point p){
        return (this.lng() == p.lng() && this.lat() == p.lat());
    }
}
