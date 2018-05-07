package gcode;

public class GCodeProperties {
public static final float filamentShiftConst = 2f;
public static final float filamentPrinterConst = 0.1f;
public static final float filamentConstFactor = 2f;
public static final float printingSpeed = 1000f;
public static final float HighSpeed = 2000f;
public static final float filamentStreamWidth = 0.3f;

public int EmptySpace= 0;
public int Filament = 1;
private float parameterE;
private float parameterF;
private String outPutFilename = "outputCode.gcode";
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
