/**
 * RSA 加密密码（避免明文传输）+ 读取服务端公钥
 */
import request from '@/utils/request'
import JSEncrypt from 'jsencrypt'

let cachedPublicKey = ''

export async function getPublicKey() {
  if (cachedPublicKey) {
    return cachedPublicKey
  }
  const res = await request.get('/publicKey')
  if (res.code === '200' && res.data) {
    cachedPublicKey = res.data
    return cachedPublicKey
  }
  throw new Error(res.msg || '获取公钥失败')
}

export async function encryptPassword(plain) {
  if (!plain) {
    return plain
  }
  const pem = await getPublicKey()
  const enc = new JSEncrypt()
  enc.setPublicKey(pem)
  const cipher = enc.encrypt(plain)
  if (!cipher) {
    throw new Error('密码加密失败')
  }
  return cipher
}

/** 给 el-upload / wangEditor 带上 JWT */
export function authHeaders() {
  const user = JSON.parse(localStorage.getItem('system-user') || localStorage.getItem('xm-user') || '{}')
  return user.token ? { Authorization: 'Bearer ' + user.token } : {}
}
