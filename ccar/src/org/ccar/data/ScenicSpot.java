package org.ccar.data;

/**
 * ��ʾ���㡣
 * 
 * @author FG
 * 
 */
public class ScenicSpot {

	/**
	 * ����� ID��
	 */
	private int id;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	/**
	 * ��������ơ�
	 */
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �����������
	 */
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ����� X ���ꡣ
	 */
	private double x;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * ����� Y ���ꡣ
	 */
	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
