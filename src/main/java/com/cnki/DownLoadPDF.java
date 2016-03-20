package com.cnki;

import com.cnki.model.DocForDownload;
import com.fetcher.Requester;
import com.fetcher.RequesterBuilder;
import com.fetcher.ResponseData;
import com.util.StringUtil;
import com.yjh.Collections.HashMap;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 2016/3/19.
 */
public class DownLoadPDF {
    private static final String base_url = "http://epub.cnki.net";
    private static final String searchPreparedUrl = "http://epub.cnki.net/KNS/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.21&PageName=ASP.brief_result_aspx&DbPrefix=SCDB&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&db_opt=CJFQ%2CCJFN%2CCDFD%2CCMFD%2CCPFD%2CIPFD%2CCCND%2CCCJD%2CHBRD&base_special1=%25&magazine_value1=%magazine_value1%&magazine_special1=%3D&publishdate_from=%publishdate_from%&publishdate_to=%publishdate_to%&his=0&__=Tue%20Feb%2025%202014%2020%3A13%3A53%20GMT%2B0800";
    private static final String searchUrl = "http://epub.cnki.net/kns/brief/brief.aspx?pagename=";
    private static final String listPageUrl = "http://epub.cnki.net/kns/brief/brief.aspx?curpage=%curpage%&RecordsPerPage=20&QueryID=7&ID=&turnpage=%turnpage%&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_result_aspx";
    private SearchCondition condition;

    private Requester requester = new RequesterBuilder()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
            .setEncoding("utf8")
            .build();
    private int page = 0;
    private int item = 0;

