#version 300 es

in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec
in vec3 vertexNormal;
in vec2 vertexTexCoord;

out vec2 texCoord;
out vec3 normal;
out vec3 eye;
out vec3 modelPosition;
out vec3 worldPosition;

uniform struct{
  mat4 viewProjMatrix;
  vec3 position;
} camera;

uniform struct{
  mat4 modelMatrix;
  mat4 modelMatrixInv;
} gameObject;

void main(void) {
  texCoord = vertexTexCoord;
  normal = (gameObject.modelMatrixInv * vec4(vertexNormal, 0.0)).xyz;
  eye = camera.position;
  modelPosition = vertexPosition.xyz;
  worldPosition = (vertexPosition * gameObject.modelMatrix).xyz;
  gl_Position = vertexPosition * gameObject.modelMatrix * camera.viewProjMatrix; //#gl_Position# built-in output, required
}