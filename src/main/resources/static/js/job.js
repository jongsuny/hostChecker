Class('com.jongsuny.check.Job', {
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
        $.bindSampleInput();
        _this._bindAddBtnEvent();
        _this._bindAddByJsonBtnEvent();
        _this._bindDeleteBtnEvent();
        _this._bindSaveJsonBtnEvent();
        _this._bindSaveJobEvent();
        _this._bindReadJobBtnEvent();

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
    _bindReadJobBtnEvent : function() {
        var _this = this;
        $("[name=readJobBtn]").on('click', function(e) {
            var ele = $(e.target);
            var jobId = ele.data('job-id');
            _this._bindRefreshJobResultBtnEvent(jobId);
            _this._readJobEvent(jobId);
        });

    },
    _bindRefreshJobResultBtnEvent : function(jobId) {
        var _this = this;
        $("[name=refreshJobResultBtn]").data('job-id', jobId);
        $("[name=refreshJobResultBtn]").on('click', function(e) {
            var ele = $(e.target);
            var jobId = ele.data('job-id');
            _this._readJobEvent(jobId);
        });
    },
     _bindAddByJsonBtnEvent : function() {
        var _this = this;
        $("[name=addJobByJsonBtn]").on('click', function(e) {
            $("#addJobByJsonModal").modal("show");
        });
    },
    _bindReadNodeDetailBtnEvent : function (){
        var _this = this;
        $("[name=nodeDetail]").on('click', function(e) {
            var ele = $(e.target);
            var jobId = ele.data('job-id');
            var ip = ele.data('ip');
            _this._readNodeResultEvent(ele, jobId, ip);
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
    },

    _renderNodeResult : function(eventTarget, data){
        var _this = this;
        var btn = eventTarget;
        var parentDiv = $("#nodeDetailModal");
        var result = data.result;
        parentDiv.find("[name=time]").val(new Date(result.startDate).format("yyyy-MM-dd hh:mm:ss"));
        parentDiv.find("[name=name]").val(btn.data("name"))
        parentDiv.find("[name=group]").val(btn.data("groups"))
        parentDiv.find("[name=ip]").val(btn.data("ip"))
        parentDiv.find("[name=status]").val(result.nodeStatus)
        parentDiv.find("[name=rt]").val(result.elapsed)
        if(result.validateEntry) {
            var entry = result.validateEntry;
            parentDiv.find("[name=url]").val(entry.url)
            if(entry.responseWrapper) {
                _this._renderNodeResultResponse(parentDiv, entry.responseWrapper);
            } else {
                parentDiv.find("[name=code]").val('');
                parentDiv.find("#nodeResultHeader .box-info").empty();
                parentDiv.find("#responseBody .box-info").empty();
            }
            _this._renderNodeResultValidations(parentDiv, entry.validationList);
        } 
        $("#nodeDetailModal").modal("show");
    },
    _renderNodeResultResponse : function (parentDiv, res){
        parentDiv.find("[name=code]").val(res.code)
        var header = parentDiv.find("#nodeResultHeader .box-info");
        header.empty();
        var body = parentDiv.find("#responseBody .box-info");
        body.empty();
        if(res.header) {
            // render header
            var html = "";
            $.each(res.header, function(k, v){
                html += '<div class="form-group"><label class="col-sm-3">'
                html += k;
                html += '</label><div class="col-sm-9"><input name="value" type="text" class="form-control" readOnly="true" value="';
                html += v;
                html += '"/></div></div>'
            })
            header.html(html)
        }
        if(res.body) {
            body.text(res.body);
        }
    },
    _renderNodeResultValidations : function (parentDiv, validations){
        var validationDiv = parentDiv.find("#responseValidations .box-info table tbody");
        validationDiv.empty();
        var htmlContent = "";
        var index = 1;
        $.each(validations, function (){
            var data = this;
            var val = this.validation;
            htmlContent += '<tr>';
            htmlContent += '<td>' + index++ +'</td>';
            htmlContent += '<td>' + data.result +'</td>';
            htmlContent += '<td>' + val.validateType +'</td>';
            htmlContent += '<td title="'+val.name+'">' + val.name +'</td>';
            htmlContent += '<td>' + val.operator +'</td>';
            htmlContent += '<td title="'+val.value+'">' + val.value +'</td>';
            if(val.validateType == "body") {
                htmlContent += '<td>see response body tab!</td>';
            } else {
                htmlContent += '<td title="'+$.nullToEmpy(data.actual)+'">' + $.trimToVisible($.nullToEmpy(data.actual)) +'</td>';    
            }
            htmlContent += '<td title="'+val.description+'">' + $.trimToVisible(val.description) +'</td>';
            htmlContent += '</tr>';
        });
        validationDiv.html(htmlContent);
    },
    _readNodeResultEvent : function(eventTarget, jobId, ip) {
        var _this = this;
        $.ajax({
            url : '/config/job/'+_this._options.domain+'/'+jobId+'/read?ip='+ip,
            type : 'get',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false,
            that : _this,
            eventTarget : eventTarget
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                var _this = this.that;
                _this._renderNodeResult(this.eventTarget,data);
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
    },
    _renderJobResult : function (data) {
        var _this = this;
        var parentDiv = $("#jobResultModal .box-info .jobResultTable")
        var title = $("#jobResultModal [name=jobName]");
        parentDiv.find("tbody").empty()
        var htmlContent = "";
        var index = 1;
        $.each(data.result.results, function(k,v) {
            var node = this;
            htmlContent += '<tr>';
            htmlContent += '<td>' + index++ +'</td>';
            htmlContent += '<td>' + k +'</td>';
            // htmlContent += '<td>' + v.nodeStatus +'</td>';
            if(v.nodeStatus == 'ALIVE') {
                htmlContent += '<td><div class="bg-green">' + v.nodeStatus +'</div></td>';
            } else if(v.nodeStatus == 'INVALID') {
                htmlContent += '<td><div class="bg-yellow">' + v.nodeStatus +'</div></td>';
            } else if(v.nodeStatus == 'DOWN') {
                htmlContent += '<td><div class="bg-orange">' + v.nodeStatus +'</div></td>';
            } else {
                htmlContent += '<td><div class="bg-red">' + v.nodeStatus +'</div></td>';
            }
            htmlContent += '<td>' + new Date(v.startDate).format("yyyy-MM-dd hh:mm:ss") +'</td>';
            htmlContent += '<td>' + v.elapsed +'</td>';
            if(v.nodeStatus != 'ALIVE' && v.nodeStatus != 'UNKNOWN' && v.nodeStatus != 'CHECKING') {
                htmlContent += '<td><button name="nodeDetail" type="button" class="btn btn-success"'
                +'data-job-id="' + data.result.jobId+'"'
                +'data-ip="' + k +'"'
                +'data-name="' + data.result.jobName +'"'
                +'data-groups="' + data.result.groups +'"'
                +'>detail</button>' +'</td></tr>';
            } else {
                htmlContent += '<td></td>';
            }
            htmlContent += '</tr>';
        });
        title.text('Job result:(' + data.result.jobName+')');
        parentDiv.find("tbody").html(htmlContent);
        _this._bindReadNodeDetailBtnEvent();
        $("#jobResultModal").modal("show");
    },
    _readJobEvent : function(jobId) {
        var _this = this;
        $.ajax({
            url : '/config/job/'+_this._options.domain+'/read?jobId='+jobId,
            type : 'get',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false,
            that : _this
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                var _this = this.that;
                _this._renderJobResult(data);
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
