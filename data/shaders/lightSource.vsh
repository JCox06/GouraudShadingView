#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTex;


out vec2 fTexelCoord;

uniform mat4 camMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;


void main() {
    gl_Position = projMatrix * camMatrix * modelMatrix * vec4(aPos, 1.0f);
    fTexelCoord = aTex;
}