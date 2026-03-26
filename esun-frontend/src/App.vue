<script setup>
import { ref, onMounted } from 'vue';
import api from './util/axios';

//---登入與註冊狀態---
const isLoginMode = ref(true);
const formData = ref({ userName: '', phone: '', password: '' });
const authMessage = ref('');
const isAuthError = ref(false);

//---社群動態牆狀態---
const isLoggedIn = ref(false); //判斷是否已登入
const currentUser = ref(null); //儲存當前登入的使用者資訊
const posts = ref([]); //儲存所有文章
const newPostContent = ref(''); //新發文的內容

//---網頁載入時自動檢查登入狀態---
onMounted(() => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    try {
      currentUser.value = JSON.parse(storedUser);
      isLoggedIn.value = true;
      fetchPosts();
    } catch (e) {
      localStorage.removeItem('user');
    }
  }
});
const createComment = async (post) => {
  if (!post.newComment || !post.newComment.trim()) return alert('留言不能為空！');
  
  const targetUserId = currentUser.value?.userId || currentUser.value?.user_id;
  
  if (!targetUserId) {
    alert('無法取得使用者 ID，請重新登入！');
    logout();
    return;
  }

  try {
    await api.post('/comments', {
      userId: targetUserId,
      postId: post.postId,
      content: post.newComment
    });
    
    post.newComment = ''; // 清空留言輸入框
    fetchPosts();         // 重新抓取文章以刷新留言列表
  } catch (error) {
    alert((error.response?.data || error.message));
  }
};

//---登入與註冊邏輯---
const handleAuth = async () => {
  if (!formData.value.phone || !formData.value.password) return alert('請填寫完整資訊');
  
  const url = isLoginMode.value ? '/users/login' : '/users/register';
  try {
    const res = await api.post(url, formData.value);
    
    if (isLoginMode.value) {
      //登入成功：後端會回傳完整的 user 物件
      currentUser.value = res.data || res; 
      isLoggedIn.value = true; //切換至動態牆畫面
      //把使用者資訊轉成字串存進瀏覽器
      localStorage.setItem('user', JSON.stringify(currentUser.value));
      fetchPosts(); // 抓取所有文章
    } else {
      //註冊成功
      authMessage.value = ' 註冊成功，請登入。';
      isAuthError.value = false;
      isLoginMode.value = true;
      formData.value.password = '';
    }
  } catch (error) {
    isAuthError.value = true;
    authMessage.value = ' 失敗：' + (error.response?.data || error.message);
  }
};

const logout = () => {
  isLoggedIn.value = false;
  currentUser.value = null;
  formData.value.password = '';
  authMessage.value = '已成功登出';
  isAuthError.value = false;
  //登出時清空瀏覽器暫存
  localStorage.removeItem('user');
};

// ---發文與動態牆邏輯---
const fetchPosts = async () => {
  try {
    const res = await api.get('/posts');
    // 檢查 res 是否為陣列。如果是 Axios 原生回應，則取 res.data
    const data = Array.isArray(res) ? res : (res.data && Array.isArray(res.data) ? res.data : null);
    
    if (data) {
      // 使用 [...data] 複製一份再反轉，避免直接更動原始狀態
      posts.value = [...data].reverse(); 
    } else {
      posts.value = [];
      console.warn("API 回傳的不是陣列格式:", res);
    }
  } catch (error) {
    console.error("無法取得文章:", error);
  }
};

const createPost = async () => {
  if (!newPostContent.value.trim()) return alert('文章內容不能為空！');
  
  // 準確抓取 userId
  const targetUserId = currentUser.value?.userId || currentUser.value?.user_id;
  
  if (!targetUserId) {
    alert('無法取得使用者 ID，請重新登入！');
    logout();
    return;
  }

  try {
    await api.post('/posts', {
      userId: targetUserId,
      content: newPostContent.value
    });
    
    newPostContent.value = ''; 
    fetchPosts(); 
  } catch (error) {
    alert((error.response?.data || error.message));
  }
};

//格式化時間的工具函數
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('zh-TW', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
};
</script>

