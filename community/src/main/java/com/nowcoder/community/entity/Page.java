package com.nowcoder.community.entity;

/**
 * @author Msq
 * @date 2020/11/19 - 1:08
 */
public class Page {

    //当前页码
    private int current = 1;

    //显示上限
    private int limit = 10;

    //数据总数
    private int rows;

    //查询路径
    private String path;

    public Page(int current, int limit, int rows, String path) {
        this.current = current;
        this.limit = limit;
        this.rows = rows;
        this.path = path;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1)
            this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100)
            this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0)
            this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /*
    获取当前页面的起始行
    current*limit - limit
     */
    public int getOffset(int current){
        return (current-1) * limit;
    }

    public int getTotalPageNum(){
        if(rows % limit != 0)
            return rows/limit + 1;
        else
            return rows/limit;
    }
}
