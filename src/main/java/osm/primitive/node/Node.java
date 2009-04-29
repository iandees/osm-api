package osm.primitive.node;

import osm.primitive.Primitive;
import osm.primitive.PrimitiveTypeEnum;

/**
 * @author Ian Dees
 * 
 */
public class Node extends Primitive {

    private double lat;
    private double lon;

    public Node(double lat, double lng) {
        this.lat = lat;
        this.lon = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public PrimitiveTypeEnum getType() {
        return PrimitiveTypeEnum.node;
    }
    
}
