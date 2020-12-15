package com.tav.service.bo;

import com.tav.service.base.db.dto.BaseFWDTOImpl;
import com.tav.service.base.db.model.BaseFWModelImpl;
import com.tav.service.dto.EvaluatePlan1DTO;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "evaluate_plan1")
public class EvaluatePlan1BO extends BaseFWModelImpl {
	private  gid;		//Khóa tự sinh
	private  expertise_organ;		//Cơ quan thẩm định
	private  plan_number;		//Số kế hoạch
	private  place1;		//Địa điểm lập kế hoạch 1
	private  expertise_date;		//Ngày lập kế hoạch
	private  dept_id;		//Tên tổ chức, đơn vị được thẩm định
	private  dept_other;		//Đơn vị khác
	private  the_bases;		//Các căn cứ
	private  the_purpose;		//Mục đích, yêu cầu
	private  place_and_time;		//Thời gian, địa điểm
	private  test_content;		//Nội dung kiểm tra
	private  the_participants;		//Thành phần tham gia
	private  organization_perform;		//Tổ chức thực hiện
	private  master_id;		//Thủ trưởng
	private  left_long;		//Kinh độ trái
	private  right_long;		//Kinh độ phải

	public EvaluatePlan1BO(){
		setColId("gid");
		setColName("gid");
		setUniqueColumn(new String[]{"gid"});
	}

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence",
		parameters = {
			@Parameter(name = "sequence", value = "evaluate_plan1_seq")
		}
	)

	@Column(name = "gid", length = 500000)
	public Long getGid(){
		return gid;
	}

	public void setGid(Long gid){
		this.gid = gid;
	}

	@Column(name = "expertise_organ", length = 500000)
	public Long getExpertise_organ(){
		return expertise_organ;
	}

	public void setExpertise_organ(Long expertise_organ){
		this.expertise_organ = expertise_organ;
	}

	@Column(name = "plan_number", length = 500000)
	public String getPlan_number(){
		return plan_number;
	}

	public void setPlan_number(String plan_number){
		this.plan_number = plan_number;
	}

	@Column(name = "place1", length = 500000)
	public Long getPlace1(){
		return place1;
	}

	public void setPlace1(Long place1){
		this.place1 = place1;
	}

	@Column(name = "expertise_date", length = 50000)
	public Date getExpertise_date(){
		return expertise_date;
	}

	public void setExpertise_date(Date expertise_date){
		this.expertise_date = expertise_date;
	}

	@Column(name = "dept_id", length = 500000)
	public Long getDept_id(){
		return dept_id;
	}

	public void setDept_id(Long dept_id){
		this.dept_id = dept_id;
	}

	@Column(name = "dept_other", length = 500000)
	public String getDept_other(){
		return dept_other;
	}

	public void setDept_other(String dept_other){
		this.dept_other = dept_other;
	}

	@Column(name = "the_bases", length = 500000)
	public String getThe_bases(){
		return the_bases;
	}

	public void setThe_bases(String the_bases){
		this.the_bases = the_bases;
	}

	@Column(name = "the_purpose", length = 500000)
	public String getThe_purpose(){
		return the_purpose;
	}

	public void setThe_purpose(String the_purpose){
		this.the_purpose = the_purpose;
	}

	@Column(name = "place_and_time", length = 500000)
	public String getPlace_and_time(){
		return place_and_time;
	}

	public void setPlace_and_time(String place_and_time){
		this.place_and_time = place_and_time;
	}

	@Column(name = "test_content", length = 500000)
	public String getTest_content(){
		return test_content;
	}

	public void setTest_content(String test_content){
		this.test_content = test_content;
	}

	@Column(name = "the_participants", length = 200)
	public String getThe_participants(){
		return the_participants;
	}

	public void setThe_participants(String the_participants){
		this.the_participants = the_participants;
	}

	@Column(name = "organization_perform", length = 500000)
	public String getOrganization_perform(){
		return organization_perform;
	}

	public void setOrganization_perform(String organization_perform){
		this.organization_perform = organization_perform;
	}

	@Column(name = "master_id", length = 500000)
	public String getMaster_id(){
		return master_id;
	}

	public void setMaster_id(String master_id){
		this.master_id = master_id;
	}

	@Column(name = "left_long", length = 500000)
	public Double getLeft_long(){
		return left_long;
	}

	public void setLeft_long(Double left_long){
		this.left_long = left_long;
	}

	@Column(name = "right_long", length = 500000)
	public Double getRight_long(){
		return right_long;
	}

	public void setRight_long(Double right_long){
		this.right_long = right_long;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		EvaluatePlan1DTO evaluatePlan1DTO = new EvaluatePlan1DTO();
		evaluatePlan1DTO.setGid(gid);
		evaluatePlan1DTO.setExpertise_organ(expertise_organ);
		evaluatePlan1DTO.setPlan_number(plan_number);
		evaluatePlan1DTO.setPlace1(place1);
		evaluatePlan1DTO.setExpertise_date(expertise_date);
		evaluatePlan1DTO.setDept_id(dept_id);
		evaluatePlan1DTO.setDept_other(dept_other);
		evaluatePlan1DTO.setThe_bases(the_bases);
		evaluatePlan1DTO.setThe_purpose(the_purpose);
		evaluatePlan1DTO.setPlace_and_time(place_and_time);
		evaluatePlan1DTO.setTest_content(test_content);
		evaluatePlan1DTO.setThe_participants(the_participants);
		evaluatePlan1DTO.setOrganization_perform(organization_perform);
		evaluatePlan1DTO.setMaster_id(master_id);
		evaluatePlan1DTO.setLeft_long(left_long);
		evaluatePlan1DTO.setRight_long(right_long);
		return evaluatePlan1DTO;
	}
}
