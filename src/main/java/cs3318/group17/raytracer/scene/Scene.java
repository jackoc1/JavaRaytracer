package cs3318.group17.raytracer.scene;

import cs3318.group17.raytracer.ColorUtil;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;
import cs3318.group17.raytracer.shapes.Shape;
import cs3318.group17.raytracer.shapes.Sphere;
import cs3318.group17.raytracer.shapes.Plane;
import cs3318.group17.raytracer.shapes.Triangle;
import cs3318.group17.raytracer.shapes.Disc;
import cs3318.group17.raytracer.shapes.Cylinder;
import cs3318.group17.raytracer.pigments.CheckerPigment;
import cs3318.group17.raytracer.pigments.SolidPigment;
import cs3318.group17.raytracer.pigments.TexmapPigment;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Scene class fully describes the state of the Scene's Camera, AmbientLight, Lights and Shapes. A Scene can then
 * be raytraced and rendered by passing it into the RayTracer class.
 */
public final class Scene {

    private Camera camera = defaultCamera();
    private AmbientLight ambientLight = defaultAmbientLight();
    private ArrayList<Light> lightsArray = new ArrayList<>();
    private ArrayList<Shape> shapesArray = new ArrayList<>();

    /**
     * Creates an instance of an empty scene with default Camera and AmbientLight.
     */
    public Scene () { }

    /**
     * Creates an instance by constructing a predefined scene using information provided in a file.
     *
     * ------------------------------------------------------------
     *
     * parameter definitions:
     *
     * [x, y, z]           <-- x-axis, y-axis, z-axis coordinates
     *
     * [red, green, blue]  <-- rgb color components expressed in float values in the inclusive range (0.0 to 1.0)
     *
     * [a1, a2, a3]        <-- attenuation constants (used in the calculation "1/(a1 + (a2*d) + (a3*(d^2)))"
     *                         light diffusion over a distance)
     *
     * [pigment_type]      <-- solid, checker, or texmap
     *
     * [pigment_index]     <-- pigments index relative to pigment list
     * [finish_index]      <-- finish index relative to finish list
     *
     * ------------------------------------------------------------
     *
     * File layout:
     *
     * x  y  z <-- (Eye)
     * x  y  z <-- (Center)
     * x  y  z <-- (Up)
     * fovy    <-- (Field of view in y direction)
     *
     *
     * AMBIENT_LIGHT: x  y  z   red green blue   a1 a2 a3
     *
     * LIGHT:  x  y  z   red green blue   a1 a2 a3
     *
     * PIGMENT: [pigment_type] red green blue
     *
     * FINISH: [ambient_light] [texture_diffusion] [spectrum] [surface_shine] [surface_reflectiveness] [surface_transmittance] [index_of_reflection]
     *
     * SHAPE: [pigment_index] [finish_index] [shape] x y z [shape specific parameters]
     *
     * ------------------------------------------------------------
     *
     * File example:
     *
     * 0    0    0
     * 0    0   -1
     * 0    1    0
     * 45
     *
     * AMBIENT_LIGHT:  0   0   0    0.1  0.1  0.1   1  0  0
     *
     * LIGHT: 10  100  10    1.0  1.0  1.0   1  0  0
     * LIGHT: 100 100 100    1.0  1.0  1.0   1  0  0
     *
     * PIGMENT: solid        1  0  0
     * PIGMENT: solid        0  1  0
     * PIGMENT: solid        0  0  1
     * PIGMENT: solid       .6 .2 .4
     *
     * FINISH: 0.4  0.6  0.0    1  0  0  0
     * FINISH: 0.4  0.6  0.7  500  0  0  0
     * FINISH: 0.3  0.5  0.6  200  0  0  0
     *
     * SHAPE: 0 1 sphere  -2   2  -13     2
     * SHAPE: 1 0 sphere  -2  -1  -14     4
     * SHAPE: 2 1 sphere   2   2  -10     2
     * SHAPE: 2 1 sphere   2   0   -8     2
     * SHAPE: 3 0 sphere   0   0    0  1000
     *
     * ------------------------------------------------------------
     *
     * @param file = The file to be read from to generate the scene.
     *
     */
    public Scene (File file) {
        try ( Scanner scanner = new Scanner(file) ) {
            camera = SceneReader.readCamera(scanner);
            ambientLight = SceneReader.readAmbientLight(scanner);
            lightsArray = SceneReader.readLights(scanner);
            ArrayList<Pigment> pigmentsArray= SceneReader.readSurfacePigments(scanner);
            ArrayList<Finish> finishesArray = SceneReader.readSurfaceFinishes(scanner);
            shapesArray = SceneReader.readShapes(scanner, pigmentsArray, finishesArray);
        }
        catch (FileNotFoundException e) {
            System.err.println( "The file was not found:" + e );
        }
    }

