<!--
comment input
displays:
- comment text input
functionality:
- comment input
-->


<script setup>
import { ref } from 'vue';

const comment = ref('');
const commentInput = ref(null);
const fontSize = ref(16); // Fest auf 16 Pixel gesetzte Schriftgröße

const adjustTextareaHeight = () => {
  const textarea = commentInput.value;
  textarea.style.height = 'auto'; // Resetting to auto height
  textarea.style.height = `${textarea.scrollHeight}px`; // Setting height based on content
};

const adjustFontSize = () => {
  const textarea = commentInput.value;
  textarea.style.fontSize = `${fontSize.value}px`; // Setting font size
};

defineExpose({
  comment,
  fontSize,
  adjustFontSize // Expose the function to adjust font size
})
</script>

<template>
  <div>
    <textarea class="module-comment" v-model="comment" ref="commentInput" @input="adjustTextareaHeight" />
  </div>
</template>

<style scoped>
.module-comment {
  padding: 2.5px;
  margin: 2.5px auto;
  /* Zentriert das Textfeld horizontal */
  width: 90%;
  min-height: 50px;
  font-size: 16px;
  /* Fest auf 16 Pixel gesetzte Schriftgröße */
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
