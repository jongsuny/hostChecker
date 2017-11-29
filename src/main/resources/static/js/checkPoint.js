
Class('com.jd.h5.check.checkPoint', {
    init : function(options) {
        var _this = this;
        _this._options = $.extend({
            "rootDiv" : 'section.content'
        }, options);
        _this._options.serviceConfig = options.serviceConfig;
        _this._options.domain = options.serviceConfig.domain;
        _this._options.dictionary = options.dictionary;
        _this._root = $(_this._options.rootDiv);
        _this._init();
        _this._bindEvent();
        _this._constant = {};
        _this._constant.prefix = '<div class="form-group"><label class="col-sm-3"><input name="name" type="text" class="form-control"';
        _this._constant.middle = '/></label><div class="col-sm-7"><input name="value" type="text" class="form-control" ';
        _this._constant.suffix = '/></div><div class="col-sm-2"><a name="delEntryBtn" class="btn btn-danger" href="javascript:void(0);">Del</a></div></div>';
    },

    _init : function() {
        var _this = this;
    },

    // event.
    _bindEvent : function() {
        var _this = this;
        _this._bindModalShowBtnEvent();
        _this._bindAddByJsonBtnEvent();
        _this._bindEditBtnEvent();
        _this._bindDeleteBtnEvent();
        _this._bindSaveJsonBtnEvent();
        _this._bindAddHeaderBtnEvent();
        _this._bindAddParemeterBtnEvent();
        _this._bindDeleteEntryEvent();
        _this._bindSaveCheckPointEvent();
        _this._bindAddValidationBtnEvent();
        _this._bindSaveCheckPointValidationsJsonEvent();
    },
     _bindModalShowBtnEvent : function() {
        var _this = this;
        $("[name=addCheckPointBtn]").on('click', function(e) {
            $("#checkPointModal").modal("show");
        });
        $("[name=addHeaderByTextBtn]").on('click', function(e) {
            $("#addHeaderByTextModal").modal("show");
            $("[name=parseCheckPointHeaderBtn]").on('click', function(e) {
                e.stopPropagation();
                _this._parseHeaderEvent(e, _this);
            });
        });
        $("[name=addParameterByURLBtn]").on('click', function(e) {
            $("#addParameterByTextModal").modal("show");
            $("#addParameterByTextModal [name=parseCheckPointParameterBtn]").on('click', function(e) {
                _this._parseParameterEvent(e, _this);
            });
        });
        $("[name=addCheckPointByJsonBtn]").on('click', function() {
            $("#addCheckPointByJsonModal").modal("show");
        });
        $("[name=addCheckPointBtn]").on('click', function(e) {
            $("#checkPointModal").modal("show");
        });

        $("[name=addValidationByJsonBtn]").on('click', function(e) {
            $("#addCheckPointValidationByJsonModal").modal("show");
        });
    },
    _bindAddHeaderBtnEvent : function() {
        var _this = this;
        
        $("[name=addHeaderEntryBtn]").on('click', function() {
            var parentDiv = $("#checkPointHeaders");
            var htmlContent = "";
            htmlContent += _this._constant.prefix + 'placeholder="header name"';
            htmlContent += _this._constant.middle + 'placeholder="enter header value here..."';
            htmlContent += _this._constant.suffix;
            var currentCentent = $(htmlContent);
            parentDiv.find(".box-info").append(currentCentent);
            parentDiv.find(".box-info").animate({ scrollTop: 9999 }, 'slow');
            _this._bindDeleteEntryEvent();
        });
    },
    _bindAddParemeterBtnEvent : function() {
        var _this = this;
        $("[name=addParameterEntryBtn]").on('click', function() {
            var parentDiv = $("#checkPointParameters");
            var htmlContent = "";
            htmlContent += _this._constant.prefix + 'placeholder="parameter name"';
            htmlContent += _this._constant.middle + 'placeholder="enter parameter value here..."';
            htmlContent += _this._constant.suffix;
            var currentCentent = $(htmlContent);
            parentDiv.find(".box-info").append(currentCentent);
            parentDiv.find(".box-info").animate({ scrollTop: 9999 }, 'slow');
            _this._bindDeleteEntryEvent();
        });
    },
    _bindDeleteEntryEvent : function(){
        var _this = this;
        $("[name=delEntryBtn]").on('click', function() {
            $(this).parent().parent().remove();
        });
    },
    _bindSaveJsonBtnEvent : function() {
        var _this = this;
        $("[name=delEntryBtn]").on('click', function() {
            $(this).parent().parent().remove();
        });
    },
    _bindSaveJsonBtnEvent : function() {
        var _this = this;
        $("[name=saveCheckPointJson]").on('click', function() {
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
     _bindAddByJsonBtnEvent : function() {
        var _this = this;
        $("[name=addCheckPointByJsonBtn]").on('click', function(e) {
            $("#addCheckPointByJsonModal").modal("show");
        });
    },
    _bindAddValidationBtnEvent : function () {
        var _this = this;
        var template = [{
          "validateType": "STATUS_CODE",
          "name": "",
          "operator": "eq",
          "value": "200",
          "description": "return code is not 200"
        },
        {
          "validateType": "header",
          "name": "Content-Type",
          "operator": "eq",
          "value": "text/html;charset=utf-8",
          "description": "content-type is not html"
        },
        {
          "validateType": "body",
          "name": "",
          "operator": "contains",
          "value": "html",
          "description": "not html"
        }];
        // type, operator, name, value, description
        _this._bindValidationInit();
        var parentDiv = $("#checkPointValidations");
        var template = parentDiv.find("[name=validateType]").first().parent().parent().clone();
        _this._options.validationTemplate = template;
        parentDiv.find("[name=addValidationBtn]").on('click', function(e){
            _this._bindValidationAddEntry();
        });
        
    },
    _bindValidationInit :function(){
        var _this = this;
        var parentDiv = $("#checkPointValidations");
        var selectType = parentDiv.find("[name=validateType]")
        $.each(_this._options.dictionary.validationTypes, function (){
            var type = this;
            var option = $("<option>").val(type.$name).text(type.$name);
            if(type.$name == 'STATUS_CODE') {
                option.selected = true;
            }
            selectType.append(option);    
        });
        var operatorType = parentDiv.find("[name=operator]")
        $.each(_this._options.dictionary.operators, function (){
            var operator = this;
            var option = $("<option>").val(operator.$name).text(operator.$name);
            if(operator.$name == 'EQUALS') {
                option.selected = true;
            }
            operatorType.append(option);    
        });
        parentDiv.find("[name=name]").val("");
        parentDiv.find("[name=value]").val("200");
        parentDiv.find("[name=description]").val("http response code is not 200!");
    },
    _bindValidationAddEntry :function(){
        var _this = this;
        var parentDiv = $("#checkPointValidations");
        var tbody = parentDiv.find("table tbody");
        var template = _this._options.validationTemplate.clone();
        template.find("[name=name]").val("");
        template.find("[name=value]").val("");
        template.find("[name=description]").val("");
        tbody.append(template);
        _this._bindDeleteEntryEvent();
        parentDiv.find(".box-info").animate({ scrollTop: 9999 }, 'slow');
    },
    _findCheckConfigById : function(checkId) {
        var _this = this;
        var _result = null;
        $.each(_this._options.serviceConfig.checkPoints, function(){
            var currentCheck = this;
            if(checkId == this.id) {
                _result = this;
                return false;
            }
        });
        return _result;
    },
    _bindDeleteBtnEvent : function() {
        var _this = this;
        $("[name=deleteCheckPoint]").on('click', function(e) {
            var checkId = $(e.target).data('check-id');
            if(checkId) {
                _this._deleteCheckPointEvent(checkId)
            }
        });
    },
    _bindSaveCheckPointEvent : function () {
        var _this = this;
        $("[name=saveCheckPointBtn]").on('click', function(e) {
            var _data = _this._buildCheckConfig(_this);
            var checkConfigId = $("#checkPointId").val();
            if(_data) {
                _this._saveCheckConfig(checkConfigId, _data)
            }
        });
    },

    _bindEditBtnEvent : function() {
        var _this = this;
        $("[name=editCheckPoint]").on('click', function(e) {
            var checkId = $(e.target).data('check-id');
            var config = _this._findCheckConfigById(checkId);
            if(config) {
                console.log(config)
                $("#checkPointId").val(config.id);
                $("#checkPointName").val(config.name);
                $("#checkPointDescription").val(config.description);
                $("#checkPointPort").val(config.port);
                $("#checkPointPath").val(config.path);
                $("#checkPointSchema").val(config.schema);
                $("#checkPointMethod").val(config.method);
                if(config.headers && config.headers.length > 0) {
                    $("#checkPointHeaders").find(".box-info").empty();
                    $.each(config.headers, function (){
                        _this._buildHeaderEntry(this, _this)
                    });
                }
                if(config.arguments && config.arguments.length > 0) {
                    $("#checkPointParameters").find(".box-info").empty();
                    $.each(config.arguments, function (){
                        _this._buildParameterEntry(this, _this)
                    });
                }
                if(config.validations && config.validations.length > 0) {
                    $("#checkPointValidations").find(".table tbody").empty();
                    $.each(config.validations, function (){
                        _this._buildValidationEntryEdit(this, _this)
                    });
                }
                $("#checkPointModal").modal("show");
            }
        });
    },
    _buildHeaderEntry : function(header, _this) {
        var parentDiv = $("#checkPointHeaders");
        var htmlContent = "";
        htmlContent += _this._constant.prefix + 'value="'+ header.name+'"';
        htmlContent += _this._constant.middle + 'value="'+ header.value+'"';
        htmlContent += _this._constant.suffix;
        var currentCentent = $(htmlContent);
        parentDiv.find(".box-info").append(currentCentent);
        _this._bindDeleteEntryEvent();
    },
     _buildParameterEntry : function(parameter, _this) {
        var parentDiv = $("#checkPointParameters");
        var htmlContent = "";
        htmlContent += _this._constant.prefix + 'value="'+ parameter.name+'"';
        htmlContent += _this._constant.middle + 'value="'+ parameter.value+'"';
        htmlContent += _this._constant.suffix;
        var currentCentent = $(htmlContent);
        parentDiv.find(".box-info").append(currentCentent);
        _this._bindDeleteEntryEvent();
    },
     _buildValidationEntryEdit : function(validation, _this) {
        var parentDiv = $("#checkPointValidations");
        var template = _this._options.validationTemplate.clone();
        if(typeof(validation.validateType) == "object") {
            template.find("[name=validateType]").val(validation.validateType.$name);    
        } else {
            template.find("[name=validateType]").val(validation.validateType);    
        }
        if(typeof(validation.operator) == "object") {
            template.find("[name=operator]").val(validation.operator.$name);
        } else {
            template.find("[name=operator]").val(validation.operator);
        }
        
        template.find("[name=name]").val(validation.name);
        template.find("[name=value]").val(validation.value);
        template.find("[name=description]").val(validation.description);
        parentDiv.find(".table tbody").append(template);
        _this._bindDeleteEntryEvent();
    },
    _bindSaveCheckPointValidationsJsonEvent : function (){
        var _this = this;
        $("[name=saveCheckPointValidationsJson]").on('click', function (){
            var content = $("#validationJsonContent").val().trim();
            try {
                var json = JSON.parse(content);
                if(json) {
                    var parentDiv = $("#checkPointValidations");
                    parentDiv.find(".table tbody").empty();
                    $.each(json, function(){
                        var validation = this;
                        if(!_this._isValidValidateTypes(validation.validateType)) {
                            $.Alert("ValidationType:" + validation.validateType + " is not valid!")
                            return false;
                        }
                        if(!_this._isValidOperators(validation.operator)) {
                            $.Alert("Operator:" + validation.operator + " is not valid!")
                            return false;
                        }
                        _this._buildValidationEntryEdit(validation, _this);
                        $("#addCheckPointValidationByJsonModal").modal("hide");
                    })
                }
            } catch(e) {
                $.Alert('Invalid validation json object!');
            }
        });
    },
    _isValidValidateTypes : function (validateType) {
        var _this = this;
        var result = false;
        var toValidate = validateType;
        $.each(_this._options.dictionary.validationTypes, function (){
            if(this.$name == toValidate) {
                result = true;
                return false;
            }
        })
        return result;
    },
    _isValidOperators : function (operator) {
        var _this = this;
        var result = false;
        var toValidate = operator;
        $.each(_this._options.dictionary.operators, function (){
            if(this.$name == toValidate) {
                result = true;
                return false;
            }
        })
        return result;
    },
    _buildCheckConfig : function(_this){
        // build basic
        var _data = {};
        _data.name = $("#checkPointName").val();
        _data.description = $("#checkPointDescription").val();
        _data.port = $("#checkPointPort").val();
        _data.path = $("#checkPointPath").val();
        _data.schema = $("#checkPointSchema").val();
        _data.method = $("#checkPointMethod").val();
        if(_data.name == "" || _data.name.length < 1) {
            $.Alert("name can not be blank.");
            return false;
        }
        if(_data.description == "" || _data.description.length < 1) {
            $.Alert("description can not be blank.");
            return false;
        }
        if(_data.port == "" || _data.port.length < 1 || _data.port < 1 || _data.port > 65535) {
            $.Alert("port is invalid.");
            return false;
        }
        if(_data.path == "" || _data.path.length < 1) {
            $.Alert("path can not be blank.");
            return false;
        }
        if(_data.schema == "" || _data.schema.length < 1) {
            $.Alert("schema can not be blank.");
            return false;
        }
        if(_data.method == "" || _data.method.length < 1) {
            $.Alert("method can not be blank.");
            return false;
        }
        // build basic finished.
        // build headers
        var headers = _this._buildEntry("#checkPointHeaders");
        _data.headers = headers;
        // build parameters
        var parameters = _this._buildEntry("#checkPointParameters");
        _data.arguments = parameters;
        // build validations
        var validations = _this._buildValidationEntry("#checkPointValidations");
        _data.validations = validations;
        
        console.log(_data)
        return _data;
    },
    _buildEntry : function (divId) {
        var parameters = new Array();
        $(divId).find(".form-group").each(function (){
            var _div = $(this);
            var name = _div.find("[name=name]").val().trim();
            var value = _div.find("[name=value]").val().trim();
            if(name == "" || value == "") {
                return;
            }
            var entry = {};
            entry.name = name;
            entry.value = value;
            parameters.push(entry);
        });
        return parameters;
    },
    _buildValidationEntry : function (divId) {
        var validations = new Array();
        $(divId).find("table tbody tr").each(function (){
            var _div = $(this);
            var validateType = _div.find("[name=validateType]").val().trim();
            var operator = _div.find("[name=operator]").val().trim();
            var name = _div.find("[name=name]").val().trim();
            var value = _div.find("[name=value]").val().trim();
            var description = _div.find("[name=description]").val().trim();
            if(value == "") {
                return;
            }
            var entry = {};
            entry.validateType = validateType;
            entry.operator = operator;
            entry.name = name;
            entry.value = value;
            entry.description = description;
            validations.push(entry);
        });
        return validations;
    },
   _saveCheckConfig : function(checkConfigId,checkConfig) {
        var _this = this;
        var actionType = 'create';
        if(checkConfigId) {
            actionType = 'update';
            checkConfig.id = checkConfigId;
        }
        $.ajax({
            url : '/config/checkPoint/'+_this._options.domain+'/' + actionType,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(checkConfig),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "CheckConfig "+actionType+" successfully." );
                $.reloadCurrent();
            } else {
                $.Alert({
                    message : "Edit CheckConfig FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update CheckConfig FAIL",
                type : "error"
            });
        });
    },
    _deleteCheckPointEvent : function(checkPointId) {
        var _this = this;
        $.ajax({
            url : '/config/checkPoint/'+_this._options.domain+'/delete?checkPointId='+checkPointId,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200 && data.result) {
                $.Alert( "CheckPoint deleted successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "delete CheckPoint FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "delete CheckPoint FAIL",
                type : "error"
            });
        });
    },
    _saveJsonEvent : function(checkConfig) {
        var _this = this;
        $.ajax({
            url : '/config/checkPoint/'+_this._options.domain+'/create',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(checkConfig),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "CheckConfig created successfully." );
                $.reloadCurrent();
            } else {
                $.Alert({
                    message : "Edit CheckConfig FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update CheckConfig FAIL",
                type : "error"
            });
        });
    },
    _parseHeaderEvent : function (e, _this) {
        var _this = _this;
        var content = $("#headerTextContent").val().trim();
        $.ajax({
            url : '/support/parse/header',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : content,
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200 && typeof(data.result) == 'object') {
                var parentDiv = $("#checkPointHeaders");
                parentDiv.find(".box-info").empty()
                var htmlContent = "";
                $.each(data.result, function() {
                    var dic = this;
                    htmlContent += _this._constant.prefix;
                    htmlContent += 'value="'+dic.name + '"';
                    htmlContent += _this._constant.middle;
                    htmlContent += 'value="'+dic.value + '"';
                    htmlContent += _this._constant.suffix;
                });
                parentDiv.find(".box-info").html(htmlContent);
                _this._bindDeleteEntryEvent();
            } else {
                $.Alert('parse header failed!');
                // $.Alert({
                //     message : "Edit CheckConfig FAIL - " + data.message,
                //     type : "error"
                // });
            }
            $("#addHeaderByTextModal").modal("hide");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update CheckConfig FAIL",
                type : "error"
            });
            $("#addHeaderByTextModal").modal("hide");
        });
    },
    _parseParameterEvent : function (e, _this) {
        var _this = _this;
        var content = $("#queryTextContent").val().trim();
        $.ajax({
            url : '/support/parse/parameter',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : content,
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200 && typeof(data.result) == 'object') {
                var parentDiv = $("#checkPointParameters");
                parentDiv.find(".box-info").empty()
                var htmlContent = "";
                $.each(data.result, function() {
                    var dic = this;
                    htmlContent += _this._constant.prefix;
                    htmlContent += 'value="'+dic.name + '"';
                    htmlContent += _this._constant.middle;
                    htmlContent += 'value="'+dic.value + '"';
                    htmlContent += _this._constant.suffix;
                });
                parentDiv.find(".box-info").html(htmlContent);
                _this._bindDeleteEntryEvent();
            } else {
                $.Alert('parse header failed!');
                // $.Alert({
                //     message : "Edit CheckConfig FAIL - " + data.message,
                //     type : "error"
                // });
            }
            $("#addParameterByTextModal").modal("hide");
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update CheckConfig FAIL",
                type : "error"
            });
            $("#addParameterByTextModal").modal("hide");
        });
    }
});
