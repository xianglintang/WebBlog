<template>
      <button @click="editArticle" class="edit-button">Edit</button>
  <div  class="article-container">
    <!-- 大纲部分 -->
    <TableOfContents class="Table-body" :headings="headings" :scrollToHeading="scrollToHeading"/>

    <div 
      v-html="CurrentArticle" 
      class="markdown-body" 
      style="font-size: small" 
      v-highlight
    ></div>
  </div>
</template>


<script setup>

import {ref, reactive, watch, onMounted} from 'vue';
import {SearchById} from '@/api/article.js';
import { useRoute,useRouter } from 'vue-router';
import router from "@/router/index.js";
//md导入，由于我们后端已经完成了md转html，所以前端不用转，现在是搞样式和代码高亮
import 'github-markdown-css'
import TableOfContents from '@/components/TableOfContents.vue'; // 导入目录组件

const route = useRoute();//需要参数

const CurrentArticle = ref(null);
const articleId = reactive({articleId:''});
const headings = ref([]);

//文章id是否变化,，变化重新根据id搜索
watch(() => route.query.articleId, (NewarticleId) => {
  // 检测搜索值是否变化,，变化则执行下去
  console.log("NewarticleId:"+NewarticleId);
  articleId.articleId = NewarticleId;
  //不用再修改，因为''后端也会判断，只能拿来拼接，然后请求
  console.log(articleId);
  console.log(articleId.articleId);
  try{
      //id查找
      SearchById(articleId.articleId).then(res=>{
      //CurrentArticle.value=res.data;//传来后端解析md文件转为html数据
      //console.log(CurrentArticle.value)
      processContent(res.data);
    })
  }
  catch(error){
    console.log(error)
  }

},{ immediate: true });

function processContent(content) {
  const parser = new DOMParser();
  const doc = parser.parseFromString(content, 'text/html');
  const hTags = doc.querySelectorAll('h1, h2, h3'); // 选择需要的标题标签

  // 为每个标题生成唯一 ID
  headings.value = Array.from(hTags).map((h, index) => {
    const id = `heading-${index}`; // 生成唯一 ID
    h.id = id; // 设置标题 ID
    return { id, text: h.textContent };
  });

  CurrentArticle.value = doc.body.innerHTML; // 将处理后的 HTML 设置为内容
}
// 用于传递给大纲组件的滚动函数
const scrollToHeading = (id) => {
  const headingElement = document.getElementById(id);
  if (headingElement) {
    const offset = 100; // 根据需要调整偏移量
    const elementPosition = headingElement.getBoundingClientRect().top;
    const offsetPosition = elementPosition + window.pageYOffset - offset;
    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth',
    });
  }
};

// 编辑文章函数
const editArticle = () => {
  // 跳转到编辑页面，传递文章 ID
  router.push({ name: 'articleEditor', query: { articleId: articleId.articleId } });
};
</script>

<style scoped>
.article-container {
  display: flex; /* 使用 flexbox 布局 */
}
.markdown-body {
  margin-left: 220px; 
}
/*.Table-body{
  margin:0auto;
}*/
/*
.markdown-body {
  box-sizing: border-box;
  
  min-width: 200px;
  max-width: 980px;
  padding: 45px;
}*/

@media (max-width: 767px) {
  .markdown-body {
    padding: 15px;
  }
}
</style>