const CONST = require('@/const/index')

// 从数组删除一个元素
export function delElement(array, idxProp, propVal){
  let newArray = new Array();
  for(var i = 0; i < array.length; i++){
    if(propVal != array[i][idxProp]){
      newArray.push(array[i])
    }
  }
  array = newArray
  return newArray
}


export function getIfNotEmpty(obj, defaultVal){
  if( obj !== undefined && obj != null ){
    return obj
  }else{
    if(defaultVal !== undefined && defaultVal != null){
      return defaultVal
    }else{
      -1
    }
  }
}

/* eslint-disable indent */
export function parseTime(time, cFormat) {
    if (arguments.length === 0) {
        return null;
    }
    const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}';
    let date;
    if (typeof(time) == 'object') {
        date = time;
    } else {
        if (('' + time).length === 10) time = parseInt(time) * 1000;
        date = new Date(time);
    }
    const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    };
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key];
        if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1];
        if (result.length > 0 && value < 10) {
            value = '0' + value;
        }
        return value || 0;
    });
    return time_str;
}

/* 计算图片长宽 */

export function formatTime(time, option) {
    time = +time * 1000;
    const d = new Date(time);
    const now = Date.now();

    const diff = (now - d) / 1000;

    if (diff < 30) {
        return '刚刚'
    } else if (diff < 3600) { // less 1 hour
        return Math.ceil(diff / 60) + '分钟前'
    } else if (diff < 3600 * 24) {
        return Math.ceil(diff / 3600) + '小时前'
    } else if (diff < 3600 * 24 * 2) {
        return '1天前'
    }
    if (option) {
        return parseTime(time, option)
    } else {
        return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
    }
}

export function getQueryObject(url) {
    url = url == null ? window.location.href : url;
    const search = url.substring(url.lastIndexOf('?') + 1);
    const obj = {};
    const reg = /([^?&=]+)=([^?&=]*)/g;
    search.replace(reg, (rs, $1, $2) => {
        const name = decodeURIComponent($1);
        let val = decodeURIComponent($2);
        val = String(val);
        obj[name] = val;
        return rs;
    });
    return obj;
}


/**
 *get getByteLen
 * @param {Sting} val input value
 * @returns {number} output value
 */
export function getByteLen(val) {
    let len = 0;
    for (let i = 0; i < val.length; i++) {
        if (val[i].match(/[^\x00-\xff]/ig) != null) {
            len += 1;
        } else { len += 0.5; }
    }
    return Math.floor(len);
}

export function cleanArray(actual) {
    const newArray = [];
    for (let i = 0; i < actual.length; i++) {
        if (actual[i]) {
            newArray.push(actual[i]);
        }
    }
    return newArray;
}

export function param(json) {
    if (!json) return '';
    return cleanArray(Object.keys(json).map(key => {
        if (json[key] === undefined) return '';
        return encodeURIComponent(key) + '=' +
            encodeURIComponent(json[key]);
    })).join('&');
}

export function param2Obj(url) {
    const search = url.split('?')[1];
    if (!search) {
        return {}
    }
    return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
}

export function html2Text(val) {
    const div = document.createElement('div');
    div.innerHTML = val;
    return div.textContent || div.innerText;
}

export function objectMerge(target, source) {
    /* Merges two  objects,
     giving the last one precedence */

    if (typeof target !== 'object') {
        target = {};
    }
    if (Array.isArray(source)) {
        return source.slice();
    }
    for (const property in source) {
        if (source.hasOwnProperty(property)) {
            const sourceProperty = source[property];
            if (typeof sourceProperty === 'object') {
                target[property] = objectMerge(target[property], sourceProperty);
                continue;
            }
            target[property] = sourceProperty;
        }
    }
    return target;
}


export function scrollTo(element, to, duration) {
    if (duration <= 0) return;
    const difference = to - element.scrollTop;
    const perTick = difference / duration * 10;
    setTimeout(() => {
        console.log(new Date())
        element.scrollTop = element.scrollTop + perTick;
        if (element.scrollTop === to) return;
        scrollTo(element, to, duration - 10);
    }, 10);
}

