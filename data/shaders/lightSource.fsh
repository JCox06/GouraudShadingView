#version 330 core


out vec4 FragColour;

in vec2 fTexelCoord;


uniform sampler2D main2D;



void main() {
    FragColour = texture(main2D, fTexelCoord);
}