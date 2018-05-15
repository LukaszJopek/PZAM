package application.src.utils;


import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ij.process.ByteProcessor;


public class ImageUtils {
public static enum Operations{Erosion, Dilate}
public static Point3D convert3D(ImageInfo imageInfo, int id) {
	Point3D point = new Point3D();
	int x = id % imageInfo.getWidth();
	int y = ( id / imageInfo.getWidth() ) % imageInfo.getHeigth();
	int z = id / ( imageInfo.getWidth() * imageInfo.getHeigth() );
	point.setPosition(x,y,z);	
	return point;
	}

	public static int convert1D(ImageInfo imageInfo,int x, int y, int z) {
	return  x + y * imageInfo.getWidth() + z * imageInfo.getWidth() * imageInfo.getHeigth();
	}
	public static int convert1D(Dimension dimension,int x, int y, int z) {
	return  x + y * (int)dimension.getWidth() + z * (int)dimension.getWidth() * (int)dimension.getHeight();
	}
	
	public static byte[] convert1D(byte[][] byteArray, ImageInfo imageInfo) {
		byte[] pixels = new byte[imageInfo.getHeigth() * imageInfo.getWidth()];
	      for(int w = 0; w < imageInfo.getWidth(); w++)
	      {
	    	  for(int h = 0; h < imageInfo.getHeigth(); h++)
	    	  	{
	    		  int ind = w + imageInfo.getWidth()*h;
	    		  int a = byteArray[h][w];
	    		  pixels[ind] = (byte)a;
	    	  	}
	    	  }
	      return pixels;
	 }
	public static int[] convertInt1D(byte[][] byteArray, ImageInfo imageInfo) {
		int[] pixels = new int[imageInfo.getHeigth() * imageInfo.getWidth()];
	      for(int w = 0; w < imageInfo.getWidth(); w++)
	      {
	    	  for(int h = 0; h < imageInfo.getHeigth(); h++)
	    	  	{
	    		  int ind = w + imageInfo.getWidth()*h;
	    		  int a = byteArray[h][w];
	    		  pixels[ind] = a;
	    	  	}
	    	  }
	return pixels;
	 }
	public static byte[][] convert2D(int[] array, ImageInfo imageInfo) {
		byte[][] pixels = new byte[imageInfo.getHeigth()][imageInfo.getWidth()];
	      for(int i = 0; i < array.length; i++)
	      {
	    	 int  x = i % imageInfo.getWidth();    // % is the "modulo operator", the remainder of i / width;
	    	 int  y = i / imageInfo.getWidth();
	    	 pixels[y][x] = (byte) array[i];
	      }
	      return pixels;
	 }
	public static int[][] convertInt2D(byte[] array, ImageInfo imageInfo) {
		int[][] pixels = new int[imageInfo.getHeigth()][imageInfo.getWidth()];
	      for(int i = 0; i < array.length; i++)
	      {
	    	 int  x = i % imageInfo.getWidth();    // % is the "modulo operator", the remainder of i / width;
	    	 int  y = i / imageInfo.getWidth();
	    	 pixels[y][x] = array[i];
	      }
	      return pixels;
	 }
	public static int[][] convertInt2D(int[] array, ImageInfo imageInfo) {
		int[][] pixels = new int[imageInfo.getHeigth()][imageInfo.getWidth()];
	      for(int i = 0; i < array.length; i++)
	      {
	    	 int  x = i % imageInfo.getWidth();    // % is the "modulo operator", the remainder of i / width;
	    	 int  y = i / imageInfo.getWidth();
	    	 pixels[y][x] = array[i];
	      }
	      return pixels;
	 }
	public static byte[][] convertTypeInt2Byte(int[][] array) {
		byte[][] pixels = new byte[array.length ][ array[0].length];
	      for(int w = 0; w <  array[0].length; w++)
	      {
	    	  for(int h = 0; h < array.length; h++)
	    	  	{	    		 
	    		  pixels[h][w] = (byte) array[h][w];
	    	  	}
	    	  }
	return pixels;
	 }
	public static int[][] convertTypeByte2Int(byte[][] array) {
		int[][] pixels = new int[array.length ][ array[0].length];
	      for(int w = 0; w <  array[0].length; w++)
	      {
	    	  for(int h = 0; h < array.length; h++)
	    	  	{	    		 
	    		  pixels[h][w] = array[h][w];
	    	  	}
	    	  }
	return pixels;
	 }
	public static byte[][] convertToByteArray(BufferedImage bufferedImage) {
		byte[][] array = new byte[bufferedImage.getHeight()][bufferedImage.getWidth()];
		
		for(int i=0;i<bufferedImage.getHeight();i++) {
			for(int j=0;j<bufferedImage.getWidth();j++) {
				//array[i][j] = (byte) bufferedImage.getRGB(i, j);
				int p = bufferedImage.getRGB(i, j);
				int a = (p>>24)&0xff;
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				int avg = (r+g+b)/3;
				avg = avg * -1;
				array[i][j] = (byte) avg;
			}
		}
		return array;
	}
	
