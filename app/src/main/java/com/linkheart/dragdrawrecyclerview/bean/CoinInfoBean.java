package com.linkheart.dragdrawrecyclerview.bean;


/**
 * <pre>
 *     author :
 *     e-mail : luyuan11233@163.com
 *     time   : 2021/07/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CoinInfoBean {

    /**
     * name         string 名称
     * priceLast    string	最新价
     * riseRate24	string	24小时涨幅,负值前面有负号
     * vol24	    string	24小时成交量
     * close	    string	收盘价
     * open	        string	开盘价
     * bid	        string	买一价
     * ask	        string	卖一价
     * mountPercent	string	量比
     */
    public String name;
    public String priceLast;
    public String riseRate24;
    public String vol24;
    public String close;
    public String open;
    public String bid;
    public String ask;
    public String amountPercent;
}