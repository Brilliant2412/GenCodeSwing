package com.tav.service.bo;

import com.tav.service.base.db.dto.BaseFWDTOImpl;
import com.tav.service.base.db.model.BaseFWModelImpl;
import com.tav.service.dto.EvaluatePlan6DTO;
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
@Table(name = "evaluate_plan6")
public class EvaluatePlan6BO extends BaseFWModelImpl {
	private Long gid;		//Khóa tự sinh
	private Long expertise_organ;		//Cơ quan thẩm định
	private String plan_number;		//Số kế hoạch
	private Long place1;		//Địa điểm lập kế hoạch 1
	private Date expertise_date;		//Ngày lập kế hoạch
	private Long dept_id1;		//Tên tổ chức, đơn vị được thẩm định
	private String dept_other1;		//Đơn vị khác
	private String the_bases1;		//Các căn cứ
	private String the_purpose1;		//Mục đích, yêu cầu
	private String place_and_time1;		//Thời gian, địa điểm
	private String test_content;		//Nội dung kiểm tra
	private String the_participants;		//Thành phần tham gia
	private String organization_perform;		//Tổ chức thực hiện
	private String master_id;		//Thủ trưởng
	private String file;		//File đính kèm

	public EvaluatePlan6BO(){
		setColId("gid");
		setColName("gid");
		setUniqueColumn(new String[]{"gid"});
	}

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence",
		parameters = {
			@Parameter(name = "sequence", value = "evaluate_plan6_seq")
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

	@Column(name = "dept_id1", length = 500000)
	public Long getDept_id1(){
		return dept_id1;
	}

	public void setDept_id1(Long dept_id1){
		this.dept_id1 = dept_id1;
	}

	@Column(name = "dept_other1", length = 500000)
	public String getDept_other1(){
		return dept_other1;
	}

	public void setDept_other1(String dept_other1){
		this.dept_other1 = dept_other1;
	}

	@Column(name = "the_bases1", length = 500000)
	public String getThe_bases1(){
		return the_bases1;
	}

	public void setThe_bases1(String the_bases1){
		this.the_bases1 = the_bases1;
	}

	@Column(name = "the_purpose1", length = 500000)
	public String getThe_purpose1(){
		return the_purpose1;
	}

	public void setThe_purpose1(String the_purpose1){
		this.the_purpose1 = the_purpose1;
	}

	@Column(name = "place_and_time1", length = 500000)
	public String getPlace_and_time1(){
		return place_and_time1;
	}

	public void setPlace_and_time1(String place_and_time1){
		this.place_and_time1 = place_and_time1;
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

	@Column(name = "file", length = 500000)
	public String getFile(){
		return file;
	}

	public void setFile(String file){
		this.file = file;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		EvaluatePlan6DTO evaluatePlan6DTO = new EvaluatePlan6DTO();
		evaluatePlan6DTO.setGid(gid);
		evaluatePlan6DTO.setExpertise_organ(expertise_organ);
		evaluatePlan6DTO.setPlan_number(plan_number);
		evaluatePlan6DTO.setPlace1(place1);
		evaluatePlan6DTO.setExpertise_date(expertise_date);
		evaluatePlan6DTO.setDept_id1(dept_id1);
		evaluatePlan6DTO.setDept_other1(dept_other1);
		evaluatePlan6DTO.setThe_bases1(the_bases1);
		evaluatePlan6DTO.setThe_purpose1(the_purpose1);
		evaluatePlan6DTO.setPlace_and_time1(place_and_time1);
		evaluatePlan6DTO.setTest_content(test_content);
		evaluatePlan6DTO.setThe_participants(the_participants);
		evaluatePlan6DTO.setOrganization_perform(organization_perform);
		evaluatePlan6DTO.setMaster_id(master_id);
		evaluatePlan6DTO.setFile(file);
		return evaluatePlan6DTO;
	}
}
