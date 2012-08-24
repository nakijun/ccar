package org.ccar.data;

/**
 * 表示一条景点记录。
 * 
 * @author FG
 * 
 */
public class ScenicSpot {

	/**
	 * 景点的 ID。
	 */
	private int id;

	/**
	 * 获取景点的 ID。
	 * 
	 * @return 景点的 ID。
	 */
	public int getID() {
		return id;
	}

	/**
	 * 设置景点的 ID。
	 * 
	 * @param id
	 *            将景点的 ID 设置为该值。
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * 景点的名称。
	 */
	private String name;

	/**
	 * 获取景点的名称。
	 * 
	 * @return 景点的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置景点的名称。
	 * 
	 * @param name
	 *            将景点的名称设置为该值。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 景点的描述。
	 */
	private String description;

	/**
	 * 获取景点的描述。
	 * 
	 * @return 景点的描述。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置景点的描述。
	 * 
	 * @param description
	 *            将景点的描述设置为该值。
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 景点的图片文件名，多个文件以“_”分割
	 */
	private String imageFiles;
	
	/**
	 * 获取景点图片文件名
	 * @return 景点图片文件名，如有多个文件，以“_”分割
	 */
	public String getImageFiles() {
		return imageFiles;
	}

	/**
	 * 设置景点图片文件名
	 * @param imageFiles 景点图片文件名，如有多个文件，以“_”分割
	 */
	public void setImageFiles(String imageFiles) {
		this.imageFiles = imageFiles;
	}

	/**
	 * 景点所在的经度。
	 */
	private double lon;

	/**
	 * 获取景点所在的经度。
	 * 
	 * @return 景点所在的经度。
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * 设置景点所在的经度。
	 * 
	 * @param lon
	 *            将景点所在的经度设置为该值。
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * 景点所在的纬度。
	 */
	private double lat;

	/**
	 * 获取景点所在的纬度。
	 * 
	 * @return 景点所在的纬度。
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * 设置景点所在的纬度。
	 * 
	 * @param lat
	 *            将景点所在的纬度设置为该值。
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * 景点所在的 X 坐标。
	 */
	private double x;

	/**
	 * 获取景点所在的 X 坐标。
	 * 
	 * @return 景点所在的 X 坐标。
	 */
	public double getX() {
		return x;
	}

	/**
	 * 设置景点所在的 X 坐标。
	 * 
	 * @param x
	 *            将景点所在的 X 坐标设置为该值。
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * 景点所在的 Y 坐标。
	 */
	private double y;

	/**
	 * 获取景点所在的 Y 坐标。
	 * 
	 * @return 景点所在的 Y 坐标。
	 */
	public double getY() {
		return y;
	}

	/**
	 * 设置景点所在的 Y 坐标。
	 * 
	 * @param y
	 *            将景点所在的 Y 坐标设置为该值。
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * 景点与当前位置的距离。
	 */
	private Double distance;
	
	/**
	 * 获取景点与当前位置的距离。
	 * 
	 * @return 景点与当前位置的距离。
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * 设置景点与当前位置的距离。
	 * 
	 * @param distance
	 *            将景点与当前位置的距离设置为该值。
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
