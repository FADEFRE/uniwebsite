<!--
readonly comment
displays:
- comment received by props
functionality:
- readonly
-->

<script setup>
const props = defineProps(['applicantComment'])
</script>

<template>
  <div>
    <textarea readonly class="module-comment">{{ props.applicantComment }}</textarea>
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
</style>