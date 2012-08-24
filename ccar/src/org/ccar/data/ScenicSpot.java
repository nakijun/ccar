package org.ccar.data;

/**
 * ��ʾһ�������¼��
 * 
 * @author FG
 * 
 */
public class ScenicSpot {

	/**
	 * ����� ID��
	 */
	private int id;

	/**
	 * ��ȡ����� ID��
	 * 
	 * @return ����� ID��
	 */
	public int getID() {
		return id;
	}

	/**
	 * ���þ���� ID��
	 * 
	 * @param id
	 *            ������� ID ����Ϊ��ֵ��
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * ��������ơ�
	 */
	private String name;

	/**
	 * ��ȡ��������ơ�
	 * 
	 * @return ��������ơ�
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���þ�������ơ�
	 * 
	 * @param name
	 *            ���������������Ϊ��ֵ��
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �����������
	 */
	private String description;

	/**
	 * ��ȡ�����������
	 * 
	 * @return �����������
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * ���þ����������
	 * 
	 * @param description
	 *            ���������������Ϊ��ֵ��
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * �����ͼƬ�ļ���������ļ��ԡ�_���ָ�
	 */
	private String imageFiles;
	
	/**
	 * ��ȡ����ͼƬ�ļ���
	 * @return ����ͼƬ�ļ��������ж���ļ����ԡ�_���ָ�
	 */
	public String getImageFiles() {
		return imageFiles;
	}

	/**
	 * ���þ���ͼƬ�ļ���
	 * @param imageFiles ����ͼƬ�ļ��������ж���ļ����ԡ�_���ָ�
	 */
	public void setImageFiles(String imageFiles) {
		this.imageFiles = imageFiles;
	}

	/**
	 * �������ڵľ��ȡ�
	 */
	private double lon;

	/**
	 * ��ȡ�������ڵľ��ȡ�
	 * 
	 * @return �������ڵľ��ȡ�
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * ���þ������ڵľ��ȡ�
	 * 
	 * @param lon
	 *            ���������ڵľ�������Ϊ��ֵ��
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * �������ڵ�γ�ȡ�
	 */
	private double lat;

	/**
	 * ��ȡ�������ڵ�γ�ȡ�
	 * 
	 * @return �������ڵ�γ�ȡ�
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * ���þ������ڵ�γ�ȡ�
	 * 
	 * @param lat
	 *            ���������ڵ�γ������Ϊ��ֵ��
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * �������ڵ� X ���ꡣ
	 */
	private double x;

	/**
	 * ��ȡ�������ڵ� X ���ꡣ
	 * 
	 * @return �������ڵ� X ���ꡣ
	 */
	public double getX() {
		return x;
	}

	/**
	 * ���þ������ڵ� X ���ꡣ
	 * 
	 * @param x
	 *            ���������ڵ� X ��������Ϊ��ֵ��
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * �������ڵ� Y ���ꡣ
	 */
	private double y;

	/**
	 * ��ȡ�������ڵ� Y ���ꡣ
	 * 
	 * @return �������ڵ� Y ���ꡣ
	 */
	public double getY() {
		return y;
	}

	/**
	 * ���þ������ڵ� Y ���ꡣ
	 * 
	 * @param y
	 *            ���������ڵ� Y ��������Ϊ��ֵ��
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * �����뵱ǰλ�õľ��롣
	 */
	private Double distance;
	
	/**
	 * ��ȡ�����뵱ǰλ�õľ��롣
	 * 
	 * @return �����뵱ǰλ�õľ��롣
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * ���þ����뵱ǰλ�õľ��롣
	 * 
	 * @param distance
	 *            �������뵱ǰλ�õľ�������Ϊ��ֵ��
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
