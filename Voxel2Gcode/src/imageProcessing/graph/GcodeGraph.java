package imageProcessing.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import core.reducer.Point;
import utils.ImageUtils;
import utils.Point2D;

public class GcodeGraph {
public enum PathCodeType {
	PATH, GRAPH, NONE
}
public enum ConnectingType{
	BEGIN, END
}
private PathCodeType pathCodeType = null;
private List<Point2D> path = null;
private Graph<Vertex,Edge> simpleGraph = null;

public GcodeGraph() {
}
public GcodeGraph(List<Point2D> path) {
	this.path = path;
	this.pathCodeType = PathCodeType.PATH;
}
public GcodeGraph(Graph<Vertex,Edge>  simpleGraph) {
	this.simpleGraph = simpleGraph;
	this.pathCodeType = PathCodeType.GRAPH;
}

public boolean isValid() {
	return path != null || simpleGraph != null ;
}
public List<List<Point2D>> getPixelPaths() {
	List<List<Point2D>> pixelPaths = new ArrayList<List<Point2D>>();
	if(simpleGraph == null) {
		pixelPaths.add(path);
	}
	return pixelPaths;
}
public List<Point2D> getPath() {
	return path;
}
public void addPointToPath(List<Point2D> points, ConnectingType connectingType ) {
	switch (connectingType) {
	case BEGIN:
			List<Point2D> tempPath = new ArrayList<Point2D>(); 
			tempPath.addAll(points);
			tempPath.addAll(path);
			path = tempPath;
		break;
	case END:
			path.addAll(points);
		break;

	default:
		break;
	}
}
public void setPath(List<Point2D> path) {
	this.path = path;
	this.pathCodeType = PathCodeType.PATH;
}
public void setReducedPath(List<Point> reducedCurce) {
	this.path = ImageUtils.convertPointToPoint2D(reducedCurce);
	this.pathCodeType = PathCodeType.PATH;
}
public Graph<Vertex, Edge> getSimpleGraph() {
	return simpleGraph;
}
public void setSimpleGraph(Graph<Vertex, Edge> simpleGraph) {
	this.simpleGraph = simpleGraph;
	this.pathCodeType = PathCodeType.GRAPH;
}
public PathCodeType getPathCodeType() {
	return this.pathCodeType;
}

}
