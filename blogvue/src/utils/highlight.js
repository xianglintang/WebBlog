import hljs from 'highlight.js';
import 'highlight.js/styles/github.css'; // 可以选择任何喜欢的样式

function addCopyButton(block) {
  const button = document.createElement('button');
  button.innerText = '复制';
  button.className = 'copy-button';
  button.style.position = 'absolute';
  button.style.top = '8px';
  button.style.right = '8px';
  button.style.background = '#fff';
  button.style.border = '1px solid #ddd';
  button.style.padding = '2px 8px';
  button.style.cursor = 'pointer';
  button.style.display = 'none';

  button.addEventListener('click', () => {
    const code = block.innerText;
    navigator.clipboard.writeText(code).then(() => {
      button.innerText = '已复制';
      setTimeout(() => {
        button.innerText = '复制';
      }, 2000);
    });
  });

  const wrapper = document.createElement('div');
  wrapper.style.position = 'relative';
  wrapper.addEventListener('mouseenter', () => {
    button.style.display = 'block';
  });
  wrapper.addEventListener('mouseleave', () => {
    button.style.display = 'none';
  });
  wrapper.appendChild(block.cloneNode(true));
  wrapper.appendChild(button);

  block.replaceWith(wrapper);
}

export default {
  mounted(el) {
    const blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
      hljs.highlightElement(block);
      addCopyButton(block);
    });
  },
  updated(el) {
    const blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
      hljs.highlightElement(block);
      addCopyButton(block);
    });
  }
};
