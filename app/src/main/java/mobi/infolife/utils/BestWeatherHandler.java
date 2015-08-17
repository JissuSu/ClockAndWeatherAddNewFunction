package mobi.infolife.utils;

import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class BestWeatherHandler extends DefaultHandler {
    private boolean in_current_conditions = false;
    private boolean in_forecast_conditions = false;
    private boolean in_night_time = false;

    private boolean error_conditions = false;
    private boolean c_stat;
    private String current_city;
    private String elementname;

    private String current_temp;
    private String current_condition;
    private String current_hum;
    private String current_wind_speed;
    private String current_wind_speed_unit;

    private String current_wind_direction;

    private String iconURL;

    private ArrayList<String> highList = new ArrayList<String>();
    ;
    private ArrayList<String> lowList = new ArrayList<String>();
    ;
    private ArrayList<String> dayList = new ArrayList<String>();
    ;
    private ArrayList<String> iconList = new ArrayList<String>();
    ;
    private ArrayList<String> conditionList = new ArrayList<String>();
    ;

    private boolean usingSITemperature = false;

    public BestWeatherHandler(boolean useC) {
        this.c_stat = useC;
    }

    public boolean getXmlValidStat() {
        return error_conditions;
    }

    public String getCurrentWind() {
        //CommonUtils.l("this.current_wind_direction"+this.current_wind_direction);

        return this.current_wind_direction + " " + this.current_wind_speed
                + " " + this.current_wind_speed_unit;
    }

    public void setCurrentWindSpeed(String wind_speed) {
        if (wind_speed.length() > 0) {
            //CommonUtils.l("wind_speed:" + wind_speed);

            this.current_wind_speed = wind_speed;
        }
    }

//	public String getCurrentWindUnit() {
//		return this.current_wind_speed_unit;
//	}

    public void setCurrentWindSpeedUnit(String wind_speed_unit) {
        if (wind_speed_unit.length() > 0) {
            //CommonUtils.l("wind_speed_unit:" + wind_speed_unit);

            this.current_wind_speed_unit = wind_speed_unit;
        }
    }

    public void setCurrentWindDirection(String wind_direction) {
        if (wind_direction.length() > 0) {
            //CommonUtils.l("wind_direction:" + wind_direction);

            this.current_wind_direction = getDirection(wind_direction);
        }
    }

    public String getCurrentCity() {
        return this.current_city;
    }

    public String getCurrentCondition() {
        if (this.current_condition == null)
            return "Click to setting";
        else if (this.current_condition.length() < 0)
            return "Click to setting";
        else
            return this.current_condition;
    }

    public String getCurrentHum() {
        return this.current_hum;
    }

    public String getIconURL() {
        return this.iconURL;
    }

    public void setCurrentCity(String city) {
        if (city.length() > 0) {
            //CommonUtils.l("city:" + city);

            this.current_city = city;
        }
    }

    public void setCurrentTemp(String temp) {
        if (temp.length() > 0) {
            //CommonUtils.l("temp:" + temp);

            this.current_temp = temp;
        }
    }

    public String getCurrentTemp() {

        return this.current_temp;
    }

    public void addDayList(String data) {

        if (data.length() > 0) {
            // Utils.l("add day = " + data);
            //CommonUtils.l("addDay:" + data);

            this.dayList.add(data);
        } else
            this.dayList.add("--");
    }

    public void addIconList(String data) {
        if (data.length() > 0) {
            //CommonUtils.l("Icon:" + data);

            // data = data.toLowerCase().replaceAll(".gif", "");
            // int startIndex = data.lastIndexOf("/") + 1;
            // data = data.substring(startIndex);
            // CommonUtils.l("add icon" + data);
            this.iconList.add(data);
        }
    }

    public void addLowList(String data) {
        if (data.length() > 0) {
            //CommonUtils.l("low:" + data);

            this.lowList.add(data);
            // Utils.l("add low = " + data);
        }
    }

    public void addHighList(String data) {
        // Utils.l("add high = " + data);

        if (data.length() > 0) {
            //CommonUtils.l("high:" + data);

            this.highList.add(data);
        }

    }

    // public void addConditionList(String data) {
    // if (data.length() > 0){
    // CommonUtils.l("con:" + data);
    //
    // this.conditionList.add(data);}
    // else
    // this.conditionList.add("--");
    // }

    public String getIconList(int index) {
        if (this.iconList.size() > index)
            return this.iconList.get(index);
        else
            return "--";
    }

    public String getLowList(int index) {

        if (this.lowList.size() > index) {
            return this.lowList.get(index);
        } else
            return "--";
    }

    public String getHighList(int index) {
        if (this.highList.size() > index) {
            return this.highList.get(index);
        } else
            return "--";
    }

    public String getdayList(int index) {
        if (this.dayList.size() > index) {
            // Utils.l("index=" + index + ":" + this.dayList.get(index));

            return this.dayList.get(index);

        } else
            return "--";
    }

    // public String getConditionList(int index) {
    // if (this.conditionList.size() > index)
    // if (this.conditionList.get(index).length() < 1)
    // return this.conditionList.get(index);
    // else
    // return "Click to setting";
    // else
    // return "--";
    // }

    public void setCurrentCondition(String condition) {
        if (condition.length() > 0) {
            //CommonUtils.l("condition:" + condition);

            this.current_condition = condition;
        }
    }

    public void setCurrentHum(String hum) {
        if (hum.length() > 0) {
            //CommonUtils.l("hum:" + hum);
            hum.replace("%", "");
            this.current_hum = hum;
        }
    }

    // public void setIconURL(String data) {
    // if (data.length() > 0){
    // CommonUtils.l("IconURL:" + data);
    //
    // // data = data.toLowerCase().replaceAll(".gif", "");
    // // int startIndex = data.lastIndexOf("/") + 1;
    // // data = data.substring(startIndex);
    // this.iconURL = data;}
    // }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        // 'Outer' Tags、
        if (localName.equals("currentconditions")) {
            this.in_current_conditions = true;
        }
        if (localName.equals("forecast")) {
            this.in_forecast_conditions = true;
        }
        if (localName.equals("nighttime")) {
            this.in_night_time = true;
        }

        this.elementname = qName;

    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        if (localName.equals("currentconditions")) {
            this.in_current_conditions = false;
        }
        if (localName.equals("forecast")) {
            this.in_forecast_conditions = false;
        }
        if (localName.equals("nighttime")) {
            this.in_night_time = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String dataAttribute = new String(ch, start, length);
        dataAttribute = dataAttribute.trim();
        if (dataAttribute.length() > 0) {

            // CommonUtils.l(dataAttribute);
            if (this.elementname.equals("temperature")) {
                if (this.in_current_conditions) {

                    if (!this.c_stat) {
                        this.setCurrentTemp(dataAttribute);
                    } else {
                        this.setCurrentTemp(CommonUtils.transferToCTemp(
                                dataAttribute, true));
                    }
                }
            }
            if (this.elementname.equals("speed")) {

                this.setCurrentWindSpeedUnit(dataAttribute);

            }
            if (this.elementname.equals("weathericon")) {
                if (!this.in_night_time) {

                    this.addIconList(dataAttribute);
                }
            }
            if (this.elementname.equals("windspeed")) {

                if (this.in_current_conditions)
                    this.setCurrentWindSpeed(dataAttribute);
            }
            if (this.elementname.equals("winddirection")) {
                if (this.in_current_conditions)
                    this.setCurrentWindDirection(dataAttribute);
            }
            if (this.elementname.equals("humidity")) {

                if (this.in_current_conditions)
                    this.setCurrentHum(dataAttribute);
            }

            if (this.elementname.equals("hightemperature")) {
                if (this.in_forecast_conditions) {

                    if (!this.c_stat) {
                        this.addHighList(dataAttribute);
                    } else {
                        this.addHighList(CommonUtils.transferToCTemp(
                                dataAttribute, true));
                    }
                }
            }
            if (this.elementname.equals("lowtemperature")) {
                if (this.in_forecast_conditions) {

                    if (!this.c_stat) {
                        this.addLowList(dataAttribute);
                    } else {
                        this.addLowList(CommonUtils.transferToCTemp(
                                dataAttribute, false));
                    }
                }
            }

            if (this.elementname.equals("weathertext")) {
                if (this.in_current_conditions) {

                    this.setCurrentCondition(dataAttribute);
                }
            }
        }
    }

    private static String getDirection(String paramString) {
        if (TextUtils.equals(paramString, "0")) {
            return "N";
        }
        if (TextUtils.equals(paramString, "22.5")) {
            return "NNE";
        }
        if (TextUtils.equals(paramString, "45")) {
            return "NE";
        }
        if (TextUtils.equals(paramString, "67.5")) {
            return "ENE";
        }
        if (TextUtils.equals(paramString, "90")) {
            return "E";
        }
        if (TextUtils.equals(paramString, "112.5")) {
            return "ESE";
        }
        if (TextUtils.equals(paramString, "135")) {
            return "SE";
        }
        if (TextUtils.equals(paramString, "157.5")) {
            return "SSE";
        }
        if (TextUtils.equals(paramString, "180")) {
            return "S";
        }

        if (TextUtils.equals(paramString, "202.5")) {
            return "SSW";
        }

        if (TextUtils.equals(paramString, "225")) {
            return "SW";
        }
        if (TextUtils.equals(paramString, "247.5")) {
            return "WSW";
        }
        if (TextUtils.equals(paramString, "270")) {
            return "W";
        }
        if (TextUtils.equals(paramString, "292.5")) {
            return "WNW";
        }
        if (TextUtils.equals(paramString, "315")) {
            return "NW";
        }
        if (TextUtils.equals(paramString, "337.5")) {
            return "NNW";
        }

        return paramString;
    }

}