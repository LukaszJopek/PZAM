package application.src.utils;

import application.src.imageProcessing.Geometry;
import application.src.imageProcessing.Geometry.Axis;

public class GridUtils {
public final static float  TATGER_CELL_SIZE_MM = 0.1f;
public final static float  TATGER_LAYER_SIZE_MM = 0.3f;

public static float getScalingFactor(Geometry geometry, Axis axis) {
	switch (axis) {
	case OX:
		return geometry.getxCellSize() / TATGER_CELL_SIZE_MM;
	case OY:
		return geometry.getyCellSize() / TATGER_CELL_SIZE_MM;

	default:
		return 0;
	}
	}
	
	public static double getPositionInMM(double position, Axis axis) {
		switch (axis) {
		case OX:
			return position * TATGER_CELL_SIZE_MM;
		case OY:
			return position * TATGER_CELL_SIZE_MM;
		case OZ:
			return position * TATGER_LAYER_SIZE_MM;

		default:
			return 0;
		}
	
} 
}
