package com.sunshineoxygen.inhome.utils;


import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.SeekableStream;
import com.sunshineoxygen.inhome.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class ImageUtil {




	public static File resizeImage(File file, int maxWidth) throws ApplicationException {
		try{
			BufferedImage thumb = Scalr.resize(readImage(file), maxWidth);
			saveImage(thumb, file);
		}catch(Exception e) {
			e.printStackTrace();
		}
	    return file;
   }

	public static void saveImage(BufferedImage bufImage, String fileExtension, OutputStream os) throws ApplicationException {
		try{
	        if (fileExtension.endsWith("png")) {
	            ImageIO.write(bufImage, "PNG" , os);
	        } else if (fileExtension.endsWith("jpeg")) {
	            ImageIO.write(bufImage, "jpg" , os);
	        } else if (fileExtension.endsWith("jpg")) {
	            ImageIO.write(bufImage, "jpg" , os);
	        } else if (fileExtension.endsWith("bmp")) {
	            ImageIO.write(bufImage, "bmp" , os);
	        } else {
	            ImageIO.write(bufImage, "GIF" , os);
	        }
	        //os.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
				}
	        }
		}
	}
	public static void saveImage(BufferedImage bufImage, File file) throws ApplicationException{
		FileOutputStream os = null;
		try{
			os = new FileOutputStream(file);
			String fileExtension = FileUtil.getSuffix(file.getName());
			saveImage(bufImage, fileExtension, os);
	        os.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}
	public static BufferedImage readImage(File file) throws ApplicationException {
		JAI.disableDefaultTileCache();
		SeekableStream seekableStream = null;
		try{
			String fileExtension = FileUtil.getSuffix(file.getName());

			seekableStream =  new FileSeekableStream(file);
			ParameterBlock pb = new ParameterBlock();
			pb.add(seekableStream);

			BufferedImage image = null;
			if (fileExtension.endsWith("png")) {
				image = JAI.create("png", pb).getAsBufferedImage();
            } else if (fileExtension.endsWith("jpeg") || fileExtension.endsWith("jpg")) {
            	image = JAI.create("jpeg", pb).getAsBufferedImage();
            } else if (fileExtension.endsWith("bmp")) {
            	image = JAI.create("bmp", pb).getAsBufferedImage();
            } else {
            	image = JAI.create("gif", pb).getAsBufferedImage();
            }
			seekableStream.close();

			return image;
		}catch(Exception e) {
			log.error("ImageUtil.readImage", file.getPath(), e.getLocalizedMessage());
		}finally{
			if(seekableStream!=null)
				try {
					seekableStream.close();
				} catch (IOException e) {

				}
		}
		return null;
	}

}
