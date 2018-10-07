// import { logObj } from '@/utils/logger'

export function hasPermission(p,...args){
  const privileges = this.$store.getters.privileges
  let r = privileges.indexOf(p) >= 0
  args.forEach(a => { r = r && privileges.indexOf(a) >= 0})
  return r
}

export function isAdmin(){
  const roles = this.$store.getters.roles
  return roles.indexOf('ADMIN') >= 0
}

export function hasPermissionRole(...permissionRoles){
  const userRole = this.$store.getters.roles;
  // logObj(userRole, 'userRole')
  let r = false;
  permissionRoles.forEach(a => { r = r && (userRole == a)})
  return r
}

export var ROLES = {
  BUTLER: 'BUTLER',
  CONSIGNOR: 'CONSIGNOR',
  ADMIN: 'ADMIN',
  GOODS_MANAGER: 'GOODS_MANAGER',
  PROJECT_MANAGER: 'PROJECT_MANAGER',
  TEMP: 'TEMP',
  ALICE: 'ALICE',
  FINANCE: 'FINANCE',
}
