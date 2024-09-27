<script setup lang="ts">
import {ref} from "vue";

import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
  }
});

axios.get(`/api/posts/${props.postId}`).then((res) => {
  post.value = res.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then((res) => {
    router.replace({name: "home"});
  });
}

</script>

<template>
  <div>
    <el-input v-model="post.title" type="text" placeholder="제목을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">글 작성완료</el-button>
  </div>
</template>

<style>

</style>