    public static void DisplayImage(byte[][] imageInByte, ImageInfo imageInfo,String label) throws IOException
    {
    	BufferedImage b = new BufferedImage(imageInfo.getHeigth(), imageInfo.getWidth(), BufferedImage.TYPE_USHORT_GRAY);
    	for (int i=0;i<imageInfo.getHeigth();i++) {
    		for (int j=0;j<imageInfo.getWidth();j++) {
    			b.setRGB(i, j, (byte)(imageInByte[i][j] * 255.0));
    		}
    	}
        ImageIcon icon=new ImageIcon(b);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(800,800);
        JLabel lbl=new JLabel();
        //lbl.setText("TEST");
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setTitle(label);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void DisplayImage(BufferedImage b) throws IOException
    {
        ImageIcon icon=new ImageIcon(b);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static BufferedImage byteArray2BufferedImage(byte[][] imageInByte, ImageInfo imageInfo) {

        	BufferedImage b = new BufferedImage(imageInfo.getHeigth(), imageInfo.getWidth(), BufferedImage.TYPE_USHORT_GRAY);
        	//b.
        	for (int i=0;i<imageInfo.getHeigth();i++) {
        		for (int j=0;j<imageInfo.getWidth();j++) {
        			b.setRGB(i, j, (byte)(imageInByte[i][j] * 255.0));
        		}
        	}
        	return b;
    }
	public static byte[][] basicMorphologicalOperations(byte[][] image, int k, Operations operations){
		// erozje moza tez zdefiniwac jako negatyw dalytacji negatywu obrazu.
		if (operations == Operations.Erosion) {
		image = negative(image);
		}
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[i].length; j++){
	            if (image[i][j] == 1){
	                for (int l=i-k; l<=i+k; l++){
	                    int remainingk = k-Math.abs(i-l);
	                    for (int m=j-remainingk; m<=j+remainingk; m++){
	                        if (l>=0 && m>=0 && l<image.length && m<image.length && image[l][m]==0){
	                            image[l][m] = 2;
	                        }
	                    }
	                }
	            }
	        }
	    }
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[i].length; j++){
	            if (image[i][j] == 2){
	                image[i][j] = 1;
	            }
	        }
	    }
	    
		if (operations == Operations.Erosion) {
		image = negative(image);
		}
	 
	    return image;
	}
	public static byte[][] negative(byte[][] image){
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[i].length; j++){
	        	image[i][j] = (byte) (1 - image[i][j]);
	        }
	    }
	    return image;
	}
	public static byte[][] findBoudaryByErosion(byte[][] image, int n,ImageInfo imageInfo, GCodeProperties gCodeProperties) {
		byte[][] firsrtErosion = copyArray(image);
		byte[][] secondErosion = copyArray(firsrtErosion);
		basicMorphologicalOperations(firsrtErosion,n-1,Operations.Erosion);
		basicMorphologicalOperations(secondErosion,n,Operations.Erosion);
		return imageSubstraction(firsrtErosion, secondErosion);
	}

	public static byte[][] copyArray(byte[][] image) {
		byte[][] results = new byte[image.length][image[0].length];
		
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[1].length; j++){
	        	results[i][j] = (byte) image[i][j];
	            
	        }
	    }
		return results;
	}
	public static byte[][] imageSubstraction(byte[][] image1, byte[][] image2){
		byte[][] output = new byte[image1.length][image1[0].length];
		int couter = 0;
		for(int i=0;i<image1.length; i++) {
			for(int j=0; j<image1[0].length;j++) {
				output[i][j] = (byte) (image1[i][j] - image2[i][j]);
				if (image1[i][j] != image2[i][j]) couter++;
			}
		}
		//System.out.println("couter = "+couter);
		return output;
	}
	public static boolean havePixel(byte[][] image, byte val, int min) {
		int counter = 0;
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[i].length; j++){
	        	if (image[i][j] == val) counter++;
	        	
	        	if(counter>=min) {
	        		return true;
	        	}
	        }
	    }
	    return false;
	}
	public static int[][] manhattanDistanceMap(int[][] image){
	    // traverse from top left to bottom right
	    for (int i=0; i<image.length; i++){
	        for (int j=0; j<image[i].length; j++){
	            if (image[i][j] == 1){
	                // first pass and pixel was on, it gets a zero
	                image[i][j] = 0;
	            } else {
	                // pixel was off
	                // It is at most the sum of the lengths of the array
	                // away from a pixel that is on
	                image[i][j] = image.length + image[i].length;
	                // or one more than the pixel to the north
	                if (i>0) image[i][j] = Math.min(image[i][j], image[i-1][j]+1);
	                // or one more than the pixel to the west
	                if (j>0) image[i][j] = Math.min(image[i][j], image[i][j-1]+1);
	            }
	        }
	    }
	    // traverse from bottom right to top left
	    for (int i=image.length-1; i>=0; i--){
	        for (int j=image[i].length-1; j>=0; j--){
	            // either what we had on the first pass
	            // or one more than the pixel to the south
	            if (i+1<image.length) image[i][j] = Math.min(image[i][j], image[i+1][j]+1);
	            // or one more than the pixel to the east
	            if (j+1<image[i].length) image[i][j] = Math.min(image[i][j], image[i][j+1]+1);
	        }
	    }
	    return image;
	}

    public static BufferedImage resizeImageWithHint(BufferedImage originalImage,Geometry geometry, ImageInfo imageInfo, int type){
		
    	float xScaling = GridUtils.getScalingFactor(geometry, Axis.OX);
    	float yScaling = GridUtils.getScalingFactor(geometry, Axis.OY);
    	
    	
    	System.out.println("Scaling factor is: x= "+yScaling+" y= "+xScaling);
    	int newX = (int) (imageInfo.getWidth() * xScaling);
    	int newY = (int) (imageInfo.getHeigth() * yScaling);
    	System.out.println("GRID IMAGE SIZE is: "+newY+" to "+newX);
    	
    	BufferedImage resizedImage = new BufferedImage(newX, newY, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, newX, newY, null);
    	g.dispose();	
    	g.setComposite(AlphaComposite.Src);

    	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    	g.setRenderingHint(RenderingHints.KEY_RENDERING,
    			RenderingHints.VALUE_RENDER_QUALITY);
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    	RenderingHints.VALUE_ANTIALIAS_ON);
    	g.scale(xScaling, yScaling);
	
    	return resizedImage;
    	}
    public static byte[][] createFrame(byte[][] image, ImageInfo imageInfo) {
    	if (!imageInfo.isUpdated()) {
    		imageInfo.setHeigth(imageInfo.getHeigth() + 1);
    		imageInfo.setWidth(imageInfo.getWidth() + 1);
    		}
    	byte[][] imageWithFrame = new byte[imageInfo.getHeigth()][imageInfo.getWidth()];
    	for (int i=1;i<imageInfo.getHeigth()-1;i++) {
    		for (int j=1;j<imageInfo.getWidth()-1;j++) {
    			imageWithFrame[i][j] = image[i-1][j-1];
    		}
    	}
    	return imageWithFrame;
    }
    public static void typeNeighborhood3x3(int[][] image, int i, int j,int component) {
    	System.out.println("=====Cell======");
    	System.out.println("i = "+i+" j = "+j+" componet = "+component);
    	for(int y=i-1; y<=i+1;y++) {
    		for(int x = j-1; x<=j+1;x++) {
    			if(GraphUtils.isOnImage(image, y,x)) {
    				System.out.print(" ["+image[y][x]+"] ");
    			}
    			
    		}
    		System.out.println("");
    	}
    	System.out.println("=====End======");
    }
    public static byte[][] visualizeGraph2(List<Point2D> pointList, int height, int width){
    	byte[][] base = new byte[height][width];
    		addLayer(base, visualizeLayer(pointList, height, width));
    	return base;
    }
    
    public static byte[][] visualizeGraph(List<Edge> edgeList, int height, int width){
    	byte[][] base = new byte[height][width];
    	for(Edge edge: edgeList) {
    		addLayer(base, visualizeLayer(edge.getAllPoints(), height, width));
    	}
    	return base;
    }
    public static byte[][] visualizeLayer(List<Point2D> points, int height, int width){
    	byte[][] image = new byte[height][width];
    	
    	for(Point2D point: points) {
    		image[(int)point.getyMM()][(int)point.getxMM()] = 1;
    	}
    	
    	return image;
    }
	public static void addLayer(byte[][] base, byte[][] newLayer) {
		for (int i =0 ; i< base.length; i++) {
			for(int j=0;j<base[0].length; j++) {
				base[i][j] = (byte) (base[i][j] + newLayer[i][j]);
			}
		}
	}
	public static void addLayer(int[][] base, int[][] newLayer) {
		for (int i =0 ; i< base.length; i++) {
			for(int j=0;j<base[0].length; j++) {
				base[i][j] = (byte) (base[i][j] + newLayer[i][j]);
			}
		}
	}
	
	public static byte[][] visualizeLayer2(List<GcodeGraph> layerGcodeList, int height, int width) {
		byte[][] image = new byte[height][width];
		for (GcodeGraph g: layerGcodeList) {
			if (g.isValid()) {
				if( g.getPathCodeType().equals(PathCodeType.PATH) ) {
					List<Point2D> points = g.getPath();
					for (int i = points.size()-1; i>=0;i--) {			
						image[(int)points.get(i).getyMM()][(int)points.get(i).getxMM()] = 1;
					}
				}
				if( g.getPathCodeType().equals(PathCodeType.GRAPH) ) {
					Set<Edge> paths = g.getSimpleGraph().edgeSet(); // narazie tylko wyciagania samych sciezek
					for (Edge edge : paths) {
						List<Point2D> points = edge.getSortedCurve();
						for (int i = points.size()-1; i>=0;i--) {
							image[(int)points.get(i).getyMM()][(int)points.get(i).getxMM()] = 1;
					}
						
					}
				}
			}
			else {
				//System.err.println("Non valid G-Code Graph object !");
			}
		}
		return image;
	}
	
    public static byte[][] standarizeImage(byte[][] img){
    	byte[][] standarized = new byte[img.length][img[0].length];
    	for (int i=0;i<img.length;i++) {
    		for(int j=0;j<img[0].length;j++) {
    			if(img[i][j]<0) standarized[i][j] = 0;
    			else standarized[i][j] = 1;
    		}
    	} 
    	return standarized;
   }
    public static byte[][] standarizeImage(int[][] img){
    	byte[][] standarized = new byte[img.length][img[0].length];
    	for (int i=0;i<img.length;i++) {
    		for(int j=0;j<img[0].length;j++) {
    			if(img[i][j]>0) standarized[i][j] = 1;
    			else standarized[i][j] = 0;
    		}
    	} 
    	return standarized;
   }
    public static byte[][] standarizeImageGrayLevel(int[][] img){
    	byte[][] standarized = new byte[img.length][img[0].length];
    	for (int i=0;i<img.length;i++) {
    		for(int j=0;j<img[0].length;j++) {
    			if(img[i][j]>0) standarized[i][j] = (byte) img[i][j];
    			else standarized[i][j] = 0;
    		}
    	} 
    	return standarized;
   }
    
    private static byte[][] prepareImage(byte[][] image) {
   		for (int i=0;i<image.length;i++) {
   			for(int j=0;j<image[0].length;j++) {
   				if(image[i][j] == 0) image[i][j] = (byte)255;
   				if(image[i][j] > 0) image[i][j] = 0;
   				}
   		}
   	return image;
    }
    
    private static byte[][] inverse(int[][] img){
    	byte[][] inversed = new byte[img.length][img[0].length];
    	for (int i=0;i<img.length;i++) {
    		for(int j=0;j<img[0].length;j++) {
    			if(img[i][j]<255) inversed[i][j] = 1;  		
    		}
    	} 
    	return inversed;
   }
    
    public static byte[][] skeletonize(byte[][] image, ImageInfo imageInfo) {
       	ByteProcessor ip = new ByteProcessor(imageInfo.getWidth(), imageInfo.getHeigth(), ImageUtils.convert1D(prepareImage(image), imageInfo));
		ip.convertToByte(true);
		ip.skeletonize();
		return inverse(ip.getIntArray());
    }
    public static byte[][] skeletonize(int[][] image, ImageInfo imageInfo) {
    	return skeletonize(ImageUtils.convertTypeInt2Byte(image), imageInfo);
    }
    
    public static List<Point> convertPoint2DToPoint(List<Point2D> sortedCurve){
    	List<Point> points = new ArrayList<Point>();
    	for (Point2D point: sortedCurve) {
    		points.add(new PointImpl((int)point.getxMM(), (int)point.getyMM()));
    	}
    	return points;
    }
    public static List<Point2D> convertPointToPoint2D(List<Point> reducedCurce){
    	List<Point2D> points = new ArrayList<Point2D>();
    	for (Point point: reducedCurce) {
    		points.add(new Point2D(point.getX(), point.getY()));
    	}
    	return points;
    }
    public static Set<Integer> uniqueVal(int[][] matrix){
    	Set<Integer> unique = new HashSet<Integer>();
    	for(int i=0; i<matrix.length;i++) {
    		for(int j=0;j<matrix[0].length;j++) {
    			unique.add(matrix[i][j]);
    		}
    	}
    	//System.out.println("Liczba wartosci unikalnych: "+unique.size());
    	//for( Integer val: unique) {
    	//	System.out.println("wartosc: "+val);
    	//}
		return unique;
    	
    }
}
