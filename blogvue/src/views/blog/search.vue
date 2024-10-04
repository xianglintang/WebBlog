<template>
  <div v-infinite-scroll="load" style="overflow: auto">
    <div v-for="article in articleList" :key="article.id" class="infinite-list-item">
      <el-card class="search_card"  shadow="hover" @click="ReadArticle(article.articleId)" >
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
  </div>
</template>

<script setup>
import {Search} from '@/api/article.js'
import { ref, watch,reactive, onMounted } from 'vue'
import { useRoute,useRouter } from 'vue-router'

const route = useRoute()//需要参数
const router = useRouter()//需要路由，重新刷新
const queryForm=reactive({title:''});
//
const count = ref(0)
const load = () => {
  count.value += 2
}

// 定义响应式数据
const articleList = ref([]);
const SearchValue = ref('');//默认''，非null，其实是title为搜索根据

// 检测搜索值是否变化,，变化重新搜索
watch(() => route.query.SearchValue, (NewSearchValue) => {
  // 检测搜索值是否变化,，变化则执行下去
  SearchValue.value = NewSearchValue || '';
  //不用再修改，因为''后端也会判断，只能拿来拼接，然后请求
  queryForm.title=SearchValue.value;
  try{
      Search(queryForm).then(res=>{
        console.log(res);
      articleList.value=res.data;
      console.log(...articleList.value)
    })
  }
  //console.log("res.data.data:"+articleList.value.map(1))
  catch(error){
    console.log(error)
  }

},{ immediate: true });


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
.title{
  display: flex;
  margin-top: 10px;
  margin-bottom: -10px;
}
.introduce{

}
.search_card{
  margin: auto;
  margin-top: 5px;
  width: 800px;
  height: 170px;
  background-color: rgb(198, 237, 225);
}
.search_card:hover{
  transform: translateY(-5px);
}
</style>