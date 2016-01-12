package me.mehmetkaya.eldevar;

import java.io.Serializable;

/**
 * Created by kullanici on 09.01.2016.
 */
public class Malzeme implements Serializable {
    public Malzeme(String isim){this.isim=isim;}
    private String isim;
    private int id;
    public String getIsim(){return isim;}
    @Override
    public String toString(){return isim;}

}