    /**
     * Sets the camera used in the scene.
     *
     * @param camera = The Camera instance to be used for the scene.
     */
    public void setCamera( Camera camera ) {
        this.camera = camera;
    }

    /**
     * Creates a new Camera object and passes it to the Scene.
     *
     * @see Camera
     * @param eye coordinates for the eye in World Space
     * @param center coordinates for where the eye is focused
     * @param up vector which decides which direction is "up" when the eye focuses on center
     * @param fovy the angle of elevation from the eye to the top of the window screen
     * @param cols number of columns to be drawn on the window screen
     * @param rows number of rows to be drawn on the window screen
     */
    public void setCamera( Point eye, Point center, Vector up, double fovy, int cols, int rows) {
        setCamera(new Camera(eye, center, up, fovy, cols, rows));
    }

    /**
     * Resets the current camera instance to the default camera.
     */
    public void resetCamera( ) {
        camera = defaultCamera();
    }

    /**
     * Sets the specified ambient light in the scene.
     *
     * @param ambientLight = The AmbientLight instance to be used in the scene.
     */
    public void setAmbientLight( AmbientLight ambientLight ) {
        this.ambientLight = ambientLight;
    }

    /**
     * Sets the Scenes colored ambient light source in the 3-dimensional plane.
     *
     * @param location a location from which the light originates
     * @param color the color associated with the light
     * @param a a float attenuation constant
     * @param b a float attenuation constant to be multiplied by the distance
     * @param c a float attenuation constant to be multiplied by the distance^2
     */
    public void setAmbientLight( Point location, Color color, float a, float b, float c ) {
        setAmbientLight(new AmbientLight(location, color, a, b, c));
    }

    /**
     * Resets the current ambient light instance to the default ambient light.
     */
    public void resetAmbientLight( ) {
        ambientLight = defaultAmbientLight();
    }

