package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import core.ImageInfo;
import core.reducer.Point;
import core.reducer.SeriesReducer;
import imageProcessing.graph.AdjacencyType;
import imageProcessing.graph.Edge;
import imageProcessing.graph.GcodeGraph;
import imageProcessing.graph.GcodeGraph.ConnectingType;
import imageProcessing.graph.Vertex;

public class GraphUtils {

	public enum Connectivity{
		Four_connectibity, Eight_Connectivity
	}
	public static final int min_path_length = 3;
	public static final double simplificiation_epsilon = 0.01;

	public GraphUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<GcodeGraph> createGraph(int[][] cclMatrix, int component, Connectivity connectivity) {
		List<Vertex> vertexList = getALLVertex(cclMatrix, component);
		for(Vertex v: vertexList) {
			if(v.getVertexType().equals(AdjacencyType.Branch_Point)) {
				cclMatrix[(int)v.getPosition().getyMM()][(int)v.getPosition().getxMM()] = 0;
			}
		}
		
		Vertex startPoint = initializePath(cclMatrix,component, Connectivity.Eight_Connectivity);
		if (startPoint == null) {
			System.err.println("Brak punktów dla komponentu : "+component);
			return new ArrayList<GcodeGraph>();
		}
		//System.out.println("punkt startowy: "+startPoint.getVertexType().toString());
		
		
		//System.out.println("One path scheme ( "+startPoint.getVertexType().toString()+ " )");
		List<GcodeGraph> gCodeGraphList = new ArrayList<GcodeGraph>();
		GcodeGraph gCodeGraph =  createStructure(cclMatrix, component, connectivity, startPoint);
		gCodeGraphList.add(gCodeGraph);
		Vertex unConnectedPart = findUnConnectedPart(cclMatrix, component);
		while(unConnectedPart != null) {		
			gCodeGraphList.add(createStructure(cclMatrix, component, connectivity, unConnectedPart));
			//System.out.println("adding new gCodeGraph...");
			unConnectedPart = findUnConnectedPart(cclMatrix, component);
		}
		//reConnectPath(gCodeGraphList);
		
		//System.out.println("gCodeGraph List size = "+gCodeGraphList.size());
		
		return gCodeGraphList;
	}
	
	private static void reConnectPath(List<GcodeGraph> gCodeGraphList) {
		for(int i=0;i<gCodeGraphList.size();i++) {
			GcodeGraph gcodeGraph = gCodeGraphList.get(i);
			if(gcodeGraph.getPath().size() < 3) {
				if(tryReconnect(gCodeGraphList, gcodeGraph)) {
					gCodeGraphList.remove(gcodeGraph);
				}
			}
		}

	}

	private static boolean tryReconnect(List<GcodeGraph> gCodeGraphList, GcodeGraph gcodeGraph) {
		for (int i=0; i< gCodeGraphList.size(); i++) {
			
			int pathSize = gCodeGraphList.get(i).getPath().size();
			Point2D start = gcodeGraph.getPath().get(0);
			Point2D end = gcodeGraph.getPath().get(gcodeGraph.getPath().size() - 1);
			
			if (gCodeGraphList.get(i).getPath().get(0).getDistanceinMM(start) < 2) {
				//System.out.println("Reconnecting path [Connect to BEGIN].");
				gCodeGraphList.get(i).addPointToPath(gcodeGraph.getPath(), ConnectingType.BEGIN);
				return true;
			}

/*			else if (gCodeGraphList.get(i).getPath().get(0).getDistance(end) < 2) {
				//System.out.println("Reconnecting path [Connect to END].");
				gCodeGraphList.get(i).addPointToPath(gcodeGraph.getPath(), ConnectingType.END);
				return true;
			}*/
			
			else if (gCodeGraphList.get(i).getPath().get(pathSize - 1).getDistanceinMM(start) < 2) {
				//System.out.println("Reconnecting path [Connect to BEGIN].");
				gCodeGraphList.get(i).addPointToPath(gcodeGraph.getPath(), ConnectingType.BEGIN);
				return true;
			}
			
			else if (gCodeGraphList.get(i).getPath().get(pathSize - 1).getDistanceinMM(end) < 2) {
				//System.out.println("Reconnecting path [Connect to END].");
				gCodeGraphList.get(i).addPointToPath(gcodeGraph.getPath(), ConnectingType.END);
				return true;
			}
		}
		return false;
	}

