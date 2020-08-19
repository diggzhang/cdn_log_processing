--创建并加载数据到临时表
drop table if exists xingze.log_detail;
create external table xingze.log_detail(
ip                  string        comment '访问IP',
http_status         string,
host                string,
uri_path            string,
agent_ip            string        comment '代理IP',
responsetime        string        comment 'responsetime（单位：ms）',
referer             string        comment 'referer',
method              string        comment 'method',
url                 string        comment '访问url',
httpcode            int           comment 'httpcode',
requestsize         int           comment 'requestsize（单位：byte）',
responsesize        string        comment 'responsesize（单位：byte）',
cache_status        string        comment 'cache命中状态',
ua                  string        comment 'UA头',
file_type           string        comment '文件类型',
province            string        comment '省',
city                string        comment '市',
operator            string        comment '运营商',
year                string,
month               string,
day                 string
)
PARTITIONED BY (date_time string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
LOCATION '/project/dw/ods/'
;