    public void restart()  {
        try {
            this.requester.close();
            new DownLoadPDF().searchByCondition(condition, page, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DocLinkItem> searchByCondition(SearchCondition condition, int startPage, int itemNumber) throws InterruptedException {
        this.condition = condition;
        if(page == 0)
            page = startPage;
        if(item == 0)
            item = itemNumber;
        try {
            ResponseData responseData = requester.createExample(searchPreparedUrl.replace("%magazine_value1%", URLEncoder.encode(condition.getJournal(), "utf-8"))
                    .replace("%publishdate_from%", condition.getStartDate()).replace("%publishdate_to%", condition.getEndDate())).doGet();

            String url = searchUrl + responseData.getBody().replace("&nbsp;","") + "&t=" + System.currentTimeMillis() + "&S=1&keyValue=";
            ResponseData countResponse = requester.createExample(url).doGet();
            //�õ��ж��ٽ��
            int pageCount = 0;
            String pc = StringUtil.findFirst(countResponse.getBody(), "�ҵ�([\\d,]+?)�����", new StringUtil.StrFilter() {
                @Override
                public String filter(Matcher matcher) {
                    return matcher.group(1).replace(",", "");
                }
            });
            if (pc != null)
                pageCount = Integer.parseInt(pc);

            //����ÿһҳ�����
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            header.put("Host", "epub.cnki.net");
            header.put("Connection", "keep-alive");
            for (int i = startPage; i <= Math.ceil(((double)pageCount) / 20); i++) {
                requester.createExample(searchPreparedUrl.replace("%magazine_value1%", URLEncoder.encode(condition.getJournal(), "utf-8"))
                        .replace("%publishdate_from%", condition.getStartDate()).replace("%publishdate_to%", condition.getEndDate())).doGet();
                String pageUrl = listPageUrl.replace("%curpage%", i + "").replace("%turnpage%", (i - 1) + "");
                ResponseData listResponse = requester.createExample(pageUrl).doGet();

                //�������е�ÿһ���������
                Document doc = Jsoup.parse(listResponse.getBody());
                Elements pages = doc.select(".fz14");
                header.put("Referer", pageUrl);
                for (int j = itemNumber - 1; j < pages.size(); ++j) {
                    String docUrl = base_url + pages.get(j).attr("href");
                    ResponseData docResponse1 = requester.createExample(docUrl)
                            .setRedirectAuto(false)
//                            .addHeader(header)
                            .addHeader("Referer", pageUrl)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "epub.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .doGet();
                    String realDetailUrl = docResponse1.getLocation();
                    ResponseData detailResponse = requester.createExample(realDetailUrl)
                            .setRedirectAuto(true)
                            .addHeader("Referer", pageUrl)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "epub.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .doGet();
                    System.out.print("��" + (page = i) + "ҳ����" + (item = j + 1) + "����");
                    DocForDownload docForDownload = pdfDownload(detailResponse.getBody(), realDetailUrl);
                }
                //ֻ�����ص�startPage����Ҫ��ָ���кſ�ʼ��������Ŀ�ű���ָ���1
                itemNumber = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("��׽������׼���ͷ����ӣ���������!" + condition.getJournal() + "����" + page +
                    " ҳ����" + item + "ƪ");
            //���¿�ʼ
            TimeUnit.SECONDS.sleep(5);
            restart();
        }
        return null;
    }

    private static final Pattern publishYearP = Pattern.compile("(\\d+)��\\d+��");
    private final Matcher publishYearM = publishYearP.matcher("");
    public DocForDownload pdfDownload( String detailBody, String refer) throws IOException, InterruptedException {
        detailBody = detailBody.replace("&#xA;                            pdf&#xA;                        ", "pdfD");
        Document doc = Jsoup.parse(detailBody);
        //����
        String title = doc.select("#chTitle").text().trim();
        System.out.println(title);
        //�ڿ���
        String journal = "";
        if(doc.select(".detailLink a").size() > 0)
            journal = doc.select(".detailLink a").get(0).text().trim();
        else {//���û��ȡ�����⣬���ҳ�棬ֱ�ӽ���
            System.out.println("no journal!");
            //���¿�ʼ
            TimeUnit.SECONDS.sleep(5);
            restart();
            return null;
        }
        //����
        Elements authorInfo = doc.select(".author p");
        String[] authorsA = null;
        String[] author_corporations = null;
        for(int i = 0; i < authorInfo.size(); i++) {
            Element e = authorInfo.get(i);
            if (e.text().contains("�����ߡ�")) {
                Elements authors = e.select("a");
                authorsA = new String[authors.size()];
                for (int j = 0; j < authors.size(); j++) {
                    authorsA[j] = authors.get(j).text().trim();
                }
            } else if (e.text().contains("��������")) {
                Elements corporations = e.select("a");
                author_corporations = new String[corporations.size()];
                for (int j = 0; j < corporations.size(); j++) {
                    author_corporations[j] = corporations.get(j).text().trim();
                }
            }
        }
        String date = "";
        publishYearM.reset(detailBody);
        while(publishYearM.find()) {
            date = publishYearM.group(1);
        }
        String institution = "";
        Elements keywords = doc.select(".keywords");
        for(Element e : keywords) {
            if(e.text().contains("������")) {
                institution = e.text().replaceAll("������", "");
            }
        }

        //������
        DocForDownload docPDF = new DocForDownload(title, journal, authorsA, author_corporations, date, institution);
        Elements downLinks = doc.select(".pdfD");
        if(downLinks.size() > 0) {
            Element downLink = downLinks.select("a").get(0);
            String downUrl = "http://www.cnki.net" +  downLink.attr("href").trim();
            //���û�������ǾͲ�Ҫ������ƪ�ĵ�
            if(authorsA != null && authorsA.length > 0) {// && journalName.equals(journal)
                //����pdf�ļ�
                StringBuilder filePath = new StringBuilder();
                String filename = StringUtil.standardFileName(docPDF.getName());
                File pdfDoc = new File(filePath.append("/home/yjh/pdfDownload/").append(docPDF.getJournal()).append("/").append(filename).append(".pdf").toString());
                filePath.setLength(0);
                File txtInfo = new File(filePath.append("/home/yjh/pdfDownload/").append(docPDF.getJournal()).append("/").append(filename).append(".txt").toString());
                filePath.setLength(0);

                if (!pdfDoc.exists()) {
                    pdfDoc.getParentFile().mkdirs();
                    pdfDoc.createNewFile();
                }
                if (!txtInfo.exists()) {
                    txtInfo.getParentFile().mkdirs();
                    txtInfo.createNewFile();
                }

                ResponseData pdfResponse = requester.createExample(downUrl)
                        .setRedirectAuto(false)
                        .addHeader("Referer", refer)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHeader("Host", "www.cnki.net")
                        .addHeader("Connection", "keep-alive")
                        .addParam("password", "")
                        .addParam("username", "")
                        .doPost();
                String downUrl2 = pdfResponse.getLocation();
                ResponseData pdfResponse2 = requester.createExample(downUrl2)
                        .setRedirectAuto(false)
                        .addHeader("Referer", refer)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHeader("Host", "www.cnki.net")
                        .addHeader("Connection", "keep-alive")
                        .addParam("password", "")
                        .addParam("username", "")
                        .doPost();
                String downUrl3 = pdfResponse2.getLocation();
                if (StringUtils.isEmpty(downUrl3)) {
                    if (pdfResponse2.getBody().contains("��ǰ�û�������������")) {
                        System.out.println("��ǰ�û�������������");
                        TimeUnit.SECONDS.sleep(10);
                        restart();
                        return null;
                    }
                }
                ResponseData pdfResponse3 = requester.createExample(downUrl3)
                        .setRedirectAuto(false)
                        .addHeader("Referer", refer)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHeader("Connection", "keep-alive")
                        .doGet();
                String downUrl4 = pdfResponse3.getLocation();
                requester.createExample(downUrl4)
                        .setRedirectAuto(true)
                        .addHeader("Referer", refer)
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHeader("Connection", "keep-alive")
                        .doDownload(pdfDoc);
                PrintWriter out = new PrintWriter(txtInfo);
                out.println(docPDF);
                out.close();
            } else {
                System.out.println("journalName error!");
                TimeUnit.SECONDS.sleep(5);
                return null;
            }
        } else {
            System.out.println("downLinks error!");
        }
        return docPDF;
    }


    public static void main(String[] args) throws Exception {
        DownLoadPDF downLoadPDF = new DownLoadPDF();
        SearchCondition condition = new SearchCondition();
        condition.setStartDate("2005-01-01");
        condition.setEndDate("2016-03-31");
        condition.setJournal("�й�ͼ���ѧ��");
        downLoadPDF.searchByCondition(condition, 6, 1);
        condition.setJournal("ͼ���鱨����");
        downLoadPDF.searchByCondition(condition, 1, 1);
        condition.setJournal("�鱨������ʵ��");
        downLoadPDF.searchByCondition(condition, 1, 1);
        condition.setJournal("�鱨��־");
        downLoadPDF.searchByCondition(condition, 1, 1);
    }
}
