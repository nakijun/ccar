package org.ccar.ar;

import java.text.DecimalFormat;

/**
 * �ռ������
 * 
 * @author swansword
 * 
 */
public class GeoCalcUtil {
	public static final double EARTH_RADIUS = 6378.137;

	/**
	 * ���㻡��
	 * 
	 * @param d
	 *            �Զ�Ϊ��λ�ľ�γ����ֵ
	 * @return �Ի���Ϊ��λ�ľ�γ����ֵ
	 */
	public static double CalcRad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * ��γ��ת��������Ϊ��λ��ƽ��ֱ������
	 * 
	 * @param lon
	 *            ����
	 * @param lat
	 *            γ��
	 * @return ƽ��ֱ������double�����飬����Ϊ��λ
	 */
	public static double[] WGS2flat(double lon, double lat) {
		double L = CalcRad(lon);
		double l = L - CalcRad(120);
		double B = CalcRad(lat);
		double cosb = Math.cos(B);
		double sinb = Math.sin(B);

		double a = EARTH_RADIUS * 1000;
		double b = 6356752.3142451793;
		double t = Math.tan(B);
		// double r = 3600 * 180 / Math.PI;
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double e12 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(b, 2);
		double n2 = e12 * Math.pow(cosb, 2);
		double N = a / Math.sqrt(1 - e2 * Math.pow(sinb, 2));

		double x = 6367449.1458 * B - 32009.8185 * cosb * sinb - 133.9975
				* cosb * Math.pow(sinb, 3) - 0.6975 * cosb * Math.pow(sinb, 5);
		double X = x + N / 2 * t * Math.pow(cosb, 2) * Math.pow(l, 2) + N / 24
				* t * Math.pow(cosb, 4)
				* (5 - Math.pow(t, 2) + 9 * n2 + 4 * Math.pow(n2, 2))
				* Math.pow(l, 4);
		double Y = N * cosb * l + N / 6 * Math.pow(cosb, 3)
				* (1 - Math.pow(t, 2) + n2) * Math.pow(l, 3);

		double[] coord = { Y, X };
		return coord;
	}

	/**
	 * ����������룬��λΪ��
	 * 
	 * @param stpoint
	 *            �������
	 * @param endpoint
	 *            �յ�����
	 * @return �������
	 */
	public static double CalcDistance(double[] stpoint, double[] endpoint) {
		double dis = Math.sqrt(Math.pow(stpoint[0] - endpoint[0], 2)
				+ Math.pow(stpoint[1] - endpoint[1], 2));
		return dis;
	}

	/**
	 * ����������룬��λΪ��
	 * 
	 * @param lon1
	 *            ��㾭��
	 * @param lat1
	 *            ���γ��
	 * @param lon2
	 *            �յ㾭��
	 * @param lat2
	 *            �յ�γ��
	 * @return �������
	 */
	public static double CalcDistance(double lon1, double lat1, double lon2,
			double lat2) {
		return CalcDistance(WGS2flat(lon1, lat1), WGS2flat(lon2, lat2));
	}

	/**
	 * �������ʽ��Ϊ�ַ�����
	 * 
	 * @param distance
	 *            ���루��λΪ�ף���
	 * @return ��ʽ������ַ��������磺960.69 ��ʽ��Ϊ 960 m��1230.321 ��ʽ��Ϊ 1.2 km��12321.123 ��ʽ��Ϊ 12 km����
	 */
	public static String formatDistance(double distance) {
		StringBuilder builder = new StringBuilder();
		if (distance < 1000) {
			builder.append((int) distance);
			builder.append(" m");
		} else if (distance < 10000) {
			DecimalFormat format = new DecimalFormat("#.#");
			builder.append(format.format(distance / 1000));
			builder.append(" km");
		} else {
			builder.append((int) (distance / 1000));
			builder.append(" km");
		}
		return builder.toString();
	}
}
