<template>
  <div>
    <div v-html="CurrentArticle" class="markdown-body" style="font-size: small" v-highlight></div>
  </div>
</template>


<script setup>

import {ref, reactive, watch, onMounted} from 'vue'
import {SearchById} from '@/api/article.js'
import { useRoute,useRouter } from 'vue-router'
//md导入，由于我们后端已经完成了md转html，所以前端不用转，现在是搞样式和代码高亮
import 'github-markdown-css'

const route = useRoute();//需要参数

const CurrentArticle = ref(null);
const articleId = reactive({articleId:''});

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
      CurrentArticle.value=res.data;//传来后端解析md文件转为html数据
      console.log(CurrentArticle.value)
    })
  }
  catch(error){
    console.log(error)
  }

},{ immediate: true });

</script>

<style scoped>
.markdown-body {
  box-sizing: border-box;
  min-width: 200px;
  max-width: 980px;
  margin: 0 auto;
  padding: 45px;
}

@media (max-width: 767px) {
  .markdown-body {
    padding: 15px;
  }
}
</style>