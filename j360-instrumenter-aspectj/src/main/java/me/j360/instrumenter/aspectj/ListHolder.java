package me.j360.instrumenter.aspectj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListHolder {

    private final List<String> list;

    public ListHolder(String[] array) {
        this.list = new ArrayList<String>(Arrays.asList(array));
    }

    public List<String> getList() {
        return new ArrayList<String>(list);
    }

    public String toString() {
        return "ListHolder" + list.toString();
    }

}
