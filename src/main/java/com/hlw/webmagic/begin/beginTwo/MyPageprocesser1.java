package com.hlw.webmagic.begin.beginTwo;

import com.hlw.util.HtmlFilterUtil;
import com.hlw.webmagic.begin.MyPipeline;
import com.hlw.webmagic.begin.beginTwo.helper.DownUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;
import us.codecraft.webmagic.selector.SmartContentSelector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: BookGrab
 * @description:
 * @author: houlongwang
 * @create: 2019-05-02 00:20
 **/
@Slf4j
public class MyPageprocesser1 implements PageProcessor {

    private Site site = Site.me().setCharset("UTF-8").setSleepTime(100).setRetryTimes(3);

    private final String provinceName = "xl7014987";
    private final String cityName = "xl7114987";

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml()
                .xpath("//*[@id=\"2020年9月份县以上行政区划代码_14987\"]/table/tbody/tr")
                .nodes();
        List<Selectable> td = nodes.get(3).xpath("td").nodes();
        List<AreaModel> list = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (i < 3) {
                continue;
            }
            List<String> string = nodes.get(i).xpath("td").all();
            if (string.size()<3) {
                continue;
            }
            String str1 = string.get(1);
            String str2 = string.get(2);
            String className = getClassName(str1);
            String code = getString(str1);
            String name = getString(str2);
            if (className.equals(provinceName)){
                /*假如下一级是市区，这一级是省份*/
                if (getClassName(nodes.get(i+1).xpath("td").all().get(1)).equals(provinceName)) {
                    AreaModel areaModel = new AreaModel();
                    areaModel.setCode(code);
                    areaModel.setName(name);
                    areaModel.setLeave("1");
                    list.add(areaModel);
                    System.out.println("省份"+code+"名称"+name);
                }else {
                    AreaModel areaModel = new AreaModel();
                    areaModel.setCode(code);
                    areaModel.setName(name);
                    areaModel.setLeave("2");
                    list.add(areaModel);
                    System.out.println("地市"+code+"名称"+name);
                }
            }else {
                AreaModel areaModel = new AreaModel();
                areaModel.setCode(code);
                areaModel.setName(name);
                areaModel.setLeave("3");
                list.add(areaModel);
            }
        }
        System.out.println(list.size());


        list.forEach(item->{
            System.out.println(getInsertSQL(item.getCode(),item.getName(),item.getLeave()));
        });
    }
    public static String getInsertSQL(String code,String name,String leave){
       return  "INSERT INTO `cim`.`t_province_city_area`( `code`, " +
                "`name`, `leave`) VALUES ( '"+code+"', '"+name+"', '"+leave+"');";
    }

    /**
     * 获取类名
     *
     * @param str
     * @return
     */
    public static String getClassName(String str){
        String dealStr = str.substring(str.indexOf("<"), str.indexOf(">"));
        return dealStr.substring(dealStr.indexOf("\"")+1,dealStr.lastIndexOf("\""));
    }
    public String getString(String str) {
       return str.substring(str.lastIndexOf('>', (str.lastIndexOf(">") - 1))+1, str.lastIndexOf("<"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        log.info("世界的位置开始：{}", "begin");
        Spider.create(new MyPageprocesser1())
                .addUrl("http://www.mca.gov.cn/article/sj/xzqh/2020/2020/2020112010001.html")
                .addPipeline(new MyPipeline()).run();
    }
}


