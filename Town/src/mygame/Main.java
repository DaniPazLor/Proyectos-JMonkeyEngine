package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication
        implements ActionListener {

    private Spatial escena;
    private BulletAppState bulletAppState;
    private RigidBodyControl suelo;
    private CharacterControl jugador;
    private Vector3f direccionMovimiento = new Vector3f();
    private boolean izquierda = false, derecha = false, arriba = false, abajo = false;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public void simpleInitApp() {
        
        // Preparar el motor de físicas
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        // Descomenta la siguiente línea para ver cómo calcula el motor
        // las colisiones
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        // Ponemos un color de fondo azul cielo
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        
        // Establecemos la velocidad de movimiento de la cámara
        flyCam.setMoveSpeed(100);
        
        // Inicializamos el teclado y la iluminación
        prepararTeclas();
        prepararIluminacion();

        // Cargamos el mapa de la ciudad y lo escalamos a un 200%
        assetManager.registerLocator("town.zip", ZipLocator.class);
//        assetManager.registerLocator("town.zip", ZipLocator.class.getName());
        escena = assetManager.loadModel("main.scene");
        escena.setLocalScale(2f);

        // Preparamos la detección de colisiones creando una forma a medida
        // a partir de la escena y hacemos un RigidBodyControl estático de
        // masa cero
        CollisionShape formaEscena =
                CollisionShapeFactory.createMeshShape((Node) escena);
        suelo = new RigidBodyControl(formaEscena, 0);
        escena.addControl(suelo);

        // Y preparamos la detección de colisiones del jugador creando una
        // forma de colisión en cápsula y asociándole un objeto de control
        // CharacterControl. Realizamos ajustes extras como tamaño,
        // salto de escalón, saltos, caidas, gravedad, etc.
        CapsuleCollisionShape capsuleShape =
                new CapsuleCollisionShape(1.5f, 6f, 1);
        jugador = new CharacterControl(capsuleShape, 0.05f);
        jugador.setJumpSpeed(20);
        jugador.setFallSpeed(30);
        jugador.setGravity(30);
        
        // Colocamos a la entidad del motor de físicas del jugador en su
        // posición inicial
        jugador.setPhysicsLocation(new Vector3f(0, 10, 0));

        // Incorporamos la escena al grafo para que se pueda ver y al
        // suelo y al jugador al motor de físicas.
        rootNode.attachChild(escena);
        bulletAppState.getPhysicsSpace().add(suelo);
        bulletAppState.getPhysicsSpace().add(jugador);
    }

    private void prepararIluminacion() {
        // Añadimos una luz ambiental
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        // Y una luz direccional
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    /** Modificamos los mappings por defecto por los nuestros */
    private void prepararTeclas() {
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Arriba", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Abajo", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Saltar", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Izquierda");
        inputManager.addListener(this, "Derecha");
        inputManager.addListener(this, "Arriba");
        inputManager.addListener(this, "Abajo");
        inputManager.addListener(this, "Saltar");
    }

    /** Lo único que hacemos aquí es marcar qué teclas están pulsadas,
     *  luego las procesaremos. */
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Izquierda")) {
            izquierda = value;
        } else if (binding.equals("Derecha")) {
            derecha = value;
        } else if (binding.equals("Arriba")) {
            arriba = value;
        } else if (binding.equals("Abajo")) {
            abajo = value;
        } else if (binding.equals("Saltar")) {
            jugador.jump();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Obtenemos la dirección de la cámara
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        
        // Obtenemos la dirección de la cámara si se moviera hacia la izquierda
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        
        // En principio no nos movemos
        direccionMovimiento.set(0, 0, 0);
        
        // Según las teclas pulsadas sumamos o restamos las direcciones
        // obtenidas antes a la dirección inicial
        if (izquierda) {
            direccionMovimiento.addLocal(camLeft);
        }
        if (derecha) {
            direccionMovimiento.addLocal(camLeft.negate());
        }
        if (arriba) {
            direccionMovimiento.addLocal(camDir);
        }
        if (abajo) {
            direccionMovimiento.addLocal(camDir.negate());
        }
        
        // Hacemos que el jugador ande
        jugador.setWalkDirection(direccionMovimiento);
        
        // Movemos la cámara para que nos siga
        cam.setLocation(jugador.getPhysicsLocation());
    }
}
