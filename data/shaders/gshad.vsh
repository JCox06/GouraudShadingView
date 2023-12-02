#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTex;


out vec2 fTexelCoord;
out vec3 fVertexColour;

uniform mat4 camMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;


uniform vec3 lightColour;
uniform vec3 objectColour;
uniform vec3 lightPos;
uniform vec3 cameraPos;

vec3 calcAmbient(vec3 colour, float strength);
vec3 calcDiffuse(vec3 colour, vec3 lightDir, vec3 normal);
vec3 calcSpec(vec3 colour, vec3 lightDir, vec3 normal, vec3 view);

void main() {
    //This lighting is being done from viewspace rather than worldspace
    //First step is to get the vertex information in viewspace system
    vec3 vertexViewPos = vec3(camMatrix * modelMatrix * vec4(aPos, 1.0f));
    vec3 viewLightPos = vec3(camMatrix * vec4(lightPos, 1.0f));

    vec3 vertToLight = normalize(viewLightPos - vertexViewPos);

    vec3 normal = mat3(transpose(inverse(camMatrix * modelMatrix))) * aNormal;

    vec3 ambientComponent = calcAmbient(lightColour, 0.1f);
    vec3 diffuseComponent = calcDiffuse(lightColour, vertToLight, normal);
    vec3 specularComponent = calcSpec(lightColour, vertToLight, normal, vertexViewPos);


    gl_Position = projMatrix * camMatrix * modelMatrix * vec4(aPos, 1.0f);
    fTexelCoord = aTex;
    fVertexColour = ambientComponent + diffuseComponent + specularComponent;
}


vec3 calcAmbient(vec3 colour, float strength) {
    return colour * strength;
}


vec3 calcDiffuse(vec3 colour, vec3 lightDir, vec3 normal) {
    //Calculate how close the light direction is to the normal
    float scaleFactor = max(dot(normal, lightDir), 0.0f);
    vec3 diffuseShading = scaleFactor * lightColour;
    return diffuseShading;
}


vec3 calcSpec(vec3 colour, vec3 lightDir, vec3 normal, vec3 viewPos) {
    float specStrength = 0.5f;
    vec3 vertToView = normalize(-viewPos);
    vec3 reflectDir = reflect(-lightDir, normal);
    float specSF = pow(max(dot(vertToView, reflectDir), 0.0f), 32) * specStrength;
    vec3 specShading = lightColour * specSF;
    return specShading;
}
