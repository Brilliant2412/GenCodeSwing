package com.tav.service.dto;

import com.tav.service.base.db.dto.BaseFWDTOImpl;
import com.tav.service.bo.EvaluatePlan1BO;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EvaluatePlan1DTO")
public class EvaluatePlan1DTO extends BaseFWDTOImpl<EvaluatePlan1BO> {
	private Long gid;		//Khóa tự sinh
	private Long expertise_organ;		//Cơ quan thẩm định
	private String expertise_organST;
	private String plan_number;		//Số kế hoạch
	private Long place1;		//Địa điểm lập kế hoạch 1
	private String place1ST;
	private Date expertise_date;		//Ngày lập kế hoạch
	private String expertise_dateST;
	private Long dept_id;		//Tên tổ chức, đơn vị được thẩm định
	private String dept_idST;
	private String dept_other;		//Đơn vị khác
	private String the_bases;		//Các căn cứ
	private String the_purpose;		//Mục đích, yêu cầu
	private String place_and_time;		//Thời gian, địa điểm
	private String test_content;		//Nội dung kiểm tra
	private String the_participants;		//Thành phần tham gia
	private String organization_perform;		//Tổ chức thực hiện
	private String master_id;		//Thủ trưởng
	private Double left_long;		//Kinh độ trái
	private Double right_long;		//Kinh độ phải

	public Long getGid(){
		return gid;
	}

	public void setGid(Long gid){
		this.gid = gid;
	}

	public Long getExpertise_organ(){
		return expertise_organ;
	}

	public void setExpertise_organ(Long expertise_organ){
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

	public void setPlan_number(String plan_number){
		this.plan_number = plan_number;
	}

	public Long getPlace1(){
		return place1;
	}

	public void setPlace1(Long place1){
		this.place1 = place1;
	}

	public String getPlace1ST(){
		return place1ST;
	}

	public void setPlace1ST(String place1ST){
		this.place1ST = place1ST;
	}

	public Date getExpertise_date(){
		return expertise_date;
	}

	public void setExpertise_date(Date expertise_date){
		this.expertise_date = expertise_date;
	}

	public String getExpertise_dateST(){
		return expertise_dateST;
	}

	public void setExpertise_dateST(String expertise_dateST){
		this.expertise_dateST = expertise_dateST;
	}

	public Long getDept_id(){
		return dept_id;
	}

	public void setDept_id(Long dept_id){
		this.dept_id = dept_id;
	}

	public String getDept_idST(){
		return dept_idST;
	}

	public void setDept_idST(String dept_idST){
		this.dept_idST = dept_idST;
	}

	public String getDept_other(){
		return dept_other;
	}

	public void setDept_other(String dept_other){
		this.dept_other = dept_other;
	}

	public String getThe_bases(){
		return the_bases;
	}

	public void setThe_bases(String the_bases){
		this.the_bases = the_bases;
	}

	public String getThe_purpose(){
		return the_purpose;
	}

	public void setThe_purpose(String the_purpose){
		this.the_purpose = the_purpose;
	}

	public String getPlace_and_time(){
		return place_and_time;
	}

	public void setPlace_and_time(String place_and_time){
		this.place_and_time = place_and_time;
	}

	public String getTest_content(){
		return test_content;
	}

	public void setTest_content(String test_content){
		this.test_content = test_content;
	}

	public String getThe_participants(){
		return the_participants;
	}

	public void setThe_participants(String the_participants){
		this.the_participants = the_participants;
	}

	public String getOrganization_perform(){
		return organization_perform;
	}

	public void setOrganization_perform(String organization_perform){
		this.organization_perform = organization_perform;
	}

	public String getMaster_id(){
		return master_id;
	}

	public void setMaster_id(String master_id){
		this.master_id = master_id;
	}

	public Double getLeft_long(){
		return left_long;
	}

	public void setLeft_long(Double left_long){
		this.left_long = left_long;
	}

	public Double getRight_long(){
		return right_long;
	}

	public void setRight_long(Double right_long){
		this.right_long = right_long;
	}


	@Override
	public EvaluatePlan1BO toModel() {
		EvaluatePlan1BO evaluatePlan1BO = new EvaluatePlan1BO();
		evaluatePlan1BO.setGid(gid);
		evaluatePlan1BO.setExpertise_organ(expertise_organ);
		evaluatePlan1BO.setPlan_number(plan_number);
		evaluatePlan1BO.setPlace1(place1);
		evaluatePlan1BO.setExpertise_date(expertise_date);
		evaluatePlan1BO.setDept_id(dept_id);
		evaluatePlan1BO.setDept_other(dept_other);
		evaluatePlan1BO.setThe_bases(the_bases);
		evaluatePlan1BO.setThe_purpose(the_purpose);
		evaluatePlan1BO.setPlace_and_time(place_and_time);
		evaluatePlan1BO.setTest_content(test_content);
		evaluatePlan1BO.setThe_participants(the_participants);
		evaluatePlan1BO.setOrganization_perform(organization_perform);
		evaluatePlan1BO.setMaster_id(master_id);
		evaluatePlan1BO.setLeft_long(left_long);
		evaluatePlan1BO.setRight_long(right_long);
		return evaluatePlan1BO;
	}

	@Override
	public Long getFWModelId() {
		return getGid();
	}

	@Override
	public String catchName() {
		return gid.toString();
	}
}
