export function logObj (obj, title, spaces) {
  console.log('obj[' + title + '] - >' + JSON.stringify(obj, null, spaces ? spaces: 0))
}

export function logInfo(message){
  console.log('[message] - >' + message)
}

export function alertObj(obj, title, spaces) {
  alert('obj[' + title + '] - >' + JSON.stringify(obj,null, spaces ? spaces : 0))
}
