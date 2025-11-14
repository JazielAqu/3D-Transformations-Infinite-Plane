#version 300 es
precision highp float;

in vec4 vertexPosition;   // location 0 
in vec3 vertexNormal;     // location 1
in vec4 vertexTexCoord;   // location 2

uniform mat4 modelMatrix;
uniform mat4 viewProjMatrix;

out vec3 vNormal;
out vec4 vTex;

void main() {
  gl_Position = viewProjMatrix * modelMatrix * vertexPosition;
  // normal as direction (w=0) so translation doesn't affect
  vNormal = (modelMatrix * vec4(vertexNormal, 0.0)).xyz;
  vTex = vertexTexCoord;
}
