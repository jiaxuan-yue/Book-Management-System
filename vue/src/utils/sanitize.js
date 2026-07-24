import DOMPurify from 'dompurify'

/** 前端二次消毒，配合后端 HtmlSanitizer 防 XSS */
export function sanitizeHtml(html) {
  if (!html) {
    return ''
  }
  return DOMPurify.sanitize(html, {
    USE_PROFILES: { html: true },
    FORBID_TAGS: ['script', 'iframe', 'object', 'embed', 'form'],
    FORBID_ATTR: ['onerror', 'onload', 'onclick', 'onmouseover', 'onfocus', 'onmouseenter']
  })
}
