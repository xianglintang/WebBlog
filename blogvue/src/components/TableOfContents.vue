<template>
  <div class="toc" :style="{ height: tocHeight + 'px' }">
    <h3>目录</h3>
    <ul>
      <li
        v-for="item in headings"
        :key="item.id"
        :class="{ active: activeHeading === item.id }"
      >
        <a href="#" @click.prevent="scrollToHeading(item.id)">{{ item.text }}</a>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted } from 'vue';

const props = defineProps({
  headings: {
    type: Array,
    required: true,
  },
  scrollToHeading: {
    type: Function,
    required: true,
  },
});

const activeHeading = ref('');
const tocHeight = ref(0);

// 监听滚动事件，更新高亮
const updateActiveHeading = () => {
  const headings = document.querySelectorAll('h1, h2, h3');
  let found = false;

  headings.forEach((heading) => {
    const rect = heading.getBoundingClientRect();
    if (rect.top >= 0 && rect.top < window.innerHeight && !found) {
      activeHeading.value = heading.id; // 更新当前高亮的标题
      found = true;
    }
  });
};

onMounted(() => {
  window.addEventListener('scroll', updateActiveHeading);
  tocHeight.value = window.innerHeight - 100; // 根据需要调整
});
</script>

<style scoped>
.toc {
  position: fixed; /* 固定位置 */
  left: 20px; /* 离左边的距离 */
  top: 100px; /* 距离顶部 */
  max-width: 200px; /* 最大宽度 */
  background-color: white; /* 背景颜色 */
  border: 1px solid #ccc; /* 边框 */
  padding: 10px; /* 内边距 */
  z-index: 100; /* 确保在其他元素之上 */
  overflow-y: auto; /* 使左侧目录可滚动 */
  height: calc(100vh - 100px); /* 使左侧目录高度为视口高度减去顶部空间 */
}
.toc h3 {
  margin: 0;
}
.toc ul {
  list-style-type: none; /* 无序列表样式 */
  padding: 0;
}
.toc li {
  margin: 5px 0; /* 每个目录项的间隔 */
}
.toc li.active {
  font-weight: bold; /* 高亮样式 */
  color: blue; /* 高亮颜色 */
}
</style>
