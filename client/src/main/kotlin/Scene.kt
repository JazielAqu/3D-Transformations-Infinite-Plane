import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.math.*
import kotlin.js.Date
import vision.gears.webglmath.*

class Scene (
  val gl : WebGL2RenderingContext) {

  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val solidProgram = Program(gl, vsIdle, fsSolid, Program.PC)

  val vsTextured = Shader(gl, GL.VERTEX_SHADER, "textured-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val texturedProgram = Program(gl, vsTextured, fsTextured, Program.PNT)

  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "background-fs.glsl")
  val backgroundProgram = Program(gl, vsQuad, fsBackground)

  val texturedQuadGeometry = TexturedQuadGeometry(gl)

  val envTexture = TextureCube(gl,
    "media/posx512.jpg", "media/negx512.jpg",
    "media/posy512.jpg", "media/negy512.jpg",
    "media/posz512.jpg", "media/negz512.jpg"
  )

  // LABTODO: load geometries from the JSON file, create Meshes
  val jsonLoader = JsonLoader()
  val slowpokeMeshes = jsonLoader.loadMeshes(gl,
    "media/slowpoke/slowpoke.json",
    Material(texturedProgram).apply{
      this["colorTexture"]?.set(
        //Texture2D(gl, "media/slowpoke/YadonDh.png"))
        envTexture)
    },
    Material(texturedProgram).apply{
      this["colorTexture"]?.set(
        //Texture2D(gl, "media/slowpoke/YadonEyeDh.png"))
        envTexture)
    }
  )

  

  val backgroundMaterial = Material(backgroundProgram)
  val backgroundMesh = Mesh(backgroundMaterial, texturedQuadGeometry)
  init{
    backgroundMaterial["envTexture"]?.set( this.envTexture )
  }
  val gameObjects = ArrayList<GameObject>()

  val slowpoke = GameObject(*slowpokeMeshes)

  init {
    // LABTODO: create and add game object using meshes loaded from JSON
    gameObjects += GameObject(backgroundMesh)
    gameObjects += slowpoke
  }

  // LABTODO: replace with 3D camera
  val camera = PerspectiveCamera(*Program.all).apply{
    position.set(1f, 1f)
  }

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  fun resize(canvas : HTMLCanvasElement) {
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>) {
    val timeAtThisFrame = Date().getTime()
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame

    gl.enable(GL.DEPTH_TEST)

    //LABTODO: move camera
    camera.move(dt, keysPressed)

    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.enable(GL.BLEND)
    gl.blendFunc(
      GL.SRC_ALPHA,
      GL.ONE_MINUS_SRC_ALPHA)

    slowpoke.roll += dt;
    slowpoke.pitch += dt / 5.0f;

    for (gameObject in gameObjects) {
      gameObject.move(dt, t, keysPressed, gameObjects)
      gameObject.update()
      gameObject.draw(camera)
    }
  }
}
