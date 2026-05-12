/* ================================================================
   JavaBot – Web frontend  (communicates with Java backend via REST)
   ================================================================ */

const API = '/api/chat';

const messagesEl = document.getElementById('messages');
const inputEl    = document.getElementById('userInput');
const sendBtnEl  = document.getElementById('sendBtn');

let busy = false;  // prevents double-sends while waiting for reply

/* ── Initialise ────────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {

  // Detect file:// — page must be served through the Java server
  if (location.protocol === 'file:') {
    appendBot(
      'This page was opened as a local file.\n\n' +
      'The chat API will not work that way.\n\n' +
      'Please open your browser and navigate to:\n' +
      '  http://localhost:8080\n\n' +
      'Make sure the JavaBot application is running first\n' +
      '(double-click run.bat or run: java -cp out com.chatbot.Main)'
    );
    if (sendBtnEl) sendBtnEl.disabled = true;
    return;
  }

  appendBot(
    "Hello! I'm JavaBot, your AI-powered assistant built with Java NLP.\n\n" +
    "I use TF-IDF vectorization and cosine similarity to understand natural\n" +
    "language, so you can talk to me conversationally.\n\n" +
    "Topics I cover:\n" +
    "  AI & Machine Learning · Programming · Science & Space\n" +
    "  Movies · Music · Sports · Food · Technology\n" +
    "  Math (just type an expression like '25*4+10')\n" +
    "  Current time & date · Jokes & more!\n\n" +
    "Use the Quick Topics in the sidebar or simply start typing."
  );
  inputEl.focus();
});

/* ── Send flow ─────────────────────────────────────────────────── */
async function sendMessage() {
  if (busy) return;
  const text = inputEl.value.trim();
  if (!text) return;

  inputEl.value = '';
  inputEl.style.height = 'auto';

  appendUser(text);
  setBusy(true);
  const typingRow = showTyping();

  try {
    const res = await fetch(API, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify({ message: text })
    });

    // Server always returns JSON (even on errors), so parse unconditionally
    const data = await res.json();
    removeTyping(typingRow);
    appendBot(data.response || data.error || 'No response from server.');

  } catch (err) {
    removeTyping(typingRow);

    // Distinguish connection refused from other failures
    const isConnRefused = err instanceof TypeError &&
      (err.message.includes('fetch') || err.message.includes('network') ||
       err.message.includes('Failed'));

    if (isConnRefused) {
      appendBot(
        'Cannot connect to the JavaBot server.\n\n' +
        'The server runs as part of the Java application.\n' +
        'Make sure you started it with run.bat (or java -cp out com.chatbot.Main)\n' +
        'and that nothing else is using port 8080.\n\n' +
        'Then open:  http://localhost:8080'
      );
    } else {
      appendBot('Error: ' + err.message + '\n\nTry refreshing the page.');
    }
  } finally {
    setBusy(false);
    inputEl.focus();
  }
}

/* Fire a canned question from the sidebar */
function quickAsk(text) {
  if (busy) return;
  closeSidebar();
  inputEl.value = text;
  autoResize(inputEl);
  sendMessage();
}

/* ── Message rendering ─────────────────────────────────────────── */
function appendUser(text) {
  messagesEl.appendChild(createRow(text, 'user'));
  scrollBottom();
}

function appendBot(text) {
  messagesEl.appendChild(createRow(text, 'bot'));
  scrollBottom();
}

function createRow(text, who) {
  const row = document.createElement('div');
  row.className = 'message ' + who;
  const safeText = escHtml(text);
  const time = now();

  if (who === 'bot') {
    row.innerHTML =
      '<div class="msg-avatar">AI</div>' +
      '<div class="msg-body">' +
        '<div class="bubble">' + safeText + '</div>' +
        '<div class="msg-time">' + time + '</div>' +
      '</div>';
  } else {
    row.innerHTML =
      '<div class="msg-body">' +
        '<div class="bubble">' + safeText + '</div>' +
        '<div class="msg-time">' + time + '</div>' +
      '</div>';
  }
  return row;
}

/* ── Typing indicator ──────────────────────────────────────────── */
function showTyping() {
  const row = document.createElement('div');
  row.className = 'message bot typing';
  row.innerHTML =
    '<div class="msg-avatar">AI</div>' +
    '<div class="msg-body">' +
      '<div class="bubble">' +
        '<span class="t-dot"></span>' +
        '<span class="t-dot"></span>' +
        '<span class="t-dot"></span>' +
      '</div>' +
    '</div>';
  messagesEl.appendChild(row);
  scrollBottom();
  return row;
}

function removeTyping(el) {
  if (el && el.parentNode) el.parentNode.removeChild(el);
}

/* ── Clear chat ────────────────────────────────────────────────── */
function clearChat() {
  if (busy) return;
  if (!confirm('Clear all messages?')) return;
  messagesEl.innerHTML = '';
  appendBot('Chat cleared! How can I help you today?');
}

/* ── Keyboard / input helpers ─────────────────────────────────── */
function handleKey(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
}

function autoResize(el) {
  el.style.height = 'auto';
  el.style.height = Math.min(el.scrollHeight, 130) + 'px';
}

/* ── Sidebar toggle (mobile) ───────────────────────────────────── */
function toggleSidebar() {
  document.getElementById('sidebar').classList.toggle('open');
  document.getElementById('overlay').classList.toggle('show');
}

function closeSidebar() {
  document.getElementById('sidebar').classList.remove('open');
  document.getElementById('overlay').classList.remove('show');
}

/* ── Utilities ─────────────────────────────────────────────────── */
function setBusy(state) {
  busy = state;
  if (sendBtnEl) sendBtnEl.disabled = state;
}

function scrollBottom() {
  requestAnimationFrame(() => { messagesEl.scrollTop = messagesEl.scrollHeight; });
}

function now() {
  return new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function escHtml(str) {
  return str
    .replace(/&/g,  '&amp;')
    .replace(/</g,  '&lt;')
    .replace(/>/g,  '&gt;')
    .replace(/"/g,  '&quot;')
    .replace(/\n/g, '<br>');
}
