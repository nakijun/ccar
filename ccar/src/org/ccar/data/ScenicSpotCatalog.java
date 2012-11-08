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
		catalog.add(new ScenicSpotCatalog("JD", "景点"));
		catalog.add(new ScenicSpotCatalog("CY", "餐饮"));
		catalog.add(new ScenicSpotCatalog("FW", "游客服务"));
		catalog.add(new ScenicSpotCatalog("CS", "卫生间"));
		catalog.add(new ScenicSpotCatalog("HC", "候车点"));
		catalog.add(new ScenicSpotCatalog("TCC", "停车场"));
		catalog.add(new ScenicSpotCatalog("ZXC", "自行车停靠点"));
		catalog.add(new ScenicSpotCatalog("MT", "码头"));
		catalog.add(new ScenicSpotCatalog("RK", "入口"));
		catalog.add(new ScenicSpotCatalog("CK", "出口"));
		catalog.add(new ScenicSpotCatalog("JZ", "建筑"));
		catalog.add(new ScenicSpotCatalog("QL", "桥路"));
		return catalog;
	}
}
