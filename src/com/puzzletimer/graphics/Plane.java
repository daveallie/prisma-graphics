package com.puzzletimer.graphics;

public class Plane {
    public Vector3 p;
    public Vector3 n;

    public Plane(Vector3 p, Vector3 n) {
        this.p = p;
        this.n = n;
    }

    public Plane(Vector3 v1, Vector3 v2, Vector3 v3) {
        this.p = v1.add(v2).add(v3).mul(0.3333333333333333D);
        this.n = v2.sub(v1).cross(v3.sub(v1)).unit();
    }
}

/* Location:              prisma-graphics.jar!/com/puzzletimer/graphics/Plane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
