package com.tav.web.dto;

public class EvaluatePlan6DTO{
	private Long gid;		//Khóa tự sinh
	private Long expertise_organ;		//Cơ quan thẩm định
	private String expertise_organST;
	private String plan_number;		//Số kế hoạch
	private Long place1;		//Địa điểm lập kế hoạch 1
	private String place1ST;
	private String expertise_date;		//Ngày lập kế hoạch
	private String expertise_dateST;
	private Long dept_id1;		//Tên tổ chức, đơn vị được thẩm định
	private String dept_id1ST;
	private String dept_other1;		//Đơn vị khác
	private String the_bases1;		//Các căn cứ
	private String the_purpose1;		//Mục đích, yêu cầu
	private String place_and_time1;		//Thời gian, địa điểm
	private String test_content;		//Nội dung kiểm tra
	private String the_participants;		//Thành phần tham gia
	private String organization_perform;		//Tổ chức thực hiện
	private String master_id;		//Thủ trưởng
	private String file;		//File đính kèm

	public Long getGid(){
		return gid;
	}

	public void setGid(Long  gid){
		this.gid = gid;
	}

	public Long getExpertise_organ(){
		return expertise_organ;
	}

	public void setExpertise_organ(Long  expertise_organ){
		this.expertise_organ = expertise_organ;
	}

	public String getExpertise_organST(){
		return expertise_organST;
	}

	public void setExpertise_organST(String expertise_organST){
		this.expertise_organST = expertise_organST;
	}

	public String getPlan_number(){
		return plan_number;
	}

	public void setPlan_number(String  plan_number){
		this.plan_number = plan_number;
	}

	public Long getPlace1(){
		return place1;
	}

	public void setPlace1(Long  place1){
		this.place1 = place1;
	}

	public String getPlace1ST(){
		return place1ST;
	}

	public void setPlace1ST(String place1ST){
		this.place1ST = place1ST;
	}

	public String getExpertise_date(){
		return expertise_date;
	}

	public void setExpertise_date(String  expertise_date){
		this.expertise_date = expertise_date;
	}

	public String getExpertise_dateST(){
		return expertise_dateST;
	}

	public void setExpertise_dateST(String expertise_dateST){
		this.expertise_dateST = expertise_dateST;
	}

	public Long getDept_id1(){
		return dept_id1;
	}

	public void setDept_id1(Long  dept_id1){
		this.dept_id1 = dept_id1;
	}

	public String getDept_id1ST(){
		return dept_id1ST;
	}

	public void setDept_id1ST(String dept_id1ST){
		this.dept_id1ST = dept_id1ST;
	}

	public String getDept_other1(){
		return dept_other1;
	}

	public void setDept_other1(String  dept_other1){
		this.dept_other1 = dept_other1;
	}

	public String getThe_bases1(){
		return the_bases1;
	}

	public void setThe_bases1(String  the_bases1){
		this.the_bases1 = the_bases1;
	}

	public String getThe_purpose1(){
		return the_purpose1;
	}

	public void setThe_purpose1(String  the_purpose1){
		this.the_purpose1 = the_purpose1;
	}

	public String getPlace_and_time1(){
		return place_and_time1;
	}

	public void setPlace_and_time1(String  place_and_time1){
		this.place_and_time1 = place_and_time1;
	}

	public String getTest_content(){
		return test_content;
	}

	public void setTest_content(String  test_content){
		this.test_content = test_content;
	}

	public String getThe_participants(){
		return the_participants;
	}

	public void setThe_participants(String  the_participants){
		this.the_participants = the_participants;
	}

	public String getOrganization_perform(){
		return organization_perform;
	}

	public void setOrganization_perform(String  organization_perform){
		this.organization_perform = organization_perform;
	}

	public String getMaster_id(){
		return master_id;
	}

	public void setMaster_id(String  master_id){
		this.master_id = master_id;
	}

	public String getFile(){
		return file;
	}

	public void setFile(String  file){
		this.file = file;
	}

}