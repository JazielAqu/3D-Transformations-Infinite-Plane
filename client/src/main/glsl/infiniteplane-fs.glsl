#version 300 es
precision highp float;

in vec3 vNormal;
in vec4 vTex;
out vec4 fragColor;

// optional for later tiling texture
uniform sampler2D colorTexture;

void main() {
  // robust divide: avoid q=0 exactly at horizon
  float q = max(abs(vTex.w), 1e-6);
  vec2 uv = vTex.xy / q;

  // procedural checker so no external texture required
  float scale = 0.25;         // larger -> smaller checks
  vec2 cell = floor(uv * scale);
  float c = mod(cell.x + cell.y, 2.0);
  vec3 base = mix(vec3(0.85), vec3(0.25), c);

  // gentle "lighting" so it's not flat: bias by Y-up normal
  vec3 n = normalize(vNormal);
  float ndotl = clamp(0.4 + 0.6 * n.y, 0.0, 1.0);

  fragColor = vec4(base * ndotl, 1.0);
}
