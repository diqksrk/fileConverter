package com.example.demo.utils;

import java.io.File;
import java.util.Comparator;

public class FileSortComparator implements Comparator {
    @Override
    public int compare(Object f1, Object f2) {
        String fileName1 = ((File) f1).getName().split("[.]")[0];
        String fileName2 = ((File) f2).getName().split("[.]")[0];

        String[] weStringSet = fileName1.split(" ");
        String[] thereStringSet = fileName2.split(" ");

        if (weStringSet.length == 1) return -1;
        if (thereStringSet.length == 1) return 1;

        int weNumber = Integer.parseInt(weStringSet[0]);
        int thereNumber = Integer.parseInt(thereStringSet[0]);

        return weNumber - thereNumber;
    }
}
