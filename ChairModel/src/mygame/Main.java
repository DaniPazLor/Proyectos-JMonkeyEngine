package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * test
 * @author Ministerio de Educación. Gobierno de España.
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        Spatial silla = assetManager.loadModel("Models/Chair/chair/chair.j3o");
        silla.scale(0.5f);
        rootNode.attachChild(silla);
       
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient); 
        
        
DirectionalLight sol = new DirectionalLight();

sol.setColor(ColorRGBA.White);

sol.setDirection(new Vector3f(-1f,-1f,-1f).normalizeLocal());

rootNode.addLight(sol);



    }
    
    
    @Override
    public void simpleUpdate(float tpf) {
       
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}