
    Class('com.jd.h5.check.checkPoint', {
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
            _this._bindEditBtnEvent();
            _this._bindDeleteBtnEvent();
            _this._bindSaveJsonBtnEvent();

        },
        _bindSaveJsonBtnEvent : function() {
            var _this = this;
            $("[name=addCheckPointByJsonBtn]").on('click', function() {
                $("#addCheckPointByJsonModal").modal("show");
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
         _bindAddBtnEvent : function() {
            var _this = this;
            $("[name=addCheckPointBtn]").on('click', function(e) {
                $("#checkPointModal").modal("show");
            });
        },
         _bindAddByJsonBtnEvent : function() {
            var _this = this;
            $("[name=addCheckPointByJsonBtn]").on('click', function(e) {
                $("#addCheckPointByJsonModal").modal("show");
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


                    $("#checkPointModal").modal("show");
                    
                }
            });
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
       _saveEvent : function(serviceConfig) {
            var _this = this;
            $.ajax({
                url : '/config/service/create',
                type : 'post',
                contentType : 'application/json; charset=utf-8',
                data : JSON.stringify(serviceConfig),
                dataType : 'json',
                cache : false
            }).done(function(data, textStatus, jqXHR) {
                if (data && data.code == 200) {
                    $.Alert( "Service created successfully." );

                } else {
                    $.Alert({
                        message : "Edit service FAIL - " + data.message,
                        type : "error"
                    });
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                $.Alert({
                    message : "Update Banner FAIL",
                    type : "error"
                });
            });
        },
        _saveJsonEvent : function(serviceConfig) {
            var _this = this;
            $.ajax({
                url : '/config/service/create/all',
                type : 'post',
                contentType : 'application/json; charset=utf-8',
                data : JSON.stringify(serviceConfig),
                dataType : 'json',
                cache : false
            }).done(function(data, textStatus, jqXHR) {
                if (data && data.code == 200) {
                    $.Alert( "Service created successfully." );
                } else {
                    $.Alert({
                        message : "Edit service FAIL - " + data.message,
                        type : "error"
                    });
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                $.Alert({
                    message : "Update Banner FAIL",
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
        }
    });
