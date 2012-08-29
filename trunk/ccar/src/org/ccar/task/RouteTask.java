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
 * 提供路径解决方案。
 * 
 * @author FG
 * 
 */
public class RouteTask {

	/**
	 * 服务地址。
	 */
	private String url;

	/**
	 * 获取路径服务地址。
	 * 
	 * @return 服务地址。
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置路径服务地址。
	 * 
	 * @param url
	 *            将路径服务地址设置为该值。
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 路径起点。
	 */
	private Point startPoint;

	/**
	 * 获取路径起点。
	 * 
	 * @return 路径起点。
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * 设置路径起点。
	 * 
	 * @param startPoint
	 *            将路径起点设置为该值。
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * 路径终点。
	 */
	private Point endPoint;

	/**
	 * 获取路径终点。
	 * 
	 * @return 路径终点。
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * 设置路径终点。
	 * 
	 * @param endPoint
	 *            将路径终点设置为该值。
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * 路径长度。
	 */
	private double length;

	/**
	 * 获取路径长度。
	 * 
	 * @return 路径长度。
	 */
	public double getLength() {
		return length;
	}

	/**
	 * 路径。
	 */
	private Polyline result;

	/**
	 * 获取路径。
	 * 
	 * @return 路径。
	 */
	public Polyline getResult() {
		return result;
	}

	/**
	 * 构造函数。
	 * 
	 * @param url
	 *            路径服务地址。
	 * @param startPoint
	 *            路径起点。
	 * @param endPoint
	 *            路径终点。
	 */
	public RouteTask(String url, Point startPoint, Point endPoint) {
		this.url = url;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	/**
	 * 解决路径问题。
	 * 
	 * @return 是否解决成功。0 代表成功，否则返回错误代码。
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
