package com.puzzletimer.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Comparator;

public class Panel3D
        extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private Mesh mesh;
    private Vector3 lightDirection;
    private Vector3 viewerPosition;
    private Vector3 cameraPosition;
    private Vector3 cameraRotation;
    private int lastX;
    private int lastY;

    public Panel3D() {
        this.mesh = new Mesh(new Face[0]);
        this.lightDirection = new Vector3(0.0D, 0.25D, -1.0D).unit();
        this.viewerPosition = new Vector3(0.0D, 0.0D, -325.0D);
        this.cameraPosition = new Vector3(0.0D, 0.0D, -2.8D);
        this.cameraRotation = new Vector3(0.0D, 0.0D, 0.0D);

        this.lastX = 0;
        this.lastY = 0;

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
        repaint();
    }

    public void setLightDirection(Vector3 lightDirection) {
        this.lightDirection = lightDirection;
        repaint();
    }

    public void setViewerPosition(Vector3 viewerPosition) {
        this.viewerPosition = viewerPosition;
        repaint();
    }

    public void setCameraPosition(Vector3 cameraPosition) {
        this.cameraPosition = cameraPosition;
        repaint();
    }

    public void setCameraRotation(Vector3 cameraRotation) {
        this.cameraRotation = cameraRotation;
        repaint();
    }

    private Vector3 toCameraCoordinates(Vector3 v) {
        return Matrix44.rotationX(-this.cameraRotation.x).mul(
                Matrix44.rotationY(-this.cameraRotation.y).mul(
                        Matrix44.rotationZ(-this.cameraRotation.z).mul(
                                v.sub(this.cameraPosition))));
    }

    private Vector3 perspectiveProjection(Vector3 v) {
        return new Vector3(
                getWidth() / 2.0D + (-v.x - this.viewerPosition.x) * (this.viewerPosition.z / v.z),
                getHeight() / 2.0D + (v.y - this.viewerPosition.y) * (this.viewerPosition.z / v.z),
                0.0D);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        Face[] faces = (Face[]) Arrays.copyOf(this.mesh.faces, this.mesh.faces.length);
        Arrays.sort(faces, new Comparator<Face>() {
            public int compare(Face f1, Face f2) {
                return Double.compare(
                        f2.centroid().z,
                        f1.centroid().z);
            }


        });
        Face[] pFaces = new Face[faces.length];
        for (int i = 0; i < pFaces.length; i++) {
            Vector3[] vertices = new Vector3[faces[i].vertices.length];
            for (int j = 0; j < vertices.length; j++) {
                vertices[j] = perspectiveProjection(toCameraCoordinates(faces[i].vertices[j]));
            }

            pFaces[i] = faces[i].setVertices(vertices);
        }


        Color backfacingColor =
                new Color(
                        (4 * getBackground().getRed() + 32) / 5,
                        (4 * getBackground().getGreen() + 32) / 5,
                        (4 * getBackground().getBlue() + 32) / 5);
        Face[] arrayOfFace1;
        int i = (arrayOfFace1 = pFaces).length;
        for (int j = 0; j < i; j++) {
            Face pFace = arrayOfFace1[j];
            Polygon polygon = new Polygon();
            Vector3[] arrayOfVector31;
            int k = (arrayOfVector31 = pFace.vertices).length;
            for (int l = 0; l < k; l++) {
                Vector3 v = arrayOfVector31[l];
                polygon.addPoint((int) v.x, (int) v.y);
            }

            Plane plane =
                    new Plane(
                            pFace.vertices[0],
                            pFace.vertices[1],
                            pFace.vertices[2]);


            if (plane.n.z >= 0.0D) {
                double light = Math.abs(this.lightDirection.dot(plane.n));


                float[] hsbColor = Color.RGBtoHSB(
                        pFace.color.getRed(),
                        pFace.color.getGreen(),
                        pFace.color.getBlue(),
                        null);
                Color fillColor = new Color(
                        Color.HSBtoRGB(
                                hsbColor[0],
                                (float) (0.875D + 0.125D * light) * hsbColor[1],
                                (float) (0.875D + 0.125D * light) * hsbColor[2]));
                g2.setColor(fillColor);
                g2.fillPolygon(polygon);


                Color outlineColor = new Color(
                        Color.HSBtoRGB(
                                hsbColor[0],
                                (float) (0.9D * (0.875D + 0.125D * light) * hsbColor[1]),
                                (float) (0.9D * (0.875D + 0.125D * light) * hsbColor[2])));
                g2.setColor(outlineColor);
                g2.drawPolygon(polygon);

            } else {
                g2.setColor(backfacingColor);
                g2.fillPolygon(polygon);
            }
        }
    }


    public void mouseClicked(MouseEvent arg0) {
    }


    public void mouseEntered(MouseEvent arg0) {
    }


    public void mouseExited(MouseEvent arg0) {
    }


    public void mousePressed(MouseEvent e) {
        this.lastX = e.getX();
        this.lastY = e.getY();
    }


    public void mouseReleased(MouseEvent e) {
    }


    public void mouseDragged(MouseEvent e) {
        double angleX = (e.getY() - this.lastY) / 50.0D;
        double angleY = (e.getX() - this.lastX) / 50.0D;

        this.mesh = this.mesh.transform(
                Matrix44.rotationZ(this.cameraRotation.z).mul(
                        Matrix44.rotationY(this.cameraRotation.y).mul(
                                Matrix44.rotationX(this.cameraRotation.x).mul(
                                        Matrix44.rotationX(angleX).mul(
                                                Matrix44.rotationY(angleY).mul(
                                                        Matrix44.rotationX(-this.cameraRotation.x).mul(
                                                                Matrix44.rotationY(-this.cameraRotation.y).mul(
                                                                        Matrix44.rotationZ(-this.cameraRotation.z)))))))));

        this.lastX = e.getX();
        this.lastY = e.getY();

        repaint();
    }


    public void mouseMoved(MouseEvent e) {
    }


    public void mouseWheelMoved(MouseWheelEvent e) {
        Vector3 direction = this.cameraPosition.unit();
        Vector3 newPosition = this.cameraPosition.add(direction.mul(0.1D * e.getWheelRotation()));
        if ((1.0D < newPosition.norm()) && (newPosition.norm() < 50.0D)) {
            this.cameraPosition = newPosition;
        }

        repaint();
    }
}

/* Location:              prisma-graphics.jar!/com/puzzletimer/graphics/Panel3D.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
