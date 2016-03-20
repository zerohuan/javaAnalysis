package com.cnki;

/**
 * String journal, String startYear, String endYear
 * Created by yjh on 2016/3/19.
 */
public class SearchCondition {
    private String journal;
    private String startDate;
    private String endDate;

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
