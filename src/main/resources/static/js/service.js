
    Class('com.jd.h5.check.service', {
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
            _this._root.find("#bannerId").val(_this._options.id);
            _this._root.find("#title").val(_this._options.title);
            _this._root.find("#weight").val(_this._options.weight);
            _this._root.find("input[id=showOn]").prop("checked", _this._options.showing);
            _this._root.find("input[id=showOff]").prop("checked", !_this._options.showing);

        },

        // event.
        _bindEvent : function() {
            var _this = this;
            _this._bindSaveBtnEvent();
            _this._bindDeleteEvent();
            _this._bindTitleLengthEvent();
        },

        _bindTitleLengthEvent : function() {
            var _this = this;
            _this._root.on("input propertychange", "[id=title]", function() {
                _this._root.find("#inputSize").text($(this).val().length);
            });
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
                    message : "Update Banner FAIL",
                    type : "error"
                });
            });
        },

        _bindDeleteEvent : function() {
            var _this = this;
            _this._root.on("click", "[id=delete]", function() {
                var _data = _this._getBannerInfo();
                if (_data) {
                    _this._saveEvent(_this._getBannerInfo());
                }

            });
        },

        _deleteEvent : function(file, target, width, height) {
            var _this = this;
            var requestData = new FormData();
            requestData.append("file", file);
            requestData.append("type", "IMAGE");
            requestData.append("width", width);
            requestData.append("height", height);
            $.ajax({
                type : 'post',
                url: $.lineLiveObsImageURL(),
                processData : false,
                contentType : false,
                data : requestData,
                beforeSend : function(xhr) {
                    // _this._root.find("#imageSrc").attr("src",
                    // "/static/images/loading.gif");
                },
                success : function(data) {

                    if (data && data.code == 200) {
                        _this._root.find("#" + target).attr("src", $.getImageURL(data.result));
                        //save obs info
                        _this._root.find("#" + target).prop("hash",data.result.hash);
                        _this._root.find("#" + target).prop("oid",data.result.oid);
                        _this._root.find("#" + target).prop("sid",data.result.sid);
                        _this._root.find("#" + target).prop("svc",data.result.svc);

                    } else {
                        $.Alert("upload failed!");
                    }
                },
                error : function() {
                    $.Alert({
                        message : " Image upload FAIL",
                        type : "error"
                    });
                }
            });
            return false;
        }

    });
