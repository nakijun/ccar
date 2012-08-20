package org.ccar.data;

/**
 * 表示景点。
 * 
 * @author FG
 * 
 */
public class ScenicSpot {

	/**
	 * 景点的 ID。
	 */
	private int id;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	/**
	 * 景点的名称。
	 */
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 景点的描述。
	 */
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 景点的 X 坐标。
	 */
	private double x;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * 景点的 Y 坐标。
	 */
	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
