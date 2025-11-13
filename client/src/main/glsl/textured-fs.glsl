#version 300 es

precision highp float;

out vec4 fragmentColor;

in vec2 texCoord; // pass this on from vertex shader
in vec3 normal;
in vec3 eye;
in vec3 modelPosition;
in vec3 worldPosition;

vec3 noiseGrad(vec3 r) {
  uvec3 s = uvec3(
    0x1D4E1D4E,
    0x58F958F9,
    0x129F129F);
  vec3 f = vec3(0, 0, 0);
  for(int i=0; i<16; i++) {
    vec3 sf =
    vec3(s & uvec3(0xFFFF))
  / 65536.0 - vec3(0.5, 0.5, 0.5);

    f += cos(dot(sf, r)) * sf;
    s = s >> 1;
  }
  return f;
}

// we need to bind texture to this
uniform struct{
  samplerCube colorTexture;
} material;

void main(void) {
  //fragmentColor = texture(material.colorTexture, texCoord);
  //fragmentColor = vec4(normal, 1.0);
  //fragmentColor = vec4(worldPosition, 1.0);
  //vec3 d = normalize(worldPosition-eye);
  //fragmentColor = vec4(reflect(d, (normal)), 1.0);
  //fragmentColor = texture(material.colorTexture, reflect(d, (normal)));
  //fragmentColor = texture(material.colorTexture, normal);
    vec3 noise = noiseGrad(modelPosition.xyz * 50.0) * 0.05;
    vec3 normalPertubed = normal + noise;
    vec3 viewDir = normalize(eye - worldPosition.xyz);
    fragmentColor = texture(material.colorTexture, reflect(-viewDir, normalPertubed));
}

