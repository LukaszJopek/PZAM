package core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gcode.GCodeGenerator;
import gcode.GCodeProperties;
import ij.ImagePlus;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import imageProcessing.BoundaryDetector;
import imageProcessing.Geometry;
import imageProcessing.Geometry.Axis;
import imageProcessing.ccl.ConnectComponent;
import imageProcessing.ccl.ImageLabel;
import imageProcessing.ccl.MatrixComponents;
import imageProcessing.graph.AdjacencyType;
import imageProcessing.thinning.Skeleton;
import imageProcessing.thinning.Thinning;
import sc.fiji.analyzeSkeleton.AnalyzeSkeleton_;
import sc.fiji.analyzeSkeleton.Edge;
import sc.fiji.analyzeSkeleton.Graph;
import sc.fiji.analyzeSkeleton.SkeletonResult;
import sc.fiji.analyzeSkeleton.Vertex;
import utils.GraphUtils;
import utils.ImageUtils;
import utils.ImageUtils.Operations;
import utils.Point2D;
import java.util.Random;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import core.reducer.Point;
import core.reducer.PointImpl;
import core.reducer.SeriesReducer;
import core.sorter.SortPoint;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Class<Main> m = Main.class;
		System.out.println("Main class fully name: "+m.getCanonicalName());
		Image image = new Image( "D:\\PL\\dydaktyka\\GIT\\Voxel2Gcode\\data\\lattice3D.raw",  70,  70,  60, 8);
		
		Geometry imageGeometry = new Geometry(image.getImageInfo(), 1f, 1f, 1f);
		float pos = imageGeometry.getPositionInMM(23, Axis.OX);
		//generateGcode(image);
		GCodeProperties gCodeProperties = new GCodeProperties();
		gCodeProperties.setFilamentStreamWidth(0.3f);

		BufferedImage originalImage = ImageUtils.byteArray2BufferedImage(image.getRaster(59), image.getImageInfo());
		BufferedImage resizedImage = ImageUtils.resizeImageWithHint(originalImage, imageGeometry, image.getImageInfo(), BufferedImage.TYPE_BYTE_GRAY);
		ImageUtils.DisplayImage(resizedImage);
		
		
    	ImageInfo imageInfo = new ImageInfo("Test", resizedImage.getHeight(), resizedImage.getHeight(), 1, 8);
		

		byte[][] erosionImage = ImageUtils.basicMorphologicalOperations(ImageUtils.convertToByteArray(resizedImage),1,Operations.Erosion);
		try {
			ImageUtils.DisplayImage(ImageUtils.convertToByteArray(resizedImage), imageInfo,"Orginal Resized");
			//ImageUtils.DisplayImage(erodedImage, imageInfo,"EROZJA");
			ImageUtils.DisplayImage(erosionImage, imageInfo,"Dilated");
			//ImageUtils.DisplayImage(erosionImage, imageInfo,"Erosion 2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		Simplify s ;
		
		List<GCodeBlock> gcodeBlockList = new ArrayList<GCodeBlock>();
		List<Point> curverPoints = new ArrayList<Point>();

		Point[] points = new Point[10];
		PointExtractor<Point> pointExtractor = null;
		Simplify<Point> simplify = new Simplify<Point>(new Point[0], pointExtractor);
		Object simplified = simplify.simplify(points, 20f, false);*/
		
/*        Random random = new Random();

        double[] points = new double[20];

        double[] fpoints;

        for (int i = 0; i < points.length; i++)

            points[i] = random.nextInt(10);

        RamerDouglasPeuckerFilter rdpf = new RamerDouglasPeuckerFilter(1);

        fpoints = rdpf.filter(points);

 

        System.out.println("Orginal points");

        for (int i = 0; i < points.length; i++)

            System.out.print(points[i] + " ");

 

        System.out.println("\nFiltered points");

        for (int i = 0; i < fpoints.length; i++)

            System.out.print(fpoints[i] + " ");  */     
	
		ImageInfo imageInfo2 = new ImageInfo("Test", 200, 200, 1, 8);
		float A = 50;
		float Frequency = 0.05f;
    	List<Point> Tpoints = new ArrayList<Point>();
    	
    	for (double x = 0; x < 200; x += 0.0001) {
    	    Tpoints.add(new PointImpl(x, (A * Math.sin(2*Math.PI * Frequency * x))+(A+1) ));
    	}
    	List<Point> reduced = SeriesReducer.reduce(Tpoints, 0.01);
    	System.out.println("");
    	System.out.println("Liczba punktow na wejsciu: "+Tpoints.size()+" Liczba punktow na wyjsciu: "+reduced.size());
    	
    	byte[][] imgTest= new byte[200][200];
    	byte[][] imgTestReduced= new byte[200][200];
    	for(int i=0;i<Tpoints.size();i++) {
    		Point p = Tpoints.get(i);
    		imgTest[(int)p.getY()][(int)p.getX()] = 127;
    		//System.out.println("x = "+p.getX()+" y= "+p.getY());
    		
    	}
    	for(int i=0;i<reduced.size();i++) {
    		Point p = reduced.get(i);
    		imgTestReduced[(int)p.getY()][(int)p.getX()] = 64;
    		
    	}

    	
    	ImageUtils.DisplayImage(imgTestReduced, imageInfo2,"reduced");
    	
    	
    	 int x = 1; // To wartoœæ przyk³adowa
         int y = 151; // To wartoœæ przyk³adowa
    	Random random = new Random();
    	List<Point2D> curvePoints = new ArrayList<Point2D>();
    	for (int i = 0 ; i<100;i++) {
    		curvePoints.add(new Point2D( random.nextInt(150) , random.nextInt(150) ));
    	}
    	
    	SortPoint sortPoint = new SortPoint(curvePoints);
    	List<Point2D> res = sortPoint.getSortedList();
    	
    	byte[][] sortTest =  new byte[200][200];
    	for (int i=0; i<res.size();i++) {
    		System.out.println("Angle = "+res.get(i).getAngle()+" yMM = "+res.get(i).getyMM()+" xMM = "+res.get(i).getxMM());
    		sortTest[(int)res.get(i).getyMM()][(int)res.get(i).getxMM()] = (byte) i;
    	}
    	//ImageUtils.DisplayImage(sortTest, imageInfo);
    	byte[][] b = ImageUtils.findBoudaryByErosion(ImageUtils.convertToByteArray(resizedImage),6, imageInfo, gCodeProperties);
    	ImageUtils.DisplayImage(b, imageInfo,"Boundary");
    	
    	ConnectComponent ccl = new ConnectComponent();
    	int[] labellingImage = ccl.compactLabeling(ImageUtils.convertInt1D(ImageUtils.convertToByteArray(resizedImage), imageInfo), imageInfo.getRasterDimentsion(), true);
    	//int[] labellingImage = ccl.compactLabeling(ImageUtils.convertInt1D(b, imageInfo), imageInfo.getRasterDimentsion(), true);
    	System.out.println("found "+ccl.getMaxLabel()+" objects");
    	byte[][] lab = ImageUtils.convert2D(labellingImage, imageInfo);
    	int[] t = ImageUtils.convertInt1D(ImageUtils.convertToByteArray(resizedImage), imageInfo);
    	//ImageUtils.DisplayImage(ImageUtils.convert2D(t, imageInfo), imageInfo, "Original converted");
    	//ImageUtils.DisplayImage(lab, imageInfo, "LABELLING 1");
    	
    	ImageLabel imageLabel = new ImageLabel(imageInfo.getWidth());
    	//int[] l2 = imageLabel.doLabel(ImageUtils.convertInt1D(b, imageInfo), imageInfo.getWidth(), imageInfo.getHeigth());
    	int[] l2 = imageLabel.doLabel(ImageUtils.convertInt1D(ImageUtils.convertToByteArray(resizedImage), imageInfo), imageInfo.getWidth(), imageInfo.getHeigth());
    	
    	for (int i=0;i<l2.length;i++) {
    		if(l2[i] > 0) l2[i] = 1;
    	}
    	
    	System.out.println("[labelling algoritm 2]found "+imageLabel.getNumberOfLabels()+" objects");
    	//ImageUtils.DisplayImage(ImageUtils.convert2D(l2, imageInfo), imageInfo, "LABELLING 2");
    	
    	//g=
    	  // MatrixComponents matrixComponents = new MatrixComponents(ImageUtils.convertInt2D( ImageUtils.convertInt1D(ImageUtils.convertToByteArray(resizedImage), imageInfo), imageInfo));
    	   MatrixComponents matrixComponents = new MatrixComponents(ImageUtils.convertInt2D( ImageUtils.convertInt1D(b, imageInfo), imageInfo));
    	   int[][] newMatrix = matrixComponents.getLabeledMatrix();
           System.out.println("Liczba zalezionych komponentow (algorytm nr 3): "+matrixComponents.getNumberOfComponents());
          // matrixComponents.showCCL();
       	for (int i=0;i<newMatrix.length;i++) {
       		for(int j=0;j<newMatrix[0].length;j++) {
    		if(newMatrix[i][j] > 0) newMatrix[i][j] = 1;
    		}
    	}
           //ImageUtils.DisplayImage(ImageUtils.convertType(newMatrix), imageInfo, "Label 4");
/*       	int i=0;
       	while(true) {
       		i++;
       		byte[][] bx = ImageUtils.findBoudaryByErosion(ImageUtils.convertToByteArray(resizedImage),i, imageInfo, gCodeProperties);
       		ImageUtils.DisplayImage(bx, imageInfo,"Boundary for iteration: "+i);
       		if (!ImageUtils.havePixel(bx, (byte) 1, 5)) {
       			break;
       		}
    	}*/
       	
       	//BoundaryDetector boundaryDetector = new BoundaryDetector(ImageUtils.convertToByteArray(resizedImage), imageInfo, gCodeProperties);
       //	boundaryDetector.execute();
       	byte[][] bx = ImageUtils.convertToByteArray(resizedImage);
       	int[][] ttt = ImageUtils.convertTypeByte2Int(bx);
       	
       	Thinning thinning = new Thinning();
       	int[][] thninned = thinning.doZhangSuenThinning(newMatrix, false);
       	ImageUtils.DisplayImage(ImageUtils.convertTypeInt2Byte(thninned), imageInfo, "Thinned Image");
       	
       	Image image2 = new Image( "D:\\PL\\dydaktyka\\GIT\\Voxel2Gcode\\data\\lattice3D.raw",  70,  70,  60, 8);
       	generateGcode(image2);
       	
//       	UndirectedGraph<String, DefaultEdge> g =
//       	        new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
//       
//       	for (int i=0;i<thninned.length;i++) {
//       		for(int j=0;j<thninned[0].length;j++) {
//    		if(thninned[i][j] > 0) thninned[i][j] = 255;
//    		}
//    	}
//       	
//       	byte[][] bt = new byte[b.length][b[0].length];
//       	for (int i=0;i<b.length;i++) {
//       		for(int j=0;j<b[0].length;j++) {
//    		bt[i][j] = (byte) (b[i][j]*128);
//    		}
//    	}
//       	
//       	Skeleton skeleton = new Skeleton(1);
//       	byte[][] skeletonizationResults = skeleton.skeletonize(bt);
//		
//       	for (int i=0;i<skeletonizationResults.length;i++) {
//       		for(int j=0;j<skeletonizationResults[0].length;j++) {
//    		if(skeletonizationResults[i][j] > 0) skeletonizationResults[i][j] = 127;
//    		if(bt[i][j] > 0) bt[i][j] = 127;
//    		}
//    	}
//       	ImageUtils.DisplayImage(b, imageInfo, "BX");
//       	ImageUtils.DisplayImage(skeletonizationResults, imageInfo, "Skeletonize Image by Skeleton Algorithm");       	
//       	ImageUtils.DisplayImage(bt, imageInfo, "Skeletonize Image by Skeleton Algorithm 2");
//       	
//       	int[][] testImg = new int[newMatrix.length][newMatrix[0].length];
//       	for (int i=0;i<newMatrix.length;i++) {
//       		for(int j=0;j<newMatrix[0].length;j++) {
//    		if(newMatrix[i][j] == 0) testImg[i][j] = 255;
//    		if(newMatrix[i][j] > 0) testImg[i][j] = 0;
//    		}
//    	}
//       	
//       	ByteProcessor ip = new ByteProcessor(imageInfo.getWidth(), imageInfo.getHeigth(), ImageUtils.convert1D(ImageUtils.convertTypeInt2Byte(testImg), imageInfo));
//		ip.convertToByte(true);
//		//ip.invert();
//		ip.skeletonize();
//		//ip.findEdges();
//	
//		//ip.
//		//ip.erode(1, 1);
//		//ip.dilate(1, 1);
//		int[][] img = ip.getIntArray();
//		
//       	//BinaryProcessor bp = new BinaryProcessor(ip);
//		//bp.skeletonize();
//		//ip.skeletonize();
//		
//		//int[][] img = bp.getIntArray();
//		byte[][] test = new byte[img.length][img[0].length];
//		
//       	for (int i=0;i<img.length;i++) {
//       		for(int j=0;j<img[0].length;j++) {
//       			if(img[i][j]<255) test[i][j] = 1;
//
//    		if(img[i][j] > 0) img[i][j] = 127;  		
//    		}
//    	}
//       	
//		System.out.println("h = "+img.length+" w = "+img[0].length);
//		ImageUtils.DisplayImage(ImageUtils.convertTypeInt2Byte(img), imageInfo, "Skeletonize Image by ByteProcessor");
//		ImageUtils.DisplayImage(test, imageInfo, "Test Image");
//		//ImageUtils.basicMorphologicalOperations(test, 1, Operations.Erosion);
//		//ImageUtils.DisplayImage(test, imageInfo, "Test Image After Erosion");
//		
//		int counter = 0;
//       	for (int i=0;i<test.length;i++) {
//       		for(int j=0;j<test[0].length;j++) {
//
//       			if(test[i][j] > 0) counter++;  		
//    		}
//    	}
//		System.out.println("liczba punktow <> 0 wynosi "+counter);
//		
//		byte[][] test2 = new byte[img.length][img[0].length];
//			for(int i=0;i<test.length;i++) {
//				for (int j=0;j<test[0].length;j++) {
//					if(GraphUtils.getAdjacecnyType(test, i, j, 1)  == AdjacencyType.End_Point) {
//						test2[i][j] = 127;
//					}
//				}
//			}
//			ImageUtils.DisplayImage(test2, imageInfo, "Branch Points");
//			counter = 0;
//	       	for (int i=0;i<test.length;i++) {
//	       		for(int j=0;j<test[0].length;j++) {
//
//	       			if(test2[i][j] > 0) counter++;  		
//	    		}
//	    	}
//			System.out.println("[Branch Points]liczba punktow <> 0 wynosi "+counter);
//		
//		
//		//	g.
//		System.out.println("minVal = "+ip.minValue()+" maxVal = "+ip.maxValue());
//       	ImagePlus imagePlus =new ImagePlus("Graph", ip );
//       	AnalyzeSkeleton_ skel = new AnalyzeSkeleton_();
//       	//skel.calculateShortestPath = true;
//       	skel.setup("", imagePlus);
//       	SkeletonResult skelResult = skel.run(AnalyzeSkeleton_.NONE, false, false, null, true, true);
//       	Graph[] graph = skelResult.getGraph();
//       	
//       	byte[][] imageOutTest = new byte[ip.getHeight()][ip.getWidth()];
//       	
//       	for(int i = 0 ; i < graph.length; i++ )
//       	{
//       	    ArrayList<Edge> listEdges = graph[i].getEdges();
//       	    ArrayList<Vertex> v = graph[i].getVertices();
//       	    System.out.println("Liczba verteksow w grafie "+i+" wynosi "+v.size()+" liczba punktow = "+  v.get(1).getPoints().size());
//       	  
//       	 
//       	    // go through all branches and remove branches under threshold
//       	    // in duplicate image
//       	    for( Edge e : listEdges )
//       	    {
//       	    	System.out.println("dlugosc krawedzi: "+e.getSlabs().size()+" [ graf: "+i+" ]");
//       	    	for(sc.fiji.analyzeSkeleton.Point p: e.getSlabs()) {    	
//       	    		imageOutTest[p.y][p.x] = 127;
//       	    	}
//       	    }
//       	}
//       	
//       	ImageUtils.DisplayImage(imageOutTest, imageInfo, "Image Reconstruct form Graph");
       	
       	System.out.println("Done.");
	
}
	
	private static void generateGcode(Image image) {
		GCodeProperties gCodeProperties = new GCodeProperties();
		gCodeProperties.setParameterE(0.2345f);
		gCodeProperties.setParameterF(200.0f);
		GCodeGenerator gcodeGenerator = new GCodeGenerator(image, gCodeProperties);
		gcodeGenerator.execute();
		
	}
	//private void 
	

}
