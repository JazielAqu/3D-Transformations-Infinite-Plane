#version 300 es
precision highp float;

// Program.PNT expects these exact names/locations:
// 0 -> vertexPosition, 1 -> vertexNormal, 2 -> vertexTexCoord
in vec4 vertexPosition;
in vec3 vertexNormal;
in vec4 vertexTexCoord;

// Match your engine's struct uniforms
struct Camera { mat4 viewProjMatrix; };
uniform Camera camera;

struct GameObject { mat4 modelMatrix; };
uniform GameObject gameObject;

out vec3 vNormal;
out vec4 vTex;

void main() {
  gl_Position = camera.viewProjMatrix * gameObject.modelMatrix * vertexPosition;
  vNormal = (gameObject.modelMatrix * vec4(vertexNormal, 0.0)).xyz;
  vTex = vertexTexCoord;
}
