<template>
  <el-tabs v-model="activeName" stretch>
    <el-tab-pane
        v-for="(tab, index) in tabsConfig"
        :key="index"
        :label="tab.label"
        :name="tab.name"
        >
        <!---->
        <div v-for="article in articleList" :key="article.id">
          <el-card class="category_card" shadow="hover" @click="ReadArticle(article.articleId)" >
            <el-tag  type="success" effect="dark" size="large" >{{article.categoryName}}</el-tag>
            <el-text class="title" type="primary" size="large" truncated>
              {{article.title}}
            </el-text>
            <el-divider style="border-width: 1px;border-color: black "></el-divider>
            <el-text class="introduce" truncated>
              {{article.introduce}}
            </el-text>
          </el-card>
        </div>
    </el-tab-pane>
  </el-tabs>

</template>

<script setup>
import {SearchByCategory_Id} from '@/api/article.js'
import { ref, watch,reactive, onMounted } from 'vue'
import { useRoute,useRouter } from 'vue-router'
const route = useRoute()//需要参数
const router = useRouter()//需要路由，重新刷新

const articleList = ref([]);
const activeName = ref('1');
const tabsConfig = [
  { label: "Spring",name:"1" },
  { label: "Unity",name:"2" },
  { label: "C",name:"3"},
  { label: "CSharp",name:"4"},
  { label: "Python",name:"5"},
  { label: "Other",name:"6"}
];


onMounted(() => {
  // 首次进入页面时加载默认分类下的文章
  loadArticlesForTab(activeName.value);
});

const loadArticlesForTab = async (id) => {
  try {
    const response = await SearchByCategory_Id(id);
    console.log(response);
    articleList.value = response.data;
    console.log(...articleList.value)
    
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

// 更新watcher以使用新定义的异步加载函数
watch(activeName, async (newactiveName) => {
  await loadArticlesForTab(newactiveName);
});


const ReadArticle=(articleId)=>{
  console.log(articleId)
  // 在任何地方，你可以使用emitter.emit发布事件
  router.push({
    name:'article',
    query:{articleId:articleId}
  });
}

</script>

<style scoped>

:deep(.el-tabs__nav-scroll) {
  display: flex;
  justify-content: center;
  text-align: center;
}
.title{
  display: flex;
  margin-top: 10px;
  margin-bottom: -10px;
}
.introduce{

}
.category_card{
  margin: auto;
  margin-top: 5px;
  width: 800px;
  height: 170px;
  background-color: rgb(198, 237, 225);
}
.category_card:hover{
  transform: translateY(-5px);
}
</style>