package com.khasang.forecast.position;

import com.khasang.forecast.AppUtils;
import com.khasang.forecast.R;


/**
 * Created by Veda on 24.11.15.
 */

public class Precipitation {
    private Type type;

    public static enum Type {
        THUNDERSTORM {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_thunderstorm;
                return AppUtils.getWeaherIcon(THUNDERSTORM, isDay);
            }
        },
        DRIZZLE {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_drizzle;
                return AppUtils.getWeaherIcon(DRIZZLE, isDay);
            }
        },
        RAIN {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_rain;
                return AppUtils.getWeaherIcon(RAIN, isDay);
            }
        },
        SNOW {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_snow;
                return AppUtils.getWeaherIcon(SNOW, isDay);
            }
        },
        ATMOSPHERE {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_fog;
                return AppUtils.getWeaherIcon(ATMOSPHERE, isDay);
            }
        },
        CLEAR {
            public int getIconResId(boolean isDay) {
                return AppUtils.getWeaherIcon(CLEAR, isDay);
            /*    if (isDay) {
                    return R.drawable.ic_sun;
                } else {
                    return R.drawable.ic_moon;
                }*/
            }
        },
        CLOUDS {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_cloud;
                return AppUtils.getWeaherIcon(CLOUDS, isDay);
            }
        },
        EXTREME {
            public int getIconResId(boolean isDay) {
                //return R.drawable.ic_extreme;
                return AppUtils.getWeaherIcon(EXTREME, isDay);
            }
        },
        ADDITIONAL {
            public int getIconResId(boolean isDay) {
                return 0;
            }
        };

        public abstract int getIconResId(boolean isDay);
    }

    @Override
    public String toString() {
        return "Precipitation{" +
                "type=" + type +
                '}';
    }

    public Precipitation() {

    }

    public Precipitation(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = stringToType(type);
    }

    public Type stringToType(String type) {
        return Type.valueOf(type);
    }

}
