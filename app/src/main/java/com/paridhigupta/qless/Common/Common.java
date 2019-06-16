package com.paridhigupta.qless.Common;

import android.content.Intent;

import com.paridhigupta.qless.Model.Floor;
import com.paridhigupta.qless.Model.Machine;
import com.paridhigupta.qless.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT" ;
    public static final String KEY_HOSTEL_FLOOR = "FLOOR_SAVE" ;
    public static final String KEY_MACHINE_LOAD_DONE = "MACHINE_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_MACHINE_SELECTED = "MACHINE_SELECTED";
    public static Floor CurrentFloor;
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static int step = 0;
    public static String hostel="";
    public static Machine currentMachine;
}
