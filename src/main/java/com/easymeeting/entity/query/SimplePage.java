package com.easymeeting.entity.query;

public class SimplePage {

    private Integer pageNo;

    private Integer pageSize;
    private int countTotal;
    private int pageTotal;
    private int start;
    private int end;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }



    public SimplePage(){
    }

    public SimplePage(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public SimplePage(Integer pageNo, int countTotal, int pageSize){
        if(pageNo == null){
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.countTotal = countTotal;
        this.pageSize = pageSize;
        action();
    }

    public void action(){
        if(this.pageSize <= 0){
            this.pageSize = 20;
        }
        if(this.countTotal > 0){
            this.pageTotal = this.countTotal % this.pageSize == 0 ?
                    this.countTotal / this.pageSize : this.countTotal / this.pageSize + 1;
        } else{
            pageTotal = 1;
        }

        if(pageNo <= 1){
            pageNo = 1;
        }
        if(pageNo > pageTotal){
            pageNo = pageTotal;
        }
        this.start = (pageNo - 1) * pageSize;
        this.end = this.pageSize;
    }

}
