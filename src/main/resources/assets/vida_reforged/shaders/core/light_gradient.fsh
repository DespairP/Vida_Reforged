#version 150

uniform vec4 backgroundColor;
uniform vec4 frontColor;
uniform vec4 frontColor2;
uniform vec2 pos;
uniform vec2 iResolution;


out vec4 fragColor;

void mainImage( in vec2 fragCoord ) {
    // Scale UV coords to account for rectangular window
    vec2 tapPoint = vec2(pos.x,pos.y);
    vec2 uv = vec2(fragCoord.x - (0.5 * tapPoint.x) * iResolution.x, fragCoord.y - (tapPoint.y * 10.0)) / iResolution.y;
    vec2 uv2 = vec2(fragCoord.x - (0.25 * tapPoint.x) * iResolution.x, fragCoord.y + tapPoint.y) / iResolution.y;
    float aspect = iResolution.y / iResolution.x;

    uv = 2.0 * uv - 0.5;
    uv2 = 2.0 * uv2 - 0.5;

    // Double the speed
    float wave = sin(2.0);
    //float wave = sin(0.6);

    // Scale to make the circle bigger so it reaches the far edges
    float circle = (uv.x * uv.x + uv.y * uv.y) * 0.7;

    float circle2 = (uv2.x * uv2.x + uv2.y * uv2.y) * 0.7;
    vec2 distVec = uv.xy - pos.xy;
    distVec.x *= aspect;
    float distance = length(distVec);


    vec4 color1 = vec4(1.0, 0.0, 0.0, 1.0); // Red
    vec4 color2 = vec4(0.0, 0.0, 1.0, 1.0); // Blue
    vec4 color3 = vec4(0.0, 1.0, 0.0, 1.0); // Green
    vec4 transparent = vec4(0.0, 0.0, 0.0, 0.0); // Blue

    vec4 gradient1 = mix(frontColor, transparent, circle);
    vec4 gradient2 = mix(frontColor2, transparent, circle2 + 0.1);
    //vec4 gradient2 = mix(color3, gradient1, circle2);
    vec4 gradientMix = mix(gradient2, gradient1, 0.6);
    // Lerp the two
    fragColor = mix(gradientMix, backgroundColor, 0.5);
}

void main() {
    mainImage(gl_FragCoord.xy);
}