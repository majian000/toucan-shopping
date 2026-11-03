/**
 * 零依赖轻提示 / 确认框（H5 不引 element-ui，用最少的 DOM 实现同等调用体验）
 */

let container = null;

function getContainer() {
  if (!container) {
    container = document.createElement('div');
    container.setAttribute('data-h5-toast', '');
    container.style.cssText = 'position:fixed;left:0;right:0;top:0;z-index:99999;pointer-events:none;';
    document.body.appendChild(container);
  }
  return container;
}

/**
 * 轻提示
 * @param {string} message 文案
 * @param {'info'|'success'|'warning'|'error'} [type]
 * @param {number} [duration] 毫秒，默认 2000
 */
export function toast(message, type = 'info', duration = 2000) {
  if (!message) return;
  const wrap = getContainer();
  const el = document.createElement('div');
  const bg = {
    info: 'rgba(0,0,0,0.78)',
    success: 'rgba(22,163,74,0.92)',
    warning: 'rgba(217,119,6,0.92)',
    error: 'rgba(220,38,38,0.92)'
  }[type] || 'rgba(0,0,0,0.78)';
  el.style.cssText = [
    'max-width:80vw', 'margin:12vh auto 0', 'padding:10px 14px', 'border-radius:8px',
    'font-size:14px', 'line-height:1.5', 'color:#fff', 'text-align:center',
    'word-break:break-all', 'background:' + bg, 'box-shadow:0 4px 16px rgba(0,0,0,0.18)',
    'transition:opacity .2s'
  ].join(';');
  el.textContent = message;
  wrap.appendChild(el);
  setTimeout(() => {
    el.style.opacity = '0';
    setTimeout(() => el.parentNode && el.parentNode.removeChild(el), 220);
  }, duration);
}

export const toastSuccess = m => toast(m, 'success');
export const toastError = m => toast(m, 'error');
export const toastWarning = m => toast(m, 'warning');

/**
 * 确认框（自绘，返回 Promise<boolean>）
 * @param {{title?:string, message?:string, okText?:string, cancelText?:string, danger?:boolean, modal?:boolean}} options
 */
export function confirm({ title = '提示', message = '', okText = '确定', cancelText = '取消', danger = false, modal = true } = {}) {
  return openDialog({ title, message, okText, cancelText, showCancel: true, danger, modal });
}

export function alertDialog({ title = '提示', message = '', okText = '我知道了' } = {}) {
  return openDialog({ title, message, okText, showCancel: false });
}

function openDialog({ title = '提示', message = '', okText = '确定', cancelText = '取消', showCancel = true, danger = false, modal = true }) {
  return new Promise(resolve => {
    const themeColor = danger ? '#dc2626' : '#0e5eaa';

    const mask = document.createElement('div');
    mask.setAttribute('data-h5-dialog', '');
    mask.style.cssText = [
      'position:fixed', 'left:0', 'right:0', 'top:0', 'bottom:0',
      modal ? 'background:rgba(0,0,0,0.45)' : 'background:transparent',
      'z-index:100000', 'display:flex', 'align-items:center', 'justify-content:center', 'padding:0 8vw'
    ].join(';');
    if (!modal) mask.style.pointerEvents = 'none';

    const card = document.createElement('div');
    card.style.cssText = [
      'width:100%', 'max-width:320px', 'background:#fff', 'border-radius:12px',
      'padding:20px 18px 12px', 'box-sizing:border-box',
      'box-shadow:0 8px 32px rgba(0,0,0,0.18)', 'animation:h5DialogIn .18s ease-out'
    ].join(';');
    if (!modal) card.style.pointerEvents = 'auto';

    const titleEl = document.createElement('div');
    titleEl.textContent = title || '';
    titleEl.style.cssText = 'font-size:16px;font-weight:600;color:#1d2129;text-align:center;margin-bottom:10px;';

    const msgEl = document.createElement('div');
    msgEl.textContent = message || '';
    msgEl.style.cssText = [
      'font-size:14px', 'line-height:1.6', 'color:#4e5969', 'text-align:center',
      'white-space:pre-line', 'word-break:break-word', 'max-height:50vh', 'overflow-y:auto'
    ].join(';');

    const btnRow = document.createElement('div');
    btnRow.style.cssText = 'display:flex;margin:18px -5px 0;';

    const mkBtn = (text, primary) => {
      const b = document.createElement('button');
      b.type = 'button';
      b.textContent = text;
      b.style.cssText = [
        'flex:1', 'height:42px', 'margin:0 5px', 'border-radius:8px', 'font-size:15px', 'cursor:pointer',
        primary ? 'border:none;background:' + themeColor + ';color:#fff;'
                : 'border:1px solid #e5e6eb;background:#fff;color:#4e5969;'
      ].join(';');
      return b;
    };

    let done = false;
    const close = val => {
      if (done) return;
      done = true;
      document.removeEventListener('keydown', onKey);
      if (mask.parentNode) mask.parentNode.removeChild(mask);
      resolve(val);
    };

    if (showCancel) {
      const cancelBtn = mkBtn(cancelText, false);
      cancelBtn.onclick = () => close(false);
      btnRow.appendChild(cancelBtn);
    }
    const okBtn = mkBtn(okText, true);
    okBtn.onclick = () => close(true);
    btnRow.appendChild(okBtn);

    mask.onclick = e => { if (e.target === mask) close(false); };
    const onKey = e => { if (e.key === 'Escape') close(false); };
    document.addEventListener('keydown', onKey);

    card.appendChild(titleEl);
    card.appendChild(msgEl);
    card.appendChild(btnRow);
    mask.appendChild(card);
    document.body.appendChild(mask);

    if (!document.getElementById('h5-dialog-style')) {
      const st = document.createElement('style');
      st.id = 'h5-dialog-style';
      st.textContent = '@keyframes h5DialogIn{from{opacity:0;transform:scale(.92)}to{opacity:1;transform:scale(1)}}';
      document.head.appendChild(st);
    }
    try { okBtn.focus(); } catch (e) { /* 忽略 */ }
  });
}

export default { toast, toastSuccess, toastError, toastWarning, confirm, alertDialog };
