package mobi.infolife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BestCityDataHandler extends DefaultHandler {
	private boolean centroid = false;
	private String elementname;

	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<String> cityIdList = new ArrayList<String>();
 	private ArrayList<String> adminList = new ArrayList<String>();
	private ArrayList<String> countryList = new ArrayList<String>();

 
	public void addAdminList(String data) {
		if (data.length() > 0) {

			this.adminList.add(data);
		} else
			this.adminList.add("--");
	}

	public void addCityIdList(String data) {
		if (data.length() > 0) {
			this.cityIdList.add(data);
			// Utils.l("add low = " + data);
		} else
			this.cityIdList.add("--");
	}

	public void addNameList(String data) {
		// Utils.l("add high = " + data);

		if (data.length() > 0)
			this.nameList.add(data);
		else
			this.nameList.add("--");
	}

	public void addCountryList(String data) {
		if (data.length() > 0)
			this.countryList.add(data);
		else
			this.countryList.add("--");
	}

	public String getAdminList(int index) {
		if (this.adminList.size() > index)
			return this.adminList.get(index);
		else
			return "--";
	}

	public String getCityIdList(int index) {

		if (this.cityIdList.size() > index) {
			return this.cityIdList.get(index);
		} else
			return "--";
	}

	public String getNameList(int index) {
		if (this.nameList.size() > index) {
			return this.nameList.get(index);
		} else
			return "--";
	}
 

	public String getCountryList(int index) {
		if (this.countryList.size() > index)
			return this.countryList.get(index);
		else
			return "--";
	}

	public int getListSize() {
		if (this.countryList != null)
			return countryList.size();
		else
			return 0;
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
		// 'Outer' Tags¡¢
		if (localName.equals("citylist")) {
			this.centroid = true;
		}
		this.elementname = qName;

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("citylist")) {
			this.centroid = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String dataAttribute = new String(ch, start, length);
		// CommonUtils.l(dataAttribute);
		if (this.elementname.equals("city")) {
 
			this.addNameList(dataAttribute);
		}
		if (this.elementname.equals("country")) {
 
			this.addCountryList(dataAttribute);
		}
		if (this.elementname.equals("adminArea")) {
			this.addAdminList(dataAttribute);
		}
		if (this.elementname.equals("location")) {
			if (this.centroid)
				this.addCityIdList(dataAttribute);
		}
 

	}

}