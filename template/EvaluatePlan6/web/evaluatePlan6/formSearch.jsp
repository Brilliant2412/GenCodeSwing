<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section id="widget-grid" class="" style="
         border-left-width: 0px;
         border-top-width: 0px;
         border-bottom-width: 0px;
         border-right-width: 0px;">
    <div class="row">
        <div class="col-lg-12">
            <div class="jarviswidget" id="wid-id-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false" data-widget-sortable="false" role="widget">
                <!--<button type="button" class="btn btn-sm btn-success" id="show_hide_go">Tìm kiếm - In danh sách</button><br>-->
                <div class="formsearchBody" style="margin-left:4px !important;">
                    <form id="productForm" class="form-horizontal bv-form" novalidate="novalidate" style="width: 100%;">
                        <button type="submit" class="bv-hidden-submit" style="display: none; width: 0px; height: 0px;"></button>
                        <fieldset>
								<div class="form-group has-feedback">
									<label class="col-lg-1 control-label" style="padding:5px 4px">Từ khóa</label>
                                	<div class="col-lg-2 selectContainer">
                                    	<input type="text" class="form-control" placeholder="Nhập từ khóa" id="keySearch">
                                	</div>
									<label class="col-lg-1 control-label">Cơ quan thẩm định</label>
									<div class="col-lg-2 selectContainer">
										<div id="cbexpertise_organSearch"></div>
									</div>
									<label class="col-lg-1 control-label">Địa điểm lập kế hoạch 1</label>
									<div class="col-lg-2 selectContainer">
										<div id="cbplace1Search"></div>
									</div>
								<label class="col-md-1 control-label" ></label>
                                <div class="col-lg-2 selectContainer">
                                    <button id="btnSearch" class="btn btn-success" type="button" style="width: 100%;">Tìm kiếm</button>
                                </div>
								</div>
								<div class="form-group has-feedback">
									<label class="col-lg-1 control-label">Ngày lập kế hoạch</label>
									<div class="col-lg-1 selectContainer">
										<input type="text" class="dateCalendar" placeholder="Từ" id="expertise_dateSearchFrom">
									</div>
									<div class="col-lg-1">
										<input type="text" class="dateCalendar" placeholder="Đến" id="expertise_dateSearchTo"> 
									</div>
								<div class="form-group has-feedback">
									<label class="col-lg-1 control-label">Ngày lập kế hoạch</label>
									<div class="col-lg-1 selectContainer">
										<input type="text" class="dateCalendar" placeholder="Từ" id="expertise_dateSearchFrom">
									</div>
									<div class="col-lg-1">
										<input type="text" class="dateCalendar" placeholder="Đến" id="expertise_dateSearchTo"> 
									</div>
								<label class="col-md-1 control-label" ></label>
                                <div class="col-lg-2 selectContainer">
                                    <button id="btnSearch" class="btn btn-success" type="button" style="width: 100%;">Tìm kiếm</button>
                                </div>
								</div>
                        </fieldset>
                        
                    </form>
                </div>
                <br>
            </div>
        </div>
</section>

<style>
    .ui-select-match-text{
        width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        padding-right: 40px;
    }
    .ui-select-toggle > .btn.btn-link {
        margin-right: 10px;
        top: 6px;
        position: absolute;
        right: 10px;
    }
    .multiselect-container .input-group{
        margin: 0px !important;
    }
    .checkbox-status .checkbox-inline:nth-child(2n+1){
        margin-left: 0px !important;
    }
    .formsearchBody{
        display: block
    }
</style>