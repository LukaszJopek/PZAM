package core.sorter;

import java.util.Comparator;

import utils.Point2D;

public class AngleDistXComparator implements Comparator<Point2D>{


	@Override
	public int compare(Point2D p1, Point2D p2) {
		
		if (p1.getAngle() > p2.getAngle()) {
		return 1;
		}
		else if (p1.getAngle() < p2.getAngle()) {
			return -1;
		}
		else {
			if (p1.getxMM() <= p2.getxMM()) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}

}
