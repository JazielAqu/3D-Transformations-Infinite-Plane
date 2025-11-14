#version 300 es
precision highp float;

in vec3 vNormal;
in vec4 vTex;

uniform sampler2D colorTexture; // optional; can be unset for procedural

out vec4 fragColor;

void main() {
  // Homogeneous division: spec says to use tex.xy / tex.w
  vec2 uv = vTex.xy / vTex.w;

  // ----- Option A: procedural checker (no texture needed) -----
  // scale controls checker size; tweak as desired
  float scale = 0.25;
  vec2 p = uv * scale;
  float c = mod(floor(p.x) + floor(p.y), 2.0);
  vec3 base = mix(vec3(0.85), vec3(0.25), c);

  // ----- Option B: if you bind a repeating texture, uncomment:
  // vec2 tiled = fract(uv * 0.25);        // tile every 4 world units
  // vec3 base = texture(colorTexture, tiled).rgb;

  // Simple facing tweak so it's not pitch black if you later add lights
  vec3 n = normalize(vNormal);
  float ndotl = clamp(0.4 + 0.6 * n.y, 0.0, 1.0);
  fragColor = vec4(0.2, 0.8, 0.2, 1.0);
}
