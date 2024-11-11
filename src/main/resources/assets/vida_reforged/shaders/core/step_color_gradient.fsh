#version 150

uniform vec2 iResolution;
uniform float angle;
uniform int num_stops;
uniform vec4 color0;
uniform vec4 color1;
uniform vec4 color2;
uniform float stop0;
uniform float stop1;
uniform float stop2;
out vec4 fragColor;

void mainImage(in vec2 fragCoord){
    // these should be uniforms
    vec2 gradient_start_pos = vec2(0.0, 0.0); // top-left
    vec2 gradient_end_pos = vec2(1.0, 1.0); // bottom-right

    // define colors and stops
    float stops[32];
    vec4 colors[32];
    stops[0] = stop0;
    stops[1] = stop1;
    stops[2] = stop2;
    colors[0] = color0;
    colors[1] = color1;
    colors[2] = color2;


    vec2 uv = (fragCoord.xy / iResolution.xy);
    uv.y = (uv.y - 1.0) * -1.0;


    float alpha = radians(angle); // this is the angle of the gradient in rad

    float gradient_startpos_rotated_x = gradient_start_pos.x * cos(-alpha) - gradient_start_pos.y * sin(-alpha);
    float gradient_endpos_rotated_x = gradient_end_pos.x * cos(-alpha) - gradient_end_pos.y * sin(-alpha);
    float len = gradient_endpos_rotated_x - gradient_startpos_rotated_x;
    float x_loc_rotated = uv.x * cos(-alpha) - uv.y * sin(-alpha);

    if (num_stops == 1) {
        fragColor = colors[0];
    }

    if (num_stops > 1) {
        fragColor = mix(
        colors[0],
        colors[1],
        smoothstep(
        gradient_startpos_rotated_x + stops[0] * len,
        gradient_startpos_rotated_x + stops[1] * len,
        x_loc_rotated
        )
        );
        for (int i = 1; i < 3 - 1; i++) {
            fragColor = mix(
            fragColor,
            colors[i + 1], smoothstep(
            gradient_startpos_rotated_x + stops[i] * len,
            gradient_startpos_rotated_x + stops[i + 1] * len,
            x_loc_rotated
            )
            );
        }
    }
}


void main() {
    mainImage(gl_FragCoord.xy);
}