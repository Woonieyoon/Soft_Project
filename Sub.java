package kr.co.company.myapp;

import android.widget.EditText;

/**
 * Created by jeonsoohwan on 2017. 9. 17..
 */

public class Sub {
    String roomID;
    String roomPassword;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public Sub(String roomID, String roomPassword) {
        this.roomID = roomID;
        this.roomPassword = roomPassword;
    }
}
