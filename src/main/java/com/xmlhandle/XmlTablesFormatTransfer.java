package com.xmlhandle;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
1. 输入/输出：
输入文件夹路径，存放待处理的excel文件，在磁盘上提前创建好；
输出文件路径，将读取的数据结果写入，在磁盘上提前创建好；

2. 使用poi-excel支持，可以读写/处理excel格式的文件

3. 流程/步骤：
（1）使用FileInputStream（文件输入流）从输入文件夹的文件读入；
（2）使用FileOutputStream（文件输出流）读入输出文件到内存中操作；
（3）初始化输出文件excel工作簿对象；
（4）遍历处理输入文件夹的每个excel文件；
	因为存在合并单元格情况，读取时一些行的某些列的值为空，但其实是有值的，值是上次读取该列得到的值；
    因此需要保存最新读取到的列的值，如果下一次读取某个列的值不为空且有变化时更新保存的值，如果为空直接使用保存的值；
	分别读取：商品名称，款式名称，颜色，尺码，条形码，售价，库存量写入“输出文件excel工作簿对象”的每一行的指定位置处中；
	因为输入表最后一行是总计，在读取时要排除；
（5）将“输出文件excel工作簿对象”结果写入输出文件；

4. 使用方式：
包装成jar插件，可以由Java，PHP等程序调用；
输入参数2个：输入文件夹路径，输出文件夹路径（均为绝对路径）；
输出返回：true（处理成功）/false（处理失败，检查相应文件是否创建好，文件格式是否变化等等）；
 */

/**
 *
 *
 * Created by yjh on 15-12-5.
 */
public class XmlTablesFormatTransfer {
    //输入文件夹，存放待处理的excel文件，提前创建好
    private static final String IN_DIR = "/home/yjh/dms/tmp/excels/excelIn/";
    //输出文件，将读取的数据结果写入，提前创建好
    private static final String OUT_FILE = "/home/yjh/dms/tmp/excels/excelOut/out.xlsx";

