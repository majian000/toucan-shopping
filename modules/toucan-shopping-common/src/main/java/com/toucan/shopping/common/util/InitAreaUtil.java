package com.toucan.shopping.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
//import com.toucan.shopping.area.export.entity.Area;

public class InitAreaUtil {

    public static void main(String[] args) throws Exception {
//        List<Area> areaList = new ArrayList<Area>();
//        BufferedReader br = new BufferedReader(new FileReader("D:/地区编码.txt"));
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            Area area = new Area();
//            String code = line.substring(0, line.indexOf(" "));
//            line = line.substring(line.indexOf(" "), line.length());
//            String[] pca = line.split(",");
//            pca[0] = "中国";
//            area.setCode(code);
//            if (pca.length == 2) {
//                area.setProvince(pca[1]);
//                area.setType((short) 1);
//                area.setParentCode("-1");
//            }
//            if (pca.length == 3) {
//                try {
//                    area.setProvince(pca[1]);
//                    area.setCity(pca[2]);
//                    area.setType((short) 2);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (pca.length == 4) {
//                try {
//                    area.setProvince(pca[1]);
//                    area.setCity(pca[2]);
//                    area.setArea(pca[3]);
//                    area.setType((short) 3);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            areaList.add(area);
//
//        }
//
//        //设置市、区县上级节点
//        for (int i = 0; i < areaList.size(); i++) {
//            Area area = areaList.get(i);
//            //市
//            if (area.getType() == 2) {
//                boolean find = false;
//                for (int j = 0; j < areaList.size(); j++) {
//                    if (areaList.get(j).getType() == 1) {
//                        if (areaList.get(j).getProvince().equals(area.getProvince())) {
//                            area.setParentCode(areaList.get(j).getCode());
//                            find = true;
//                            break;
//                        }
//                    }
//                }
//                //直辖市
//                if (!find) {
//                    area.setIsMunicipality((short) 1);
//                    area.setParentCode("-1");
//                }
//            }
//
//            //区县
//            if (area.getType() == 3) {
//                for (int j = 0; j < areaList.size(); j++) {
//                    if (areaList.get(j).getType() == 2) {
//                        if (areaList.get(j).getCity().equals(area.getCity()) && areaList.get(j).getArea() == null) {
//                            area.setParentCode(areaList.get(j).getCode());
//                        }
//                    }
//                }
//            }
//        }
//
//
//        for (int i = 0; i < areaList.size(); i++) {
//            Area area = areaList.get(i);
//            StringBuilder sql = new StringBuilder("insert into bbs_area(code,parent_code,province,city,area,area_sort,is_municipality,type,app_code,create_admin_id,delete_status) ");
//            sql.append(" values( ");
//            sql.append(" '" + area.getCode() + "' ");
//            sql.append(",");
//            sql.append(" '" + area.getParentCode() + "' ");
//            sql.append(",");
//            sql.append(" '" + (area.getProvince() != null ? area.getProvince() : "") + "' ");
//            sql.append(",");
//            sql.append(" '" + (area.getCity() != null ? area.getCity() : "") + "' ");
//            sql.append(",");
//            sql.append(" '" + (area.getArea() != null ? area.getArea() : "") + "' ");
//            sql.append(",");
//            sql.append(" '" + (areaList.size() - i) + "' ");
//            sql.append(",");
//            sql.append(" " + area.getIsMunicipality() + " ");
//            sql.append(",");
//            sql.append(" '" + area.getType() + "' ");
//            sql.append(",");
//            sql.append(" '10001001' ");
//            sql.append(",");
//            sql.append(" -1 ");
//            sql.append(",");
//            sql.append(" 0 ");
//            sql.append(" ); ");
////            System.out.println(area.getCode()+" "+area.getParentCode()+" "+area.getProvince()+" "+area.getCity()+" "+area.getArea());
//            System.out.println(sql);
//        }
    }
}
