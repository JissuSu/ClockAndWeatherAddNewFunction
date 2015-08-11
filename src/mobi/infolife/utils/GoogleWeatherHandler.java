package mobi.infolife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GoogleWeatherHandler extends DefaultHandler {
	private boolean in_forecast_information = false;
	private boolean in_current_conditions = false;
	private boolean in_forecast_conditions = false;
	private boolean error_conditions = false;

	private String current_city;
	private Integer current_f_temp;
	private Integer current_c_temp;
		

	private String current_condition;
	private String current_hum;
	private String current_wind;
	private String iconURL;

	private ArrayList<String> highList = new ArrayList<String>();;
	private ArrayList<String> lowList = new ArrayList<String>();;
	private ArrayList<String> dayList = new ArrayList<String>();;
	private ArrayList<String> iconList = new ArrayList<String>();;
	private ArrayList<String> conditionList = new ArrayList<String>();;

	private boolean usingSITemperature = false;

	public boolean getXmlValidStat() {
		return error_conditions;
	}

	public String getCurrentWind() {
		return current_wind;
	}

	public void setCurrentWind(String current_wind) {
		this.current_wind = current_wind;
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

	public void setCurrentFTemp(Integer temp) {
		if (temp == null)
			temp = 0;
		this.current_f_temp = temp;
	}

	public Integer getCurrentFTemp() {
		if (this.current_f_temp == null)
			return 0;
		else
			return this.current_f_temp;
	}

	public void setCurrentCTemp(Integer temp) {
		if (temp == null)
			temp = 0;
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

	public String getLowList(int index, boolean useCelsius) {

		if (this.lowList.size() > index) {
			// Utils.l("index=" + index + ":" + this.lowList.get(index));
			if (this.lowList.get(index) != null) {
				if (useCelsius)
					return this.lowList.get(index);
				else
					return this.transferToFTemp(this.lowList.get(index),false);
			} else
				return "--";
		} else
			return "--";
	}

	public String getHighList(int index, boolean useCelsius) {
		if (this.highList.size() > index) {
			if (this.highList.get(index) != null) {
				if (useCelsius)
					return this.highList.get(index);
				else
					return this.transferToFTemp(this.highList.get(index),true);
			} else
				return "--";
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

	public Integer getCurrentCTemp() {
		if (this.current_c_temp == null)
			return 0;
		else
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
		// 'Outer' Tags
		if (localName.equals("forecast_information")) {
			this.in_forecast_information = true;
		} else if (localName.equals("current_conditions")) {
			this.in_current_conditions = true;
		} else if (localName.equals("forecast_conditions")) {
			this.in_forecast_conditions = true;
		} else if (localName.equals("problem_cause")) {
			this.error_conditions = true;
		} else {
			String dataAttribute = atts.getValue("data");
			// 'Inner' Tags of "<forecast_information>"
			if (localName.equals("city")) {
				this.setCurrentCity(dataAttribute);
			} else if (localName.equals("postal_code")) {
			} else if (localName.equals("latitude_e6")) {
				/* One could use this to convert city-name to Lat/Long. */
			} else if (localName.equals("longitude_e6")) {
				/* One could use this to convert city-name to Lat/Long. */
			} else if (localName.equals("forecast_date")) {
			} else if (localName.equals("current_date_time")) {

			} else if (localName.equals("unit_system")) {
				if (dataAttribute.equals("SI"))
					this.usingSITemperature = true;
			}
			// SHARED(!) 'Inner' Tags within "<current_conditions>" AND
			// "<forecast_conditions>"
			else if (localName.equals("day_of_week")) {
				if (this.in_current_conditions) {
					// not use
				} else if (this.in_forecast_conditions) {
					this.addDayList(dataAttribute);
				}
			} else if (localName.equals("icon")) {
				if (this.in_current_conditions) {
					this.setIconURL(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.addIconList(dataAttribute);
				}
			} else if (localName.equals("condition")) {
				if (this.in_current_conditions) {
					this.setCurrentCondition(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.addConditionList(dataAttribute);
				}
			}

			// 'Inner' Tags within "<current_conditions>"
			else if (localName.equals("temp_f")) {
				this.setCurrentFTemp(Integer.parseInt(dataAttribute));
			} else if (localName.equals("temp_c")) {
				this.setCurrentCTemp(Integer.parseInt(dataAttribute));
			} else if (localName.equals("humidity")) {
				this.setCurrentHum(dataAttribute);
			} else if (localName.equals("wind_condition")) {
				this.setCurrentWind(dataAttribute);
			}
			// 'Inner' Tags within "<forecast_conditions>"
			else if (localName.equals("low")) {
				// int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.addLowList(dataAttribute);
				} else {
					this.addLowList(this.transferToCTemp(dataAttribute,false));
				}
			} else if (localName.equals("high")) {
				// int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.addHighList(dataAttribute);
				} else {
					this.addHighList(this.transferToCTemp(dataAttribute,true));
				}
			}
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("forecast_information")) {
			this.in_forecast_information = false;
		} else if (localName.equals("current_conditions")) {
			this.in_current_conditions = false;
		} else if (localName.equals("forecast_conditions")) {
			this.in_forecast_conditions = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		/*
		 * <element>characters</element>
		 */
	}

	private String transferToCTemp(String tempF,boolean highStat ) {
		try {
			String tempC = "";
			if(highStat)
				tempC = String.valueOf(Math.ceil((Integer.parseInt(tempF) - 32) / 1.8));
			else
				tempC = String.valueOf(Math.floor((Integer.parseInt(tempF) - 32) / 1.8));

			int dotIndex = 
					tempC.indexOf(".");
			return tempC.substring(0, dotIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	private String transferToFTemp(String tempC,boolean highStat) {
		try {
			String tempF ="";
			if(highStat)
				tempF = String.valueOf(Math.ceil((Integer.parseInt(tempC) * 1.8 + 32)));
			else
				tempF = String.valueOf(Math.floor((Integer.parseInt(tempC) * 1.8 + 32)));

			int dotIndex = tempF.indexOf(".");
			return tempF.substring(0, dotIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	// private String transferToFTemp(String ctemp) {
	// try {
	// String ftemp = String.valueOf((Integer.parseInt(ctemp) - 32) / 1.8);
	// int dotIndex = ftemp.indexOf(".");
	// return ftemp.substring(0, dotIndex);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return "error";
	// }
	// }
}