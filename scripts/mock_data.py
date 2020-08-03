import csv
import random
import datetime
from faker import Faker
from faker.providers import internet

from fake_useragent import UserAgent
ua = UserAgent()


fake = Faker()
fake.add_provider(internet)

"""
## 消息体示例：

[9/Jun/2015:01:58:09 +0800] 192.168.15.75 - 1542 "-" "GET http://www.aliyun.com/index.html" 200 191 2830 MISS "Mozilla/5.0 (compatible; AhrefsBot/5.0; +http://ahrefs.com/robot/)" "text/html"

## 字段解释：

字段	                            描述
[9/Jun/2015:01:58:09 +0800]	       日志开始时间
192.168.15.75	                   访问IP
-	                               代理IP
1542	                           responsetime（单位：ms）
"-"	                               referer
GET	                               method
http://www.aliyun.com/index.html   访问url
200	                               httpcode
191	                               requestsize（单位：byte）
2830	                           responsesize（单位：byte）
MISS	                           cache命中状态
Mozilla/5.0（compatible; ...）	    UA头
text/html	                       文件类型
"""
CDN_HEADER = [
    "datetime",
    "ip",
    "proxy_ip",
    "responsetime",
    "referer",
    "method",
    "url",
    "httpcode",
    "requestsize",
    "responsesize",
    "cache",
    "ua",
    "filetype",
]


def cdn_traffic_mock(records, headers, date_is):
    with open("mock_cdn_data.csv", 'a+') as csvFile:
        writer = csv.DictWriter(csvFile, fieldnames=headers)
        for i in range(records):
            random_time = date_is - datetime.timedelta(hours=random.randint(0,23))
            row = {
                "datetime": random_time.strftime("[%d/%b/%Y:%H:%M:%S +0800]"),
                "ip": fake.ipv4_public(),
                "proxy_ip":"-",
                "responsetime": random.randint(100, 2000),
                "referer": "-",
                "method": "GET",
                "url": "http://www.aliyun.com/" + fake.uri_page() + fake.uri_extension(),
                "httpcode": [200, 404, 500, 204][random.randint(0,4)],
                "requestsize": random.randint(100, 1000),
                "responsesize": random.randint(1000, 2000),
                "cache": "MISS",
                "ua": ua.random,
                "filetype": "text/html"
            }
            writer.writerow(row)
    return None


def time_range_generator(how_many_days):
    days = how_many_days
    now = datetime.datetime.now()
    date_list = [now - datetime.timedelta(days=x) for x in range(days)]
    return date_list

if __name__ == '__main__':
    records = 3000

    for date_is in time_range_generator(120):
        cdn_traffic_mock(records, CDN_HEADER, date_is)
