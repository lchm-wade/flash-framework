package com.foco.model.page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description 分页响应体
 * @Author lucoo
 * @Date 2021/6/13 9:16
 **/
public class PageResponse<T> {
    private Integer total;
    private Boolean hasPre;

    private Boolean hasNext;

    private Integer current;

    private Integer pages;

    private Integer size;

    /**
     * 扩展数据
     */
    private Map<String, Object> extra = new HashMap<>();

    private static final long serialVersionUID = 1L;
    protected List<T> records;

    public PageResponse() {
        this.total = 0;
        this.current = 0;
        this.size = 0;
        this.pages = 0;
        this.hasPre = false;
        this.hasNext = false;
        this.records = new LinkedList<>();
    }

    public PageResponse(PageList page) {
        this.total = page.getTotal();
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.pages = page.getPages();
        this.hasPre = page.getHasPre();
        this.hasNext = page.getHasNext();
        this.records = page;
    }


    public Boolean getHasPre() {
        return hasPre;
    }

    public void setHasPre(Boolean hasPre) {
        this.hasPre = hasPre;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void putExtra(String key, Object val) {
        extra.put(key, val);
    }
}
