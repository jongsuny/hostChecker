Class('com.jongsuny.check.addService', {
    init : function(options) {
        var _this = this;
        _this._options = $.extend({
            "rootDiv" : 'section.content'
        }, options);
        _this._root = $(_this._options.rootDiv);
        console.log("## _this._options.rootDiv:", _this._options.rootDiv);
        console.log("## _this._root:", _this._root);
        _this._init();
        _this._bindEvent();
    },

    _init : function() {
        var _this = this;
    },

    // event.
    _bindEvent : function() {
        var _this = this;
        _this._bindSaveBtnEvent();
        _this._bindSaveAllInOneBtnEvent();
        $.bindSampleInput();
    },
    _getServiceConfig : function() {
        var _this = this;
        var domain = _this._root.find("#domain").val().trim();
        if (domain.length < 1) {
            $.Alert("domain can not be blank.");
            return false;
        }
        var serviceName = _this._root.find("#serviceName").val().trim();
        if (serviceName.length < 1) {
            $.Alert("title can not be blank.");
            return false;
        }
        var env = _this._root.find("#env").val().trim();
        if (env.length < 1) {
            $.Alert("title can not be blank.");
            return false;
        }
        var description = _this._root.find("#description").val().trim();
        if (description.length < 1) {
            $.Alert("title can not be blank.");
            return false;
        }
        var _data = {
            "domain": domain,
            "serviceName": serviceName,
            "env": env,
            "description": description
        }
        return _data;
    },

    _bindSaveBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("#create").on('click', function() {
            var _data = _this._getServiceConfig();
            if (_data) {
                _this._saveEvent(_data);
            }

        });
    },
    _bindSaveAllInOneBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("#createByJson").on('click', function() {
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
});
