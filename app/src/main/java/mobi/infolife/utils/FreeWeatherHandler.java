package mobi.infolife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FreeWeatherHandler extends DefaultHandler {
	private boolean in_forecast_information = false;
	private boolean in_current_conditions = false;
	private boolean in_forecast_conditions = false;
	private boolean error_conditions = false;
	private boolean c_stat = true;

	private String current_city;
	private String elementname;

	private String current_f_temp;
	private String current_c_temp;

	private String current_condition;
	private String current_hum;
	private String current_wind_speed;
	private String current_wind_direction;

	private String iconURL;

	private ArrayList<String> highList = new ArrayList<String>();;
	private ArrayList<String> lowList = new ArrayList<String>();;
	private ArrayList<String> dayList = new ArrayList<String>();;
	private ArrayList<String> iconList = new ArrayList<String>();;
	private ArrayList<String> conditionList = new ArrayList<String>();;

	private boolean usingSITemperature = false;

	public FreeWeatherHandler(boolean useC) {
		this.c_stat = useC;
	}

	public boolean getXmlValidStat() {
		return error_conditions;
	}

	public String getCurrentWind() {
		return this.current_wind_direction + " " + this.current_wind_speed;
	}

	public void setCurrentWindSpeed(String wind_speed) {
		this.current_wind_speed = wind_speed;
	}

	public void setCurrentWindDirection(String wind_direction) {
		this.current_wind_direction = wind_direction;
	}

	public String getCurrentCity() {
		return this.current_city;
	}

	public String getCurrentCondition() {
		if (this.current_condition == null)
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
		this.current_city = city;
	}

	public void setCurrentFTemp(String temp) {

		this.current_f_temp = temp;
	}

	public String getCurrentFTemp() {

		return this.current_f_temp;
	}

	public void setCurrentCTemp(String temp) {

		this.current_c_temp = temp;
	}

	public void addDayList(String data) {

		if (data.length() > 0) {
			// Utils.l("add day = " + data);

			this.dayList.add(data);
		} else
			this.dayList.add("--");
	}

	public void addIconList(String data) {
		if (data.length() > 0) {
			data = data.toLowerCase().replaceAll(".gif", "");
			int startIndex = data.lastIndexOf("/") + 1;
			data = data.substring(startIndex);
			// CommonUtils.l("add icon" + data);
			this.iconList.add(data);
		} else
			this.iconList.add("--");
	}

	public void addLowList(String data) {
		if (data.length() > 0) {
			this.lowList.add(data);
			// Utils.l("add low = " + data);
		} else
			this.lowList.add("--");
	}

	public void addHighList(String data) {
		// Utils.l("add high = " + data);

		if (data.length() > 0)
			this.highList.add(data);
		else
			this.highList.add("--");
	}

	public void addConditionList(String data) {
		if (data.length() > 0)
			this.conditionList.add(data);
		else
			this.conditionList.add("--");
	}

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

	public String getConditionList(int index) {
		if (this.conditionList.size() > index)
			if (this.conditionList.get(index).length() < 1)
				return this.conditionList.get(index);
			else
				return "Click to setting";
		else
			return "--";
	}

	public String getCurrentCTemp() {
		return this.current_c_temp;
	}

	public void setCurrentCondition(String condition) {
		this.current_condition = condition;
	}

	public void setCurrentHum(String hum) {
		this.current_hum = hum;
	}

	public void setIconURL(String data) {
		data = data.toLowerCase().replaceAll(".gif", "");
		int startIndex = data.lastIndexOf("/") + 1;
		data = data.substring(startIndex);
		this.iconURL = data;
	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("current_condition")) {
			this.in_current_conditions = true;
		}
		this.elementname = qName;

	}

 

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("current_condition")) {
			this.in_current_conditions = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String dataAttribute = new String(ch, start, length);
		// CommonUtils.l(dataAttribute);
		if (this.elementname.equals("temp_C")) {
			this.setCurrentCTemp(dataAttribute);

		}
		if (this.elementname.equals("temp_F")) {
			this.setCurrentFTemp(dataAttribute);
		}
		if (this.elementname.equals("weatherIconUrl")) {
			this.addIconList(dataAttribute);
		}
		if (this.elementname.equals("windspeedKmph")) {
			if (this.in_current_conditions)
				this.setCurrentWindSpeed(dataAttribute);
		}
		if (this.elementname.equals("winddir16Point")) {
			if (this.in_current_conditions)
				this.setCurrentWindDirection(dataAttribute);
		}
		if (this.elementname.equals("humidity")) {
			if (this.in_current_conditions)
				this.setCurrentHum(dataAttribute);
		}
		if (this.elementname.equals("tempMaxC")) {
			if (this.c_stat)
				this.addHighList(dataAttribute);

		}
		if (this.elementname.equals("tempMinC")) {
			if (this.c_stat)

				this.addLowList(dataAttribute);

		}

		if (this.elementname.equals("tempMaxF")) {
			if (!this.c_stat)
				this.addHighList(dataAttribute);

		}
		if (this.elementname.equals("tempMinF")) {
			if (!this.c_stat)

				this.addLowList(dataAttribute);

		}
		if (this.elementname.equals("weatherDesc")) {
			if (this.in_current_conditions) {

				dataAttribute = dataAttribute.replace("<![CDATA[", "");
				dataAttribute = dataAttribute.replace("]]>", "");
				this.setCurrentCondition(dataAttribute);
			}
		}
	}
}