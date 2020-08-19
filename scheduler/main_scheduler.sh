#!/bin/bash
set -e # exit on error

#---------------------------------------------------------------------
# SCRIPT CONFIGURATION
#---------------------------------------------------------------------

SCRIPT_NAME=$(basename "$0")
VERSION=0.1

# Global variables
YEAR=$(date -d -0day '+%Y')
MONTH=$(date -d -0day '+%m')
DAY=$(date -d -0day '+%d')
LOGFILE=/tmp/scheduling_"$YEAR$MONTH$DAY".log
WORK_DIR=/tmp/bigdata

# make sure we're in the directory where the script lives
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

#---------------------------------------------------------------------
# UTILITY FUNCTIONS
#---------------------------------------------------------------------

# log message to screen and log file
log ()
{
    echo "[${SCRIPT_NAME}]: $1" >> "$LOGFILE"
    echo "[${SCRIPT_NAME}]: $1"
}

# Define script functions here

uploadIpDb() {
  log "1. 上传IP库到hdfs根目录, 当有需要更新数据时，打开下面main调用的注释"
  hdfs dfs -rm -f /ip2region.db
  hadoop dfs -put "$SCRIPT_DIR"/ip2region.db /
  hadoop dfs -put mock_cdn_data.csv /project/raw/
}

create_table() {
  log "2. 建表"
  hive -f ./create_table.sql
}

cleaning() {
  log "1. 执行清洗逻辑 $1"
  export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:"$SCRIPT_DIR"/ip2region-1.7.2.jar
  export LIBJARS="$SCRIPT_DIR"/ip2region-1.7.2.jar
  hadoop jar "$SCRIPT_DIR"/training-1.0.jar org.xingze.etl.CDNLogCleaningDriver -libjars ${LIBJARS} /project/raw/mock_cdn_data.csv /project/dw/raw/date_time="$1"
}

load_partition() {
  log "2. 加载分区"
  hive --hiveconf day="$1"  -f ./load_partition_data.sql
}

sql() {
  log "3. 分析"
  log "计算 $1 信息"
  hive --hiveconf day="$1"  -f ./group_by_prov.sql
}

main() {
    log "version $VERSION"
    # log "initial"
    # uploadIpDb >> "$LOGFILE"
    # create_table >> "$LOGFILE"
    log "core"
    log "$SCRIPT_DIR"
    cleaning "$YEAR$MONTH$DAY"
    load_partition "$YEAR$MONTH$DAY"
    sql "$YEAR$MONTH$DAY"
}

main "$@"
