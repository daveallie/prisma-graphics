package com.puzzletimer.graphics;

public class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 neg() {
        return new Vector3(
                -this.x,
                -this.y,
                -this.z);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(
                this.x + v.x,
                this.y + v.y,
                this.z + v.z);
    }

    public Vector3 sub(Vector3 v) {
        return new Vector3(
                this.x - v.x,
                this.y - v.y,
                this.z - v.z);
    }

    public Vector3 mul(double s) {
        return new Vector3(
                s * this.x,
                s * this.y,
                s * this.z);
    }

    public double dot(Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(
                this.y * v.z - this.z * v.y,
                this.z * v.x - this.x * v.z,
                this.x * v.y - this.y * v.x);
    }

    public double norm() {
        return Math.sqrt(dot(this));
    }

    public Vector3 unit() {
        return mul(1.0D / norm());
    }
}

/* Location:              prisma-graphics.jar!/com/puzzletimer/graphics/Vector3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