    /**
     * 调用该方法开始处理，
     * @return 返回true，处理完成，返回false，处理失败（可能由于文件为创建好，xml格式有变化等原因）
     */
    public static boolean handle() {
        File dir = new File(IN_DIR);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".xlsx");
            }
        });
        try {
            //初始化输出文件excel工作簿对象
            XSSFWorkbook workbook = initOut();
            //遍历处理输入文件夹的每个excel文件
            for(File f : files) {
                handle(f, workbook.getSheetAt(0));
            }
            //结果写入输出文件
            try (FileOutputStream fos = new FileOutputStream(new File(OUT_FILE))) {
                workbook.write(fos);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 处理单个表
     * @param f
     * @throws IOException
     */
    private static void handle(File f, XSSFSheet outSheet) throws IOException {
        try (FileInputStream fis = new FileInputStream(f)) {
            try(XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
                XSSFSheet spreadsheet = workbook.getSheetAt(0);
                Iterator <Row> rowIterator = spreadsheet.iterator();
                XSSFRow row;
                int rowCount = 0;
                //因为存在合并单元格情况，一些行的某些列的值为空，但其实是有值的，值是上次读取该列得到的值
                //因此需要保存最新读取到的列的值，如果下一次读取某个列的值不为空且有变化时更新保存的值，如果为空直接使用保存的值
                //这里使用一个map（键值对）的形式统一保存
                Map<String, String> tempMap = new HashMap<>();
                while (rowIterator.hasNext())
                {
                    row = (XSSFRow) rowIterator.next();
                    if(rowCount++ == 0) continue; //跳过表头
                    int colNum = row.getPhysicalNumberOfCells();
                    if(colNum == 11) {
                        InFormat inFormat = new InFormat();
                        Cell ksCell = row.getCell(0);
                        if(!StringUtils.isEmpty(ksCell.getStringCellValue())) {
                            //读取商品名称（原表为“款式”）
                            inFormat.setKs(getValue(ksCell, tempMap, "ks"));
                            //款式名称
                            inFormat.setKsName(getValue(row.getCell(1), tempMap, "ksName"));
                            //颜色
                            inFormat.setColor(getValue(row.getCell(2), tempMap, "color"));
                            //尺码
                            inFormat.setCm(getValue(row.getCell(3), tempMap, "cm"));
                            //条形码
                            inFormat.setTxm(getValue(row.getCell(4), tempMap, "txm"));
                            //售价
                            inFormat.setLsj(getValue(row.getCell(5), tempMap, "lsj"));
                            //库存量
                            inFormat.setKcsl(getValue(row.getCell(6), tempMap, "kcsl"));
                            //因为输入的表最后有“总计栏”，因此要排除
                            if(!inFormat.getCm().equals("总计:") && !inFormat.getCm().equals("总计")) {
                                writeResult(inFormat, outSheet);
                                System.out.println(inFormat);
                            }
                        }
                    }
                }
            }
        }
    }

    private static XSSFWorkbook initOut() throws IOException {
        try (FileInputStream fin = new FileInputStream(OUT_FILE)) {
            return new XSSFWorkbook(fin);
        }
    }
    private static int rowId = 1;
    private static void writeResult(InFormat inFormat, XSSFSheet outSheet) {
        Row row = outSheet.createRow(rowId++);
        setValue(row, 2, inFormat.getKs());
        setValue(row, 5, inFormat.getKsName());
        setValue(row, 4, inFormat.getTxm());
        setValue(row, 7, 0);
        setValue(row, 9, inFormat.getLsj());
//        setValue(row, 10, inFormat.getKs());
        setValue(row, 11, inFormat.getColor());
        setValue(row, 12, inFormat.getCm());
        setValue(row, 13, inFormat.getKcsl());
    }

    /**
     * 用于保存输入信息的格式
     */
    private static class InFormat {
        private String ksName;
        private String ks;
        private String color;
        private String cm;
        private String txm;
        private String lsj;
        private String kcsl;

        public String getCm() {
            return cm;
        }

        public void setCm(String cm) {
            this.cm = cm;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getKcsl() {
            return kcsl;
        }

        public void setKcsl(String kcsl) {
            this.kcsl = kcsl;
        }

        public String getKs() {
            return ks;
        }

        public void setKs(String ks) {
            this.ks = ks;
        }

        public String getKsName() {
            return ksName;
        }

        public void setKsName(String ksName) {
            this.ksName = ksName;
        }

        public String getLsj() {
            return lsj;
        }

        public void setLsj(String lsj) {
            this.lsj = lsj;
        }

        public String getTxm() {
            return txm;
        }

        public void setTxm(String txm) {
            this.txm = txm;
        }

        @Override
        public String toString() {
            return "InFormat{" +
                    "cm='" + cm + '\'' +
                    ", ksName='" + ksName + '\'' +
                    ", ks='" + ks + '\'' +
                    ", color='" + color + '\'' +
                    ", txm='" + txm + '\'' +
                    ", lsj='" + lsj + '\'' +
                    ", kcsl='" + kcsl + '\'' +
                    '}';
        }
    }

    /**
     * 给指定行的指定位置创建单元格并写入值
     * @param row
     * @param index
     * @param value
     */
    private static void setValue(Row row, int index, Object value) {
        row.createCell(index).setCellValue(String.valueOf(value));
    }

    /**
     * 从指定的单元格中读取值
     * @param cell
     * @param map
     * @param name
     * @return
     */
    private static String getValue(Cell cell, Map<String, String> map, String name) {
        String temp = "";
        String t;
        String oldValue = (t = map.get(name)) == null ? "" : t;
        int cellType = cell.getCellType();
        if(cellType == Cell.CELL_TYPE_STRING)
            temp = cell.getStringCellValue();
        else if(cellType == Cell.CELL_TYPE_NUMERIC)
            temp = String.valueOf(cell.getNumericCellValue());
        String result = StringUtils.isEmpty(temp) ? oldValue : temp;
        map.put(name, result);
        return result;
    }
    /**
     * 款式	款式名称	颜色	尺码	条形码	零售价	库存数量	库存金额	在途数量	库存总量	库存总金额
     4808084K	裤子	001-黑色	M	4808084K00104	199.00 	1 	199.00 		1 	199.00
     小计:			1 	199.00 		1 	199.00
     合计:			1 	199.00 		1 	199.00
     4809370K			M	4809370K00104	159.00 	1 	159.00 		1 	159.00
     小计:			1 	159.00 		1 	159.00
     合计:			1 	159.00 		1 	159.00
     4809511K			M	4809511K00104	199.00 	1 	199.00 		1 	199.00
     小计:			1 	199.00 		1 	199.00
     002-白色	M	4809511K00204	199.00 	1 	199.00 		1 	199.00
     小计:			1 	199.00 		1 	199.00
     合计:			2 	398.00 		2 	398.00
     */
    /**
     * 输出格式：
     * 供应商ID	商品分类ID	供应商商品ID	商品助记码	商品国际条码	商品名称	商品系列	商品状态(0:可售卖，1:不可售卖)
     * 商品产地	商品吊牌价	款式	颜色	尺码	库存数量
     */

    public static void main(String[] args) throws IOException {
        handle();
    }
}
