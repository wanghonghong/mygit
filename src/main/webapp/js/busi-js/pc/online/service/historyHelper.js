CommonUtils.regNamespace("historyHelper")
historyHelper = (function () {
    "use strict";
    var position = "hx";
    var storageType = ['localStorage', 'webSql', 'indexDb'];
    var webStorage = {
        localStorage: false,
        webSql: true,
        indexDb: false
    };
    webStorage.LOCAL_STORAGE = "localStorage";
    webStorage.WEB_SQL = "webSql";
    webStorage.INDEX_DB = "indexDb";
    webStorage.switchChange = function (config) {
        for (var type in storageType) {
            if (storageType[type] === config) {
                this[storageType[type]] = true;
            } else {
                this[storageType[type]] = false;
            }
        }
    };
    webStorage.getUsedType = function () {
        for (var type in storageType) {
            if (this[storageType[type]]) {
                return storageType[type];
            }
        }
    };
    var HistoryEntity = function (values) {
        var history_id = values ? values.history_id : null;
        var from_user = values ? values.from_user : null;
        var to_user = values ? values.to_user : null;
        var create_date = values ? values.date : new Date().Format("yyyy-MM-dd hh:mm:ss");
        var type = values ? values.type : '';
        var file_name = values ? values.file_name : '';
        var file_url = values ? values.file_url : '';
        var position = values ? values.position : null;
        var msg = values ? values.msg : null;
        var is_reply = values ? values.is_reply : 0;
        var file_source = values ?values.file_source:'';
        var source = values?values.source:'webim';
    };
    HistoryEntity.prototype.toArr = function () {
        var arr = [];
        for (var pop in this.getPropertys()) {
            arr.push(this[pop]);
        }
        return arr;
    };
    HistoryEntity.prototype.getPropertys = function () {
        var entity = {};
        if (this.history_id) {
            entity.history_id = this.history_id;
        }
        entity.from_user = this.from_user || '';
        entity.to_user = this.to_user || '';
        entity.create_date = this.create_date || new Date().Format("yyyy-MM-dd hh:mm:ss");
        entity.type = this.type || '';
        entity.file_name = this.file_name || '';
        entity.file_url = this.file_url || '';
        entity.position = this.position || 'hx';
        entity.msg = this.msg || '';
        entity.is_reply = 0;
        entity.file_source = this.file_source;
        entity.source = this.source;
        return entity;
    }
    var _init;
    var WebSql = {
        init: function () {
            var db = openDatabase('hx_history', '1.0', 'history', 5 * 1024 * 1024);
            db.transaction(function (tx) {
                // 1. history_id 2.from 3.to 4.date 5.content 6.type 7.name 8.url
                tx.executeSql('CREATE TABLE IF NOT EXISTS historys(history_id INTEGER PRIMARY KEY,from_user,to_user,create_date,type,file_name,file_url,position,msg,is_reply,file_source,source)', [], function () {
                });
            });
        },
        saveHistory: function (message) {
            try {
                var db = openDatabase('hx_history', '1.0', 'history', 10 * 1024 * 1024);
                db.transaction(function (tx) {
                    tx.executeSql('INSERT INTO historys(from_user,to_user,create_date,type,file_name,file_url,position,msg,is_reply,file_source,source) VALUES (?,?,?,?,?,?,?,?,?,?,?)', message.toArr(), function (tx, results) {

                    });
                });
            } catch (e) {
                console.error(e);
            }
        },
        loadHistory: function (name, callback) {
            try {
                var db = openDatabase('hx_history', '1.0', 'history', 10 * 1024 * 1024);
                db.transaction(function (tx) {
                    // tx.executeSql('SELECT * FROM (SELECT * FROM  historys H WHERE H.from_user = ? OR H.to_user = ? ORDER BY H.history_id DESC limit 0,10)AS H ORDER BY  H.history_id ',[name,name],function(tx,results){
                    tx.executeSql('SELECT * FROM (SELECT * FROM  historys H WHERE H.from_user = ? OR H.to_user = ? ORDER BY H.history_id DESC LIMIT 0,10)AS H ORDER BY  H.history_id ', [name, name], function (tx, results) {
                        var rows = results.rows;
                        var arr = [];
                        for (var i = 0; i < rows.length; i++) {
                            var obj = rows[i];
                            //console.log('obj:',obj);
                            //var history = new HistoryEntity(obj);
                            arr.push(obj);
                        }
                        for (var j = 0; j < arr.length; j++) {
                            console.log(arr[j]);
                            // alert(arr[0].from_user);
                        }
                        if (typeof callback === 'function') {

                            callback(arr);
                        }
                    });
                });
            } catch (e) {
                //alert(e);
                callback(null, e);
            }
        },
        loadHistorys: function (name, suser, callback) {
            try {
                var db = openDatabase('hx_history', '1.0', 'history', 10 * 1024 * 1024);
                db.transaction(function (tx) {
                    tx.executeSql('SELECT * FROM (SELECT * FROM  historys H WHERE ( H.from_user = ? AND H.to_user = ? ) OR (H.from_user = ? AND H.to_user = ?) ORDER BY H.history_id DESC LIMIT 0,10)AS H ORDER BY  H.history_id ',
                        [name, suser, suser, name], function (tx, results) {
                            var rows = results.rows;
                            var arr = [];
                            for (var i = 0; i < rows.length; i++) {
                                var obj = rows[i];
                                arr.push(obj);
                            }
                            console.log('用户历史记录',arr);
                            if (typeof callback === 'function') {
                                callback(arr);
                            }
                        });
                });
            } catch (e) {
                console.error(e);
            }
        },
        loadLastHistory: function (callback) {
            var db = openDatabase('hx_history', '1.0', 'history', 10 * 1024 * 1024);
            db.transaction(function (tx) {
                tx.executeSql('SELECT * FROM historys H GROUP BY H.from_user HAVING MAX(H.create_date)', [], function (tx, results) {
                    var rows = results.rows;
                    var arr = [];
                    for (var i = 0; i < rows.length; i++) {
                        var obj = rows[i];
                        arr.push(obj);
                    }
                    if (typeof callback === 'function') {
                        callback(arr);
                    }
                });
            });
        }
    };
    var indexed = {

        db: null,
        init: function() {
            var indexed_db = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
            if (indexed_db) {
                var dbRequest = indexed_db.open('test', 3);
                dbRequest.onupgradeneeded = function(evt){
                    indexed.db = dbRequest.result;
                    if (!indexed.db.objectStoreNames.contains('historys')) {
                        var objectStore = indexed.db.createObjectStore("historys", {
                            // primary key
                            keyPath: "history_id",
                            // auto increment
                            autoIncrement: true
                        });
                        objectStore.createIndex("userIndex", ["from_user", "to_user"], {unique: false});
                        //objectStore.createIndex("from_user_index","from_user",{unique:false});
                        //objectStore.createIndex("to_user_index","to_user",{unique:false});
                    }
                };
                dbRequest.onsuccess = function(event){
                    indexed.db = dbRequest.result;
                };
            }
        },
        saveHistory: function(entity) {
            var transaction = indexed.db.transaction(['historys'], 'readwrite');
            var objectStore = transaction.objectStore('historys');
            objectStore.add(entity);
        },
        load:function(name, suser,callback){
            var historys = [];
            var deferred = $.Deferred();
            deferred.done(_loadHistory(name,suser,function(his){
                console.log('his',his);
                historys = historys.concat(his);
            })).done(_loadHistory(suser,name,function(his){
                console.log('his',his);
                historys = historys.concat(his);
            }));
            setTimeout(function(){
                if(typeof callback==='function'){
                    historys = indexed.sort(historys);
                    console.log(historys);
                    callback(historys);
                }
            },200);
            //deferred.resolve(name,suser);
        },
        loadHistorys: function(name, suser, callback){
            var deferred = $.Deferred();
            deferred.done(indexed.load(name,suser,function(result){
                if(callback){
                    callback(result)
                }
            }));

        },
        loadHistory:function(name, suser,callback){
            var transaction = indexed.db.transaction(['historys'], 'readonly');
            var objectStore = transaction.objectStore('historys');
            var index = objectStore.index('userIndex');
            //var from_index = objectStore.index('from_user_index');
            //var to_index = objectStore.index('to_user_index');
            console.log('name and suer...........', name, suser);
            //var from_keyRange = IDBKeyRange.bound(suer,name,false,false);
            //var to_keyRange = IDBKeyRange.bound(suer,name,false,false);
            var historys =[]  ;
            var keyRange = IDBKeyRange.only([name, suser]);
            index.openCursor(keyRange, IDBCursor.PREV).onsuccess = function(evt){
                var cursor = evt.target.result;
                if (cursor) {
                    historys.push(cursor.value);
                    cursor.continue();
                }else{
                    if(callback){
                        callback(historys);
                    }
                }

            }

        },
        loadLastHistory: function() {

        },
        closedb: function(db) {
            db.close();
        },
        sort:function(historys){
            //var sortHistory = [];

            for(var i =0;i<historys.length;i++){
                var temp ;
                var tempHistory;
                if(!temp){
                    temp = new Date(historys[i].create_date);
                }
                for(var j = 0;j<historys.length;j++){
                    var current = new Date(historys[j].create_date);
                    if(current.getTime()>temp.getTime()){
                        tempHistory = historys[i];
                        historys[i] = historys[j];
                        historys[j] = tempHistory;
                    }
                }
            }
            if(historys.length>10)
                historys.length = 10;
            //console.log(historys);
            return historys;
        }
    };

    var _saveHistory;
    var _loadHistory;
    var _loadHistorys;
    var _loadLastHistory;

    (function () {
        try {
            var db = openDatabase('hx_history', '1.0', 'history', 10 * 1024 * 1024);
            //var indexedDB = window.indexedDB;
            var indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
            /*if (indexedDB) {
                console.log('used indexedDB............');
                webStorage.switchChange(webStorage.INDEX_DB);
            } else if (typeof db.transaction === 'function') {
                webStorage.switchChange(webStorage.WEB_SQL);
            } else {
                webStorage.switchChange(webStorage.LOCAL_STORAGE);
            }
             * */
             if(typeof db.transaction ==='function'){
             console.log('used indexedDB............');
             webStorage.switchChange(webStorage.WEB_SQL);
             }else if(indexedDB){
             webStorage.switchChange(webStorage.INDEX_DB)
             }else{
             webStorage.switchChange(webStorage.LOCAL_STORAGE);
             }

            var type = webStorage.getUsedType();
            //'localStorage','webSql','indexDb'
            switch (type) {
                case 'localStorage':

                    break;
                case 'webSql':
                    _init = WebSql.init;
                    _saveHistory = WebSql.saveHistory;
                    _loadHistory = WebSql.loadHistory;
                    _loadHistorys = WebSql.loadHistorys;
                    _loadLastHistory = WebSql.loadLastHistory;
                    break;
                case 'indexDb':
                    _init = indexed.init;
                    _saveHistory = indexed.saveHistory;
                    _loadLastHistory = indexed.loadLastHistory;
                    _loadHistory = indexed.loadHistory;
                    _loadHistorys = indexed.loadHistorys;
                    break;
            }
        } catch (e) {
            console.error(e);
        }
    })();
    var _entityFactory = function (message, type, model, fileObj) {
        try {
            var history = new HistoryEntity();
            switch (model) {
                case 'right':
                    history.from_user = message.from;
                    history.to_user = message.to;
                    if (message && fileObj && fileObj.filename !== '') {
                        history.file_name = fileObj.filename;
                        if(message.uri&&message.entities){
                            history.file_url = message.uri + '/' + message.entities[0].uuid;
                        }else{
                            history.file_url = message.url;
                        };
                    }
                    break;
                case 'left':
                    history.from_user = message.from;
                    history.to_user = message.to;
                    if (message.filename && message.filename !== '') {
                        history.file_name = message.filename;
                        history.file_url = message.url;
                    }
                    break;
            }
            if (type === 'face' && typeof message.data === 'object') {
                var arr = message.data;
                var msg = '';
                for (var i = 0; i < arr.length; i++) {
                    var face = emoji.mapping[arr[i].data];
                    console.log('face', face);
                    if (face && face !== '') {
                        msg += face;
                    } else {
                        msg += arr[i].data;
                    }
                }
                history.msg = msg;
            } else {
                history.msg = message.data || message.msg;
            }
            history.create_date = new Date().Format('yyyy-MM-dd hh:mm:ss');
            history.type = type;
            history.position = position;
            history.file_source = message.file_source;
            history.source=message.source||'webim';
            return history;

        } catch (e) {
            console.error(e);
        }
    };

    var _saveBuffer = function(buffer){
        var storage = window.localStorage;
        var bufferStr = JSON.stringify(buffer);
        storage.setItem('buffer',bufferStr);
    };
    var _loadBuffer = function(){
        var storage = window.localStorage;
        var bufferStr = storage.getItem('buffer');
        var buffer = {} ;
        if(bufferStr&&bufferStr.length>0){
             buffer = JSON.parse(bufferStr);
        }
        return buffer;
    };

    var _cleanBuffer = function(name){
        var storage = window.localStorage;
        if(name){
            var bufferStr = storage.getItem('buffer');
            console.log('bufferStr-----:',typeof bufferStr);
            var buffer = JSON.parse(bufferStr);
            if(buffer&&buffer[name]){
                delete buffer[name];
            }
            var isNull = true;

            for(var i in buffer){
                isNull = false;
            }
            if(isNull){
                storage.removeItem('buffer');
            }else{
                bufferStr = JSON.stringify(buffer);
                storage.setItem('buffer',bufferStr);
            }
        }else{
            storage.removeItem('buffer');
        }
    }


    return {
        init: _init,
        saveHistory: _saveHistory,
        entityFactory: _entityFactory,
        loadHistorys: _loadHistorys,
        loadLastHistory: _loadLastHistory,
        position: position,
        loadBuffer:_loadBuffer,
        saveBuffer:_saveBuffer,
        cleanBuffer:_cleanBuffer
    };

})();