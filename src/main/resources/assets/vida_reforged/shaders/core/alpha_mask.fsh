#version 150
uniform sampler2D maskTex;
uniform sampler2D colorTex;

out vec4 colorOut;
in smooth vec2 texCoords;
void main(){

    vec4 color = texture(colorTex,texCoords);
    vec4 mask  = texture(maskTex,texCoords);

    colorOut = vec4(color.rgb,color.a * mask.r);//alpha value can be in any channel ,depends on texture format.
}