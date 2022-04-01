package cs3318.group17.raytracer.scene;

import cs3318.group17.raytracer.ColorUtil;
import cs3318.group17.raytracer.Log;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.*;
import cs3318.group17.raytracer.shapes.*;
import cs3318.group17.raytracer.shapes.Shape;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Delegate class for Scene which handles reading Scene descriptions when passed in as a scene description text file.
 * Some methods require information about the Scene not contained in the Scene description file or SceneReader to be
 * passed as arguments, but since this is only a delegate class this isn't a big issue.
 */
public class SceneReader {

    /**
     * cols and rows parameters tie this method to the Scene class since the entire Camera is not described in the
     * scene desctiption file.
     *
     * @param scanner Scanner reading a scene description text file
     * @return the Camera described by the scene description file
     * @throws FileNotFoundException
     */
    public static Camera readCamera(Scanner scanner) throws FileNotFoundException {
        try {
            Point eye = readPoint(scanner);
            Point center = readPoint(scanner);
            Vector up = readVector(scanner);
            double fovy = scanner.nextDouble();
            int cols = scanner.nextInt();
            int rows = scanner.nextInt();

            return new Camera(eye, center, up, fovy, cols, rows);
        }
        catch ( InputMismatchException e ) {
            throw new InputMismatchException("Value in file at camera block is of incorrect type");
        }
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return the AmbientLight described in the scene description file
     * @throws FileNotFoundException
     */
    public static AmbientLight readAmbientLight(Scanner scanner) throws FileNotFoundException {
        try {
            AmbientLight ambientLight;
            if (scanner.hasNext("AMBIENT_LIGHT:")) {
                String elementStartTag = scanner.next();  // Captures the start of line tag required (a hasNextAndSkip would of been ideal.)
                ambientLight = new AmbientLight(readPoint(scanner),
                        readColor(scanner),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat());
            }
            else {
                throw new InputMismatchException("Ambient Light definition should follow directly after Camera");
            }
            return ambientLight;
        }
        catch (InputMismatchException e) {
            throw new InputMismatchException("Value in file at ambient lights block is of incorrect type.");
        }
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return the Light sources described in the scene description file
     * @throws FileNotFoundException
     */
    public static ArrayList<Light> readLights(Scanner scanner) throws FileNotFoundException {
        try {
            ArrayList<Light> lightsArray = new ArrayList<>();
            while (scanner.hasNext("LIGHT:")) {  // While next line starts with 'LIGHT:'
                String elementStartTag = scanner.next();
                lightsArray.add(new Light(readPoint(scanner),
                        readColor(scanner),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat()));
            }
            return lightsArray;
        }
        catch ( InputMismatchException e ) {
            throw new InputMismatchException("Value in file at lights block is of incorrect type");
        }
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return the Pigments described in the scene description file
     * @throws FileNotFoundException
     */
    public static ArrayList<Pigment> readSurfacePigments(Scanner scanner) throws FileNotFoundException {
        try {
            ArrayList<Pigment> pigmentsArray = new ArrayList<>();
            while (scanner.hasNext("PIGMENT:")) {  // While next line starts with 'PIGMENT:'
                String elementStartTag = scanner.next();
                String name = scanner.next();
                switch (name) {
                    case "solid" -> pigmentsArray.add(new SolidPigment(readColor(scanner)));
                    case "checker" -> pigmentsArray.add(new CheckerPigment(readColor(scanner), readColor(scanner), scanner.nextDouble()));
                    case "texmap" -> {
                        File bmpFile = new File(scanner.next());
                        try {
                            pigmentsArray.add(new TexmapPigment(bmpFile, scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
                        } catch (IOException ex) {
                            Log.error("Could not locate texmap file '" + bmpFile.getName() + "'.");
                            System.exit(1);
                        }
                    }
                    default -> throw new UnsupportedOperationException("Unrecognized pigment: '" + name + "'.");
                }
            }
            return pigmentsArray;
        }
        catch (InputMismatchException e) {
            throw new InputMismatchException("Value in file at surface pigments block is of incorrect type");
        }
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return the Finishes described in the scene description file
     * @throws FileNotFoundException
     */
    public static ArrayList<Finish> readSurfaceFinishes(Scanner scanner) throws FileNotFoundException {
        try {
            ArrayList<Finish> finishesArray = new ArrayList<>();
            while (scanner.hasNext("FINISH:")) {  // While next line starts with 'FINISH:'
                String elementStartTag = scanner.next();
                finishesArray.add(new Finish(scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat(),
                        scanner.nextFloat()));
            }
            return finishesArray;
        }
        catch ( InputMismatchException e ) {
            throw new InputMismatchException("Value in file at surface finishes block is of incorrect type");
        }
    }

    /**
     * This method is also tied to the Scene class as it requires the Scene's Pigment and Finishes arrays to be passed
     * in as parameters.
     *
     * @param scanner Scanner reading a scene description text file
     * @param pigmentsArray the array of Pigments currently described in a Scene
     * @param finishesArray the array of finishes currently described in a Scene
     * @return  the Shapes described in the scene description file
     * @throws FileNotFoundException
     */
    public static ArrayList<Shape> readShapes(Scanner scanner, ArrayList<Pigment> pigmentsArray,
                                       ArrayList<Finish> finishesArray) throws FileNotFoundException {
        try {
            ArrayList<Shape> shapesArray = new ArrayList<>();
            while (scanner.hasNext("SHAPE:")) {  // While next line starts with 'SHAPE:'
                String elementStartTag = scanner.next();
                int pigNum = scanner.nextInt();
                int finishNum = scanner.nextInt();

                String name = scanner.next();
                Shape shape;
                switch (name) {
                    case "sphere" -> shape = new Sphere(pigmentsArray.get(pigNum), finishesArray.get(finishNum), readPoint(scanner), scanner.nextDouble());
                    case "plane" -> shape = new Plane(pigmentsArray.get(pigNum), finishesArray.get(finishNum), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                    case "cylinder" -> shape = new Cylinder(pigmentsArray.get(pigNum), finishesArray.get(finishNum), readPoint(scanner), readVector(scanner), scanner.nextDouble(), scanner.nextDouble());
                    case "disc" -> shape = new Disc(pigmentsArray.get(pigNum), finishesArray.get(finishNum), readPoint(scanner), readVector(scanner), scanner.nextDouble());
                    case "triangle" -> shape = new Triangle(pigmentsArray.get(pigNum), finishesArray.get(finishNum), readPoint(scanner), readPoint(scanner), readPoint(scanner));
                    default -> throw new UnsupportedOperationException("Unrecognized shape: '" + name + "'.");
                }

                shapesArray.add(shape);
            }
            return shapesArray;
        }
        catch (InputMismatchException e) {
            throw new InputMismatchException("Value in file at shapes block is of incorrect type");
        }
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return  the Color described in the scene description file
     * @throws InputMismatchException
     */
    public static Color readColor(Scanner scanner) throws InputMismatchException {
        return new Color(ColorUtil.clamp(scanner.nextFloat()), ColorUtil.clamp(scanner.nextFloat()), ColorUtil.clamp(scanner.nextFloat()));
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return  the Vector described in the scene description file
     * @throws InputMismatchException
     */
    public static Vector readVector(Scanner scanner) throws InputMismatchException {
        return new Vector(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
    }

    /**
     *
     * @param scanner Scanner reading a scene description text file
     * @return the Point described in the scene description file
     * @throws InputMismatchException
     */
    public static Point readPoint(Scanner scanner) throws InputMismatchException {
        return new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
    }
}
