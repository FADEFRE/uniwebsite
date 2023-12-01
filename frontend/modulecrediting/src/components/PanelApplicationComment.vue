<template>
  <div>
    <textarea class="module-comment" v-model="applicantComment" ref="commentInput" @input="adjustTextareaHeight" />
  </div>
</template>

<script setup>
import { ref } from 'vue';

const applicantComment = ref('');
const commentInput = ref(null);

const adjustTextareaHeight = () => {
  const textarea = commentInput.value;
  textarea.style.height = 'auto'; // Resetting to auto height
  textarea.style.height = `${textarea.scrollHeight}px`; // Setting height based on content
};

defineExpose({
  applicantComment
})
</script>

<style scoped>
.module-comment {
  padding: 2.5px;
  margin: 2.5px auto;
  /* Zentriert das Textfeld horizontal */
  width: 90%;
  min-height: 50px;
  font-size: 12px;
  color: #333333;
  box-shadow: 0 0 5px rgba(0, 128, 255, 0);
  border: 1px solid #aaaaaa;
  border-radius: 5px;
  transition: border-color 0.2s;
  resize: none;
}

.module-comment:hover,
.module-comment:focus {
  border-color: black;
}
</style>
