package org.out386.dashchargestats;


class Constants {
    static final String UI_UPDATE_INTENT = "UI_UPDATE";
    static final String UI_UPDATE_MESSAGE = "UI_MESSAGE_STARTING";
    static final String UI_UPDATE_MESSAGE_CODE = "UI_MESSAGE_STARTING_CODE";
    static final String[] PATHS = {"/sys/class/power_supply/battery/fastchg_starting"
            , "/sys/class/power_supply/battery/fastchg_status"
            , "/sys/class/power_supply/battery/input_current_limited"
            , "/sys/class/power_supply/battery/input_current_max"
            , "/sys/class/power_supply/battery/current_now"
            , "/sys/class/power_supply/battery/status",
    };
}
