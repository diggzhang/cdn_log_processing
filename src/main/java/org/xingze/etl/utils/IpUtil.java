package org.xingze.etl.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @program: bigdata-start
 * @author: huzekang
 * @create: 2020-08-02 10:50
 **/
public enum IpUtil {
    instance;

    private static DbSearcher searcher;

    private static void init() throws DbMakerConfigException, FileNotFoundException {
        if (searcher == null) {
            DbConfig config = new DbConfig();
            //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
            String dbfile = IpUtil.class.getClassLoader().getResource("ip2region.db").getPath();
            searcher = new DbSearcher(config, dbfile);
        }

    }

    public static String parserIp(String ip) {
        //根据ip进行位置信息搜索
        try {
            init();
            //采用Btree搜索
            DataBlock block = searcher.btreeSearch(ip);
            //打印位置信息（格式：国家|大区|省份|城市|运营商）
            return block.getRegion();
        } catch (DbMakerConfigException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

