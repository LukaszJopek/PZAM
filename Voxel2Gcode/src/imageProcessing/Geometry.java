package imageProcessing;

import core.ImageInfo;
import imageProcessing.Geometry.Axis;
import utils.GridUtils;
import utils.Point3D;

public class Geometry {
public enum Axis{ OX, OY, OZ};
private float xCellSize = 0.0f;
private float yCellSize = 0.0f;
private float zCellSize = 0.0f;
private ImageInfo imageInfo;
private Point3D origin;
public Geometry(ImageInfo imageInfo) {
	this.imageInfo = imageInfo;
	origin =  originPointCalculate();
}
public Geometry(ImageInfo imageInfo, float xCellSize, float yCellSize, float zCellSize) {
	this.imageInfo = imageInfo;
	this.xCellSize = xCellSize;
	this.yCellSize = yCellSize;
	this.zCellSize = zCellSize;
	origin =  originPointCalculate();
}
public ImageInfo getImageInfo() {
	return imageInfo;
}
public void setImageInfo(ImageInfo imageInfo) {
	this.imageInfo = imageInfo;
}
public float getxCellSize() {
	return xCellSize;
}
public void setxCellSize(float xCellSize) {
	this.xCellSize = xCellSize;
}
public float getyCellSize() {
	return yCellSize;
}
public void setyCellSize(float yCellSize) {
	this.yCellSize = yCellSize;
}
public float getzCellSize() {
	return zCellSize;
}
public void setzCellSize(float zCellSize) {
	this.zCellSize = zCellSize;
}
private Point3D originPointCalculate() {
	origin = new Point3D();
	int x = (int) Math.round((imageInfo.getWidth() / 2.0) + 0.5);
	int y = (int) Math.round((imageInfo.getHeigth() / 2.0) + 0.5);
	int z = (int) Math.round((imageInfo.getDepth() / 2.0) + 0.5);
	origin.setPosition(x, y, z);
	return origin;
}
public float getPositionInMM(int position, Axis axis) {
	float xScaling = GridUtils.getScalingFactor(getxCellSize(), Axis.OX);
	float yScaling = GridUtils.getScalingFactor(getyCellSize(), Axis.OY);
	float zScaling = GridUtils.getScalingFactor(getzCellSize(), Axis.OZ);
	switch(axis){
		case OX: 
			return (position - origin.getXCoordinate()) * xScaling;
		case OY :
			return (position - origin.getYCoordinate()) * yScaling;
		case OZ :
			return (position - origin.getZCoordinate()) * zScaling;
		default:
		return 0;
	}
}


}
