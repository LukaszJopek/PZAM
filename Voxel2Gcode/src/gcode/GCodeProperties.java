package gcode;

public class GCodeProperties {
public int EmptySpace= 0;
public int Filament = 1;
private float parameterE;
private float parameterF;
private String outPutFilename = "outputCode.gcode";
private float filamentStreamWidth = 0.0f;
public float getFilamentStreamWidth() {
	return filamentStreamWidth;
}
public void setFilamentStreamWidth(float filamentStreamWidth) {
	this.filamentStreamWidth = filamentStreamWidth;
}
public String getOutPutFilename() {
	return outPutFilename;
}
public void setOutPutFilename(String outPutFilename) {
	this.outPutFilename = outPutFilename;
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

}
