## CDN统计

- 统计：https://help.aliyun.com/document_detail/27141.html?spm=a2c4g.11174283.2.33.7c2b7035evg2Yh
- 日志：https://help.aliyun.com/document_detail/27141.html?spm=a2c4g.11174283.2.33.7c2b7035evg2Yh

1) 日志  80M
	严格按照日志的日志要求
		responsesize：数值类型  弄点脏数据进来，-

2) 使用MR进行数据清洗 ETL  ==> HDFS
	ip ==> IP库解析  省份 城市 运营商
		ip2region
	responsesize脏数据过滤掉
	
3) Hive 表关联上清洗后的数据
	external table  partition day

4) 求每个省份的流量和、每个省份的请求次数和

