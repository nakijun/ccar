package org.ccar.task;

import java.io.IOException;

import org.ccar.R;
import org.ccar.http.HttpUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

/**
 * �ṩ·�����������
 * 
 * @author FG
 * 
 */
public class RouteTask {

	/**
	 * �����ַ��
	 */
	private String url;

	/**
	 * ��ȡ·�������ַ��
	 * 
	 * @return �����ַ��
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * ����·�������ַ��
	 * 
	 * @param url
	 *            ��·�������ַ����Ϊ��ֵ��
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * ·����㡣
	 */
	private Point startPoint;

	/**
	 * ��ȡ·����㡣
	 * 
	 * @return ·����㡣
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * ����·����㡣
	 * 
	 * @param startPoint
	 *            ��·���������Ϊ��ֵ��
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * ·���յ㡣
	 */
	private Point endPoint;

	/**
	 * ��ȡ·���յ㡣
	 * 
	 * @return ·���յ㡣
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * ����·���յ㡣
	 * 
	 * @param endPoint
	 *            ��·���յ�����Ϊ��ֵ��
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * ·�����ȡ�
	 */
	private double length;

	/**
	 * ��ȡ·�����ȡ�
	 * 
	 * @return ·�����ȡ�
	 */
	public double getLength() {
		return length;
	}

	/**
	 * ·����
	 */
	private Polyline result;

	/**
	 * ��ȡ·����
	 * 
	 * @return ·����
	 */
	public Polyline getResult() {
		return result;
	}

	/**
	 * ���캯����
	 * 
	 * @param url
	 *            ·�������ַ��
	 * @param startPoint
	 *            ·����㡣
	 * @param endPoint
	 *            ·���յ㡣
	 */
	public RouteTask(String url, Point startPoint, Point endPoint) {
		this.url = url;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	/**
	 * ���·�����⡣
	 * 
	 * @return �Ƿ����ɹ���0 ����ɹ������򷵻ش�����롣
	 */
	public int Solve() {
		String url = this.url + "/solve?stops="
				+ String.valueOf(startPoint.getX()) + ","
				+ String.valueOf(startPoint.getY()) + ";"
				+ String.valueOf(endPoint.getX()) + ","
				+ String.valueOf(endPoint.getY())
				+ "&returnDirections=false&f=json";
		try {
			String route = HttpUtils.getUrl(url);
			if (route != null && !route.isEmpty()) {
				try {
					JSONObject object = (JSONObject) new JSONTokener(route)
							.nextValue();
					JSONObject feature = object.getJSONObject("routes")
							.getJSONArray("features").getJSONObject(0);
					length = feature.getJSONObject("attributes").getDouble(
							"Total_Length");
					JsonParser parser = new JsonFactory()
							.createJsonParser(feature.getString("geometry"));
					result = (Polyline) GeometryEngine.jsonToGeometry(parser)
							.getGeometry();
					return 0;
				} catch (JSONException e) {
					return R.string.json_related_error;
				}
			} else {
				return R.string.no_route_solution_found;
			}
		} catch (IOException e) {
			return R.string.route_solve_error;
		}
	}
}
