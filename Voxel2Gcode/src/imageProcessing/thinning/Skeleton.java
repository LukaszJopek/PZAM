package imageProcessing.thinning;

/**
 *
 * @author Šarūnas Gruodis
 *
 * source: http://www.cyut.edu.tw/~yltang/course/image%20processing/notes-ip09.pdf
 */


public class Skeleton {
  private byte image[][], edge[][];
  private int threshold = 128;

  public Skeleton(){}

  // Constructor takes threshold for binarization.
  public Skeleton(int threshold){
      this.threshold = threshold;
  }


  //Input image matrix, output skeletonized image matrix. Rewrites original.
  public byte[][] skeletonize(byte img[][]) {
    this.image = img;
    int  x, y;
    boolean repeat;
    int rows = img[0].length;
    int cols = img.length;
    edge = new byte[rows][cols];

  
    for (x = 0; x < rows; x++)
      for (y = 0; y < cols; y++) {
        img[x][y] = (byte) (((img[x][y] & 0xFF) < threshold) ? 0 : 1);   //binarize image;
        edge[x][y] = 0;
      }

    // Make image borders equal zero
    for (x = 0; x < rows; x++)
      img[x][0] = img[x][cols-1] = 0;
    for (y = 0; y < cols; y++)
      img[0][y] = img[rows - 1][y] = 0;

    do {
      repeat = false;
      // step1 definition 1 -----------------
      for (x = 1; x < rows-1; x++)
        for (y = 1; y < cols-1; y++)
          if (img[x][y] != 0)
	    if (stepEdge1(x, y))
              edge[x][y] = 1;

      // delete edges
      for (x = 1; x < rows-1; x++)
        for (y = 1; y < cols-1; y++)
          if (edge[x][y] == 1) {
            img[x][y] = edge[x][y] = 0;
	    repeat = true;
	  }

      // step2  definition 2 -----------------
      for (x = 1; x < rows-1; x++)
        for (y = 1; y < cols-1; y++)
          if (img[x][y] != 0)
	    if (stepEdge2(x, y))
              edge[x][y] = 1;

      // delete edges
      for (x = 1; x < rows-1; x++)
        for (y = 1; y < cols-1; y++)
          if (edge[x][y] == 1) {
            img[x][y] = edge[x][y] = 0;
	    repeat = true;
	  }
    } while (repeat == true);

    // output
    for (x = 0; x < rows; x++)
      for (y = 0; y < cols; y++)
        img[x][y] = (byte) ((img[x][y] == 1) ? 0 : 255);  //back to grayscale for output;
    return img;
  }


  // definition 1
  private boolean stepEdge1(int x, int y) {
    if (2<=N(x, y) && N(x, y)<=6 && S(x, y)==1 &&
        image[x-1][y]*image[x][y+1]*image[x+1][y]==0 &&     // p2*p4*p6 == 0
        image[x][y+1]*image[x+1][y]*image[x][y-1]==0)       // p4*p6*p8 == 0
      return true;
    return false;
  }


  // definition 2
  private boolean stepEdge2(int x, int y) {
    if (2<=N(x, y) && N(x, y)<=6 && S(x, y)==1 &&
        image[x-1][y]*image[x][y+1]*image[x][y-1]==0 &&     // p2*p4*p8 == 0
        image[x-1][y]*image[x+1][y]*image[x][y-1]==0)       // p2*p6*p8 == 0
      return true;
    return false;
  }


  // Calculate how many 1: p2-p9
  private int N(int x, int y) {
    int  i, j, sum = 0;
    for (i= x-1; i <= x+1; i++)
      for (j= y-1; j <= y+1; j++) {
        if (i == x && j == y)
          continue;
        sum += image[i][j];
      }
    return sum;
  }


  // perejimu skaicius is 1 i 0
  private int S(int x, int y) {
    int  total = 0;
    if (image[x-1][y] - image[x-1][y+1] == -1) total++;    // p2 --> p3
    if (image[x-1][y+1] - image[x][y+1] == -1) total++;    // p3 --> p4
    if (image[x][y+1] - image[x+1][y+1] == -1) total++;    // p4 --> p5
    if (image[x+1][y+1] - image[x+1][y] == -1) total++;    // p5 --> p6
    if (image[x+1][y] - image[x+1][y-1] == -1) total++;    // p6 --> p7
    if (image[x+1][y-1] - image[x][y-1] == -1) total++;    // p7 --> p8
    if (image[x][y-1] - image[x-1][y-1] == -1) total++;    // p8 --> p9
    if (image[x-1][y-1] - image[x-1][y] == -1) total++;    // p9 --> p2
    return total;
  }


}