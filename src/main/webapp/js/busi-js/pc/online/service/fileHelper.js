CommonUtils.regNamespace('fileHelper');
fileHelper = (function () {
    "use strict";
    var FILE_STORAGE;
    var SERVICE_STORAGE;
    var LOCAL_STORAGE = (function(){
        var LOCAL_SIZE = 1024 * 1024 * 100;
        var FILE_PATH = 'wxFile';
        var requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;
        var _saveFlag = false;
        var _fs;
        var errorHandler = function (err) {
            var msg = 'An error occured: ';
            console.log(err);
            switch (err.name) {
                case 'NotFoundError':
                    msg += 'File or directory not found';
                    break;

                case FileError.NOT_READABLE_ERR:
                    msg += 'File or directory not readable';
                    break;

                case FileError.PATH_EXISTS_ERR:
                    msg += 'File or directory already exists';
                    break;

                case FileError.TYPE_MISMATCH_ERR:
                    msg += 'Invalid filetype';
                    break;

                default:
                    msg += 'Unknown Error';
                    break;
            };
            return msg;
        };
        var _initFs = function (fs) {
            _fs = fs;
            fs.root.getDirectory(FILE_PATH, {create: true}, function (dirEntry) {

            }, errorHandler);
        };
        if (requestFileSystem) {
            _saveFlag = true;
            requestFileSystem(window.TEMPORARY, LOCAL_SIZE, _initFs, errorHandler);
        }
        var _saveFile = function (file,callback) {
            _fs.root.getFile(FILE_PATH+'/'+file.name, {create: true}, function (fileEntry) {
                fileEntry.createWriter(function (fileWriter) {
                    fileWriter.write(file);
                    if(typeof callback ==='function'){
                        callback(file.name);
                    }
                }, errorHandler);
            }, errorHandler);
        };
        var _loadFile = function (fileName, callback) {
            _fs.root.getFile(FILE_PATH+'/'+fileName, {create: false}, function (fileEntry) {
                var src = fileEntry.toURL();
                if (typeof callback === 'function') {
                    callback(src);
                }
            }, errorHandler);
        };

        return {
            saveFile:_saveFile,
            loadFile:_loadFile
        }
    })();

    var TEMP_STORAGE = {
        _getFileUrl : function(){

        }
    }
    var STORAGE_TYPE;
    var  fileSystem = window.requestFileSystem || window.webkitRequestFileSystem;
    if(fileSystem){
        FILE_STORAGE = LOCAL_STORAGE;
        STORAGE_TYPE = 'LOCAL';
    }else if(SERVICE_STORAGE ){
        FILE_STORAGE = SERVICE_STORAGE;
        STORAGE_TYPE = 'SERVICE';
    }else{
        FILE_STORAGE = TEMP_STORAGE;
        STORAGE_TYPE = 'TEMP';
    }

    return {
        fileStorage:FILE_STORAGE,
        storageType:STORAGE_TYPE
    };
}());
