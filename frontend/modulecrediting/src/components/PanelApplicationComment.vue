<!--
comment input
displays:
- comment text input
functionality:
- comment input
-->

<script setup>
import { ref } from 'vue';

const applicantComment = ref()

defineExpose({
  applicantComment
})
</script>

<template>
  <div>
    <textarea class="module-comment" v-model="applicantComment"/>
  </div>
</template>

<style scoped>
.module-comment {
  padding: 5px;
  margin: 5px;

  width: 100%;
  height: 100%;
  font-size: 16px;
  color: #333333;

  box-shadow: 0 0 5px rgba(0, 128, 255, 0);
  border: 1px solid #aaaaaa;
  border-radius: 5px;
  transition: border-color 0.2s;
}

.module-comment:hover, .module-comment:focus {
  border-color: black;
}
</style>