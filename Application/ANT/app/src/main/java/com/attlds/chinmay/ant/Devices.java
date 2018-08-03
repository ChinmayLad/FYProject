package com.attlds.chinmay.ant;

import android.provider.BaseColumns;

/**
 * Created by chinmay on 17/3/16.
 */
public class Devices {

    public Devices(){}

    public static abstract class Attld implements BaseColumns{
        public static final String TABLE_NAME = "ANT_DEVICE";
        public static final String COLUMN_NAME_UU_ID = "uu_id";
        public static final String COLUMN_NAME_DEV_NAME = "dev_name";
        public static final String COLUMN_NAME_USER = "user_id";
    }

    public static abstract class LostDevices implements BaseColumns{
        public static final String TABLE_NAME = "LOST_DEVICE";
        public static final String COLUMN_NAME_UU_ID = "uu_id";
    }
}
