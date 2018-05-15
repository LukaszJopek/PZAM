package application.src.imageProcessing.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import application.src.core.sorter.SortPoint;
import application.src.utils.Point2D;

public class Edge {

	private double pathLength = 0.0;
	private AdjacencyType startPointType;
	private AdjacencyType endPointType;
	private List<Point2D> points = null;
	private UUID uuid = UUID.randomUUID();
	private EdgeType edgeType = EdgeType.Unset;
	private Vertex startVertex = null;
	private Vertex endVertex = null;

	public Edge(AdjacencyType startPointType) {
		this.startPointType = startPointType;
		points = new ArrayList<Point2D>();
	}
	
	public void addPoint(int i, int j) {
		points.add(new Point2D(i, j));
	}
	public void addPoint(Point2D point2d) {
		points.add(point2d);
	}
	public int getSize() {
		return points.size();
	}
	public Point2D getPoint(int id) {
		return points.get(id);
	}
	public List<Point2D> getAllPoints(){
		return points;
	}
	public List<Point2D> getSortedCurve() {
		SortPoint sortPoint = new SortPoint(points);
		return sortPoint.getSortedList();
	}
	public void setEndPointType(AdjacencyType endPointType) {
		this.endPointType = endPointType;
	}
	
	public boolean containPoint(Point2D point2d) {
		for(Point2D p: points) {
			if(isEquals(p.getxMM(), point2d.getxMM(),0.1) && isEquals(p.getyMM(), point2d.getyMM(),0.1)) {
				return true;
			}
		}
		return points.contains(point2d);
	}
	private boolean isEquals(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}
	
	public AdjacencyType getStartPointType() {
		return startPointType;
	}

	public void setStartPointType(AdjacencyType startPointType) {
		this.startPointType = startPointType;
	} 
	
	public Vertex getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(Vertex startVertex) {
		this.startVertex = startVertex;
	}

	public Vertex getEndVertex() {
		return endVertex;
	}

	public void setEndVertex(Vertex endVertex) {
		this.endVertex = endVertex;
	}

	public void setPathLength(double pathLength) {
		this.pathLength = pathLength;
	}
	public Point2D getStartPoint() {
		return points.get(0);
	}
	public Point2D getEndPoint() {
		return points.get(points.size()-1);
	}
	public AdjacencyType getEndPointType() {
		return endPointType;
	}

	public EdgeType getEdgeType() {
		return edgeType;
	}

	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}

	public double getPathLength() {
		pathLength = points.get(0).getDistanceinMM(points.get(points.size()-1));
		return pathLength;
	}

	public UUID getUuid() {
		return uuid;
	}

}
