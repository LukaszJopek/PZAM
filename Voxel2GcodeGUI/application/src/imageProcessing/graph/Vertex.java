package application.src.imageProcessing.graph;

import java.util.UUID;

import application.src.utils.Point2D;


public class Vertex {
	
	private AdjacencyType vertexType;
	private Point2D position; 
	private UUID uuid = UUID.randomUUID();
	private boolean visited = false;
	public Vertex(int y, int x, AdjacencyType adjacencyType) {
		position = new Point2D(x, y);
		vertexType = adjacencyType;
	}
	public Vertex(Point2D point, AdjacencyType adjacencyType) {
		position = point;
		vertexType = adjacencyType;
	}
	public AdjacencyType getVertexType() {
		return vertexType;
	}
	public Point2D getPosition() {
		return position;
	}
	public UUID getUuid() {
		return uuid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((vertexType == null) ? 0 : vertexType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (vertexType != other.vertexType)
			return false;
		return true;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	

}
