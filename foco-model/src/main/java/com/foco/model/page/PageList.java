package com.foco.model.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PageList<T> extends ArrayList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer total;

    private Boolean hasPre;

    private Boolean hasNext;

    private Integer current;

    private Integer pages;

    private Integer size;
    public PageList() {
        super();
    }

    public PageList(int size) {
        super(size);
    }

    public PageList(Collection<? extends T> c) {
        super(c);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
    /*public PageList<T> PageList(PageList<?> src) {
        PageList pl = new PageList();
        pl.setTotal(src.getTotal());
        pl.setHasPre(src.getHasPre());
        pl.setHasNext(src.getHasNext());
        pl.setCurrent(src.getCurrent());
        pl.setPages(src.getPages());
        pl.setSize(src.getSize());
        return pl;
    }*/

}
