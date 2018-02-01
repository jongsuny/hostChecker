
Class('com.jongsuny.check.group', {
    init : function(options) {
        var _this = this;
        _this._options = $.extend({
            "rootDiv" : 'section.content'
        }, options);
        _this._options.serviceConfig = options.serviceConfig;
        _this._options.domain = options.serviceConfig.domain;
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
        _this._bindSaveGroupBtnEvent();
        _this._bindDeleteGroupBtnEvent();
        _this._bindCreateGroupBtnEvent();
        _this._bindAddNodeBtnEvent();
        _this._bindAddMultipleNodeBtnEvent();
        _this._bindDeleteNodeBtnEvent();
        _this._bindCheckAllNodeBtnEvent();
    },
    _bindSaveGroupBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=saveGroup]").on('click', function(e) {
            var groupId = $(e.target).data('group-id');
            var _data = _this._getGroup(groupId);
            if (_data) {
                _this._saveGroupEvent(_data);
            }

        });
    },
    _bindDeleteGroupBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=deleteGroup]").on('click', function(e) {
            var groupId = $(e.target).data('group-id');
            if (groupId) {
                _this._deleteGroupEvent(groupId);
            }

        });
    },
    _bindCreateGroupBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=createGroup]").on('click', function() {
            var _data = _this._getGroup('newGroup');
            if (_data) {
                _this._createGroupEvent(_data);
            }

        });
    },
    _bindAddNodeBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=addNode]").on('click', function(e) {
            $("#addNodeModal").modal("show");
            var groupId = $(e.target).data('group-id');
            $("#addNodeGroupId").val(groupId);
            
        });
        $("#addNodeSaveBtn").on('click', function() {
            var groupId = $("#addNodeGroupId").val();
            var _data = _this._getNode(groupId);
            if(_data) {
                _this._createNodeEvent(groupId, _data, false)
            }
        })
    },
    _bindAddMultipleNodeBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=addMultipleNode]").on('click', function(e) {
            $("#addMultipleNodeModal").modal("show");
            var groupId = $(e.target).data('group-id');
            $("#addMultipleNodeGroupId").val(groupId);
            
        });
        $("#addMultipleNodeSaveBtn").on('click', function() {
            var groupId = $("#addMultipleNodeGroupId").val();
            var _data = _this._getMultipleNode(groupId);
            if(_data) {
                _this._createNodeEvent(groupId, _data, true)
            }
        })
    },
    _bindDeleteNodeBtnEvent : function() {
        var _this = this;
        // $("#save").on('click',function(e){console.log(e)});
        $("[name=deleteNode]").on('click', function(e) {
            var groupId = $(e.target).data('group-id');
            var query = '[data-group-id='+groupId+'][name=ip]';
            var checkedCount = 0;
            $.each($(query),function(){
                if(this.checked){
                    checkedCount++;
                }
            });
            if(checkedCount < 1) {
                $.Alert('No node to delete. Please select at list one node.')
                return;
            }
            _this._deleteNodeEvent(groupId);

        });
    },
    _bindCheckAllNodeBtnEvent : function() {
        var _this = this;
        $("[name=checkAllNode]").on('click', function(e) {
            var groupId = $(e.target).data('group-id');
            var checked = $(e.target).is(':checked');
            var query = '[data-group-id='+groupId+'][name=ip]';
            $(query).prop('checked', checked);

        });
    },
    _getNode : function(groupId) {
        var _this = this;
        var parentDiv = $("#addNodeModal");
        var ip = parentDiv.find("[name=ip]").val().trim();
        if (ip.length < 1) {
            $.Alert("IP Address can not be blank.");
            return false;
        }
        var hostname = parentDiv.find("[name=hostname]").val().trim();
        var description = parentDiv.find("[name=description]").val().trim();
        if (description.length < 1) {
            $.Alert("description can not be blank.");
            return false;
        }
        var _data = {
            "ip": ip,
            "groupId": groupId,
            "hostname" : hostname,
            "description": description
        }
        return _data;
    },
    _getMultipleNode : function(groupId) {
        var _this = this;
        var parentDiv = $("#addMultipleNodeModal");
        var content = parentDiv.find("#ipList").val().trim();
        if (content.length < 1) {
            $.Alert("IP Address can not be blank.");
            return false;
        }
        if (content.startsWith('[')) {
            try {
                return JSON.parse(content);
            } catch(e) {
                $.Alert('json is invalid!')
                return null;
            }
        } else {
            var list = content.split('\n');
            var _data = new Array();
            $.each(list, function(){
                var line = this;
                if(line.trim() == "") {
                    return;
                }
                var splited = line.split(',');
                var ip = {};
                if(splited.length > 1) {
                    if(splited.length != 3) {
                        $.Alert(line + ' is invalid!')
                        return false;
                    }
                    ip = {
                        "ip": splited[0].trim(),
                        "groupId": groupId,
                        "hostname" : splited[1].trim(),
                        "description": splited[2].trim()
                    }
                } else {
                    ip = {
                        "ip": line,
                        "groupId": groupId,
                        "hostname" : line,
                        "description": ''
                    }
                }
                _data.push(ip);
            });
            return _data;
        }
        return null;
    },
    _getGroup : function(groupId) {
        var _this = this;
        var parentDiv = $("#" + groupId);
        var groupName = parentDiv.find("[name=groupName]").val().trim();
        if (groupName.length < 1) {
            $.Alert("groupName can not be blank.");
            return false;
        }
        var defaultGroup = parentDiv.find("[name=isDefaultGroup]").is(':checked');
        var description = parentDiv.find("[name=description]").val().trim();
        if (description.length < 1) {
            $.Alert("description can not be blank.");
            return false;
        }
        var _data = {
            "domain": _this._options.domain,
            "groupId": groupId,//
            "groupName": groupName,
            "description": description,
            "defaultGroup": defaultGroup
        }
        return _data;
    },
    
    
    _saveGroupEvent : function(group) {
        var _this = this;
        $.ajax({
            url : '/config/group/'+group.domain+'/update',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(group),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "group updated successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "Edit group FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update group FAIL",
                type : "error"
            });
        });
    },
    _createGroupEvent : function(group) {
        var _this = this;
        $.ajax({
            url : '/config/group/'+group.domain+'/create',
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(group),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "group created successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "create group FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "create group FAIL",
                type : "error"
            });
        });
    },
    _deleteGroupEvent : function(groupId) {
        var _this = this;
        $.ajax({
            url : '/config/group/'+_this._options.domain+'/delete?groupId=' + groupId,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200 && data.result) {
                $.Alert( "group deleted successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "delete group FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "delete group FAIL",
                type : "error"
            });
        });
    },
    _createNodeEvent : function(groupId, node, isMultiple) {
        var _this = this;
        var url = '/config/node/'+_this._options.domain+'/'+groupId+'/create';
        if(isMultiple) {
            url = url + 's'
        }
        $.ajax({
            url : url,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(node),
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $.Alert( "node created successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "create node FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "create node FAIL",
                type : "error"
            });
        });
    },
    _deleteNodeEvent : function(groupId) {
        var _this = this;
        var query = '[data-group-id='+groupId+'][name=ip]';
        var checkedCount = 0;
        var param = new Array();
        $.each($(query),function(){
            if(this.checked){
                checkedCount++;
                param.push(this.value);
            }
        });
        var url = '';
        if(checkedCount == 1) {
            url = '/config/node/'+_this._options.domain + '/' + groupId +'/delete?groupId=' + groupId;
        } else {
            url = '/config/node/'+_this._options.domain + '/' + groupId +'/deletes';
        }
        url = '/config/node/'+_this._options.domain + '/' + groupId +'/deletes';
        $.ajax({
            url : url,
            type : 'post',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            data : JSON.stringify(param),
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200 && data.result) {
                $.Alert( "node deleted successfully." );
                $.reloadCurrent();

            } else {
                $.Alert({
                    message : "delete node FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "delete node FAIL",
                type : "error"
            });
        });
    }

});
