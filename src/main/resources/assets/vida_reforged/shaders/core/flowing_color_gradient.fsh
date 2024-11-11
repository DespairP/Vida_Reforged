// Created by inigo quilez - iq/2014
// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
#version 150
uniform vec4 color0;
uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform float iTime;
uniform vec2 iResolution;
out vec4 fragColor;

mat2 Rot(float a)
{
    float s = sin(a);
    float c = cos(a);
    return mat2(c, -s, s, c);
}

vec2 hash(vec2 p)
{
    p = vec2(dot(p, vec2(2127.1, 81.17)), dot(p, vec2(1269.5, 283.37)));
    return fract(sin(p)*43758.5453);
}

float noise(in vec2 p)
{
    vec2 i = floor(p);
    vec2 f = fract(p);

    vec2 u = f*f*(3.0-2.0*f);

    float n = mix(mix(dot(-1.0+2.0*hash(i + vec2(0.0, 0.0)), f - vec2(0.0, 0.0)),
    dot(-1.0+2.0*hash(i + vec2(1.0, 0.0)), f - vec2(1.0, 0.0)), u.x),
    mix(dot(-1.0+2.0*hash(i + vec2(0.0, 1.0)), f - vec2(0.0, 1.0)),
    dot(-1.0+2.0*hash(i + vec2(1.0, 1.0)), f - vec2(1.0, 1.0)), u.x), u.y);
    return 0.5 + 0.5*n;
}


void mainImage(in vec2 fragCoord)
{
    vec2 uv = fragCoord/iResolution.xy;
    float ratio = iResolution.x / iResolution.y;

    vec2 tuv = uv;
    tuv -= .5;

    // rotate with Noise
    float degree = noise(vec2(iTime*.1, tuv.x*tuv.y));

    tuv.y *= 1./ratio;
    tuv *= Rot(radians((degree-.5)*720.+180.));
    tuv.y *= ratio;


    // Wave warp with sin
    float frequency = 5.;
    float amplitude = 30.;
    float speed = iTime * 2.;
    tuv.x += sin(tuv.y*frequency+speed)/amplitude;
    tuv.y += sin(tuv.x*frequency*1.5+speed)/(amplitude*.5);

    // draw the image
    vec3 layer1 = mix(color0.xyz, color1.xyz, smoothstep(-.3, .2, (tuv*Rot(radians(-5.))).x));
    vec3 layer2 = mix(color2.xyz, color3.xyz, smoothstep(-.3, .2, (tuv*Rot(radians(-5.))).x));

    fragColor = vec4(mix(layer1, layer2, smoothstep(.5, -.3, tuv.y)), color0.w);
}

void main() {
    mainImage(gl_FragCoord.xy);
}