package utils;

import gcode.GCodeProperties;
import imageProcessing.Geometry;

public class Point2D extends Point{

	private double xMM = 0.0;
	private double yMM = 0.0;
	private double angle = 0.0;
	
	public Point2D(double xMM, double yMM) {
		this.xMM = xMM;
		this.yMM = yMM;
		calculateAngle();
	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	public double getxMM() {
		return xMM;
	}
	public void setxMM(double xMM) {
		this.xMM = xMM;
	}
	public double getyMM() {
		return yMM;
	}
	public void setyMM(double yMM) {
		this.yMM = yMM;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double calculateAngle() {
		double d=Math.abs(xMM)+Math.abs(yMM);
		 
		if ((xMM>=0)&&(yMM>=0))
			angle=yMM/d;
		 
		if ((xMM<0)&&(yMM>=0))
			angle=2-yMM/d;
		 
		if ((xMM<0)&&(yMM<0))
			angle=2+Math.abs(yMM)/d;
		 
		if ((xMM>=0)&&(yMM<0))
			angle=4-Math.abs(yMM)/d;
		return angle;
	}
	public double getDistanced(Point2D point) {
		return Math.sqrt( ((xMM - point.xMM) *  (xMM - point.xMM) ) + ((yMM - point.yMM) *  (yMM - point.yMM)) ); 
	}
	public double getDistanceinMM(Point2D point) {
		double xmm = xMM * GridUtils.TATGER_CELL_SIZE_MM;
		double ymm = yMM * GridUtils.TATGER_CELL_SIZE_MM;
		Point2D localGridPoint = new Point2D(point.xMM * GridUtils.TATGER_CELL_SIZE_MM, point.yMM * GridUtils.TATGER_CELL_SIZE_MM);
		return Math.sqrt( ((xmm - localGridPoint.xMM) *  (xmm - localGridPoint.xMM) ) + ((ymm - localGridPoint.yMM) *  (ymm - localGridPoint.yMM)) ); 		
	}
	public double getFilamentLength(Geometry geometry, Point2D point) {
		double distance = getDistanceinMM(point) ;
		double filamentConst = (geometry.getxCellSize() * geometry.getyCellSize()) + (GCodeProperties.filamentStreamWidth * GridUtils.TATGER_CELL_SIZE_MM);
		return distance * filamentConst;
	}
	
	@Override
	public String toString() {
		return "x = "+xMM+" y = "+yMM+" angle = "+angle;
		
	}

}
