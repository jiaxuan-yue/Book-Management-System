<!--
  App.vue —— Vue 应用的根组件

  这是整个前端应用的最外层组件，在 main.js 中被挂载到 #app 元素上。
  使用 <RouterView/> 组件作为路由出口，根据当前 URL 路径渲染对应的页面组件。

  页面渲染逻辑由 router/index.js 中的路由配置决定：
  - /login     → Login.vue（登录页）
  - /register  → Register.vue（注册页）
  - /manager/* → Manager.vue（后台管理布局）+ 子页面
  - /front/*   → Front.vue（前台展示布局）+ 子页面
-->
<template>
  <RouterView/>
</template>