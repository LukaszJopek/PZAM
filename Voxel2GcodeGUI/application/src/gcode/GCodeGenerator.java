package application.src.gcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import application.src.core.Image;
import application.src.core.ImageInfo;
import application.src.gcode.stateMachines.EventType;
import application.src.gcode.stateMachines.FilamentControlStateMachine;
import application.src.imageProcessing.BoundaryDetector;
import application.src.imageProcessing.Geometry;
import application.src.imageProcessing.graph.Edge;
import application.src.imageProcessing.graph.GcodeGraph;
import application.src.imageProcessing.graph.GcodeGraph.PathCodeType;
import application.src.utils.Display;
import application.src.utils.GCodeUtils;
import application.src.utils.ImageUtils;
import application.src.utils.Point2D;



public class GCodeGenerator {
private Image image;
private GCodeProperties gCodeProperties;
public enum LayerType{
	FIRST, NON_FIRST
}
public GCodeGenerator(Image image, GCodeProperties gCodeProperties) {
	this.image = image;
	this.gCodeProperties = gCodeProperties;
}
public Image getImage() {
	return image;
}
public void setImage(Image image) {
	this.image = image;
}
public GCodeProperties getCodeProperties() {
	return gCodeProperties;
}
public void setCodeProperties(GCodeProperties codeProperties) {
	this.gCodeProperties = codeProperties;
}
public void execute() {
	//Geometry imageGeometry = new Geometry(image.getImageInfo(), 0.24f, 0.24f, 0.24f);
	//Geometry imageGeometry = new Geometry(image.getImageInfo(), 0.25f, 0.25f, 0.25f);
	Geometry imageGeometry = new Geometry(image.getImageInfo(), 0.35f, 0.35f, 0.35f);
	//Geometry imageGeometry = new Geometry(image.getImageInfo(), 1.0f, 1.0f, 1.0f);
	List<GCodeBlock> header = createHeader();
	List<List<String>> sliceList = new ArrayList<List<String>>();
	for(int i=0;i<4/*image.getImageInfo().getDepth();*/;i++) {
		System.out.println("processing "+i+" slice from: "+image.getImageInfo().getDepth());
		sliceList.add( generateGCodeAtSlice(imageGeometry,i) );
	}
	
	sliceList.add(GCodeUtils.endPrintSequence());
	GCodeUtils.gCodeWriter(sliceList, header, gCodeProperties);
}
private List<String> generateGCodeAtSlice(Geometry geometry, int nSlice) {
	return createLayer(geometry,nSlice,getLayerType(nSlice));
}
private LayerType getLayerType(int nSlice) {
	return nSlice == 0 ? LayerType.FIRST : LayerType.NON_FIRST;
}

private List<String> createLayer(Geometry imageGeometry, int nSlice, LayerType layerType) {
	
	BufferedImage raster = ImageUtils.byteArray2BufferedImage(image.getRaster(nSlice), image.getImageInfo());
	BufferedImage resizedImage = ImageUtils.resizeImageWithHint(raster, imageGeometry, image.getImageInfo(), BufferedImage.TYPE_BYTE_GRAY);
	System.out.println("image resized.");
	ImageInfo resizedImageInfo = new ImageInfo("Resized Slice_"+nSlice, resizedImage.getHeight(),resizedImage.getWidth(),image.getImageInfo().getDepth(),image.getImageInfo().getNBits());
	BoundaryDetector boundaryDetector = new BoundaryDetector(ImageUtils.convertToByteArray(resizedImage), resizedImageInfo, gCodeProperties);
	boundaryDetector.execute();
	System.out.println("Boundary detected.");
	
	List<GcodeGraph> layerGcodeList = new ArrayList<GcodeGraph>();
	int[][] allComponents = new int[resizedImageInfo.getHeigth()][resizedImageInfo.getWidth()];
	
	for(int i = 0; i<boundaryDetector.getBoundaryList().size();i++) {
		byte[][] skeletonizedBoundary = ImageUtils.skeletonize(boundaryDetector.getBoundaryList().get(i), resizedImageInfo);
		MatrixComponents matrixComponents = new MatrixComponents(ImageUtils.convertTypeByte2Int( skeletonizedBoundary ));
		int[][] newMatrix = matrixComponents.getLabeledMatrix();
		ImageUtils.addLayer(allComponents, newMatrix);
		
/*		try {
			Display display = Display.getInstance();
			//display.DisplayImage(ImageUtils.convertToByteArray(resizedImage), resizedImageInfo , "Original image ");
			//Thread.sleep(100);
			//display.DisplayImage(ImageUtils.standarizeImage(boundaryDetector.getBoundaryList().get(i)), resizedImageInfo , "before skeletonized Boundary ");
			//Thread.sleep(500);
			//display.DisplayImage(ImageUtils.standarizeImage(allBoundary), resizedImageInfo , "ALL  Boundary ");
			//Thread.sleep(500);
			//display.DisplayImage(ImageUtils.standarizeImage(allComponents), resizedImageInfo , "ALL Components ");
			//Thread.sleep(100);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Set<Integer> uniqueVals = ImageUtils.uniqueVal(newMatrix);
    	for( Integer val: uniqueVals) {
    		//System.out.println("wartosc: "+val);
    		if(val.equals(0)) continue;
    		layerGcodeList.addAll(GraphUtils.createGraph(newMatrix, val, Connectivity.Eight_Connectivity));
    	}
		//for(int j = 1 ; j< matrixComponents.getNumberOfComponents();j++) {
		//	
		//}		
		
		System.out.println("Slice : ["+nSlice+"] boundary detector iteration nr "+i+", found: "+uniqueVals.size()+" objects...");
		
	}
	
	List<String> gCodeCommands = new ArrayList<String>();
	
	byte[][] layerImage = ImageUtils.visualizeLayer2(layerGcodeList, resizedImageInfo.getHeigth(), resizedImageInfo.getWidth());
	try {
		Display display = Display.getInstance();
		display.DisplayImage(ImageUtils.standarizeImageGrayLevel(allComponents), resizedImageInfo , "ALL Components ");
		Thread.sleep(1000);
		display.DisplayImage(layerImage, resizedImageInfo , "Layer: "+nSlice);
		Thread.sleep(1000);
	} catch (IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	switch (layerType) {
	case FIRST:
		gCodeCommands = createFirstLayer(imageGeometry, layerGcodeList,nSlice);
		break;
	case NON_FIRST:
		gCodeCommands = createLayer(imageGeometry, layerGcodeList,nSlice);
		break;
	default:
		System.err.println("No layer type set. Set to 'First'.");
		break;
	}
	   
	System.out.println("Generated of "+gCodeCommands.size()+" G-CODE commands.");
//	for(String command: gCodeCommands) {
//		System.out.println("Command: "+command);
//	}
	
	return gCodeCommands;
}

private List<String> createFirstLayer(Geometry geometry, List<GcodeGraph> layerGcodeList, int slice) {
	List<String> gCodeCommands = new ArrayList<String>();
	FilamentControlStateMachine fsm = FilamentControlStateMachine.getIntance();
	fsm.setGeometry(geometry);
	gCodeCommands.addAll(fsm.generateNewCommnand(EventType.INITIAL, new Point2D(0,0), slice));
	
	//fsm.generateNewCommnand(EventType., new, slice)
	
	for (GcodeGraph g: layerGcodeList) {
		if (g.isValid()) {
			if( g.getPathCodeType().equals(PathCodeType.PATH) ) {
				List<Point2D> points = g.getPath();
				System.out.println("points size(): "+points.size());
				for (int i = points.size()-1; i>=0;i--) {
						if (i == points.size()-1) {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_PATH, points.get(i), slice));
						}
						else if(i == 0 ) {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.LAST_POINT, points.get(i), slice));
						}
						else {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_POINT, points.get(i), slice));
						}
						
				}
			}
			if( g.getPathCodeType().equals(PathCodeType.GRAPH) ) {
				Set<Edge> paths = g.getSimpleGraph().edgeSet(); // narazie tylko wyciagania samych sciezek
				for (Edge edge : paths) {
					List<Point2D> points = edge.getSortedCurve();
					for (int i = points.size()-1; i>=0;i--) {
						if (i == 0) 
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_PATH, points.get(i), slice));
						
						gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_POINT, points.get(i), slice));
				}
					
				}
			}
		}
		else {
			//System.err.println("Non valid G-Code Graph object !");
		}
	}
	return gCodeCommands;
}
private List<String> createLayer(Geometry geometry, List<GcodeGraph> layerGcodeList, int slice) {
	List<String> gCodeCommands = new ArrayList<String>();
	FilamentControlStateMachine fsm = FilamentControlStateMachine.getIntance();
	fsm.setGeometry(geometry);
	
	// przejscie do nowej warstwy
	GcodeGraph firstPath = getFistValidStructure(layerGcodeList);
	if(firstPath != null) {
		List<Point2D> points = new ArrayList<Point2D>();
		if( firstPath.getPathCodeType().equals(PathCodeType.PATH) ) {
			points.addAll(firstPath.getPath());
		}
		else {
			Set<Edge> paths = firstPath.getSimpleGraph().edgeSet();
			for (Edge path: paths) {
				points.addAll(path.getSortedCurve());
			}
		}
		for (int i = 0; i<points.size();i++) {
			if (i == 0) {
				gCodeCommands.addAll(fsm.generateNewCommnand(EventType.LAYER_END, points.get(i), slice));
				}
			else if (i== points.size()-1) {
				gCodeCommands.addAll(fsm.generateNewCommnand(EventType.LAST_POINT, points.get(i), slice));
			}else {
				gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_POINT, points.get(i), slice));
			}
		}
	}	
	
	// warstwa
	
	for (GcodeGraph g: layerGcodeList) {
		if (g.isValid()) {
			if( g.getPathCodeType().equals(PathCodeType.PATH) ) {
				List<Point2D> points = g.getPath();
				for (int i = 0; i<points.size();i++) {
						if (i == 0) {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_PATH, points.get(i), slice));
							}
						else if (i== points.size()-1) {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.LAST_POINT, points.get(i), slice));
						}else {
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_POINT, points.get(i), slice));
						}
				}
			}
			if( g.getPathCodeType().equals(PathCodeType.GRAPH) ) {
				Set<Edge> paths = g.getSimpleGraph().edgeSet(); // narazie tylko wyciagania samych sciezek
				for (Edge edge : paths) {
					List<Point2D> points = edge.getSortedCurve();
					for (int i = 0; i<points.size();i++) {
						if (i == 0) 
							gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_PATH, points.get(i), slice));
						
						gCodeCommands.addAll(fsm.generateNewCommnand(EventType.NEW_POINT, points.get(i), slice));
				}
					
				}
			}
		}
		else {
			//System.err.println("Non valid G-Code Graph object !");
		}
	}
	return gCodeCommands;
}

private GcodeGraph getFistValidStructure(List<GcodeGraph> layerGcodeList) {
	for(int i=0;i<layerGcodeList.size();i++) {
		if(layerGcodeList.get(i).isValid()) {
			return layerGcodeList.remove(i);
		}
	}
	return null;
}
private List<GCodeBlock> createHeader(){
	List<GCodeBlock> header = new ArrayList<GCodeBlock>();
	
	//Naglowek z przykladu...
	//M107
	//M190 S58 ; set bed temperature and wait for it to be reached
	//M104 S218 T1 ; set temperature
	//G28 ; home all axes
	//G1 Z5 F300 ; lift nozzle

	//; Filament gcode

	//M109 S218 T1 ; set temperature and wait for it to be reached
	//G21 ; set units to millimeters
	//G90 ; use absolute coordinates
	//M82 ; use absolute distances for extrusion
	//G92 E0
	//T1
	//G92 E0
	
	header.add(new GCodeBlock("M107"));
	header.add(new GCodeBlock("M190 S44"));
	header.add(new GCodeBlock("G28"));		
	header.add(new GCodeBlock("G1 Z0.12 F300"));
			
	header.add(new GCodeBlock("M109 S195 T0"));
	header.add(new GCodeBlock("G21"));
	header.add(new GCodeBlock("G90"));
	header.add(new GCodeBlock("M82"));
	header.add(new GCodeBlock("G92 E0"));
	header.add(new GCodeBlock("T0"));
	header.add(new GCodeBlock("G92 X-200 Y-125"));
	header.add(new GCodeBlock("M104 S195")); 
	return header;
}

}
