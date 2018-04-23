package utils;

import imageProcessing.Geometry.Axis;

public class GridUtils {
public final static float  TATGER_CELL_SIZE_MM = 0.1f;

public static float getScalingFactor(float cellSize, Axis axis) {
	return cellSize / TATGER_CELL_SIZE_MM;	
} 
}
