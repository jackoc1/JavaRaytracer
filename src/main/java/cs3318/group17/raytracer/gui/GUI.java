package cs3318.group17.raytracer.gui;

import cs3318.group17.raytracer.ColorUtil;
import cs3318.group17.raytracer.RayTracer;
import cs3318.group17.raytracer.scene.Scene;
import cs3318.group17.raytracer.scene.AmbientLight;
import cs3318.group17.raytracer.scene.Camera;
import cs3318.group17.raytracer.scene.Light;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.SolidPigment;
import cs3318.group17.raytracer.shapes.Sphere;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame {

    private Scene scene;

//  --- COLUMNS AND ROWS SECTION ----------------------------------------
    private final JLabel ColumnsLabel = new JLabel("Columns: ");
    private final JLabel RowsLabel = new JLabel("Rows: ");

    private final JTextField ColumnsBox = new JTextField();
    private final JTextField RowsBox = new JTextField();

    private final JButton ColsRowsSubmitButton = new JButton("Next!");
//  --- COLUMNS AND ROWS SECTION ^^^ ------------------------------------


//  --- FileName --------------------------------------------------------
    private final JLabel outFileNameLabel = new JLabel("Output file name:");
    private final JTextField outFileNameTextField = new JTextField("example.bmp");
//  --- FileName ^^^ ----------------------------------------------------


//  --- Camera Section --------------------------------------------------
    private final JLabel CameraLabel = new JLabel("Camera");
//  --- Eye Field --------------------------------------------------
    private final JTextField xEyeCoordinateBox = new JTextField("0");
    private final JTextField yEyeCoordinateBox = new JTextField("0");
    private final JTextField zEyeCoordinateBox = new JTextField("0");

    private final JLabel eyeLabel = new JLabel("Eye : ");
    private final JLabel eyelblX = new JLabel("x :");
    private final JLabel eyelblY = new JLabel("y :");
    private final JLabel eyelblZ = new JLabel("z :");
//  --- Eye Field ^^^ ----------------------------------------------
//  --- Center Field -----------------------------------------------
    private final JTextField xCenterCoordinateBox = new JTextField("0");
    private final JTextField yCenterCoordinateBox = new JTextField("0");
    private final JTextField zCenterCoordinateBox = new JTextField("-1");

    private final JLabel CenterLabel = new JLabel("Center : ");
    private final JLabel CenterlblX = new JLabel("x :");
    private final JLabel CenterlblY = new JLabel("y :");
    private final JLabel CenterlblZ = new JLabel("z :");
//  --- Center Field ^^^ -------------------------------------------
//  --- Up Field -----------------------------------------------
    private final JTextField xUpCoordinateBox = new JTextField("0");
    private final JTextField yUpCoordinateBox = new JTextField("1");
    private final JTextField zUpCoordinateBox = new JTextField("0");

    private final JLabel UpLabel = new JLabel("Up : ");
    private final JLabel UplblX = new JLabel("x :");
    private final JLabel UplblY = new JLabel("y :");
    private final JLabel UplblZ = new JLabel("z :");
//  --- Center Field ^^^ -------------------------------------------
//  --- Camera ^^^ ------------------------------------------------------


//  --- Ambient Light Section --------------------------------------------------
    private final JLabel AmbientLightLabel = new JLabel("Ambient Light");
//  --- Ambient Light Location Field --------------------------------------------------
    private final JTextField xlocationCoordinateBox = new JTextField("0");
    private final JTextField ylocationCoordinateBox = new JTextField("0");
    private final JTextField zlocationCoordinateBox = new JTextField("0");

    private final JLabel locationLabel = new JLabel("Location : ");
    private final JLabel locationlblX = new JLabel("x :");
    private final JLabel locationlblY = new JLabel("y :");
    private final JLabel locationlblZ = new JLabel("z :");
//  --- Ambient Light Location Field ^^^ ----------------------------------------------
//  --- RGB Components Field -----------------------------------------------
    private final JTextField xRedComponentBox = new JTextField("0.2");
    private final JTextField yGreenComponentBox = new JTextField("0.2");
    private final JTextField zBlueComponentBox = new JTextField("0.2");

    private final JLabel ComponentsLabel = new JLabel("Components : ");
    private final JLabel ComponentslblX = new JLabel("r :");
    private final JLabel ComponentslblY = new JLabel("g :");
    private final JLabel ComponentslblZ = new JLabel("b :");
    //  --- RGB Components Field ^^^ -------------------------------------------
//  --- Attenuation Constants Field -----------------------------------------------
    private final JTextField rAttenuationBox = new JTextField("1");
    private final JTextField gAttenuationBox = new JTextField("0");
    private final JTextField bAttenuationBox = new JTextField("0");

    private final JLabel attenLabel = new JLabel("Attenuation Constants : ");
    private final JLabel attenLabelA = new JLabel("a :");
    private final JLabel attenLabelB = new JLabel("b :");
    private final JLabel attenLabelC = new JLabel("c :");
//  --- Attenuation Constants Field ^^^ -------------------------------------------
//  --- Ambient Light Section ^^^ ------------------------------------------------------



//  --- Light Section ------------------------------------------------------------------
    private final JButton btnLight = new JButton("Add Light!");
//  --- Ambient Light Location Field ----------------------------------------------
    private final JTextField xlocationLightCoordinateBox = new JTextField("10");
    private final JTextField ylocationLightCoordinateBox = new JTextField("10");
    private final JTextField zlocationLightCoordinateBox = new JTextField("10");

    private final JLabel locationLightLabel = new JLabel("Location : ");
    private final JLabel locationLightlblX = new JLabel("x :");
    private final JLabel locationLightlblY = new JLabel("y :");
    private final JLabel locationLightlblZ = new JLabel("z :");
//  --- Ambient Light Location Field ^^^ -----------------------------------------
//  --- RGB Components Field -----------------------------------------------------
    private final JTextField xLightRedComponentBox = new JTextField("1.0");
    private final JTextField yLightGreenComponentBox = new JTextField("1.0");
    private final JTextField zLightBlueComponentBox = new JTextField("1.0");

    private final JLabel LightComponentsLabel = new JLabel("Components : ");
    private final JLabel LightComponentslblX = new JLabel("r :");
    private final JLabel LightComponentslblY = new JLabel("g :");
    private final JLabel LightComponentslblZ = new JLabel("b :");
//  --- RGB Components Field ^^^ --------------------------------------------------
//  --- Attenuation Constants Field -----------------------------------------------
    private final JTextField rLightAttenuationBox = new JTextField("1");
    private final JTextField gLightAttenuationBox = new JTextField("0");
    private final JTextField bLightAttenuationBox = new JTextField("0");

    private final JLabel attenLightLabel = new JLabel("Attenuation Constants : ");
    private final JLabel attenLightLabelA = new JLabel("a :");
    private final JLabel attenLightLabelB = new JLabel("b :");
    private final JLabel attenLightLabelC = new JLabel("c :");
//  --- Attenuation Constants Field ^^^ -------------------------------------------
//  --- Light Section ^^^ --------------------------------------------------------------



//  --- Shape Section ------------------------------------------------------------------
    private final JButton btnSphere = new JButton("Add Sphere!");
//  --- Ambient Light Location Field ----------------------------------------------
    private final JTextField xSphereCenterCoordinateBox = new JTextField("3");
    private final JTextField ySphereCenterCoordinateBox = new JTextField("3");
    private final JTextField zSphereCenterCoordinateBox = new JTextField("-15");
    private final JTextField radiusSphereCenterCoordinateBox = new JTextField("1");

    private final JLabel SphereCenterLabel = new JLabel("center : ");
    private final JLabel SphereCenterlblX = new JLabel("x :");
    private final JLabel SphereCenterlblY = new JLabel("y :");
    private final JLabel SphereCenterlblZ = new JLabel("z :");
    private final JLabel SphereRadiuslbl = new JLabel("radius :");
//  --- Shape Location & size Field ^^^ -----------------------------------------
//  --- RGB Components Field -----------------------------------------------
    private final JTextField xSphereRedComponentBox = new JTextField("1");
    private final JTextField ySphereGreenComponentBox = new JTextField("0");
    private final JTextField zSphereBlueComponentBox = new JTextField("0");

    private final JLabel SphereComponentsLabel = new JLabel("Components : ");
    private final JLabel SphereComponentslblX = new JLabel("r :");
    private final JLabel SphereComponentslblY = new JLabel("g :");
    private final JLabel SphereComponentslblZ = new JLabel("b :");
//  --- RGB Components Field ^^^ -------------------------------------------
//  --- Shape Section ^^^ --------------------------------------------------------------


//  --- Draw Scene Button ----
    private final JButton drawSceneButton = new JButton("Draw!");
//  --------------------------

    /**
     * Launches Graphical User Interface for drawing shapes and setting the scene.
     */
    public GUI() {
        setTitle("RayTracer");
        setSize(1000,600);
        setLocation(new Point(100,100));
        setLayout(null);
        setResizable(false);

        initColumnsAndRows();
        initColumnsAndRowsEvents();

    }

    private void initEvents() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });

        btnLight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addLight(e);
            }
        });

        btnSphere.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSphere(e);
            }
        });

        drawSceneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawScene(e);
            }
        });


    }

    private void initColumnsAndRows() {
        ColumnsLabel.setBounds(50 , 20, 80, 30);
        add(ColumnsLabel);
        ColumnsBox.setBounds(150 , 20, 50, 30);
        add(ColumnsBox);

        RowsLabel.setBounds(50 , 50, 80, 30);
        add(RowsLabel);
        RowsBox.setBounds(150 , 50, 50, 30);
        add(RowsBox);

        ColsRowsSubmitButton.setBounds(270, 50, 60, 30);
        add(ColsRowsSubmitButton);

    }

    private void initColumnsAndRowsEvents() {
        ColsRowsSubmitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ApplyRowsColumns(e);
            }
        });
    }

    private void ApplyRowsColumns(ActionEvent e) {
        if ( ! ColumnsBox.getText().isEmpty() && ! RowsBox.getText().isEmpty()) {
            try {
                int cols = Integer.parseInt(ColumnsBox.getText());
                int rows = Integer.parseInt(RowsBox.getText());
                if (cols > 0 && rows > 0) {

                    getContentPane().remove(ColumnsBox);
                    getContentPane().remove(ColumnsLabel);
                    getContentPane().remove(RowsBox);
                    getContentPane().remove(RowsLabel);
                    getContentPane().remove(ColsRowsSubmitButton);
                    repaint();

                    scene = new Scene();

                    initCamera();
                    initAmbientLight();
                    initLight();
                    initShape();

                    drawSceneButton.setBounds(630 , 210, 60, 30);
                    add(drawSceneButton);


                    outFileNameLabel.setBounds(490, 170, 170, 30);
                    add(outFileNameLabel);

                    outFileNameTextField.setBounds(610, 170, 120, 30);
                    add(outFileNameTextField);

                    initEvents();
                }
            }
            catch (NumberFormatException error){
                throw new NumberFormatException( error.getMessage() );
            }
        }
    }

    private void initCamera() {
        CameraLabel.setBounds(200, 120, 80, 25);
        add(CameraLabel);

        positionRow(10, eyeLabel, eyelblX, xEyeCoordinateBox, eyelblY, yEyeCoordinateBox, eyelblZ, zEyeCoordinateBox);
        positionRow(50, CenterLabel, CenterlblX, xCenterCoordinateBox, CenterlblY, yCenterCoordinateBox, CenterlblZ, zCenterCoordinateBox);
        positionRow(90, UpLabel, UplblX, xUpCoordinateBox, UplblY, yUpCoordinateBox, UplblZ, zUpCoordinateBox);
    }

    private void initAmbientLight() {
        AmbientLightLabel.setBounds(200, 280, 140, 25);
        add(AmbientLightLabel);

        positionRow(150, locationLabel, locationlblX, xlocationCoordinateBox, locationlblY, ylocationCoordinateBox, locationlblZ, zlocationCoordinateBox);
        positionRow(190, ComponentsLabel, ComponentslblX, xRedComponentBox, ComponentslblY, yGreenComponentBox, ComponentslblZ, zBlueComponentBox);
        positionRow(250, attenLabel, attenLabelA, rAttenuationBox, attenLabelB, gAttenuationBox, attenLabelC, bAttenuationBox);
    }

    private void initLight() {
        btnLight.setBounds(200, 430, 140, 25);
        add(btnLight);

        positionRow(320, locationLightLabel, locationLightlblX, xlocationLightCoordinateBox, locationLightlblY, ylocationLightCoordinateBox, locationLightlblZ, zlocationLightCoordinateBox);
        positionRow(360, LightComponentsLabel, LightComponentslblX, xLightRedComponentBox, LightComponentslblY, yLightGreenComponentBox, LightComponentslblZ, zLightBlueComponentBox);
        positionRow(400, attenLightLabel, attenLightLabelA, rLightAttenuationBox, attenLightLabelB, gLightAttenuationBox, attenLightLabelC, bLightAttenuationBox);
    }

    private void initShape() {
        int height = 10;
        positionRowColumn2(height, SphereCenterLabel, SphereCenterlblX, xSphereCenterCoordinateBox, SphereCenterlblY, ySphereCenterCoordinateBox, SphereCenterlblZ, zSphereCenterCoordinateBox);

        SphereRadiuslbl.setBounds(660 , height, 60, 30);
        add(SphereRadiuslbl);

        radiusSphereCenterCoordinateBox.setBounds(700 , height, 30, 30);
        add(radiusSphereCenterCoordinateBox);

        btnSphere.setBounds(400, 90, 140, 25);
        add(btnSphere);

        positionRowColumn2(60, SphereComponentsLabel, SphereComponentslblX, xSphereRedComponentBox, SphereComponentslblY, ySphereGreenComponentBox, SphereComponentslblZ, zSphereBlueComponentBox);
    }

    private void positionRow(int height, JLabel mainLabel, JLabel labelX, JTextField textField1, JLabel labelY, JTextField textField2, JLabel labelZ, JTextField textField3) {
        mainLabel.setBounds(20 , height, 80, 40);
        add(mainLabel);

        labelX.setBounds(120 , height, 30, 30);
        add(labelX);
        textField1.setBounds(150 , height, 30, 30);
        add(textField1);

        labelY.setBounds(190 , height, 30, 30);
        add(labelY);
        textField2.setBounds(230 , height, 30, 30);
        add(textField2);

        labelZ.setBounds(270 , height, 30, 30);
        add(labelZ);
        textField3.setBounds(310, height, 30, 30);
        add(textField3);
    }

    private void positionRowColumn2(int height, JLabel mainLabel, JLabel labelX, JTextField textField1, JLabel labelY, JTextField textField2, JLabel labelZ, JTextField textField3) {
        mainLabel.setBounds(360 , height, 120, 40);
        add(mainLabel);

        labelX.setBounds(460 , height, 30, 30);
        add(labelX);
        textField1.setBounds(490 , height, 30, 30);
        add(textField1);

        labelY.setBounds(540 , height, 30, 30);
        add(labelY);
        textField2.setBounds(570 , height, 30, 30);
        add(textField2);

        labelZ.setBounds(610 , height, 30, 30);
        add(labelZ);
        textField3.setBounds(630, height, 30, 30);
        add(textField3);
    }

    private void addSphere(ActionEvent e) {

        float r = Float.parseFloat(xSphereRedComponentBox.getText());
        float g = Float.parseFloat(ySphereGreenComponentBox.getText());
        float b = Float.parseFloat(zSphereBlueComponentBox.getText());
        Color sphereColor = new Color(ColorUtil.clamp(r), ColorUtil.clamp(g), ColorUtil.clamp(b));

        SolidPigment solidPigment = new SolidPigment(sphereColor);

        Finish defaultFinish = new Finish(0.4F,  0.6F,  0.7F,  500F,  0F,0F,  0F);

        float x = Float.parseFloat(xSphereCenterCoordinateBox.getText());
        float y = Float.parseFloat(ySphereCenterCoordinateBox.getText());
        float z = Float.parseFloat(zSphereCenterCoordinateBox.getText());
        float radius = Float.parseFloat(radiusSphereCenterCoordinateBox.getText());
        cs3318.group17.raytracer.math.Point sphereCenter = new cs3318.group17.raytracer.math.Point( x, y, z );
        Sphere sphere = new Sphere( solidPigment, defaultFinish, sphereCenter, radius );


        scene.addShape(sphere);

        xSphereCenterCoordinateBox.setText("");
        ySphereCenterCoordinateBox.setText("");
        zSphereCenterCoordinateBox.setText("");
        radiusSphereCenterCoordinateBox.setText("");

        xSphereRedComponentBox.setText("");
        ySphereGreenComponentBox.setText("");
        zSphereBlueComponentBox.setText("");

        repaint();

    }

    private void addLight(ActionEvent e) {
        float x = Float.parseFloat(xlocationLightCoordinateBox.getText());
        float y = Float.parseFloat(ylocationLightCoordinateBox.getText());
        float z = Float.parseFloat(zlocationLightCoordinateBox.getText());
        cs3318.group17.raytracer.math.Point lightLocation = new cs3318.group17.raytracer.math.Point( x, y, z );

        float r = Float.parseFloat(xLightRedComponentBox.getText());
        float g = Float.parseFloat(yLightGreenComponentBox.getText());
        float b = Float.parseFloat(zLightBlueComponentBox.getText());

        Color lightColor = new Color(ColorUtil.clamp(r), ColorUtil.clamp(g), ColorUtil.clamp(b));

        float c1 = Float.parseFloat(rLightAttenuationBox.getText());
        float c2 = Float.parseFloat(gLightAttenuationBox.getText());
        float c3 = Float.parseFloat(bLightAttenuationBox.getText());
        Light light = new Light(lightLocation, lightColor, c1, c2, c3);
        scene.addLight(light);

        xlocationLightCoordinateBox.setText("");
        ylocationLightCoordinateBox.setText("");
        zlocationLightCoordinateBox.setText("");
        xLightRedComponentBox.setText("");
        yLightGreenComponentBox.setText("");
        zLightBlueComponentBox.setText("");
        rLightAttenuationBox.setText("");
        gLightAttenuationBox.setText("");
        bLightAttenuationBox.setText("");

        repaint();
    }

    private void drawScene(ActionEvent e) {
        double FOVY = 30;

        float x = Float.parseFloat(xEyeCoordinateBox.getText());
        float y = Float.parseFloat(yEyeCoordinateBox.getText());
        float z = Float.parseFloat(zEyeCoordinateBox.getText());
        cs3318.group17.raytracer.math.Point eye = new cs3318.group17.raytracer.math.Point( x, y, z );

        x = Float.parseFloat(xCenterCoordinateBox.getText());
        y = Float.parseFloat(yCenterCoordinateBox.getText());
        z = Float.parseFloat(zCenterCoordinateBox.getText());
        cs3318.group17.raytracer.math.Point center = new cs3318.group17.raytracer.math.Point( x, y, z );

        x = Float.parseFloat(xUpCoordinateBox.getText());
        y = Float.parseFloat(yUpCoordinateBox.getText());
        z = Float.parseFloat(zUpCoordinateBox.getText());
        cs3318.group17.raytracer.math.Vector up = new cs3318.group17.raytracer.math.Vector( x, y, z );


        Camera camera = new Camera(eye, center, up, FOVY, scene.getNumberOfColumns(), scene.getNumberOfRows());
        scene.setCamera(camera);


        x = Float.parseFloat(xlocationCoordinateBox.getText());
        y = Float.parseFloat(ylocationCoordinateBox.getText());
        z = Float.parseFloat(zlocationCoordinateBox.getText());
        cs3318.group17.raytracer.math.Point ambientLightLocation = new cs3318.group17.raytracer.math.Point( x, y, z );

        float r = Float.parseFloat(xRedComponentBox.getText());
        float g = Float.parseFloat(yGreenComponentBox.getText());
        float b = Float.parseFloat(zBlueComponentBox.getText());
        Color ambientLightColor = new Color(ColorUtil.clamp(r), ColorUtil.clamp(g), ColorUtil.clamp(b));

        float c1 = Float.parseFloat(rAttenuationBox.getText());
        float c2 = Float.parseFloat(gAttenuationBox.getText());
        float c3 = Float.parseFloat(bAttenuationBox.getText());
        AmbientLight ambientLight = new AmbientLight(ambientLightLocation, ambientLightColor, c1, c2, c3);
        scene.setAmbientLight(ambientLight);

        RayTracer rayTracer = new RayTracer(scene);

        // addAdditionalStuffForTest01(); !!!IMPORTANT!!! For testing purposes this is not commented out!!!

        String filename = outFileNameTextField.getText();

        String fileExtension = filename.substring(filename.lastIndexOf(".")+1);

        rayTracer.drawTo(filename, fileExtension);

        rayTracer.openDrawnFile();
    }

    private void addAdditionalStuffForTest01() {
        // Added to recreate additional finishes and pigments necessary for additional spheres.
        // Also adds light source which is the second of the two in the text01 file.

        cs3318.group17.raytracer.math.Point lightLocation = new cs3318.group17.raytracer.math.Point(100F, 100F, 100F);
        Color lightColor = new Color(ColorUtil.clamp(1), ColorUtil.clamp(1), ColorUtil.clamp(1));

        Light light01 = new Light(lightLocation, lightColor, 1, 0, 0);
        scene.addLight(light01);
        System.out.println(scene.getLightsArray());

        Color color01 = new Color(ColorUtil.clamp(0), ColorUtil.clamp(1), ColorUtil.clamp(0));
        Color color02 = new Color(ColorUtil.clamp(0), ColorUtil.clamp(0), ColorUtil.clamp(1));

        SolidPigment pigment01 = new SolidPigment(color01);
        SolidPigment pigment02 = new SolidPigment(color02);

        Finish finish00 = new Finish(0.4F, 0.6F, 0.0F, 1F, 0F, 0F, 0F);
        Finish finish01 = new Finish(0.4F, 0.6F, 0.7F, 500F, 0F, 0F, 0F);


        cs3318.group17.raytracer.math.Point sphereCenter01 = new cs3318.group17.raytracer.math.Point(1, 0, -15);
        int radius01 = 2;
        Sphere sphere01 = new Sphere(pigment01, finish00, sphereCenter01, radius01);
        scene.addShape(sphere01);

        cs3318.group17.raytracer.math.Point sphereCenter02 = new cs3318.group17.raytracer.math.Point(5, -5, -25);
        int radius02 = 3;
        Sphere sphere02 = new Sphere(pigment02, finish01, sphereCenter02, radius02);
        scene.addShape(sphere02);

        cs3318.group17.raytracer.math.Point sphereCenter03 = new cs3318.group17.raytracer.math.Point(-5, 0, -30);
        int radius03 = 4;
        Sphere sphere03 = new Sphere(pigment02, finish01, sphereCenter03, radius03);
        scene.addShape(sphere03);

    }
}
