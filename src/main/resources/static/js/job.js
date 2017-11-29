
    Class('com.jd.h5.check.Job', {
        init : function(options) {
            var _this = this;
            _this._options = $.extend({
                "rootDiv" : 'section.content'
            }, options);
            _this._options.serviceConfig = options.serviceConfig;
            _this._options.domain = options.serviceConfig.domain;
            _this._root = $(_this._options.rootDiv);
            _this._init();
            _this._bindEvent();
        },

        _init : function() {
            var _this = this;
        },

        // event.
        _bindEvent : function() {
            var _this = this;
            _this._bindAddBtnEvent();
            _this._bindAddByJsonBtnEvent();
            _this._bindDeleteBtnEvent();
            _this._bindSaveJsonBtnEvent();
            _this._bindSaveJobEvent();

        },
        _bindSaveJsonBtnEvent : function() {
            var _this = this;
            $("[name=addJobByJsonBtn]").on('click', function() {
            	saveJob
                $("#addJobByJsonModal").modal("show");
            });
        },
        _bindSaveJsonBtnEvent : function() {
            var _this = this;
            $("[name=saveJobJson]").on('click', function() {
                var content = $("#jsonContent").val().trim();
                if(content == "" || content.length < 1) {
                    $.Alert("json data can not be blank.");
                    return false;
                }
                var _data = null;
                try {
                    _data = JSON.parse(content);
                } catch(e) {
                    $.Alert('invalid json format!')
                    return false;
                }
                if (_data) {
                    _this._saveJsonEvent(_data);
                }
            });
        },
         _bindAddBtnEvent : function() {
            var _this = this;
            $("[name=addJobBtn]").on('click', function(e) {
            	var parentDiv = $("#addJobModal");
            	var checkPoints = parentDiv.find("#checkPointId");
            	checkPoints.empty();
            	$.each(_this._options.serviceConfig.checkPoints, function() {
            		var checkPoint = this;
            		var option = $("<option>").val(checkPoint.id).text(checkPoint.name);
            		checkPoints.append(option);
            	});
            	var groups = parentDiv.find("[name=groupList]");
            	var html = "";
            	var tmplPre = '<div class="checkbox"><label>'
            	// 
            	var tmplTail = '</label></div>';
				$.each(_this._options.serviceConfig.groups, function() {
            		var group = this;
            		var node = '<input type="checkbox" name="group" value="' + group.groupId + '"/>';
            		node += group.groupName;
            		html += tmplPre + node + tmplTail;
            	});
            	groups.html(html);
                $("#addJobModal").modal("show");
            });
        },
        _bindSaveJobEvent : function() {
            var _this = this;
            $("[name=saveJob]").on('click', function() {
                var _data = _this._getJob();
                if(_data) {
                    _this._saveJsonEvent(_data)
                }
            });
        },
         _bindAddByJsonBtnEvent : function() {
            var _this = this;
            $("[name=addJobByJsonBtn]").on('click', function(e) {
                $("#addJobByJsonModal").modal("show");
            });
        },
        
        _getJob : function() {
            var _this = this;
            var parentDiv = $("#addJobModal");
            var domain = _this._options.domain;
            if (domain.length < 1) {
                $.Alert("domain can not be blank.");
                return false;
            }
            var jobName = parentDiv.find("[name=jobName]").val().trim();
            if (jobName.length < 1) {
                $.Alert("jobName can not be blank.");
                return false;
            }
            var checkPointId = parentDiv.find("[name=checkPointId]").val().trim();
            if (checkPointId.length < 1) {
                $.Alert("checkPointId can not be blank.");
                return false;
            }
            var groups = parentDiv.find("[name=groupList] [name=group]");
            var checkedCount = 0;
            var groupIds = new Array();
            $.each(groups, function() {
                if(this.checked){
                    checkedCount++;
                    groupIds.push(this.value);
                }
            });
            if(checkedCount < 1) {
            	$.Alert('Please select at least one group!')
            	return null;
            }
            var _data = {
                "domain": domain,
                "jobName": jobName,
                "checkPointId": checkPointId,
                "groups": groupIds
            }
            return _data;
        },
        _bindDeleteBtnEvent : function() {
            var _this = this;
            $("[name=deleteJob]").on('click', function(e) {
                var jobid = $(e.target).data('job-id');
                if(jobid) {
                    _this._deleteJobEvent(jobid)
                }
            });
        },
        _deleteJobEvent : function(jobId) {
            var _this = this;
            $.ajax({
                url : '/config/job/'+_this._options.domain+'/delete?jobId='+jobId,
                type : 'post',
                contentType : 'application/json; charset=utf-8',
                dataType : 'json',
                cache : false
            }).done(function(data, textStatus, jqXHR) {
                if (data && data.code == 200 && data.result) {
                    $.Alert( "Job deleted successfully." );
                    $.reloadCurrent();

                } else {
                    $.Alert({
                        message : "delete Job FAIL - " + data.message,
                        type : "error"
                    });
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                $.Alert({
                    message : "delete Job FAIL",
                    type : "error"
                });
            });
        },
        _saveJsonEvent : function(job) {
            var _this = this;
            $.ajax({
                url : '/config/job/create',
                type : 'post',
                contentType : 'application/json; charset=utf-8',
                data : JSON.stringify(job),
                dataType : 'json',
                cache : false
            }).done(function(data, textStatus, jqXHR) {
                if (data && data.code == 200) {
                    $.Alert( "Job created successfully." );
                    $.reloadCurrent();
                } else {
                    $.Alert({
                        message : "Edit Job FAIL - " + data.message,
                        type : "error"
                    });
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                $.Alert({
                    message : "Update Job FAIL",
                    type : "error"
                });
            });
        }
    });
