@file:Suppress("UnsafeCastFromDynamic")

import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.*

// Attributes per Program.PNT: 0=position, 1=normal, 2=texcoord
class InfinitePlaneGeometry(private val gl: WebGL2RenderingContext) : Geometry() {

  private val posBuffer: WebGLBuffer = gl.createBuffer()!!
  private val nrmBuffer: WebGLBuffer = gl.createBuffer()!!
  private val uvwqBuffer: WebGLBuffer = gl.createBuffer()!!
  private val idxBuffer: WebGLBuffer = gl.createBuffer()!!

  init {
    // Positions: vec4 (x, y, z, w)
    val positions = Float32Array(arrayOf(
      0f, 0f,  0f, 1f,   // v0 origin (finite)
      1f, 0f,  0f, 0f,   // v1 +X at infinity
     -1f, 0f,  0f, 0f,   // v2 -X at infinity
      0f, 0f, -1f, 0f    // v3 -Z at infinity
    ))

    // Normals: vec3 (all up)
    val normals = Float32Array(arrayOf(
      0f, 1f, 0f,
      0f, 1f, 0f,
      0f, 1f, 0f,
      0f, 1f, 0f
    ))

    // Homogeneous texcoords: vec4 (u, v, unused, q); (u,v) = (x,z), q=w
    val uvwq = Float32Array(arrayOf(
      0f,  0f, 0f, 1f,   // v0
      1f,  0f, 0f, 0f,   // v1
     -1f,  0f, 0f, 0f,   // v2
      0f, -1f, 0f, 0f    // v3
    ))

    // Indices: 3 triangles fan around v0
    val indices = Uint16Array(arrayOf<Short>(
      0, 1, 3,
      0, 3, 2,
      0, 2, 1
    ))

    // Upload buffers
    gl.bindBuffer(GL.ARRAY_BUFFER, posBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, positions, GL.STATIC_DRAW)

    gl.bindBuffer(GL.ARRAY_BUFFER, nrmBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, normals, GL.STATIC_DRAW)

    gl.bindBuffer(GL.ARRAY_BUFFER, uvwqBuffer)
    gl.bufferData(GL.ARRAY_BUFFER, uvwq, GL.STATIC_DRAW)

    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, idxBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER, indices, GL.STATIC_DRAW)
  }

  override fun draw() {
    // pos -> attr 0 (vec4)
    gl.bindBuffer(GL.ARRAY_BUFFER, posBuffer)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0, 4, GL.FLOAT, false, 0, 0)

    // normal -> attr 1 (vec3)
    gl.bindBuffer(GL.ARRAY_BUFFER, nrmBuffer)
    gl.enableVertexAttribArray(1)
    gl.vertexAttribPointer(1, 3, GL.FLOAT, false, 0, 0)

    // texcoord -> attr 2 (vec4)
    gl.bindBuffer(GL.ARRAY_BUFFER, uvwqBuffer)
    gl.enableVertexAttribArray(2)
    gl.vertexAttribPointer(2, 4, GL.FLOAT, false, 0, 0)

    // draw
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, idxBuffer)
    gl.drawElements(GL.TRIANGLES, 9, GL.UNSIGNED_SHORT, 0)
  }
}
