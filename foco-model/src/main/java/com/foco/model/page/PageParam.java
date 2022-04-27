package com.foco.model.page;

import java.io.Serializable;

public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean openPage = false;
    /**
     * 每页数据
     */
    private Integer size;
    /**
     *
     */
    private Integer start;

    /**
     * 目标页码
     */
    private Integer current;
    private final Integer DEFAULT_CURRENT=1;
    private final Integer DEFAULT_SIZE=10;
    public PageParam() {
        this.current = DEFAULT_CURRENT;
        this.size = DEFAULT_SIZE;
    }

    /**
     * 添加构造方法，加入页码和数量，以及是否开启开关
     */
    public PageParam(Integer current, Integer size, Boolean openPage) {
        this(current,size);
        this.openPage = openPage;
    }
    public PageParam(Integer current, Integer size, Boolean openPage,Integer start) {
        this(current,size,start);
        this.openPage = openPage;
    }
    public PageParam(Integer current, Integer size) {
        setCurrent(current);
        setSize(size);
    }
    public PageParam(Integer current, Integer size,Integer start) {
        setCurrent(current);
        setSize(size);
        setStart(start);
    }
    public Boolean getOpenPage() {
        return openPage;
    }

    public void setOpenPage(Boolean openPage) {
        this.openPage = openPage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size<1?DEFAULT_SIZE:size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current<=0?DEFAULT_CURRENT:current;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}