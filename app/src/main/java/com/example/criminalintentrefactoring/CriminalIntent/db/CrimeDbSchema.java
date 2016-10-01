package com.example.criminalintentrefactoring.CriminalIntent.db;

/**
 * Created by 离子态狍子 on 2016/9/30.
 */

public class CrimeDbSchema {
    /**
     * 常量类
     */
    public static final class CrimeTable{
        public static final String NAME = "crime";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";

        }
    }
}
