package core;

import java.awt.Dimension;

public class ImageInfo {
	private int width;
	private int heigth;
	private int depth;
	private int nBits;
	private String fileName;
	PreviousState previousState;
	
	
	private class PreviousState{
		private int width;
		private int heigth;
		private int depth;
		private int nBits;
		private String fileName;
		
		public PreviousState(String fileName, int width, int heigth, int depth, int nbits) {
			this.fileName = fileName;
			this.width = width;
			this.heigth = heigth;
			this.depth = depth;
	}
		public boolean isUpdated(ImageInfo imageInfo) {
			if (imageInfo.getFileName().contentEquals(this.fileName)) {
				return false;
			}
			if (imageInfo.getHeigth() != this.heigth) {
				return false;
			}
			if (imageInfo.getWidth() != this.width) {
				return false;
			}
			if (imageInfo.getDepth() != this.depth) {
				return false;
			}
			if (imageInfo.getNBits() != this.nBits) {
				return false;
			}
			return true;
		}
		public boolean isUpdated(String fileName, int width, int heigth, int depth, int nBits) {
			if (!fileName.contentEquals(this.fileName)) {
				return true;
			}
			if (heigth != this.heigth) {
				return true;
			}
			if (width != this.width) {
				return true;
			}
			if (depth != this.depth) {
				return true;
			}
			if (nBits != this.nBits) {
				return true;
			}
			return false;
		}
		public int getWidth() {
			return width;
		}
		public int getHeigth() {
			return heigth;
		}
		public int getDepth() {
			return depth;
		}
		public int getnBits() {
			return nBits;
		}
		public String getFileName() {
			return fileName;
		}
}
	
	public ImageInfo(String fileName, int width, int heigth, int depth, int nbits) {
		this.fileName = fileName;
		this.width = width;
		this.heigth = heigth;
		this.depth = depth;
		this.previousState = new PreviousState(fileName, width, heigth, depth, nbits);
	}
	public String getFileName() {
		return fileName;
	}	
	public int getWidth() {
		return width;
	}
	public int getHeigth() {
		return heigth;
	}
	public int getDepth() {
		return depth;
	}
	public int getNBits() {
		return nBits;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getImageSizein() {
		return width * heigth * depth;
	}
	public int getImageSizeinByte() {
		return width * heigth * depth * nBits;
	}
	public Dimension getRasterDimentsion() {
		return new Dimension(heigth, width);
	}
	public boolean isUpdated() {
		return previousState.isUpdated (fileName, width, heigth, depth, nBits);
	}
	public Dimension getOriginalDimension() {
		Dimension dimension = new Dimension(previousState.getHeigth(), previousState.width);
		return dimension;
	}
	
}