	private static GcodeGraph createStructure(int[][] cclMatrix, int component, Connectivity connectivity, Vertex startPoint) {
		GcodeGraph gCodeGraph = new GcodeGraph();
		if (startPoint.getVertexType() == AdjacencyType.Connectivity || startPoint.getVertexType() == AdjacencyType.End_Point) {

			Edge path = pathTracking(cclMatrix, component, startPoint, connectivity);
			if(path.getSize() > 0) {
				
			//System.out.println("path length = "+path.getSize());
			List<Point> reduced = SeriesReducer.reduce(ImageUtils.convertPoint2DToPoint(path.getAllPoints()), 0.01);
			//gCodeGraph.setReducedPath(ImageUtils.convertPoint2DToPoint(path.getAllPoints()));
			gCodeGraph.setReducedPath(reduced);
			
	
			
/*			byte[][] image = ImageUtils.visualizeGraph2(path.getAllPoints(), cclMatrix.length, cclMatrix[0].length);
			
			ImageInfo imageInfo = new ImageInfo("",cclMatrix[0].length,cclMatrix.length, 1, 8);
			try {
				Display display = Display.getInstance();
				display.DisplayImage(image, imageInfo , "Tracked Path List");
				Thread.sleep(100);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			}
			
		}
		else {
			//System.out.println("Graph Scheme");
			Graph<Vertex, Edge> graph = generateGraph(cclMatrix, component, startPoint, connectivity);
			//showGraph(graph);
			//gCodeGraph.setSimpleGraph(graph);
			gCodeGraph = new GcodeGraph();
		}
		return gCodeGraph;
	}
	
	private static List<Point> edge2SimplifiedPath(Edge edge) {
		return SeriesReducer.reduce(ImageUtils.convertPoint2DToPoint(edge.getSortedCurve()), simplificiation_epsilon);
	}

	private static void showComponent(int[][] cclMatrix, int component) {
		int[][] newMatrix = new int[cclMatrix.length][cclMatrix[0].length];
				
       	for (int i=0;i<newMatrix.length;i++) {
       		for(int j=0;j<newMatrix[0].length;j++) {
    		if(cclMatrix[i][j] == component) newMatrix[i][j] = 1;
    		}
    	}
		ImageInfo imageInfo = new ImageInfo("Testowy CCL", cclMatrix[0].length, cclMatrix.length, 1, 8);
		try {
			Display display = Display.getInstance();
			display.DisplayImage(ImageUtils.convertTypeInt2Byte(newMatrix), imageInfo , "image for Component = "+component);
			//ImageUtils.DisplayImage(ImageUtils.convertTypeInt2Byte(newMatrix), imageInfo , "image for Component = "+component);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static Edge pathTracking(int[][] cclMatrix, int component,Vertex vertex, Connectivity connectivity) {
		Edge edge = new Edge(vertex.getVertexType());
		edge.addPoint(vertex.getPosition());
		eraseVisitedPoint(cclMatrix, vertex.getPosition());	
		Point2D nextPoint = getNextPoint(vertex.getPosition(), cclMatrix, component);
		while(nextPoint != null) {
			if(edge.containPoint(nextPoint)) {
				continue;
			}
			edge.addPoint(nextPoint);
			eraseVisitedPoint(cclMatrix, nextPoint);			
			nextPoint =  getNextPoint(nextPoint, cclMatrix, component);
		}
				
		return edge;
	}
	private static void eraseVisitedPoint(int [][] cclMatrix, Point2D visitedPoint) {
		cclMatrix[(int)visitedPoint.getyMM()][(int)visitedPoint.getxMM()] = 0;
	}
	private static Point2D getNextPoint(Point2D position, int[][] cclMatrix, int component) {
		int x = (int)position.getxMM();
		int y = (int)position.getyMM();
		
		for(int i=y-1;i<=y+1;i++) {
			for(int j=x-1;j<=x+1;j++) {
				if (i == y && j == x) continue;
				
				if(isOnImage(cclMatrix, i, j) && cclMatrix[i][j] == component) {
					return new Point2D(j, i);
				}
			}
		}
		return null;
	}
	
	private static Graph<Vertex, Edge> generateGraph(int[][] cclMatrix, int component,Vertex vertex, Connectivity connectivity) {
		List<Vertex> vertexList = getALLVertex(cclMatrix, component);
		//System.out.println("Found "+(vertexList.size() + 1)+" vertexs");
		List<Edge> edgeList = new ArrayList<Edge>();
		Graph<Vertex, Edge> g = new SimpleGraph<>(Edge.class);
		
		for (Vertex v : vertexList) {
			List<Point2D> startPoints = getStartPointForEdge(cclMatrix,component, v);
			if(startPoints.size() < 1) {
				//ImageUtils.typeNeighborhood3x3(cclMatrix, (int)vertex.getPosition().getyMM(), (int)vertex.getPosition().getxMM(),component);
			}
			for (Point2D sPoint: startPoints) {
				//System.out.println("corrected detected of start points...");
				//ImageUtils.typeNeighborhood3x3(cclMatrix, (int)sPoint.getyMM(), (int)sPoint.getxMM(),component);
				Edge edge = createEdge(cclMatrix,component,v, sPoint);
				edgeList.add(edge);
				
				addStructureToGraph(g, vertexList, edge);
				
			}
		}
		
		
/*		byte[][] image = ImageUtils.visualizeGraph(edgeList, cclMatrix.length, cclMatrix[0].length);
		
		ImageInfo imageInfo = new ImageInfo("",cclMatrix[0].length,cclMatrix.length, 1, 8);
		try {
			Display display = Display.getInstance();
			display.DisplayImage(image, imageInfo , "Tracked Graph List");
			Thread.sleep(1);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//Graph<Vertex, Edge> g = new SimpleGraph<>(Edge.class);
		
		return g;
	}
	private static void addStructureToGraph(Graph<Vertex, Edge> g, List<Vertex> vertexList, Edge edge) {
		boolean isIdentified = idenfityVertexFromPoint(vertexList, edge);
		if(isIdentified && edge.getPathLength() >= min_path_length) {
			g.addVertex(edge.getStartVertex());
			g.addVertex(edge.getEndVertex());
			g.addEdge(edge.getStartVertex(), edge.getEndVertex(), edge);
		}
		else if ( edge.getPathLength() >= min_path_length){
			Vertex virtualVertex = new Vertex(edge.getEndPoint(), edge.getEndPointType());
			g.addVertex(edge.getStartVertex());
			g.addVertex(virtualVertex);
			g.addEdge(edge.getStartVertex(), virtualVertex, edge);
		}
	}
	private static List<Vertex> getALLVertex(int[][] cclMatrix, int component) {
		List<Vertex> vertexList = new ArrayList<Vertex>();
		for(int i=0;i<cclMatrix.length;i++) {
			for (int j=0;j<cclMatrix[0].length;j++) {
				if(getAdjacecnyType(cclMatrix, i, j, component)  == AdjacencyType.Branch_Point) {
					vertexList.add(new Vertex(i,j, AdjacencyType.Branch_Point));
				}
			}
		}
		return vertexList;
	}
	
	private static Edge createEdge(int[][] cclMatrix, int component, Vertex vertex, Point2D startPoint) {
		Edge edge = new Edge(vertex.getVertexType());
		edge.addPoint(vertex.getPosition());
		edge.addPoint(startPoint);
		edge.setStartVertex(vertex);
		eraseVisitedPoint(cclMatrix, startPoint);

		//System.out.println("Tracking New Edge Path");
		Point2D nextPoint = getNextPoint(startPoint, cclMatrix, component);
		while(nextPoint != null) {
			if(getAdjacecnyType(cclMatrix, (int)nextPoint.getyMM(), (int)nextPoint.getxMM(), component) == AdjacencyType.Branch_Point) {
				if (vertex.getPosition().equals(nextPoint)) {continue;}
				else {
					edge.addPoint(nextPoint);
					edge.setEndPointType(AdjacencyType.Branch_Point);
					break;
				}
			}
			if(!edge.containPoint(nextPoint)) {
				edge.addPoint(nextPoint);
				eraseVisitedPoint(cclMatrix, nextPoint);
			}		
			nextPoint =  getNextPoint(nextPoint, cclMatrix, component);
		}
		return edge;
	}
	private static List<Point2D> getStartPointForEdge(int[][] cclMatrix, int component, Vertex vertex) {
		List<Point2D> startPoints = new ArrayList<Point2D>();
		int i = (int) vertex.getPosition().getyMM();
		int j = (int) vertex.getPosition().getxMM();
		
    	for(int y=i-1; y<=i+1;y++) {
    		for(int x = j-1; x<=j+1;x++) {
				if(i == y && j == x) {continue;}
				
				if(isOnImage(cclMatrix, y,x)) {
					if (cclMatrix[y][x] == component) {
						startPoints.add(new Point2D(x, y));
					}
				}
			}
    	}		
		return startPoints;
	}
	
	private static boolean idenfityVertexFromPoint(List<Vertex> vertexList, Edge edge) {
		double max_dist = Math.sqrt(2) + 0.01;
		for(Vertex v: vertexList) {
			if (edge.getEndPoint().equals(v.getPosition())) {
				edge.setEndVertex(v);
				return true;
			}
			if (v.getPosition().getDistanceinMM(edge.getEndPoint()) <= max_dist) {
				edge.setEndVertex(v);
				return true;
			}		
		}
		return false;
	}
	
	/**
	 * Metoda tworzy dla podanego komponentu punkt startowy sciezki (Vertex).
	 * 
	 * @param cclMatrix mapa CCL
	 * @param component id komponentu
	 * @param connectivity rodzaj polaczeniowosci
	 * @return Jesli znajdzie punkt 'Laczenia' to zrawca go jako vertex, jesli to to szuka 
	 * punktu 'koncowego', a jesli tego nie ma to zwraca pierwszy, znaleziony punkt sciezki. 
	 * Jesli na obrazie CCL nie ma pixeli o wartosci 'component' wtedy zwraca null.
	 */
	private static Vertex initializePath(int[][] cclMatrix, int component, Connectivity connectivity){
		
		for (int i=0; i<cclMatrix.length;i++) {
			for(int j=0; j<cclMatrix[0].length;j++) {
				if (getAdjacecnyType(cclMatrix, i, j, component) == AdjacencyType.Branch_Point){
					return new Vertex(i,j,AdjacencyType.Branch_Point);
				}
			}
		}
		
		for (int i=0; i<cclMatrix.length;i++) {
			for(int j=0; j<cclMatrix[0].length;j++) {
				if (getAdjacecnyType(cclMatrix, i, j, component) == AdjacencyType.End_Point){
					return new Vertex(i,j,AdjacencyType.End_Point);
				}
			}
		}	
		
		for (int i=0; i<cclMatrix.length;i++) {
			for(int j=0; j<cclMatrix[0].length;j++) {
				if (getAdjacecnyType(cclMatrix, i, j, component) == AdjacencyType.Connectivity){
					return new Vertex(i,j,AdjacencyType.Connectivity);
				}
			}
		}
		
	

		return null;
	}
	
	public static Vertex findUnConnectedPart(int[][] cclMatrix, int component) {
		
		for (int i=0; i<cclMatrix.length;i++) {
			for(int j=0; j<cclMatrix[0].length;j++) {
				AdjacencyType adjacecnyType = getAdjacecnyType(cclMatrix, i, j, component);
				if (!adjacecnyType.equals(AdjacencyType.None) ){
					//System.out.println("New Vertex found... in "+i+" "+j+" as "+adjacecnyType.toString());
					return new Vertex(i,j,adjacecnyType);
				}
			}
		}
		return null;
	}
	public static AdjacencyType getAdjacecnyType(int [][] cclMatrix, int i, int j, int component) {
		if(cclMatrix[i][j] != component)return AdjacencyType.None;
		
		int activePixel = 0;
		for (int y=i-1;y<=i+1;y++) {
			for(int x=j-1;x<=j+1;x++) {
				if(i == y && j == x) {continue;}
				if(isOnImage(cclMatrix, y,x)) {
					if (cclMatrix[y][x] == component) {
						activePixel++;
					}	
				}
			}
		}
	
		if (activePixel == 1) return AdjacencyType.End_Point;
		if (activePixel == 2) return AdjacencyType.Connectivity;
		if (activePixel > 2) return AdjacencyType.Branch_Point;
		
		return AdjacencyType.None;
	}
		public static AdjacencyType getAdjacecnyType(byte [][] cclMatrix, int i, int j, int component) {
			if(cclMatrix[i][j] != component)return AdjacencyType.None;
			
			int activePixel = 0;
			for (int y=i-1;y<=i+1;y++) {
				for(int x=j-1;x<=j+1;x++) {
					if(i == y && j == x) {continue;}
					if(isOnImage(cclMatrix, y,x)) {
						if (cclMatrix[y][x] == component)
						activePixel++;
						}
				}
			}
	
		if (activePixel == 1) return AdjacencyType.End_Point;
		if (activePixel == 2) return AdjacencyType.Connectivity;
		if (activePixel > 2) return AdjacencyType.Branch_Point;
		
		return AdjacencyType.None;
	}
	public static boolean isOnImage(int[][] image, int i , int j) {
		return i>0 && j>0 && i<image.length && j<image[0].length;
				
	}
	public static boolean isOnImage(byte[][] image, int i , int j) {
		return i>0 && j>0 && i<image.length && j<image[0].length;
				
	}

	public static void showGraph( Graph<Vertex, Edge> graph) {
		System.out.println("=====Graph travelling...=====");
		GraphIterator<Vertex, Edge> iterator = 
                new DepthFirstIterator<Vertex, Edge>(graph);
        while (iterator.hasNext()) {
            System.out.println( iterator.next().getPosition().toString());
            
            
        }
        System.out.println("=============================.");
	}
	private static List<Edge> getAllEdgesFromNode(Graph<Vertex, Edge> graph, Vertex startNode) {
		//graph.
		return null;
		}
	private void BreadthFirstSearch(Graph<Vertex, Edge> graph){
		
	}


}
