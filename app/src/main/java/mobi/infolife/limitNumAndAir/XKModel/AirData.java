package mobi.infolife.limitNumAndAir.XKModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by longlong on 15-8-4.
 */

@Table(name = "AirData")
public class AirData extends Model {

	@Column(name = "ad_cityname")
	public String cityname;

	@Column(name = "ad_airstatus")
	public String airstatus;

	@Column(name = "ad_airnum")
	public String airnum;

	public AirData(String cityname, String airstatus, String airnum) {
		super();
		this.cityname = cityname;
		this.airstatus = airstatus;
		this.airnum = airnum;
	}

	public AirData() {
		super();
	}
}
