select province, sum(cast(responsesize as int)) as responsesize, count(1) as req_cnt
from xingze.log_detail
where date_time=${hiveconf:day}
group by province;

