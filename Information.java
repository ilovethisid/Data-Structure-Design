public class Information {
	int num_road_div_cd;
	int num_road_axis_cd;
	int num_link_id=0;
	String road_div_code[];
	String road_axis_code[];
	
	void allocate_road_div_cd() {
		road_div_code=new String [num_road_div_cd];
	}
	
	void allocate_road_axis_cd() {
		road_axis_code=new String [num_road_axis_cd];
	}
}