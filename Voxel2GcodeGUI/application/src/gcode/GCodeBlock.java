package application.src.gcode;

public class GCodeBlock {
private float newX;
private float newY;
private float newZ;
private float parameterE;
private float parameterF;
private GCodeMovementCommands command;
private String cgodeCommand;
public GCodeBlock(float newX, float newY, float newZ, GCodeMovementCommands command, GCodeProperties gCodeProperties) {
	this.parameterE = gCodeProperties.getParameterE();
	this.parameterF = gCodeProperties.getParameterF();
	this.cgodeCommand = command.toString() + " X"+newX+" Y"+newY+" Z"+newZ+" E"+parameterE+" F"+parameterF;
}
public GCodeBlock(String gcodeCommand) {
	this.cgodeCommand = gcodeCommand;
}
public float getParameterE() {
	return parameterE;
}
public void setParameterE(float parameterE) {
	this.parameterE = parameterE;
}
public float getParameterF() {
	return parameterF;
}
public void setParameterF(float parameterF) {
	this.parameterF = parameterF;
}
public float getNewX() {
	return newX;
}
public void setNewX(float newX) {
	this.newX = newX;
}
public float getNewY() {
	return newY;
}
public void setNewY(float newY) {
	this.newY = newY;
}
public float getNewZ() {
	return newZ;
}
public void setNewZ(float newZ) {
	this.newZ = newZ;
}
public GCodeMovementCommands getCommand() {
	return command;
}
public void setCommand(GCodeMovementCommands command) {
	this.command = command;
}
public String getCGodeCommand() {
	return cgodeCommand;
	}
}
