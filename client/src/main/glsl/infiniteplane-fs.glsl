#version 300 es
precision highp float;

in vec3 vNormal;
in vec4 vTex;
out vec4 fragColor;

// If you later bind a texture, keep this uniform here
uniform sampler2D colorTexture;

void main() {
  // Robust divide: avoid q=0 at the exact horizon
  float q = max(abs(vTex.w), 1e-6);
  vec2 uv = vTex.xy / q;

  // Procedural checker so we don't depend on any image
  float scale = 0.25;          // bigger -> smaller checks
  vec2 p = uv * scale;

  // Avoid weirdness with negative huge values: floor first, then mod on small ints
  vec2 f = floor(p);
  float c = mod(f.x + f.y, 2.0);

  vec3 base = mix(vec3(0.85), vec3(0.25), c);

  // Gentle lambert-ish boost so it's not flat black if lighting gets added later
  vec3 n = normalize(vNormal);
  float ndotl = clamp(0.4 + 0.6 * n.y, 0.0, 1.0);

  fragColor = vec4(base * ndotl, 1.0);
}
