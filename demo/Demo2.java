import cs3318.group17.raytracer.scene.Scene;
import cs3318.group17.raytracer.RayTracer;

public class Demo2 {
    public static void main(String[] args ) {
        Scene scene = new Scene();

        // Camera and Ambient Light
        scene.setCamera(Scene.createPoint(0, -8, 0), Scene.createPoint(0, 0, 0),
                Scene.createVector(0, 0, 1), 70, 400, 300);
        scene.setAmbientLight(Scene.createPoint(0, 0, 0), Scene.createColor(1f, 1f, 1f),
                1, 0, 0);

        // Add Light sources
        scene.addLight(Scene.createPoint(0, -4, 8), Scene.createColor(0.6f, 0f, 0f),
                0f, 0f, 0.01f);
        scene.addLight(Scene.createPoint(3.464, 2, 8), Scene.createColor(0f, 0.6f, 0f),
                0f, 0f, 0.01f);
        scene.addLight(Scene.createPoint(-3.464, 2, 8), Scene.createColor(0f, 0f, 0.6f),
                0f, 0f, 0.01f);

        // Add Shapes
        scene.addCylinder(Scene.createCheckerPigment(Scene.createColor(1f, 0f, 1f), Scene.createColor(0f, 1f, 0f), 2),
                Scene.createFinish(0.2f, 0.9f, 1f, 1000f, 0f, 0f, 0f),
                Scene.createPoint(-3, -2, -2), Scene.createVector(-1, 2, 1),
                2, 4);

        scene.addCylinder(Scene.createSolidPigment(Scene.createColor(0.8f, 0.8f, 0.8f)),
                Scene.createFinish(0.4f, 0.6f, 0.7f, 500f, 0f, 0f, 0f),
                Scene.createPoint(0, 0, 0), Scene.createVector(3, 0, 5),
                1, 3);

        scene.addTriangle(Scene.createSolidPigment(Scene.createColor(0.6f, 0.6f, 0.8f)),
                Scene.createFinish(0.4f, 0.6f, 0.7f, 500f, 0f, 0f, 0f),
                Scene.createPoint(-2, 6, -2), Scene.createPoint(1, 4, -3),
                Scene.createPoint(-2, 5, -6));

        scene.addDisc(Scene.createCheckerPigment(Scene.createColor(0f, 0f, 1f), Scene.createColor(0.5f, 1f, 1f), 1),
                Scene.createFinish(0.4f, 0.6f, 0.7f, 500f, 0f, 0f, 0f),
                Scene.createPoint(0, 0, 5), Scene.createVector(0, -1, 0), 3);

        RayTracer rayTracer = new RayTracer(scene);

        rayTracer.drawTo("demo2.png", "png");
    }
}
