package application.src.core.sorter;

import java.util.List;

import application.src.utils.Point2D;


public class SortPoint {
private boolean isSorted = false;

private List<Point2D> curvePointsList;
	public SortPoint(List<Point2D> curvePointsList) {
		this.curvePointsList = curvePointsList;
	}
	private void sort() {
		curvePointsList.sort(new AngleDistXComparator());
	}
	public List<Point2D> getSortedList() {
		sort();
		isSorted = true;
		return curvePointsList;
	}
	public boolean isSorted() {
		return isSorted;
	}
}
