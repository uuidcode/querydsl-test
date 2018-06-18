package com.github.uuidcode.querydsl.test.entity;

import java.io.Serializable;

public class CoreObject<T extends CoreObject> implements Serializable {
    private Integer page;
    private Integer lastPage;
    private Integer startPage;
    private Integer endPage;
    private Integer beforeStartPage;
    private Integer beforeEndPage;
    private Integer nextStartPage;
    private Integer nextEndPage;

    public T paging(Integer page, Integer pageSize, Integer pageItemSize, Integer totalCount) {
        this.page = page;
        this.startPage = calculateStartPage(page, pageSize);
        this.beforeStartPage = calculateBeforeStartPage(pageSize);
        this.beforeEndPage = calculateBeforeEndPage(pageSize);
        this.lastPage = calculateLastPage(page, pageSize, pageItemSize, totalCount);
        this.endPage = calculateEndPage(pageSize);
        this.nextStartPage = calculateNextStartPage(pageSize);
        this.nextEndPage = calculateNextEndPage(pageSize);
        return (T) this;
    }

    protected Integer calculateNextEndPage(Integer pageSize) {
        if ((startPage + pageSize) <= lastPage) {
            if ((endPage + pageSize) <= lastPage) {
                return endPage + pageSize;
            }
            return lastPage;
        }
        return null;
    }

    protected Integer calculateNextStartPage(Integer pageSize) {
        if ((startPage + pageSize) <= lastPage) {
            return startPage + pageSize;
        }
        return null;
    }

    protected Integer calculateBeforeEndPage(Integer pageSize) {
        if (pageSize > startPage) {
            return null;
        }

        return beforeStartPage + pageSize - 1;

    }

    protected Integer calculateBeforeStartPage(Integer pageSize) {
        if (pageSize > startPage) {
            return null;
        }
        return startPage - pageSize;
    }

    protected Integer calculateEndPage(Integer pageSize) {
        if ((lastPage - startPage) >= pageSize) {
            return (startPage + pageSize - 1);
        }
        return lastPage;
    }

    protected Integer calculateLastPage(Integer currentPage, Integer pageSize,
                                        Integer pageItemSize, Integer totalCount) {
        if (totalCount <= pageItemSize) {
            return 1;
        }

        if (isMultiple(pageItemSize, totalCount)) {
            return totalCount / pageItemSize;
        }

        return totalCount / pageItemSize + 1;

    }

    private Boolean isMultiple(Integer lower, Integer larger) {
        return (larger % lower) == 0;
    }

    private Integer calculateStartPage(Integer currentPage, Integer pageSize) {
        if (currentPage <= pageSize) {
            return 1;
        }

        int mod = currentPage%pageSize;
        if(mod==0) mod=pageSize;
        return currentPage-(mod-1);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public Boolean hasBefore() {
        return beforeStartPage != null && beforeEndPage != null;
    }

    public Integer getBeforeStartPage() {
        return beforeStartPage;
    }

    public Integer getBeforeEndPage() {
        return beforeEndPage;
    }

    public Boolean hasNext() {
        return nextStartPage != null && nextEndPage != null;
    }

    public Integer getNextStartPage() {
        return nextStartPage;
    }

    public Integer getNextEndPage() {
        return nextEndPage;
    }
}
