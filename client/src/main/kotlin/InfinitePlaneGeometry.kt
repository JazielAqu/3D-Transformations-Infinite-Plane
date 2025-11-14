import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLVertexArrayObject
import org.khronos.webgl.WebGL2RenderingContext as GL
import vision.gears.webglmath.Geometry

class InfinitePlaneGeometry(private val gl: GL) : Geometry() {

  private val vao: WebGLVertexArrayObject = gl.createVertexArray()!!
  private val posBuffer: WebGLBuffer = gl.createBuffer()!!
  private val nrmBuffer: WebGLBuffer = gl.createBuffer()!!
  private val uvwqBuffer: WebGLBuffer = gl.createBuffer()!!
  private val idxBuffer: WebGLBuffer = gl.createBuffer()!!

  init {
    // --- Positions: vec4 per vertex (x,y,z,w) ---
    // v0: origin (finite), v1/v2/v3: ideal points (w=0)
    val positions = Float32Array(floatArrayOf(
      0f, 0f,  0f, 1f,   // v0 origin (w=1)
      1f, 0f,  0f, 0f,   // v1 +X  (w=0)
     -1f, 0f,  0f, 0f,   // v2 -X  (w=0)
      0f, 0f, -1f, 0f    // v3 -Z  (w=0)
    ))

    // --- Normals: vec3 (all up) ---
    val normals = Float32Array(floatArrayOf(
      0f, 1f, 0f,
      0f, 1f, 0f,
      0f, 1f, 0f,
      0f, 1f, 0f
    ))

    // --- Homogeneous texcoords: vec4 (u,v,unused,q)
    // Spec says: "values may be the same as vertex positions, but swap y and z for a horizontal plane"
    // Here y=0 anyway; we map (u,v) := (x,z), keep 3rd=0, q := w.
    val uvwq = Float32Array(floatArrayOf(
      0f,  0f, 0f, 1f,   // v0 -> (u=0, v=0, _, q=1)
      1f,  0f, 0f, 0f,   // v1 -> (1, 0, 0, 0)
     -1f,  0f, 0f, 0f,   // v2 -> (-1, 0, 0, 0)
      0f, -1f, 0f, 0f    // v3 -> (0, -1, 0, 0)
    ))

    // --- Indices: 3 triangles in a fan around v0 ---
    val indices = Uint16Array(shortArrayOf(
      0, 1, 3,   // v0-v1-v3
      0, 3, 2,   // v0-v3-v2
      0, 2, 1    // v0-v2-v1
    ))

    // Upload buffers
    gl.bindVertexArray(vao)

    gl.bindBuffer(GL.ARRAY_BUFFER, posBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, positions, GL.STATIC_DRAW)
    // attribute 0 = vertexPosition (vec4)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, 4, GL.FLOAT, false, 0, 0)

    gl.bindBuffer(GL.ARRAY_BUFFER, nrmBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, normals, GL.STATIC_DRAW)
    // attribute 1 = vertexNormal (vec3)
    gl.enableVertexAttribArray(1)
    gl.vertexAttribPointer(1, 3, GL.FLOAT, false, 0, 0)

    gl.bindBuffer(GL.ARRAY_BUFFER, uvwqBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, uvwq, GL.STATIC_DRAW)
    // attribute 2 = vertexTexCoord (vec4)
    gl.enableVertexAttribArray(2)
    gl.vertexAttribPointer(2, 4, GL.FLOAT, false, 0, 0)

    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, idxBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER, indices, GL.STATIC_DRAW)

    gl.bindVertexArray(null)
  }

  override fun draw() {
    gl.bindVertexArray(vao)
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, idxBuffer)
    gl.drawElements(GL.TRIANGLES, 9, GL.UNSIGNED_SHORT, 0)
    gl.bindVertexArray(null)
  }
}
