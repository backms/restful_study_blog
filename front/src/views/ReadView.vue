<script setup lang="ts">

import {onMounted} from "vue";
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
  }
});

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const router = useRouter();

const moveToEdit = () => {
  router.push({name : "edit", params : { postId : props.postId }});
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((res) => {
    post.value = res.data;
  });
})

</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>

  <el-button type="warning" @click="moveToEdit()">수정</el-button>
</template>

<script>

</script>