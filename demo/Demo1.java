import cs3318.group17.raytracer.scene.Scene;
import cs3318.group17.raytracer.RayTracer;

public class Demo1 {
    public static void main(String[] args ) {
        Scene scene = new Scene();

        // Camera and Ambient Light
        scene.setCamera(Scene.createPoint(0, 0, 0), Scene.createPoint(0, 0, -1),
                Scene.createVector(0, 1, 0), 45, 400, 300);
        scene.setAmbientLight(Scene.createPoint(0, 0, 0), Scene.createColor(0.1f, 0.1f, 0.1f),
                1, 0, 0);

        // Add Light sources
        scene.addLight(Scene.createPoint(10, 100, 10), Scene.createColor(1f, 0f, 0f),
                1f, 0f, 0f);
        scene.addLight(Scene.createPoint(100, 100, 100), Scene.createColor(1f, 0f, 0f),
                1f, 0f, 0.01f);

        // Add Shapes
        scene.addSphere(Scene.createSolidPigment(Scene.createColor(1f, 0f, 0f)),
                Scene.createFinish(0.7f, 0.3f, 0f, 1f, 0f, 0f, 0f),
                Scene.createPoint(-2, 2, -13), 2);
        scene.addSphere(Scene.createSolidPigment(Scene.createColor(1f, 0f, 0f)),
                Scene.createFinish(0.7f, 0.3f, 0f, 1f, 0f, 0f, 0f),
                Scene.createPoint(-2, -1, -14), 4);
        scene.addSphere(Scene.createSolidPigment(Scene.createColor(1f, 0f, 0f)),
                Scene.createFinish(0.7f, 0.3f, 0f, 1f, 0f, 0f, 0f),
                Scene.createPoint(2, 2, -10), 2);
        scene.addSphere(Scene.createSolidPigment(Scene.createColor(1f, 0f, 0f)),
                Scene.createFinish(0.7f, 0.3f, 0f, 1f, 0f, 0f, 0f),
                Scene.createPoint(2, 0, -8), 2);
        scene.addSphere(Scene.createSolidPigment(Scene.createColor(1f, 0f, 0f)),
                Scene.createFinish(0.7f, 0.3f, 0f, 1f, 0f, 0f, 0f),
                Scene.createPoint(0, 0, 0), 1000);

        RayTracer rayTracer = new RayTracer(scene);

        rayTracer.drawTo("demo1.png", "png");
    }
}

