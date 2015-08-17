package mobi.infolife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class YahooCityDataHandler extends DefaultHandler {
	private boolean centroid = false;
	private String elementname;

	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<String> latList = new ArrayList<String>();
	private ArrayList<String> lonList = new ArrayList<String>();
	private ArrayList<String> adminList = new ArrayList<String>();
	private ArrayList<String> countryList = new ArrayList<String>();

	public void addLonList(String data) {

		if (data.length() > 0) {
			// Utils.l("add day = " + data);

			this.lonList.add(data);
		} else
			this.lonList.add("--");
	}

	public void addAdminList(String data) {
		if (data.length() > 0) {

			this.adminList.add(data);
		} else
			this.adminList.add("--");
	}

	public void addLatList(String data) {
		if (data.length() > 0) {
			this.latList.add(data);
			// Utils.l("add low = " + data);
		} else
			this.latList.add("--");
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

	public String getLatList(int index) {

		if (this.latList.size() > index) {
			return this.latList.get(index);
		} else
			return "--";
	}

	public String getNameList(int index) {
		if (this.nameList.size() > index) {
			return this.nameList.get(index);
		} else
			return "--";
	}

	public String getLonList(int index) {
		if (this.lonList.size() > index) {
			// Utils.l("index=" + index + ":" + this.dayList.get(index));

			return this.lonList.get(index);

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
		// 'Outer' Tags
		if (localName.equals("centroid")) {
			this.centroid = true;
		}
		this.elementname = qName;

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("centroid")) {
			this.centroid = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		String dataAttribute = new String(ch, start, length);
		// CommonUtils.l(dataAttribute);
		if (this.elementname.equals("name")) {
 
			this.addNameList(dataAttribute);
		}
		if (this.elementname.equals("country")) {
 
			this.addCountryList(dataAttribute);
		}
		if (this.elementname.equals("admin1")) {
			this.addAdminList(dataAttribute);
		}
		if (this.elementname.equals("latitude")) {
			if (this.centroid)
				this.addLatList(dataAttribute);
		}
		if (this.elementname.equals("longitude")) {
			if (this.centroid)
				this.addLonList(dataAttribute);
		}

	}

}