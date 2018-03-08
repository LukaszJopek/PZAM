package segments;

import points.Point;
import vector.Vector;

public interface SegmentMethods {
	public void setEnd(Point point);
	public Point getEnd();
	public Vector getDirectionality();
	public double getLength();
	
}
