package DataQuery;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ReadColor {
	
		/*
		public static void main(String[] args) throws Exception {
			int x = 0;
			ReadColor rc = new ReadColor();
			x = rc.getScreenPixel(100, 345);
			System.out.println(x + " - ");
			rc.getImagePixel("D:\\car.bmp");
		}
		 */
		/**
		 * 读取照片rgb值，并与目标rgb值做比较，计算在阈值范围内的像素点，返回计算好的频率和场强数组
		 * @param image 图片路径
		 * @param target 目标颜色rgb数组
		 * @param threshold 阈值
		 * @param minLevels 最小场强
		 * @param maxLevels 最大场强
		 * @param minFrequences 最小频率
		 * @param maxFrequences 最大频率
		 * @return 与目标颜色相近的频率和场强数组
		 */
		public static LinkedList<ArrayList<Double>> getImagePixel(String image,int[] target,Double threshold
				,Double minLevels,Double maxLevels,Double minFrequences,Double maxFrequences){
			LinkedList<ArrayList<Integer>> ll = new LinkedList<ArrayList<Integer>>();
			int[] rgb = new int[3];
			File file = new File(image);
			if (!file.exists()) {
                return null;
           }
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			int width = bi.getWidth();
			int height = bi.getHeight();
			int minx = bi.getMinX();
			int miny = bi.getMinY();
			
			/*有用的像素点的最大值和最小值,对他们反向赋值*/
			int minUsefulx = 0;
			int maxUsefulx = 0;
			int minUsefuly = height;
			int maxUsefuly = miny;
			int count = 0;
			
			for (int i = minx; i < width; i++) {
				for (int j = miny; j < height; j++) {
					int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					//System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","+ rgb[1] + "," + rgb[2] + ")");
					/*判断像素是否在阈值范围之内*/
					if(Math.sqrt(Math.pow(rgb[0]-target[0],2)+Math.pow(rgb[1]-target[1],2)+Math.pow(rgb[2]-target[2],2))<threshold){
						/*第一次进来时的x值，为最小x值*/
						count++;
						if(count == 1){
							minUsefulx = i;
						}
						/*最后一次的x值为最大x值*/
						maxUsefulx = i;
						if(j < minUsefuly){
							minUsefuly = j;
						}
						if(j > maxUsefuly){
							maxUsefuly = j;
						}
						ArrayList<Integer> al = new ArrayList<Integer>();
						al.add(i);
						al.add(j);
						//System.out.println(al);
						ll.add(al);
						//每一个x值，只取一个y值，取到值后，判断下一个x值。
						break;
					}
				}
			}
			/*
			System.out.println("width=" + width + ",height=" + height + ".");
			System.out.println("minx=" + minx + ",miniy=" + miny + ".");
			System.out.println("image="+image+" target="+target[0]+","+target[1]+","+target[2]+" threshold="+threshold+" count="+count);
			System.out.println("minUsefulx="+minUsefulx+",maxUsefulx="+maxUsefulx+",minUsefuly="+minUsefuly+",maxUsefuly="+maxUsefuly);
			System.out.println("minLevels="+minLevels+",maxLevels="+maxLevels+",minFrequences="+minFrequences+",maxFrequences="+maxFrequences);
			*/
			return getData(minUsefulx,maxUsefulx,minUsefuly,maxUsefuly,minLevels,maxLevels,minFrequences,maxFrequences,ll);
		}
		/**
		 * 获取像素点所代表的场强和频率
		 * @param minUsefulx x方向像素点最小值
		 * @param maxUsefulx x方向像素点最大值
		 * @param minUsefuly y方向像素点最小值
		 * @param maxUsefuly y方向像素点最大值
		 * @param minLevels 最小场强（对应y方向）
		 * @param maxLevels 最大场强（对应y方向）
		 * @param minFrequences 最小频率（对应x方向）
		 * @param maxFrequences 最大频率（对应x方向）
		 * @param ll 像素二维数组
		 * @return
		 */
		private static LinkedList<ArrayList<Double>> getData(int minUsefulx,int maxUsefulx,int minUsefuly,int maxUsefuly,
				Double minLevels,Double maxLevels,Double minFrequences,Double maxFrequences,LinkedList<ArrayList<Integer>>ll){
			
			Double frequencesRange = maxFrequences - minFrequences;//频率范围
			Double levelsRange = maxLevels - minLevels;//场强范围
			int xRange = maxUsefulx - minUsefulx;//x像素范围
			int yRange = maxUsefuly - minUsefuly;//y像素范围
			LinkedList<ArrayList<Double>> data = new LinkedList<ArrayList<Double>>();
			Double frequences = 0.0;
			Double levels = 0.0;
			int size = ll.size();
			for(int i=0;i<size;i++){
				frequences = (ll.get(i).get(0) - minUsefulx)*frequencesRange/xRange + minFrequences;
				BigDecimal bg = new BigDecimal(frequences);  
				frequences = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();  

				levels = maxLevels - (ll.get(i).get(1)-minUsefuly)*levelsRange/yRange;
				BigDecimal bd = new BigDecimal(levels);  
				levels = bd.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				ArrayList<Double> al = new ArrayList<Double>();
				al.add(frequences);
				al.add(levels);
				//System.out.println(al);
				data.add(al);
			}
			return data;
		}
		
		/**
		 * 返回屏幕色彩值
		 * 
		 * @param x
		 * @param y
		 * @return
		 * @throws AWTException
		 */
		public int getScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。
			Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
			rb = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
			Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
			System.out.println(di.width);
			System.out.println(di.height);
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = rb.createScreenCapture(rec);
			int pixelColor = bi.getRGB(x, y);

			return 16777216 + pixelColor; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
		}
}

	
	

