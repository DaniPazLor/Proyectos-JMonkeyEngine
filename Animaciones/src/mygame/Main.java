package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


/**
 * Animaciones en jME3
 * @author Ministerio de Educación. Gobierno de España.
 */
public class Main extends SimpleApplication {

    private AnimChannel canal;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Creamos las luces

        // Primero una luz ambiente
        AmbientLight ambiente = new AmbientLight();
        ambiente.setColor(ColorRGBA.White.mult(3f));
        rootNode.addLight(ambiente);

        // Luego una direccional que proviene del frente
        DirectionalLight sol = new DirectionalLight();
        sol.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(sol);

        // Cargamos el modelo Oto
        Node modelo = (Node) assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
        modelo.setLocalScale(0.5f);
        rootNode.attachChild(modelo);

        // Creamos un control de animación asociado al modelo
        AnimControl control = modelo.getControl(AnimControl.class);

        // Creamos un canal de animación y le decimos que queremos reproducir
        // la animación "stand" (de pie)
        canal = control.createChannel();
        canal.setAnim("stand");

        // Configuramos una acción que se lanzará al pulsar la barra espaciadora
        inputManager.addMapping("Andar", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Andar");

    }

    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            // Si acabamos de pulsar la tecla, queremos cambiar la animación
            // a "andar". Si la hemos soltado, la cambiaremos a "de pie"
            if (keyPressed) {
                if (!canal.getAnimationName().equals("Walk")) {
                    canal.setAnim("Walk", 0.50f);
                    canal.setLoopMode(LoopMode.Loop); // repetir automáticamente
                }
            }
            else {
                if (!canal.getAnimationName().equals("stand")) {
                    canal.setAnim("stand", 0.50f);
                    canal.setLoopMode(LoopMode.DontLoop); // no repetir
                }
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}