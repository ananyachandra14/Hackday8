package com.example.ananyachandra.accelerometertest;

/**
 * Created by ananya.chandra on 23/06/17.
 */

public class XYZ {
    float x;
    float y;
    float z;

    public XYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"x\":").append(x);
        sb.append(", \"y\":").append(y);
        sb.append(", \"z\":").append(z);
        sb.append('}');
        return sb.toString();
    }
}
