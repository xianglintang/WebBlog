import { createRouter, createWebHistory } from 'vue-router';
import home from '../views/blog/home.vue';
import about from '../views/blog/about.vue';
import article_category from '../views/blog/article_category.vue';
import search from '../views/blog/search.vue';
import article from '../views/blog/article.vue';
import login from '../views/blog/login.vue';
import admin from '../views/blog/admin.vue';

const routes = [
    {
        path: '/',
        redirect: '/home',//要这样，不要/home/spring，这样的话，home是没有被点到的，但是home默认选中spring
    },
    {
        path: '/home',
        name: 'home',
        component: home,
    },
    {
        path: '/search',
        name: 'search',
        component: search,
    },
    {
        path: '/article',
        name: 'article',
        component: article,
    },
    {
        path: '/article_category',
        name: 'article_category',
        component: article_category,
    },
    {
        path: '/about',
        name: 'about',
        component: about,
    },
    {
        path: '/login',
        name: 'login',
        component: login,
    },
    {
        path: '/admin',
        name: 'admin',
        component: admin,
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes:routes,
});

// 添加全局前置守卫
router.beforeEach((to, from, next) => {
    // 检查即将进入的路由是否匹配任何已定义的路由
    if (!to.matched.length) {
        // 如果没有匹配到任何路由，则重定向到首页
        next({ path: '/home' });
    } else {
        const token = sessionStorage.getItem('token');
        const toPath = to.path;
        //admin页面是不存在按钮触发的，所以不能根据token就强制转回login，
        //只有手动或者登录要到admin，才触发
        if (toPath == '/login') {
            //判断是否已经登录过了
            if (token) {
                console.log(toPath);
                next({ path: '/admin' });
            } else {
                next()
            }
        } else {
            //只要不是admin，就不用拦截
            next();
        }
    }
});
export default router;
