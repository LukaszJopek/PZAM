package src.core;

import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import core.ImageInfo;

public class IO {
	public static byte[] readAlternateImpl(ImageInfo imageInfo){
		
		log("Reading in binary file named : " + imageInfo.getFileName());
		File file = new File(imageInfo.getFileName());
		log("File size: " + file.length());
		byte[] result = null;
		 try {
		 		InputStream input =  new BufferedInputStream(new FileInputStream(file));
		 		result = readAndClose(input,imageInfo);
		    }
		    catch (FileNotFoundException ex){
		    	log(ex);
		    }
		    return result;
		  }

	private static byte[] readAndClose(InputStream aInput, ImageInfo imageInfo){
		    //carries the data from input to output :
			int imageSize = imageInfo.getImageSizein();
		    byte[] bucket = new byte[imageSize]; 
		    ByteArrayOutputStream result = null; 
		    try  {
		      try {
		        //Use buffering? No. Buffering avoids costly access to disk or network;
		        //buffering to an in-memory stream makes no sense.
		        result = new ByteArrayOutputStream(bucket.length);
		        int bytesRead = 0;
		        while(bytesRead != -1){
		          //aInput.read() returns -1, 0, or more :
		          bytesRead = aInput.read(bucket);
		          if(bytesRead > 0){
		            result.write(bucket, 0, bytesRead);
		          }
		        }
		      }
		      finally {
		        aInput.close();
		        //result.close(); this is a no-operation for ByteArrayOutputStream
		      }
		    }
		    catch (IOException ex){
		      log(ex);
		    }
		    return result.toByteArray();
		  }
		  
		  private static void log(Object aThing){
		    System.out.println(String.valueOf(aThing));
		  }
}
