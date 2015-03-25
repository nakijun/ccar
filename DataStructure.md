# 数据结构 #
**景点（T\_ScenicSpot）**| **字段名称** | **中文名称** | **类型（长度）** | **描述** |
|:-----------------|:-----------------|:-----------------------|:-----------|
| ID | ID | INTEGER |  |
| Code | 类型编码 | TEXT(10) | CRK：出入口；JD：景点；JDCR：景点出入口<br />QL：桥路；CY：餐饮；CS：厕所；<br />FW：游客服务；ZXC：自行车停靠点；<br />TCC：停车场；MT：码头；HC：候车点 |
| Name | 名称 | TEXT(100) |  |
| Description | 描述 | TEXT(200) |  |
| RoadID | 道路ID | INTEGER |  |
| ImageFiles | 图像文件 | TEXT(50) | 以"-"隔开 |
| AudioFiles | 音频文件 | TEXT(50) | 以"-"隔开 |
| Remark | 备注 | TEXT(50) |  |
| Lon | 经度 | REAL(15,8) |  |
| Lat | 纬度 | REAL(15,8) |  |
| X | X坐标 | REAL(15,8) |  |
| Y | Y坐标 | REAL(15,8) |  |