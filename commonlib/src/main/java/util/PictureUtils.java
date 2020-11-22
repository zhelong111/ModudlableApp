package util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtils {
	public static final int REQUEST_CODE_IMAGE = 0x100; // 	请求相册
	public static final int REQUEST_CODE_CAMERA = 0x101; // 请求相机
	
	/**
	 * @deprecated
	 * 打开相册选择图片
	 * if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
	      Uri uri = data.getData();
	      Cursor cursor = getContentResolver().query(uri, null, null, null,null);
	      if (cursor != null && cursor.moveToFirst()) {
	          String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
	    }
	 * @param context
	 */
	 public static void pickPicutre(Activity context) {
		 Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 context.startActivityForResult(intent, REQUEST_CODE_IMAGE);
	 }
	 
	 /**
	  * 打开相机，拍照后将照片保存到指定路径
	  * @param context
	  * @param targetPath
	  */
	 public static void openCamera(Activity context, String targetPath) {
		 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//path为保存图片的路径，执行完拍照以后能保存到指定的路径下
		File file = new File(targetPath);
		Uri imageUri = Uri.fromFile(file );
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
	 }

	 /**
	  * 打开相机，拍照后，相片数据手动解析
	  * if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
	      Bundle bundle = data.getExtras();
	      // 获取相机返回的数据，并转换为Bitmap图片格式 ，这是缩略图
	      Bitmap bitmap = (Bitmap) bundle.get("data");
	   }
	  * @param context
	  */
	 public static void openCamera(Activity context) {
		 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
	 }
	 
	 // ---------------------------------------- 压缩图片 ------------------------------
	 private static Bitmap compressImage(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024> 500) {	//循环判断如果压缩后图片是否大于200kb,大于继续压缩
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 5;//每次都减少10
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
			return bitmap;
		}
	 
	 // ---------------------------------------- 压缩图片 ------------------------------
	 public static String compressAndReturnPath(Bitmap image) { 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024>300) {	//循环判断如果压缩后图片是否大于300kb,大于继续压缩
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;//每次都减少10
			}
			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/faccpptest/";
			File outDir = new File(outPath);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File target = new File(outDir, "compress.jpg"); 
			BufferedOutputStream bos = null;
			int len = 0;
			byte[] buffer = new byte[1024];
			try {
				bos = new BufferedOutputStream(new FileOutputStream(target));
				while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
					bos.write(buffer, 0, len);
				}
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return target.getAbsolutePath();
	 }
	 
	 // ---------------------------------------- 压缩图片 ------------------------------
	 public static String compressAndReturnPath(String filePath, int quality) {
		 	Bitmap image = getImage(filePath);
			// 旋转图片
		 	int degree = readPictureDegree(filePath);
		 	image = rotateBitmap(degree, image);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024 > 300) {	//循环判断如果压缩后图片是否大于300kb,大于继续压缩
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 5;//每次都减少10
			}
			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/two/";
			File outDir = new File(outPath);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
		 	String suffix = ".jpg";
		 	if (filePath.endsWith(".png")) {
				suffix = ".png";
			} else if (filePath.endsWith(".jpg")) {
				suffix = ".jpg";
			} else if (filePath.endsWith(".gif")) {
				suffix = ".gif";
			} else if (filePath.endsWith(".jpeg")) {
				suffix = ".jpeg";
			} else if (filePath.endsWith(".mp4")) {
				suffix = ".mp4";
			} else if (filePath.endsWith(".3gp")) {
				suffix = ".3gp";
			}
//			File target = new File(outDir, "compress" + suffix);
		    File target = new File(outDir, TimeUtils.sdfFileName.format(new Date())
					+ SystemClock.currentThreadTimeMillis() + suffix);
			BufferedOutputStream bos = null;
			int len = 0;
			byte[] buffer = new byte[2048];
			try {
				bos = new BufferedOutputStream(new FileOutputStream(target));
				while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
					bos.write(buffer, 0, len);
				}
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		 	if (image != null) {
				image.recycle();
				image = null;
			}
			return target.getAbsolutePath();
	 }
	 
	 public static Bitmap getImage(String srcPath) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			//开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
			
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//			float hh = 800f;//这里设置高度为800f
//			float ww = 480f;//这里设置宽度为480f
			 float hh = 1600f;//这里设置高度为800f
			 float ww = 1080f;//这里设置宽度为480f
			//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
			if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;

			newOpts.inSampleSize = be;//设置缩放比例
			//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			return bitmap;
	 }


	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				default:
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/*
        * 旋转图片
        * @param angle
        * @param bitmap
        * @return Bitmap
        */
	public static Bitmap rotateBitmap(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();
        matrix.postRotate(angle);
//		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}


	private static Bitmap comp(Bitmap image) {  
	      
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	    if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
	        baos.reset();//重置baos即清空baos  
	        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
	    }  
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
	    float hh = 800f;//这里设置高度为800f  
	    float ww = 480f;//这里设置宽度为480f  
	    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	    int be = 1;//be=1表示不缩放  
	    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;//设置缩放比例  
	    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
	}  

	public static String getPicturePath() {
		String tempImgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
				+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+ ".jpg";
		return tempImgPath;
	}

	/**
	 * 获取本地图片宽高
	 * @param imgPath
	 * @return
	 */
	public static int[] getImgWidthHeight(String imgPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		int width = options.outWidth;
		int height = options.outHeight;
		return new int[] {width, height};
	}


	/**
	 * 根据原图和变长绘制圆形图片
	 *
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * 根据原图添加圆角
	 *
	 * @param source
	 * @return
	 */
	public static Bitmap createRoundConerImage(Bitmap source, int sideLength, float radius) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(sideLength, sideLength, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

}
