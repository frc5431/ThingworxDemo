#version 330

layout (location=0) in vec3 position;
layout (location=2) in vec3 normal;

out vec3 surfaceNormal;
out vec3 toLightVector;

uniform vec3 lightPosition;

uniform mat4 worldMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
	vec4 mvPos = viewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * modelMatrix * worldMatrix* mvPos;	
	
	surfaceNormal = (worldMatrix*vec4(normal,0.0)).xyz;
	toLightVector= (worldMatrix*vec4(lightPosition,0.0)).xyz-mvPos.xyz;
}