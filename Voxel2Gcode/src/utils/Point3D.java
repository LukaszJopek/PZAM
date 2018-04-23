package utils;

public class Point3D extends Point{
private int x;
private int y;
private int z;
	
	@Override
	public Point getPosition() {
		return null;
	}
	public int getXCoordinate() {
		return x;
	}
	public int getYCoordinate() {
		return y;
	}
	
	public int getZCoordinate() {
		return z;
	}
	
	
	public void setPosition(int id) {
		
	}
	public void setPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