export function toggleClass(element, className) {
    if (!element || !className) {
        return;
    }
    let classString = element.className;
    const nameIndex = classString.indexOf(className);
    if (nameIndex === -1) {
        classString += '' + className;
    } else {
        classString = classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length);
    }
    element.className = classString;
}

export const pickerOptions = [
    {
        text: '今天',
        onClick(picker) {
            const end = new Date();
            const start = new Date(new Date().toDateString());
            end.setTime(start.getTime());
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近一周',
        onClick(picker) {
            const end = new Date(new Date().toDateString());
            const start = new Date();
            start.setTime(end.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近一个月',
        onClick(picker) {
            const end = new Date(new Date().toDateString());
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近三个月',
        onClick(picker) {
            const end = new Date(new Date().toDateString());
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
        }
    }]

export function getTime(type) {
    if (type === 'start') {
        return new Date().getTime() - 3600 * 1000 * 24 * 90
    } else {
        return new Date(new Date().toDateString())
    }
}

export function debounce(func, wait, immediate) {
    let timeout, args, context, timestamp, result;

    const later = function () {
        // 据上一次触发时间间隔
        const last = +new Date() - timestamp;

        // 上次被包装函数被调用时间间隔last小于设定时间间隔wait
        if (last < wait && last > 0) {
            timeout = setTimeout(later, wait - last);
        } else {
            timeout = null;
            // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
            if (!immediate) {
                result = func.apply(context, args);
                if (!timeout) context = args = null;
            }
        }
    };

    return function (...args) {
        context = this;
        timestamp = +new Date();
        const callNow = immediate && !timeout;
        // 如果延时不存在，重新设定延时
        if (!timeout) timeout = setTimeout(later, wait);
        if (callNow) {
            result = func.apply(context, args);
            context = args = null;
        }

        return result;
    };
}

export function timestampToDate(t) {
    return new Date(parseInt(t)).toLocaleString().substr(0, 9);
}

export function cloneObject(source){
  return Object.assign({}, source);
}

export function deepClone(source) {
    if (!source && typeof source !== 'object') {
        throw new Error('error arguments', 'shallowClone');
    }
    const targetObj = source.constructor === Array ? [] : {};
    for (const keys in source) {
        if (source.hasOwnProperty(keys)) {
            if (source[keys] && typeof source[keys] === 'object') {
                targetObj[keys] = source[keys].constructor === Array ? [] : {};
                targetObj[keys] = deepClone(source[keys]);
            } else {
                targetObj[keys] = source[keys];
            }
        }
    }
    return targetObj;
}
//deepclone with date
export function deepCloneWithDate(source) {
    if (!source && typeof source !== 'object') {
        throw new Error('error arguments', 'shallowClone');
    }
    const targetObj = source.constructor === Array ? [] : {};
    for (const keys in source) {
        if (source.hasOwnProperty(keys)) {
            if (source[keys] && typeof source[keys] === 'object') {
                targetObj[keys] = source[keys].constructor === Array ? [] : {};
                if(source[keys].constructor === Date){
                    targetObj[keys] = new Date(source[keys]);
                    continue;
                }
                targetObj[keys] = deepClone(source[keys]);
            } else {
                targetObj[keys] = source[keys];
            }
        }
    }
    return targetObj;
}

export function strToJson(str) {
    if (str != '') {
        return eval('(' + str + ')');
    } else {
        return str
    }
}

/* Handle Enum */
export const customerStatusMap = [
    { status: "ACTIVE", label: "正常" },
    { status: "LOCKED", label: "锁定" },
    { status: "DISABLE", label: "禁用" },
    { status: "UNBOUND", label: "未绑定" }
]

export const genderMap = [
    { gender: "LADY", label: "女" },
    { gender: "LORD", label: "男" },
    { gender: "UNKNOWN", label: "未知" }
]

export const dividendStatusMap = [
    { status: "UNCONFIRMED", label: "未确认收益类型和收货地址" },
    { status: "READY", label: "待发货" },
    { status: "SENDING", label: "发货中" },
    { status: "SENT", label: "已发货" },
    { status: "RECEIVED", label: "已收货" }
]
export const refundStatusMap = [
    { status: "CREATED", label: "新建" },
    { status: "CONFIRMED", label: "待执行" },
    { status: "REJECTED", label: "已拒绝" },
    { status: "SUCCEED", label: "已退款" },
]
export const refundTypeMap = [
    { status: "FULL", label: "全额退款" },
    { status: "EARNEST", label: "只退定金" },
    { status: "BALANCE", label: "只退尾款" },
    { status: "CUSTOM", label: "自定金额" },
]
export const landownershipTplStatusMap = [
    { status: "ACTIVE", label: "上架" },
    { status: "INACTIVE", label: "下架" },
    { status: "DELETED", label: "已删除" },
]

export const platformMap = [
    { channel: "TAO_BAO", label: "淘宝众筹" },
    { channel: "KAI_SHI_BA", label: "开始吧众筹" },
    { channel: "YOU_ZAN", label: "有赞商城" },
    { channel: "PING_AN", label: "平安众筹" },
    { channel: "XIA_CHU_FANG", label: "下厨房" },
    { channel: "T_MALL", label: "天猫商城" },
    { channel: "DEFAULT", label: "庄游" }
]

export const orderStatusMap = [
    { status: "ACTIVE", label: "已签约" },
    { status: "INACTIVE", label: "未签约" },
    { status: "CANCELLED", label: "已取消" },
    { status: "REFUNDING", label: "退款中" },
    { status: "UNPAID", label: "未付款" },
    { status: "INVALID", label: "无效" }
]

export const projectStatusMap = [
    { status: "SETUP", label: "立项" },
    { status: "REVIEW", label: "审核" },
    { status: "OPEN", label: "开放" },
    { status: "CLOSED", label: "众筹结束" },
    { status: "DELETED", label: "已删除" },
]

export const planStatusMap = [
    { status: "UNCONFIRMED", label:"未确认收益"},
    { status: "CONFIRM", label:"确认收益"},
]
export const statusMap=[
    {code:'ACTIVE',name:'正常'},
    {code:'LOCKED',name:'锁定'},
    {code:'DISABLE',name:'禁用'},
    {code:'UNBOUND',name:'未绑定'}
]
/* 对象存储服务url */
export const ossPrefix = process.env.OSS_URL


// check if a object is empty
export function isNotEmpty(obj){
  let result = false;
  if (obj === undefined || obj === null){
    result = false;
  } else if ((typeof obj === 'string' || obj === 'String') && obj.trim() == ''){
    result = false;
  }else{
    result = true;
  }
  return result
}

// check if a object is not a empty array
export  function isNotEmptyArray(obj){
  if(isNotEmpty(obj)){
    //logObj((typeof obj),'isNotEmpty')
    //logObj(CONST.OBJECT_TYPE.OBJECT, '')
    if ((typeof obj) === CONST.OBJECT_TYPE.OBJECT && isNumber(obj.length)){
      //logObj(obj, 'isArray')
      if(obj.length > 0){
        //logObj(obj, 'isNumber')
        return true;
      }
    }
  }
  return false;
}

// check is a object is a number
export function isNumber(obj){
  if (typeof obj === CONST.OBJECT_TYPE.NUMBER && isFinite(obj)){
    return true;
  }
  return false
}

export function formatRestParams(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'shallowClone');
  }
  // const targetObj = source.constructor === Array ? [] : {};
  for (const keys in source) {
    if (source.hasOwnProperty(keys)) {
      if (source[keys] == undefined || source[keys] == null || source[keys] == '') {
        delete source[keys]
      }
    }
  }
  return source;
}
