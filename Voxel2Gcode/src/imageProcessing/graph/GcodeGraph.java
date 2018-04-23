package imageProcessing.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import core.reducer.Point;
import utils.ImageUtils;
import utils.Point2D;

public class GcodeGraph {
private List<Point2D> path = null;
private Graph<Vertex,Edge> simpleGraph = null;

public GcodeGraph() {
}
public GcodeGraph(List<Point2D> path) {
	this.path = path;
}
public GcodeGraph(Graph<Vertex,Edge>  simpleGraph) {
	this.simpleGraph = simpleGraph;
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
public void setPath(List<Point2D> path) {
	this.path = path;
}
public void setReducedPath(List<Point> reducedCurce) {
	this.path = ImageUtils.convertPointToPoint2D(reducedCurce);
}
public Graph<Vertex, Edge> getSimpleGraph() {
	return simpleGraph;
}
public void setSimpleGraph(Graph<Vertex, Edge> simpleGraph) {
	this.simpleGraph = simpleGraph;
}

}
