package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.input.*;
import com.jme3.input.controls.*;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private Geometry geom2; 
    private Node n;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        
        Sphere s = new Sphere(64, 64, 1.0f);
        Geometry geom2 = new Geometry("Esfera", s);
        geom2.setLocalTranslation(0, 2.5f, 0);
        geom2.setMaterial(mat);
        
        n = new Node("Pivote");
        n.attachChild(geom);
        n.attachChild(geom2);
        
        rootNode.attachChild(n);
        
        inputManager.addMapping("Rotar",
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addListener(analogListener, new String[]{"Rotar"});
        
    }
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
          if (name.equals("Rotar")) {
            n.rotate(0, 2*value*speed, 0);
          }
        }
    };
    
    @Override
    public void simpleUpdate(float tpf) {
        //n.rotate(0, 2*tpf, 0);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}