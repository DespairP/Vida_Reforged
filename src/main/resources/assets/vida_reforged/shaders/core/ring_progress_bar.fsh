#version 150

#define RADIUS 0.4
#define AA (1.0 / iResolution.y)
#define PI 3.14159265359

uniform vec2 iResolution;
uniform float thickness;
uniform vec4 fromColor;
uniform vec4 toColor;
uniform float progress;
out vec4 fragColor;

float DrawCircle(vec2 uv, vec2 p, float r){
    uv += p;
    return smoothstep(r+AA, r-AA, length(uv));
}

vec2 RotationUV(vec2 uv, float d){
    float s = sin(d);
    float c = cos(d);
    mat2 rotMatrix = mat2(c, -s, s, c);
    return uv * rotMatrix;
}

void mainImage(in vec2 fragCoord) {
    vec2 uv = (fragCoord - 0.5 * iResolution.xy) / iResolution.y;
    float circle = smoothstep(RADIUS+AA, RADIUS-AA, abs(length(uv) - RADIUS) + RADIUS - thickness);
    float radial = (atan(uv.x, -uv.y) / 3.14159265359 * 0.5) + 0.5;
    float time = progress / 100.0;
    float angle = time * 2.0 * PI;
    radial += time;
    circle *= step(fract(radial), fract(time));

    circle = max(circle, DrawCircle(uv, vec2(0, -RADIUS), thickness));
    circle = max(circle, DrawCircle(RotationUV(uv, angle), vec2(0, -RADIUS), thickness));

    vec4 color = vec4(circle, circle, circle, 1.0);
    fragColor = color == vec4(0.0, 0.0, 0.0, 1.0) ? vec4(0.0, 0.0, 0.0, 0.0) : mix(circle * fromColor, circle * toColor, smoothstep(0.0, 0.4, (uv * mat2(cos(-5.0), -sin(-5.0), sin(-5.0), cos(-0.0))).y));
}

void main() {
    mainImage(gl_FragCoord.xy);
}