    /**
     * Adds the specified light to the scene.
     *
     * @param light = The light instance to be added to the scene.
     */
    public boolean addLight( Light light ) {
        if ( ! lightsArray.contains( light )) {
            lightsArray.add(light);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Creates a colored light source in the 3-dimensional plane and adds to Scene Light array.
     *
     * @param location a location from which the light originates
     * @param color the color associated with the light
     * @param a attenuation constant
     * @param b attenuation constant to be multiplied by the distance
     * @param c attenuation constant to be multiplied by the distance^2
     */
    public boolean addLight( Point location, Color color, float a, float b, float c ) {
        return addLight(new Light(location, color, a, b, c));
    }

    /**
     * Removes the specified light from the scene.
     *
     * @param light = The light instance to be removed from the scene.
     *
     * @return True if the scene contained the light specified.
     */
    public boolean removeLight( Light light ) {
        if ( lightsArray.contains( light ) ) {
            lightsArray.remove(light);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes all(if any) lights from the scene.
     */
    public void resetLights() {
        lightsArray.clear();
    }

    /**
     * Adds the specified shape to the scene.
     *
     * @param shape = The shape instance to be added to the scene.
     */
    public void addShape( Shape shape ) {
        shapesArray.add( shape );
    }

    /**
     * Constructs a Sphere at the specified center location with the specified radius and adds it to Scene Shapes array.
     *
     * @param center the Point location of the Sphere's center
     * @param radius the radius of the Sphere
     */
    public void addSphere( Pigment pigment, Finish finish, Point center, double radius ) {
        addShape(new Sphere(pigment, finish, center, radius));
    }

    /**
     * Constructs a right Cylinder from a point on the Cylinder's base, an orientation for the Cylinder's axis and a
     * radius and height to determine the Cylinder's size and adds it to Scene Shapes array.
     *
     * @param centerOfBase a Point in World Space which will serve as the center of the Cylinder's Disc base
     * @param axis a Vector which is parallel to the Cylinder's intended axis
     * @param radius the length of the Cylinder's radius in World Units
     * @param height the height of the Cylinder from its base to its top in World Units
     */
    public void addCylinder( Pigment pigment, Finish finish, Point centerOfBase, Vector axis, double radius, double height ) {
        addShape(new Cylinder(pigment, finish, centerOfBase, axis, radius, height));
    }

    /**
     * Creates a 2-dimensional Disc in 3-dimensional space and adds it to Scene Shapes array.
     *
     * @param center Point at centre of the disc surface
     * @param normal Normal Vector to the disc surface with magnitude 1
     * @param radius radius of the disc surface
     */
    public void addDisc( Pigment pigment, Finish finish, Point center, Vector normal, double radius ) {
        addShape(new Disc(pigment, finish, center, normal, radius));
    }

    /**
     * Construct a plane of the form ax + by + cz - d = 0 from a Point on the Plane and the normal Vector to the Plane
     * and adds it to Scene Shapes array.
     *
     * @param p a Point on the Plane
     * @param normal the normal to the Plane
     */
    public void addPlane( Pigment pigment, Finish finish, Point p, Vector normal ) {
        addShape(new Plane(pigment, finish, p, normal));
    }

    public void addTriangle( Pigment pigment, Finish finish, Point p1, Point p2, Point p3 ) {
        addShape(new Triangle(pigment, finish, p1, p2, p3));
    }

    /**
     * Removes the specified shape from the scene.
     *
     * @param shape = The shape instance to be removed from the scene.
     *
     * @return True if the scene contained the light specified.
     */
    public boolean removeShape( Shape shape ) {
        if ( shapesArray.contains( shape ) ) {
            shapesArray.remove(shape);
            return true;
        }
        else {
            return false;
        }
    }

    public static Point createPoint(double x, double y, double z) { return new Point(x, y, z); }

    public static Vector createVector(double x, double y, double z) { return new Vector(x, y, z); }

    public static Color createColor(float red, float blue, float green) {
        return new Color(ColorUtil.clamp(red), ColorUtil.clamp(blue), ColorUtil.clamp(green));
    }

    public static CheckerPigment createCheckerPigment( Color color1, Color color2, double scale ) {
        return new CheckerPigment(color1, color2, scale);
    }

    public static SolidPigment createSolidPigment( Color color ) {
        return new SolidPigment(color);
    }

    public static TexmapPigment createTexmapPigment(File bmpFile, double sa, double sb, double sc, double sd,
                                                    double ta, double tb, double tc, double td) throws IOException {
        return new TexmapPigment(bmpFile, sa, sb, sc, sd, ta, tb, tc, td);
    }

    public static Finish createFinish(float amb, float diff, float spec, float shiny, float refl, float trans,
                                      float ior) {
        return new Finish(amb, diff, spec, shiny, refl, trans, ior);
    }

    /**
     * Removes all(if any) shapes from the scene.
     */
    public void resetShapes( ) {
        shapesArray.clear( );
    }

    private Camera defaultCamera() {
        Point eye = new Point( 0, 30, -200 );
        Point center = new Point( 0, 10, -100 );
        Vector up = new Vector( 0, 1, 0 );
        double fovy = 60;

        return new Camera( eye, center, up, fovy, 400, 300 );
    }

    private AmbientLight defaultAmbientLight() {
        Point location = new Point( 0, 0, 0 );
        Color color = new Color( ColorUtil.clamp( 1 ), ColorUtil.clamp( 1 ), ColorUtil.clamp( 1 ) );
        int a = 1;
        int b = 0;
        int c = 0;
        return new AmbientLight(location, color, a, b, c);
    }

    /**
     * Return the number of columns of pixels in the scene.
     *
     * @return The number of columns in the scene.
     */
    public int getNumberOfColumns() { return camera.getNumberOfCols(); }

    /**
     * Returns the number of rows of pixels in the scene.
     *
     * @return The number of rows in the scene.
     */
    public int getNumberOfRows() { return camera.getNumberOfRows(); }


    /**
     * Returns the camera instance used in the scene.
     *
     * @return The camera instance.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Returns the ambient light instance used in the scene.
     *
     * @return The ambient light instance.
     */
    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public ArrayList<Light> getLightsArray() {
        return new ArrayList<>(lightsArray);
    }

    public ArrayList<Shape> getShapesArray() {
        return new ArrayList<>(shapesArray);
    }
}
