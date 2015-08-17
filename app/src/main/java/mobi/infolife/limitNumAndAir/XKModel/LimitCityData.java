package mobi.infolife.limitNumAndAir.XKModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "LimitCityData")
public class LimitCityData extends Model {

	@Column(name = "lcd_id")
	public String id;

	@Column(name = "lcd_name")
	public String name;

	@Column(name = "lcd_limittimearea")
	public String limittimearea;

	@Column(name = "lcd_limitplatenum")
	public String limitplatenum;

	@Column(name = "lcd_limitother")
	public String limitother;

	@Column(name = "lcd_limitinfo")
	public String limitinfo;

	@Column(name = "lcd_limitareaimg")
	public String limitareaimg;

	public LimitCityData(String id, String name, String limittimearea,
			String limitplatenum, String limitother, String limitinfo,
			String limitareaimg) {
		super();
		this.id = id;
		this.name = name;
		this.limittimearea = limittimearea;
		this.limitplatenum = limitplatenum;
		this.limitother = limitother;
		this.limitinfo = limitinfo;
		this.limitareaimg = limitareaimg;
	}

	public LimitCityData() {
		super();
	}

	@Override
	public String toString() {
		return "LimitCityData{" + "id='" + id + '\'' + ", name='" + name + '\''
				+ ", limittimearea='" + limittimearea + '\''
				+ ", limitplatenum='" + limitplatenum + '\'' + ", limitother='"
				+ limitother + '\'' + ", limitinfo='" + limitinfo + '\''
				+ ", limitareaimg='" + limitareaimg + '\'' + '}';
	}
}
