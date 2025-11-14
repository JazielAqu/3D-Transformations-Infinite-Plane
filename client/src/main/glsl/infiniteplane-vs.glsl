#version 300 es
precision highp float;

// Attribute locations must match Program.PNT:
// 0 -> vertexPosition, 1 -> vertexNormal, 2 -> vertexTexCoord
in vec4 vertexPosition;
in vec3 vertexNormal;
in vec4 vertexTexCoord;

// Your engine expects struct uniforms like this:
struct Camera {
  mat4 viewProjMatrix;
};
uniform Camera camera;

struct GameObject {
  mat4 modelMatrix;
};
uniform GameObject gameObject;

out vec3 vNormal;
out vec4 vTex;

void main() {
  // Use the struct uniforms instead of bare names
  gl_Position = camera.viewProjMatrix * gameObject.modelMatrix * vertexPosition;

  // Treat normal as a direction (w=0) so it ignores translation
  vNormal = (gameObject.modelMatrix * vec4(vertexNormal, 0.0)).xyz;

  // Pass homogeneous texcoord through
  vTex = vertexTexCoord;
}
