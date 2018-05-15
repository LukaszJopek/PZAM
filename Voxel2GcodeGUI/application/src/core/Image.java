package src.core;

public class Image {
private ImageInfo imageInfo;
private byte[] pixelData;

	public Image(String fileName, int width, int heigth, int depth,int nBits) {
		this.imageInfo = new ImageInfo(fileName, width, heigth, depth,nBits);
		pixelData = createImage();
	}
	private byte[] createImage(ImageInfo imageInfo) {
		pixelData = IO.readAlternateImpl(imageInfo);
		return pixelData;
		
	}
	private byte[] createImage() {
		pixelData = IO.readAlternateImpl(imageInfo);
		return pixelData;
		
	}
	public ImageInfo getImageInfo() {
		return imageInfo;
	}
	public byte[] getPixelData() {
		return pixelData;
	}
	public byte[][] getRaster(int id) {
		byte[][] raster = new byte[imageInfo.getHeigth() ][ imageInfo.getWidth()];	
/*		System.out.println("pixelData length = "+pixelData.length);
		System.out.println("raster length =  ( y : "+raster.length+" x : "+raster[0].length+" )");
		System.out.println("image Heigth = "+imageInfo.getOriginalDimension().getHeight());
		System.out.println("image Width = "+imageInfo.getOriginalDimension().getWidth());*/
		for(int i = 0; i<imageInfo.getOriginalDimension().getHeight() ; i++) {
			for (int j = 0; j<imageInfo.getOriginalDimension().getWidth(); j++){	
				try {
					raster[i][j] = pixelData[ImageUtils.convert1D(imageInfo.getOriginalDimension(), j, i, id)];
					}
				catch(Exception e) {
					System.out.println("Error in i= "+i+" j= "+j+" id(z) = "+id+" give -->"+ImageUtils.convert1D(imageInfo, j, i, id)+" / "+pixelData.length);
					return null;
				}
				
				}
			}
		return ImageUtils.createFrame(raster, imageInfo);
	}
}
