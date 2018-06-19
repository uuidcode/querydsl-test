package com.github.uuidcode.querydsl.test.entity;

import java.io.Serializable;

public class CoreObject<T extends CoreObject> implements Serializable {
    private Long page;
    private Long lastPage;
    private Long startPage;
    private Long endPage;
    private Long beforeStartPage;
    private Long beforeEndPage;
    private Long nextStartPage;
    private Long nextEndPage;
    private Long totalCount;

    public Long getTotalCount() {
        return this.totalCount;
    }

    public CoreObject setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public T paging(Integer page, Long totalCount) {
        return paging(new Long(page), 10L, 10L, totalCount);
    }

    public T paging(Integer page, Long pageSize, Long pageItemSize, Long totalCount) {
        return paging(new Long(page), pageSize, pageItemSize, totalCount);
    }

    public T paging(Long page, Long pageSize, Long pageItemSize, Long totalCount) {
        this.page = page;
        this.startPage = calculateStartPage(page, pageSize);
        this.beforeStartPage = calculateBeforeStartPage(pageSize);
        this.beforeEndPage = calculateBeforeEndPage(pageSize);
        this.lastPage = calculateLastPage(page, pageSize, pageItemSize, totalCount);
        this.endPage = calculateEndPage(pageSize);
        this.nextStartPage = calculateNextStartPage(pageSize);
        this.nextEndPage = calculateNextEndPage(pageSize);
        this.totalCount = totalCount;
        return (T) this;
    }

    protected Long calculateNextEndPage(Long pageSize) {
        if ((startPage + pageSize) <= lastPage) {
            if ((endPage + pageSize) <= lastPage) {
                return endPage + pageSize;
            }
            return lastPage;
        }
        return null;
    }

    protected Long calculateNextStartPage(Long pageSize) {
        if ((startPage + pageSize) <= lastPage) {
            return startPage + pageSize;
        }
        return null;
    }

    protected Long calculateBeforeEndPage(Long pageSize) {
        if (pageSize > startPage) {
            return null;
        }

        return beforeStartPage + pageSize - 1;

    }

    protected Long calculateBeforeStartPage(Long pageSize) {
        if (pageSize > startPage) {
            return null;
        }
        return startPage - pageSize;
    }

    protected Long calculateEndPage(Long pageSize) {
        if ((lastPage - startPage) >= pageSize) {
            return (startPage + pageSize - 1);
        }
        return lastPage;
    }

    protected Long calculateLastPage(Long currentPage, Long pageSize,
                                        Long pageItemSize, Long totalCount) {
        if (totalCount <= pageItemSize) {
            return 1L;
        }

        if (isMultiple(pageItemSize, totalCount)) {
            return totalCount / pageItemSize;
        }

        return totalCount / pageItemSize + 1;

    }

    private Boolean isMultiple(Long lower, Long larger) {
        return (larger % lower) == 0;
    }

    private Long calculateStartPage(Long currentPage, Long pageSize) {
        if (currentPage <= pageSize) {
            return 1L;
        }

        long mod = currentPage % pageSize;

        if(mod == 0) {
            mod = pageSize;
        }

        return currentPage - (mod - 1);
    }

    public Long getPage() {
        return page;
    }

    public Long getLastPage() {
        return lastPage;
    }

    public Long getStartPage() {
        return startPage;
    }

    public Long getEndPage() {
        return endPage;
    }

    public Boolean hasBefore() {
        return beforeStartPage != null && beforeEndPage != null;
    }

    public Long getBeforeStartPage() {
        return beforeStartPage;
    }

    public Long getBeforeEndPage() {
        return beforeEndPage;
    }

    public Boolean hasNext() {
        return nextStartPage != null && nextEndPage != null;
    }

    public Long getNextStartPage() {
        return nextStartPage;
    }

    public Long getNextEndPage() {
        return nextEndPage;
    }
}
