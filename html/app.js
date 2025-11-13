(function(){
  const display = document.getElementById('display');
  const keys = document.querySelector('.keys');
  let expr = '';
  // conversion targets
  const convBin = document.getElementById('conv-bin');
  const convOct = document.getElementById('conv-oct');
  const convDec = document.getElementById('conv-dec');
  const convHex = document.getElementById('conv-hex');

  function updateDisplay(txt){
    display.textContent = txt || '0';
    // update conversions when display contains a parsable number
    updateConversionsFromString(String(txt || ''));
  }

  function toDigit(n){
    return n < 10 ? String(n) : String.fromCharCode(55 + n); // 10->A
  }

  function formatNumberInBase(value, base, maxFracDigits = 12){
    if(!Number.isFinite(value)) return '—';
    const sign = value < 0 ? '-' : '';
    const abs = Math.abs(value);
    const intPart = Math.floor(abs);
    let intStr = intPart.toString(base).toUpperCase();
    // convert fractional part if needed
    const frac = abs - intPart;
    if(frac === 0) return sign + intStr;
    // fraction conversion
    let fracPart = frac;
    let fracStr = '';
    let i = 0;
    while(fracPart > 0 && i < maxFracDigits){
      fracPart *= base;
      const digit = Math.floor(fracPart);
      fracStr += toDigit(digit);
      fracPart -= digit;
      i++;
    }
    return sign + intStr + '.' + (fracStr || '0');
  }

  function updateConversionsFromString(txt){
    // try to parse plain numeric strings produced by evaluation or typing
    // disallow expressions containing operator characters
    if(!/^[\s+-]?\d*(?:\.\d+)?(?:e[+-]?\d+)?$/i.test(txt.trim())){
      // not a simple numeric literal => clear conversions
      convBin.textContent = '—';
      convOct.textContent = '—';
      convDec.textContent = '—';
      convHex.textContent = '—';
      return;
    }
    const n = Number(txt);
    if(!Number.isFinite(n)){
      convBin.textContent = '—';
      convOct.textContent = '—';
      convDec.textContent = '—';
      convHex.textContent = '—';
      return;
    }
    convBin.textContent = formatNumberInBase(n, 2);
    convOct.textContent = formatNumberInBase(n, 8);
    convDec.textContent = String(n);
    convHex.textContent = formatNumberInBase(n, 16);
  }

  function append(value){
    expr += value;
    updateDisplay(expr);
  }

  function clearAll(){ expr = ''; updateDisplay('0'); }
  function backspace(){ expr = expr.slice(0,-1); updateDisplay(expr || '0'); }

  function safeEvaluate(input){
    // allow only digits, whitespace, operators, parentheses and dot
    if(!/^[0-9+\-*/().\s]+$/.test(input)){
      throw new Error('Неподдерживаемые символы');
    }
    // prevent sequences like "++" starting expressions is ok but keep it simple
    // Use Function to evaluate; wrapped in try/catch
    // Replace Unicode operators if present
    const normalized = input.replace(/×/g, '*').replace(/÷/g, '/').replace(/−/g,'-');
    // Avoid accidental leading zeros issues - let JS handle numbers
    // Evaluate
    /* eslint-disable no-new-func */
    return Function('return (' + normalized + ')')();
  }

  keys.addEventListener('click', (e)=>{
    const btn = e.target.closest('button');
    if(!btn) return;
    const v = btn.dataset.value;
    const action = btn.dataset.action;
    if(action === 'clear') return clearAll();
    if(action === 'back') return backspace();
    if(action === 'equals'){
      try{
        const res = safeEvaluate(expr || '0');
        expr = String(res);
        updateDisplay(expr);
      }catch(err){
        updateDisplay('Ошибка');
        expr = '';
      }
      return;
    }
    if(v) append(v);
  });

  // Keyboard support
  window.addEventListener('keydown', (e)=>{
    if(e.key === 'Escape') return clearAll();
    if(e.key === 'Backspace') return backspace();
    if(e.key === 'Enter' || e.key === '='){
      e.preventDefault();
      try{
        const res = safeEvaluate(expr || '0');
        expr = String(res);
        updateDisplay(expr);
      }catch{
        updateDisplay('Ошибка');
        expr = '';
      }
      return;
    }
    // allowed keys: digits, operators, parentheses, dot, space
    if(/^[0-9+\-*/().]$/.test(e.key)){
      append(e.key);
    }
  });

  // Initialize
  clearAll();
})();