<template>
  <div class="app-container">
    
    <div v-if="!isLoggedIn" class="auth-card">
      <h2 style="color: #00a497; text-align: center;">玉山社群平台</h2>
      <div class="tabs">
        <button :class="{ active: isLoginMode }" @click="isLoginMode = true">登入</button>
        <button :class="{ active: !isLoginMode }" @click="isLoginMode = false">註冊</button>
      </div>
      
      <div style="padding: 20px;">
        <input v-if="!isLoginMode" v-model="formData.userName" placeholder="姓名" class="input-field" />
        <input v-model="formData.phone" placeholder="手機號碼 (帳號)" class="input-field" />
        <input v-model="formData.password" type="password" placeholder="密碼" @keyup.enter="handleAuth" class="input-field" />
        <button @click="handleAuth" class="btn-primary">{{ isLoginMode ? '登入' : '註冊' }}</button>
        <p v-if="authMessage" :style="{ color: isAuthError ? 'red' : 'green', textAlign: 'center', marginTop: '10px' }">
          {{ authMessage }}
        </p>
      </div>
    </div>

    <div v-else class="wall-container">
      <header class="wall-header">
        <h2>歡迎回來，{{ currentUser?.userName }} </h2> 
        <button @click="logout" class="btn-outline">登出</button>
      </header>

      <div class="create-post-card">
        <textarea 
          v-model="newPostContent" 
          placeholder="今天有什麼新鮮事？" 
          rows="3"
        ></textarea>
        <div class="post-actions">
          <button @click="createPost" class="btn-primary">發佈貼文</button>
        </div>
      </div>

      <div class="post-list">
        <div v-if="posts.length === 0" class="empty-state">目前還沒有人發文喔，搶頭香！</div>
        
        <div v-for="post in posts" :key="post.postId" class="post-card">
          <div class="post-meta">
            <span class="author-name">{{ post.user?.userName || '未知使用者' }}</span>
            <span class="post-time">{{ formatDate(post.createdAt) }}</span>
          </div>

          <div class="post-content">{{ post.content }}</div>

          <div class="comments-section" v-if="post.comments && post.comments.length > 0">
            <div v-for="comment in post.comments" :key="comment.comment_id" class="comment-item">
              <span class="comment-author">{{ comment.user?.userName || '未知' }}: </span>
              <span class="comment-text">{{ comment.content }}</span>
            </div>
          </div>
          
          <div class="add-comment-section">
            <input 
              v-model="post.newComment" 
              type="text" 
              placeholder="留言..." 
              @keyup.enter="createComment(post)"
              class="comment-input"
            />
            <button @click="createComment(post)" class="btn-comment">送出</button>
          </div>
          </div>
        </div>
      </div>
    </div>
</template>

<style scoped>
/* 基礎設定 */
.app-container { min-height: 100vh; background-color: #f0f2f5; display: flex; justify-content: center; font-family: sans-serif; padding-top: 40px;}

/* 共用元件 */
.input-field { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 6px; box-sizing: border-box;}
.btn-primary { width: 100%; padding: 12px; background-color: #00a497; color: white; border: none; border-radius: 6px; font-size: 16px; cursor: pointer;}
.btn-primary:hover { background-color: #008a7d; }
.btn-outline { padding: 8px 16px; background: transparent; border: 1px solid #00a497; color: #00a497; border-radius: 6px; cursor: pointer; }

/* 登入卡片 */
.auth-card { width: 350px; background: white; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); align-self: flex-start; }
.tabs { display: flex; border-bottom: 1px solid #ddd; }
.tabs button { flex: 1; padding: 15px; background: none; border: none; font-size: 16px; cursor: pointer; }
.tabs button.active { color: #00a497; font-weight: bold; border-bottom: 2px solid #00a497; }

/* 動態牆區域 */
.wall-container { width: 600px; max-width: 100%; }
.wall-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.wall-header h2 { margin: 0; color: #333; }

/* 發文框 */
.create-post-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 20px; }
textarea { width: 100%; border: none; resize: none; font-size: 16px; outline: none; margin-bottom: 10px; box-sizing: border-box; }
.post-actions { display: flex; justify-content: flex-end; border-top: 1px solid #eee; padding-top: 10px; }
.post-actions button { width: auto; padding: 8px 20px; }

/* 留言區塊樣式 */
.comments-section {
  margin-top: 15px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}
.comment-item {
  font-size: 14px;
  margin-bottom: 8px;
  background-color: #f0f2f5;
  padding: 8px 12px;
  border-radius: 12px;
  display: inline-block;
  max-width: 100%;
  word-wrap: break-word;
}
.comment-author {
  font-weight: bold;
  color: #1c1e21;
}
.add-comment-section {
  display: flex;
  margin-top: 10px;
  gap: 10px;
}
.comment-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
}
.btn-comment {
  padding: 8px 15px;
  background-color: #00a497;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}
.btn-comment:hover {
  background-color: #008a7d;
}

/* 貼文卡片 */
.post-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 15px; }
.post-meta { display: flex; justify-content: space-between; margin-bottom: 12px; align-items: center; }
.author-name { font-weight: bold; color: #1c1e21; font-size: 16px; }
.post-time { font-size: 13px; color: #65676b; }
.post-content { color: #050505; font-size: 15px; line-height: 1.5; white-space: pre-wrap; /* 保留換行符號 */ }
.empty-state { text-align: center; color: #777; padding: 20px; }
</style>
