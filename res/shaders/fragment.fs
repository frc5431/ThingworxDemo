#version 330

out vec4 fragColor;

in vec3 surfaceNormal;
in vec3 toLightVector;

uniform vec3 lightColor;
uniform vec4 color;


void main()
{
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = (max(nDotl,0.0)*0.5)+0.5;
	vec3 diffuse = vec3(lightColor.x*brightness,lightColor.y*brightness,lightColor.z*brightness);
	
	fragColor=vec4(diffuse,1.0)*color;
}