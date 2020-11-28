<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="dialog-formView">
	<form:form id="evaluatePlan1Form" modelAttribute="evaluatePlan1Form" class="form-horizontal">	
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<input type="hidden" id="gid" name="gid" value=""/>
		<input type="hidden" id="isedit" name="isedit" value="0"/>
		<fieldset>
			<legend class="fs-legend-head">
				<span class="iconFS"></span>
				<span class="titleFS" style="color: #047fcd !important;"><b>Thông tin chung</b></span>
			</legend>
			<div class="form-group-add row">
				<label class="col-md-1 control-label ">Cơ quan thẩm định</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="expertise_organView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Số kế hoạch</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="plan_numberView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Địa điểm lập kế hoạch 1</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="place1View" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Ngày lập kế hoạch</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="expertise_dateView" type="text" readonly="true"/>
				</div>
			</div>

			<div class="form-group-add row">
				<label class="col-md-1 control-label ">Tên tổ chức, đơn vị được thẩm định</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="dept_idView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Đơn vị khác</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="dept_otherView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Các căn cứ</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="the_basesView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Mục đích, yêu cầu</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="the_purposeView" type="text" readonly="true"/>
				</div>
			</div>

			<div class="form-group-add row">
				<label class="col-md-1 control-label ">Thời gian, địa điểm</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="place_and_timeView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Nội dung kiểm tra</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="test_contentView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Thành phần tham gia</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="the_participantsView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Tổ chức thực hiện</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="organization_performView" type="text" readonly="true"/>
				</div>
			</div>

			<div class="form-group-add row">
				<label class="col-md-1 control-label ">Thủ trưởng</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="master_idView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Kinh độ trái</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="left_longView" type="text" readonly="true"/>
				</div>
				<label class="col-md-1 control-label ">Kinh độ phải</label>
				<div class="col-md-2">
					<input class="form-control" placeholder="" name="" id="right_longView" type="text" readonly="true"/>
				</div>
			</div>

		</fieldset>
	</form:form>
</div>
<script type="text/javascript">
	$("#dialog-formView").dialog({
		width: isMobile.any() ? $(window).width() : ($(window).width() / 5 * 4),
		height: $(window).height() / 5 * 4,
		autoOpen: false,
		modal: true,
		position: [($(window).width() / 10 * 1), 50],
		open: function () {
			$('.areaTable').addClass('custom-overlay-popup-add-edit');
			$('.dialogAddEdit').css('z-index', 1001);

		},
		close: function () {
			$('.areaTable').removeClass('custom-overlay-popup-add-edit');

		},
		buttons: [{
			html: "<fmt:message key='button.close' />",
			"class": "btn btn-default",
			click: function () {
			$(this).dialog('close');
			}
 			}, {
				html: "<fmt:message key='button.update' />",
				"class": "btn btn-primary",
				"id": "btnAddTblInfoNotifyYes",
				click: function () {
					var item = $('#isedit').val();
					if (item === '0') {
						addTblDocumentTypeMethod();
					} else {
						editTblDocumentTypeMethod();
					}
				}
			}
		]
	});
</script>