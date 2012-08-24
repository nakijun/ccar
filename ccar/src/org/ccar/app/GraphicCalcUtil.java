package org.ccar.app;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * ͼ�μ��㹤����
 * @author swansword
 *
 */
public class GraphicCalcUtil {
	/**
	 * �����������
	 * @param p1 ��1
	 * @param p2 ��2
	 * @return ��������
	 */
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
	
	/**
	 * �����������
	 * @param x1 ��1��x����
	 * @param y1 ��1��y����
	 * @param x2 ��2��x����
	 * @param y2 ��2��y����
	 * @return ��������
	 */
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	/**
     * �жϵ��Ƿ��ھ�����
     * @param point Ŀ���
     * @param rect Ŀ�����
     * @return Ŀ�����Ŀ������ڷ���true�����򷵻�false
     */
    public static boolean isPointInRectangle(Point point, Rect rect) {
    	return rect.contains(point.x, point.y);
    }
    
    /**
     * �жϵ��Ƿ���Բ��
     * @param point Ŀ���
     * @param centre Բ������
     * @param radius �뾶
     * @return Ŀ�����Բ�ڷ���true�����򷵻�false
     */
    public static boolean isPointInCircle(Point point, Point centre, int radius) {
    	return getDistance(point, centre) < radius;
    }
}
