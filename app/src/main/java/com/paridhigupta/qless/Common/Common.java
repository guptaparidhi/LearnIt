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
    public static final int TIME_SLOT_TOTAL = 24;
    public static Floor CurrentFloor;
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static int step = 0;
    public static String hostel="";
    public static Machine currentMachine;

    public static String convertTimeSlotToString(int slot) {
        switch(slot){
            case 0:
                return  "00:00 - 1:00";
            case 1:
                return  "1:00 - 2:00";
            case 2:
                return  "2:00 - 3:00";
            case 3:
                return  "3:00 - 4:00";
            case 4:
                return  "4:00 - 5:00";
            case 5:
                return  "5:00 - 6:00";
            case 6:
                return  "6:00 - 7:00";
            case 7:
                return  "7:00 -8:00";
            case 8:
                return  "8:00 - 9:00";
            case 9:
                return  "9:00 - 10:00";
            case 10:
                return  "10:00 - 11:00";
            case 11:
                return  "11:00 - 12:00";
            case 12:
                return  "12:00 - 13:00";
            case 13:
                return  "13:00 - 14:00";
            case 14:
                return  "14:00 - 15:00";
            case 15:
                return  "15:00 - 16:00";
            case 16:
                return  "16:00 - 17:00";
            case 17:
                return  "17:00 - 18:00";
            case 18:
                return  "18:00 - 19:00";
            case 19:
                return  "19:00 - 20:00";
            case 20:
                return  "20:00 - 21:00";
            case 21:
                return  "21:00 - 22:00";
            case 22:
                return  "22:00 - 23:00";
            case 23:
                return  "23:00 - 00:00";
            default:
                return"Closed";

        }
    }
}
