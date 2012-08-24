package org.ccar.app;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * 图形计算工具类
 * @author swansword
 *
 */
public class GraphicCalcUtil {
	/**
	 * 计算两点距离
	 * @param p1 点1
	 * @param p2 点2
	 * @return 两点间距离
	 */
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
	
	/**
	 * 计算两点距离
	 * @param x1 点1的x坐标
	 * @param y1 点1的y坐标
	 * @param x2 点2的x坐标
	 * @param y2 点2的y坐标
	 * @return 两点间距离
	 */
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	/**
     * 判断点是否在矩形内
     * @param point 目标点
     * @param rect 目标矩形
     * @return 目标点在目标矩形内返回true，否则返回false
     */
    public static boolean isPointInRectangle(Point point, Rect rect) {
    	return rect.contains(point.x, point.y);
    }
    
    /**
     * 判断点是否在圆内
     * @param point 目标点
     * @param centre 圆心坐标
     * @param radius 半径
     * @return 目标点在圆内返回true，否则返回false
     */
    public static boolean isPointInCircle(Point point, Point centre, int radius) {
    	return getDistance(point, centre) < radius;
    }
}
