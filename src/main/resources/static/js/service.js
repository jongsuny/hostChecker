Class('com.jongsuny.check.service', {
    init : function(options) {
        var _this = this;
        _this._options = $.extend({
            "rootDiv" : 'section.content'
        }, options);
        _this._options.serviceConfig = options.serviceConfig;
        _this._options.domain = options.serviceConfig.domain;
        _this._root = $(_this._options.rootDiv);
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
        $.bindSampleInput();
        _this._bindSaveBtnEvent();
        _this._bindDeleteEvent();
    },
    _getServiceConfig : function() {
        var _this = this;

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
            "domain": _this._options.domain,
            "serviceName": serviceName,//
            "env": env,
            "description": description
        }
        return _data;
    },

    _bindSaveBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("#save").on('click', function() {
            var _data = _this._getServiceConfig();
            if (_data) {
                _this._saveEvent(_data);
            }

        });
    },
   _saveEvent : function(serviceConfig) {
        var _this = this;
        $.ajax({
            url : '/config/service/update',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(serviceConfig),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "Service updated successfully." );
                $.reloadCurrent();
            } else {
                $.Alert({
                    message : "Edit service FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Create service FAIL",
                type : "error"
            });
        });
    },

    _bindDeleteEvent : function() {
        var _this = this;
        _this._root.on("click", "[id=delete]", function() {
            _this._deleteEvent()
        });
    },

    _deleteEvent : function() {
        var _this = this;
        $.ajax({
            url : '/config/service/delete?domain='+_this._options.domain,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "Service deleted successfully." );
                location.href="/"
            } else {
                $.Alert({
                    message : "Delete service FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Delete service FAIL",
                type : "error"
            });
        });
    }
});
