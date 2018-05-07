package imageProcessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.ImageInfo;
import gcode.GCodeProperties;
import utils.ImageUtils;
import utils.ImageUtils.Operations;

public class BoundaryDetector {
	private int maxIteration = 1000;
	private int minNumberOfPixel = 5;
	private byte[][] image;
	private ImageInfo imageInfo;
	private GCodeProperties codeProperties;
	private List<byte[][]> boundaryList = new ArrayList<byte[][]>();
	public BoundaryDetector(byte[][] image, ImageInfo imageInfo, GCodeProperties codeProperties) {
		this.image = image;
		this.imageInfo = imageInfo;
		this.codeProperties = codeProperties;
	}
	public int getMaxIteration() {
		return maxIteration;
	}
	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}
	public int getMinNumberOfPixel() {
		return minNumberOfPixel;
	}
	public void setMinNumberOfPixel(int minNumberOfPixel) {
		this.minNumberOfPixel = minNumberOfPixel;
	}
	public List<byte[][]> getBoundaryList() {
		return boundaryList;
	}
	
	

	private byte[][] findBoudaryByErosion(byte[][] image, int n,ImageInfo imageInfo, GCodeProperties gCodeProperties) {
		byte[][] firstErosion = ImageUtils.copyArray(image);
		byte[][] secondErosion = ImageUtils.copyArray(firstErosion);
		ImageUtils.basicMorphologicalOperations(firstErosion,n-1,Operations.Erosion);
		ImageUtils.basicMorphologicalOperations(secondErosion,n,Operations.Erosion);
		this.image = ImageUtils.copyArray(secondErosion);
		return ImageUtils.imageSubstraction(firstErosion, secondErosion);
	}

	public void execute() {
		int i = 0;
       	while(true) {
       		i++;
       		byte[][] bx = findBoudaryByErosion(image,4, imageInfo, codeProperties);
       		if (!ImageUtils.havePixel(bx, (byte) 1, minNumberOfPixel) || i > maxIteration) {
       			break;
       		}
       		System.out.println("boudary iteration");
       		boundaryList.add(bx);
    	}
	}
	public byte[][] getAllBoundary(){
		byte[][] allBoundary = new byte[imageInfo.getHeigth()][imageInfo.getWidth()];
		for (byte[][] layer: boundaryList) {
			addLayer(allBoundary, layer);
		}		
		return allBoundary;
	}
	private void addLayer(byte[][] base, byte[][] newLayer) {
		for (int i =0 ; i< base.length; i++) {
			for(int j=0;j<base[0].length; j++) {
				base[i][j] = (byte) (base[i][j] + newLayer[i][j]);
			}
		}
	}
}
