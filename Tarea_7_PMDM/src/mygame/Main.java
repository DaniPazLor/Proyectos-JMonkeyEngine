package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * Bolos
 *
 * @author Ministerio de Educación. Gobierno de España.
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private BulletAppState bulletAppState;
    private Material bola_mat, piedras_mat;
    private RigidBodyControl bola_fis;

    private float coordX = -1.5f;
    private float coordY = 1.5f;
    private float coordZ = -8f;

    @Override
    public void simpleInitApp() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Crear materiales
        crearMateriales();

        // Creamos el suelo
        crearSuelo();

        //Creamos bolos
        for (int i = 0; i < 4; i++) {
            crearBolos(coordX, coordY, coordZ);
            coordX += 1.3f;

        }
        coordX = -1f;
        coordZ += 1;
        for (int j = 0; j < 3; j++) {
            crearBolos(coordX, coordY, coordZ);
            coordX += 1.3f;

        }
        coordX = -0.5f;
        coordZ += 1;
        for (int j = 0; j < 2; j++) {
            crearBolos(coordX, coordY, coordZ);
            coordX += 1.3f;
        }
        crearBolos(0f, coordY, coordZ + 1);

        // Creamos la pared
        crearPared();

        // Crear la luz
        crearLuz();

        // Colocamos la cámara en una posición adecuada para ver la superficie
        // del suelo y mirando hacia ella.
        cam.setLocation(new Vector3f(0, 4f, 6f));
        cam.lookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);

        // Ponemos un color de fondo azul oscuro
        viewPort.setBackgroundColor(new ColorRGBA(0f, 0f, 0.2f, 0));

          //Añadimos controles
        inputManager.addMapping("CrearBola",
//                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, new String[]{"CrearBola"});
        
       

    }
    
     private ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean pulsado, float tpf) {
            
//            if (name.equals("CrearBola")) {
            if (pulsado) {
              // ¡Bola va!
        hazBola();

            }
        }



    };

    private void crearSuelo() {

        // Para hacer el suelo usaremos estas variables:
        // suelo    : la forma del suelo
        // suelo_mat: el material del suelo
        // suelo_geo: el spatial de tipo geometría que contendrá el suelo
        // suelo_fis: el objeto de control de físicas que está asociado al suelo
        // Creamos una forma de caja de 5 metros de ancho,
        // 10 de largo y 10cm de alto.
        Box suelo = new Box(Vector3f.ZERO, 5f, 0.1f, 10f);

        // ajustamos el tamaño de la textura que le dará aspecto de suelo
        // para que no se deforme
        suelo.scaleTextureCoordinates(new Vector2f(6, 3));

        // Creamos un spatial de tipo geometría para asociarlo al suelo
        Geometry suelo_geo = new Geometry("Floor", suelo);
        // asignamos el material
        suelo_geo.setMaterial(piedras_mat);
        // bajamos el suelo 10cm para que el origen esté un poco por encima
        suelo_geo.setLocalTranslation(0, -0.1f, 0);
        // y, finalmente, lo incluimos en el grafo de escena
        rootNode.attachChild(suelo_geo);

        // Crearemos un objeto de control físico para asociarlo al suelo
        // IMPORTANTE: tiene masa 0 para convertirlo en un objeto estático
        RigidBodyControl suelo_fis = new RigidBodyControl(0.0f);
        // asociamos el objeto de control a la geometría del suelo
        suelo_geo.addControl(suelo_fis);
        // y añadimos el objeto de control al motor de físicas
        bulletAppState.getPhysicsSpace().add(suelo_fis);

    }

    private void crearBolos(float x, float y, float z) {
        Node n = new Node("nodoBolo");

        Spatial bolo = assetManager.loadModel("Models/Pin.obj");

        bolo.scale(0.2f);
        bolo.setLocalTranslation(x, y, z);
        n.attachChild(bolo);
        rootNode.attachChild(n);
        
        // Creamos el objeto de control físico asociado a la bola con un peso
        // de 1Kg.
        RigidBodyControl bolo_fis = new RigidBodyControl(1.5f);
        // Asociar la geometría de la bola al control físico
        bolo.addControl(bolo_fis);
        // Añadirla al motor de física
        bulletAppState.getPhysicsSpace().add(bolo_fis);
    }

    private void crearPared() {

        // Para hacer la pared usaremos estas variables:
        // pared    : la forma de la pared
        // pared_geo: el spatial de tipo geometría que contendrá la pared
        // pared_fis: el objeto de control de físicas que está asociado 
        // Creamos una forma de caja de 5 metros de ancho,
        // 10 de largo y 10cm de alto.
        Box pared = new Box(Vector3f.ZERO, 5f, 0.1f, 10f);

        // Ajustamos el tamaño de la textura que le dará el aspecto deseado
        // para que no se deforme
        pared.scaleTextureCoordinates(new Vector2f(6, 3));

        // Creamos un spatial de tipo geometría para asociarlo a la pared
        Geometry pared_geo = new Geometry("Floor", pared);
        // asignamos el material
        pared_geo.setMaterial(piedras_mat);
        // lo desplazamos detrás del suelo e inclinado 90 grados en el eje X
        // para ponerlo en vertical
        pared_geo.setLocalTranslation(0, -0.1f, -10);
        pared_geo.rotate((float) Math.PI / 2.0f, 0f, 0);
        // y, finalmente, lo incluimos en el grafo de escena
        rootNode.attachChild(pared_geo);

        // Crearemos un objeto de control físico para asociarlo a la pared
        // IMPORTANTE: tiene masa 0 para convertirlo en un objeto estático
        RigidBodyControl pared_fis = new RigidBodyControl(0.0f);
        // asociamos el objeto de control a la geometría de la pared
        pared_geo.addControl(pared_fis);
        // y añadimos el objeto de control al motor de físicas
        bulletAppState.getPhysicsSpace().add(pared_fis);
    }

    private void crearLuz() {

        // Crearemos una luz direccional que parezca venir de la parte
        // superior derecha del jugador, por detrás.
        DirectionalLight luz = new DirectionalLight();

        // de color blanco y no excesivamente brillante
        luz.setColor(ColorRGBA.White.mult(0.8f));

        // proveniente de la parte superior derecha de la posición inicial
        // del jugador, por detrás.
        luz.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());

        // añadir la luz al grafo
        rootNode.addLight(luz);
    }

    private void crearMateriales() {
        // Creamos el material de la pared y el suelo a partir de una textura
        // predefinida, haciendo que se repita por su superficie
        piedras_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/wood5.png");
        key.setGenerateMips(true);
        Texture textura = assetManager.loadTexture(key);
        textura.setWrap(WrapMode.Repeat);
        piedras_mat.setTexture("ColorMap", textura);

        // Creamos el material de la bola a partir de una base iluminada y
        // la hacemos de color azul reflectante.
        bola_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        bola_mat.setBoolean("UseMaterialColors", true);
        bola_mat.setColor("Ambient", ColorRGBA.Cyan);
        bola_mat.setColor("Diffuse", ColorRGBA.Cyan);
        bola_mat.setColor("Specular", ColorRGBA.White);
        bola_mat.setFloat("Shininess", 1);

    }

    public void hazBola() {

        // Crear una esfera de 40 centímetros de diámetro
        Sphere esfera = new Sphere(32, 32, 0.4f);

        // Asociar la forma a una geometría nueva
        Geometry bola_geo = new Geometry("bola", esfera);
        // asignarle el material
        bola_geo.setMaterial(bola_mat);

        // añadirla al grafo de escena
        rootNode.attachChild(bola_geo);

        // la colocamos en la posición de la cámara
        bola_geo.setLocalTranslation(cam.getLocation());

        //Añadimos controles
        inputManager.addMapping("Lanzar",
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(analogListener, new String[]{"Lanzar"});

        // Creamos el objeto de control físico asociado a la bola con un peso
        // de 1Kg.
        bola_fis = new RigidBodyControl(1f);
        // Asociar la geometría de la bola al control físico
        bola_geo.addControl(bola_fis);
        // Añadirla al motor de física
        bulletAppState.getPhysicsSpace().add(bola_fis);

////        bola_fis.setLinearVelocity(cam.getDirection().mult(8));
    }

    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {

            if (name.equals("Lanzar")) {
                        // ¡Empujar la bola en la dirección que mira la cámara a una velocidad
        // de 8 metros por segundo!
                bola_fis.setLinearVelocity(cam.getDirection().mult(8));

            }

        }

    };

    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
