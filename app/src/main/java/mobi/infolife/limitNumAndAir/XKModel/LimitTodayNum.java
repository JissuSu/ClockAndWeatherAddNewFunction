package mobi.infolife.limitNumAndAir.XKModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "LimitTodayNum")
public class LimitTodayNum extends Model {

	@Column(name = "ltn_name")
	public String name;

	@Column(name = "ltn_id")
	public String id;

	@Column(name = "ltn_date")
	public String date;

	@Column(name = "ltn_nums")
	public String nums;

	public LimitTodayNum(String name, String id, String date, String nums) {
		super();
		this.name = name;
		this.id = id;
		this.date = date;
		this.nums = nums;
	}

	public LimitTodayNum() {
		super();
	}

	@Override
	public String toString() {
		return "LimitTodayNum{" + "name='" + name + '\'' + ", id='" + id + '\''
				+ ", date='" + date + '\'' + ", nums='" + nums + '\'' + '}';
	}
}
