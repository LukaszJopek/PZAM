package gcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import core.Image;
import core.ImageInfo;
import imageProcessing.BoundaryDetector;
import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import imageProcessing.ccl.MatrixComponents;
import utils.GCodeUtils;
import utils.GraphUtils;
import utils.GraphUtils.Connectivity;
import utils.ImageUtils;
import utils.Point2D;
import utils.Point3D;

public class GCodeGenerator {
private Image image;
private GCodeProperties gCodeProperties;
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
	Geometry imageGeometry = new Geometry(image.getImageInfo(), 1f, 1f, 1f);
	List<GCodeBlock> header = createHeader();
	List<List<GCodeBlock>> sliceList = new ArrayList<List<GCodeBlock>>();
	for(int i=0;i<image.getImageInfo().getDepth();i++) {
		System.out.println("processing "+i+" slice from: "+image.getImageInfo().getDepth());
		sliceList.add( generateGCodeAtSlice(imageGeometry,i) );
	}
	
	//GCodeUtils.gCodeWriter(sliceList, header, gCodeProperties);
}
private List<GCodeBlock> generateGCodeAtSlice(Geometry geometry, int nSlice) {
	//byte[][] raster = image.getRaster(nSlice);	
	List<GCodeBlock> gcodeBlockList = new ArrayList<GCodeBlock>();	
/*	for( int i = 0 ;i<image.getImageInfo().getHeigth();i++) {				
		gcodeBlockList.addAll(generateGCodeAtLine(geometry,raster,i,nSlice));
		gcodeBlockList.add(jumpToNewPosotion(geometry, nSlice, i)); // nowa linia po y 
	}
	gcodeBlockList.add(jumpToNewPosotion(geometry, nSlice+1, 0)); // nowa linia po z 
*/	
	System.out.println("Xmm size: "+geometry.getxCellSize());
	createFistLayer(geometry,nSlice);
	
	
	
	
	return gcodeBlockList;	
}

private List<GCodeBlock> createFistLayer(Geometry imageGeometry, int nSlice) {
	BufferedImage raster = ImageUtils.byteArray2BufferedImage(image.getRaster(nSlice), image.getImageInfo());
	BufferedImage resizedImage = ImageUtils.resizeImageWithHint(raster, imageGeometry, image.getImageInfo(), BufferedImage.TYPE_BYTE_GRAY);
	ImageInfo resizedImageInfo = new ImageInfo("Resized Slice_"+nSlice, resizedImage.getHeight(),resizedImage.getWidth(),image.getImageInfo().getDepth(),image.getImageInfo().getNBits());
	BoundaryDetector boundaryDetector = new BoundaryDetector(ImageUtils.convertToByteArray(resizedImage), resizedImageInfo, gCodeProperties);
	boundaryDetector.execute();
	
	for(int i = 0; i<boundaryDetector.getBoundaryList().size();i++) {
		byte[][] skeletonized = ImageUtils.skeletonize(boundaryDetector.getBoundaryList().get(i), resizedImageInfo);
/*		try {
			ImageUtils.DisplayImage(skeletonized, resizedImageInfo, "Skeletonized Boundary");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		MatrixComponents matrixComponents = new MatrixComponents(ImageUtils.convertTypeByte2Int( skeletonized ));
		int[][] newMatrix = matrixComponents.getLabeledMatrix();
		
		for(int j = 1 ; j< matrixComponents.getNumberOfComponents();j++) {
			GraphUtils.createGraph(newMatrix, j, Connectivity.Eight_Connectivity);
		}		
		System.out.println("Iteration nr "+i+" found: "+matrixComponents.getNumberOfComponents()+" objects...");
	}
	   
	   
	return null;
}

private GCodeBlock jumpToNewPosotion(Geometry geometry, int nSlice, int i) {
	float xmm = geometry.getPositionInMM(0, Axis.OX);
	float ymm = geometry.getPositionInMM(i+1, Axis.OY);
	float zmm = geometry.getPositionInMM(nSlice, Axis.OZ);
	String newLine = GCodeUtils.createNewLineCommand(xmm,ymm,zmm, gCodeProperties);
	GCodeBlock newLineCommand = new GCodeBlock(newLine);
	System.out.println(newLine);
	return newLineCommand;
}
private List<GCodeBlock> generateGCodeAtLine(Geometry geometry,byte[][] raster, int line, int nSlice) {
	
	List<GCodeBlock> gcodeListAtLine = new ArrayList<GCodeBlock>();
	int lineLength = image.getImageInfo().getWidth();
	
	int xPos= 0;
	float newY = geometry.getPositionInMM(line, Axis.OY);
	float newZ = geometry.getPositionInMM(nSlice, Axis.OZ);
	while(xPos < lineLength) {
		
		 int blockLength = getHomogeneityBlock(raster,xPos,line);
		 float newX = geometry.getPositionInMM(xPos+blockLength, Axis.OX);
		 byte val = raster[line][xPos];
		 GCodeBlock gCodeBlock = new GCodeBlock(newX, newY, newZ, chooseCommand(val,blockLength),gCodeProperties);
		 xPos = xPos+blockLength;
		 System.out.println(gCodeBlock.getCGodeCommand());
		 gcodeListAtLine.add(gCodeBlock);
	}
	
	return gcodeListAtLine;
}
private GCodeMovementCommands chooseCommand(byte val, int blockLength) {
	switch (val) {
	case 0:
		return GCodeMovementCommands.G0;
	case 1:
		return GCodeMovementCommands.G1;
	default:
		return GCodeMovementCommands.G0;
	}
}

private int getHomogeneityBlock(byte[][] raster, int startPos,int line) {
	byte startVal = raster[line][startPos];
	byte testVal = startVal;
	int blockLength = 0;
	while(testVal == startVal) {
		int endPos = startPos + blockLength;
		if (endPos >= raster[0].length) {
			break;
		}
		testVal= raster[line][(startPos + blockLength)];
		blockLength++;
	}
	return blockLength;
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
	header.add(new GCodeBlock("M190 S58"));
	header.add(new GCodeBlock("M104 S218 T1"));
	header.add(new GCodeBlock("G28"));		
	header.add(new GCodeBlock("G1 Z5 F300"));
			
	header.add(new GCodeBlock("M109 S218 T1"));
	header.add(new GCodeBlock("G21"));
	header.add(new GCodeBlock("G90"));
	header.add(new GCodeBlock("M82"));
	header.add(new GCodeBlock("G92 E0"));
	header.add(new GCodeBlock("T1"));
	header.add(new GCodeBlock("G92 E0"));
	return header;
}

}
