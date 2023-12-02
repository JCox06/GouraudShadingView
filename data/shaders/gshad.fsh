#version 330 core


out vec4 FragColour;

in vec2 fTexelCoord;
in vec3 fVertexColour;


uniform sampler2D main2D;



void main() {
    FragColour = texture(main2D, fTexelCoord) * vec4(fVertexColour, 1.0f);
}