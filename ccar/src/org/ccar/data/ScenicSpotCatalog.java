package org.ccar.data;

import java.util.ArrayList;
import java.util.List;

public class ScenicSpotCatalog {
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ScenicSpotCatalog(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static List<ScenicSpotCatalog> getCatalog() {
		List<ScenicSpotCatalog> catalog = new ArrayList<ScenicSpotCatalog>();
		catalog.add(new ScenicSpotCatalog("JD", "����"));
		catalog.add(new ScenicSpotCatalog("CY", "����"));
		catalog.add(new ScenicSpotCatalog("FW", "�οͷ���"));
		catalog.add(new ScenicSpotCatalog("CS", "������"));
		catalog.add(new ScenicSpotCatalog("HC", "�򳵵�"));
		catalog.add(new ScenicSpotCatalog("TCC", "ͣ����"));
		catalog.add(new ScenicSpotCatalog("ZXC", "���г�ͣ����"));
		catalog.add(new ScenicSpotCatalog("MT", "��ͷ"));
		catalog.add(new ScenicSpotCatalog("RK", "���"));
		catalog.add(new ScenicSpotCatalog("CK", "����"));
		catalog.add(new ScenicSpotCatalog("JZ", "����"));
		catalog.add(new ScenicSpotCatalog("QL", "��·"));
		return catalog;
	}
}
