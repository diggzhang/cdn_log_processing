ALTER TABLE xingze.log_detail
ADD PARTITION (date_time='${hiveconf:day}')
LOCATION '/project/dw/raw/date_time=${hiveconf:day}';